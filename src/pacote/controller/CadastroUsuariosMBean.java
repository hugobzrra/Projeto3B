package pacote.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.Validator;

import pacote.arq.AbstractController;
import pacote.dao.GenericDAOImpl;
import pacote.dao.IGenericDAO;
import pacote.dao.UsuarioDAO;
import pacote.dominio.Arquivo;
import pacote.dominio.Pessoa;
import pacote.dominio.TipoUsuario;
import pacote.dominio.Usuario;
import pacote.negocio.ProcessadorCadastro;
import pacote.util.CriptografiaUtils;
import pacote.util.ValidatorUtil;

/** 
 * MBean que controla operações relacionadas à criação/edição de usuários.<br/>
 * 
 * @author Hugo Bezerra e Matheus Bezerra
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CadastroUsuariosMBean extends AbstractController {

	/** Indica se é cadastro (true) ou edição (false) */
	private boolean cadastro;
	
	/** Armazena os dados do usuário que será cadastrado */
	private Usuario usuario;
	
	/** Permite o acesso ao banco. */
	private IGenericDAO dao;
	
	/** Tamanho máximo para upload, em bytes. */
	private final Integer TAM_MAXIMO_ARQUIVO = 2097152;
	
	/** Formatos permitidos para o envio da foto do usuário. */
	private final String[] FORMATOS_PERMITIDOS = {"png", "jpg", "jpeg"};
	
	private void init() {
		usuario = new Usuario();
		usuario.setPessoa(new Pessoa());
		
		dao = new UsuarioDAO();

		cadastro = false;
	}
	
	/** Entra na tela de cadastro de usuários. */
	public String entrarCadastroUsuarios(){
		init();
		return Paginas.CADASTRAR_USUARIO;
	}
	
	/** Entra na tela de edição de usuários. */
	public String entrarEdicaoUsuarios(Usuario usuario){
		init();
		this.usuario = usuario; //o usuário a ser editado será o recebido pelo parâmetro
		cadastro = false;
		return Paginas.CADASTRAR_USUARIO;
	}
	
	/** Cadastra ou edita um usuário no banco. */
	public String salvar() throws InstantiationException,
			IllegalAccessException {
		
		IGenericDAO dao = new GenericDAOImpl();
		boolean erro = false; //Indica se houve erro na validação dos dados do usuário
		
		//Realizando validações
		
		cadastro = usuario.getId() == 0 ? true : false; //Se for cadastro, true; se for edição, false
		
		if (cadastro){	
			//Cadastro
			if (ValidatorUtil.isNotEmpty(dao.findByExactField("email", usuario.getEmail(), Usuario.class))){
				addMsgError("Já existe um usuário com o email informado.");
				erro = true;
			}
			if (!usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
				addMsgError("A senha informada não confere com sua confirmação.");
				erro = true;
			}
		} else {
			//Edição
			if (ValidatorUtil.isNotEmpty(usuario.getSenha()) && ValidatorUtil.isEmpty(usuario.getNovaSenhaConfirmacao())
					|| (ValidatorUtil.isEmpty(usuario.getSenha()) && ValidatorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()))){
				
				addMsgError("Informe a senha e sua confirmação.");
				erro = true;
			
			} else if (ValidatorUtil.isNotEmpty(usuario.getSenha()) && ValidatorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()) 
					&& !usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
				
				addMsgError("A senha informada não confere com sua confirmação.");
				erro = true;
			}
		}
		
		if (!ValidatorUtil.validateEmail(usuario.getEmail())){
			addMsgError("O email informado é inválido.");
			erro = true;
		}
		
		//Se houve algum erro no processo de validação dos dados, impede o avanço do cadastro
		if (erro)
			return null;
		
		if (!cadastro && ValidatorUtil.isEmpty(usuario.getSenha())){
			//Se for edição, só deve modificar a senha caso o usuário tenha digitado alguma coisa
			//no campo de senha, ou seja, caso a senha esteja vazia, ela não deve ser modificada (deve
			//permanecer a mesma do banco).
			
			dao.detach(usuario);
			Usuario usuarioBanco = dao.findByPrimaryKey(usuario.getId(), Usuario.class);
			usuario.setSenha(usuarioBanco.getSenha()); //A senha do banco já está criptografada 
			
		} else {
			//Nos demais casos (cadastro ou edição com mudança de senha), a senha não está criptografada
			usuario.setSenha(CriptografiaUtils.criptografarMD5(usuario.getSenha()));
		}
		
		try {
			boolean validou = validarImagemCadastro();
			
			if (!validou){
				return null;
			}
			
			//Se o usuário anexou foto
			if (usuario.getArquivo() != null && ValidatorUtil.isNotEmpty(usuario.getArquivo().getFileName())
					&& usuario.getArquivo().getSize() != -1){
				
				//Cria nova entidade arquivo
				Arquivo arq = new Arquivo();
				arq.setNome(usuario.getArquivo().getFileName());
				arq.setBytes(usuario.getArquivo().getContents());
				
				ProcessadorCadastro p = new ProcessadorCadastro();
				p.setObj(arq);
				p.execute(); //Cadastra a foto no banco
				
				//Informa o ID da foto do usuário, para que possa ser carregada posteriormente
				usuario.setIdFoto(arq.getId());
			}
			
			ProcessadorCadastro p = new ProcessadorCadastro();
			p.setObj(usuario);
			p.execute(); //Cadastra/edita o usuário
			
			addMsgInfo((cadastro ? "Cadastro realizado" : "Alteração realizada") + " com sucesso!");
			
			return posCadastro();
			
		} catch (Exception e) {
			tratamentoErroPadrao(e);
			return null;
		} 
	}
	
	/** Verifica se a imagem anexada do usuário está de acordo com o esperado */
	private boolean validarImagemCadastro(){
		if (usuario.getArquivo() != null && ValidatorUtil.isNotEmpty(usuario.getArquivo().getFileName())
				&& usuario.getArquivo().getSize() != -1){
			//Verificando extensão
			
			String[] nomes = usuario.getArquivo().getFileName().split("\\.");
			String extensao = nomes[nomes.length - 1].toLowerCase();
			
			List<String> formatos = Arrays.asList(FORMATOS_PERMITIDOS);
			
			if (!formatos.contains(extensao)){
				String msg = "O formato de arquivo que você anexou não é suportado. Por favor, ";
				msg += "envie em um dos seguintes formatos: png ou jpg.";
				
				addMsgError(msg);
				return false;
			} 
			
			//Verificando tamanho arquivo
			if (usuario.getArquivo().getSize() > TAM_MAXIMO_ARQUIVO) {
				addMsgError("O tamanho máximo para upload de arquivo é de 2 MB.");
				return false;
			}
		}
		
		return true;
	}
	
	protected String posCadastro() {
		return Paginas.PORTAL_INICIO;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
	
