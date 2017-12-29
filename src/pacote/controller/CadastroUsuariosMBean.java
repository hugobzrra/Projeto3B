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
 * MBean que controla opera��es relacionadas � cria��o/edi��o de usu�rios.<br/>
 * 
 * @author Hugo Bezerra e Matheus Bezerra
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class CadastroUsuariosMBean extends AbstractController {

	/** Indica se � cadastro (true) ou edi��o (false) */
	private boolean cadastro;
	
	/** Armazena os dados do usu�rio que ser� cadastrado */
	private Usuario usuario;
	
	/** Permite o acesso ao banco. */
	private IGenericDAO dao;
	
	/** Tamanho m�ximo para upload, em bytes. */
	private final Integer TAM_MAXIMO_ARQUIVO = 2097152;
	
	/** Formatos permitidos para o envio da foto do usu�rio. */
	private final String[] FORMATOS_PERMITIDOS = {"png", "jpg", "jpeg"};
	
	private void init() {
		usuario = new Usuario();
		usuario.setPessoa(new Pessoa());
		
		dao = new UsuarioDAO();

		cadastro = false;
	}
	
	/** Entra na tela de cadastro de usu�rios. */
	public String entrarCadastroUsuarios(){
		init();
		return Paginas.CADASTRAR_USUARIO;
	}
	
	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoUsuarios(Usuario usuario){
		init();
		this.usuario = usuario; //o usu�rio a ser editado ser� o recebido pelo par�metro
		cadastro = false;
		return Paginas.CADASTRAR_USUARIO;
	}
	
	/** Cadastra ou edita um usu�rio no banco. */
	public String salvar() throws InstantiationException,
			IllegalAccessException {
		
		IGenericDAO dao = new GenericDAOImpl();
		boolean erro = false; //Indica se houve erro na valida��o dos dados do usu�rio
		
		//Realizando valida��es
		
		cadastro = usuario.getId() == 0 ? true : false; //Se for cadastro, true; se for edi��o, false
		
		if (cadastro){	
			//Cadastro
			if (ValidatorUtil.isNotEmpty(dao.findByExactField("email", usuario.getEmail(), Usuario.class))){
				addMsgError("J� existe um usu�rio com o email informado.");
				erro = true;
			}
			if (!usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
				addMsgError("A senha informada n�o confere com sua confirma��o.");
				erro = true;
			}
		} else {
			//Edi��o
			if (ValidatorUtil.isNotEmpty(usuario.getSenha()) && ValidatorUtil.isEmpty(usuario.getNovaSenhaConfirmacao())
					|| (ValidatorUtil.isEmpty(usuario.getSenha()) && ValidatorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()))){
				
				addMsgError("Informe a senha e sua confirma��o.");
				erro = true;
			
			} else if (ValidatorUtil.isNotEmpty(usuario.getSenha()) && ValidatorUtil.isNotEmpty(usuario.getNovaSenhaConfirmacao()) 
					&& !usuario.getSenha().equals(usuario.getNovaSenhaConfirmacao())){
				
				addMsgError("A senha informada n�o confere com sua confirma��o.");
				erro = true;
			}
		}
		
		if (!ValidatorUtil.validateEmail(usuario.getEmail())){
			addMsgError("O email informado � inv�lido.");
			erro = true;
		}
		
		//Se houve algum erro no processo de valida��o dos dados, impede o avan�o do cadastro
		if (erro)
			return null;
		
		if (!cadastro && ValidatorUtil.isEmpty(usuario.getSenha())){
			//Se for edi��o, s� deve modificar a senha caso o usu�rio tenha digitado alguma coisa
			//no campo de senha, ou seja, caso a senha esteja vazia, ela n�o deve ser modificada (deve
			//permanecer a mesma do banco).
			
			dao.detach(usuario);
			Usuario usuarioBanco = dao.findByPrimaryKey(usuario.getId(), Usuario.class);
			usuario.setSenha(usuarioBanco.getSenha()); //A senha do banco j� est� criptografada 
			
		} else {
			//Nos demais casos (cadastro ou edi��o com mudan�a de senha), a senha n�o est� criptografada
			usuario.setSenha(CriptografiaUtils.criptografarMD5(usuario.getSenha()));
		}
		
		try {
			boolean validou = validarImagemCadastro();
			
			if (!validou){
				return null;
			}
			
			//Se o usu�rio anexou foto
			if (usuario.getArquivo() != null && ValidatorUtil.isNotEmpty(usuario.getArquivo().getFileName())
					&& usuario.getArquivo().getSize() != -1){
				
				//Cria nova entidade arquivo
				Arquivo arq = new Arquivo();
				arq.setNome(usuario.getArquivo().getFileName());
				arq.setBytes(usuario.getArquivo().getContents());
				
				ProcessadorCadastro p = new ProcessadorCadastro();
				p.setObj(arq);
				p.execute(); //Cadastra a foto no banco
				
				//Informa o ID da foto do usu�rio, para que possa ser carregada posteriormente
				usuario.setIdFoto(arq.getId());
			}
			
			ProcessadorCadastro p = new ProcessadorCadastro();
			p.setObj(usuario);
			p.execute(); //Cadastra/edita o usu�rio
			
			addMsgInfo((cadastro ? "Cadastro realizado" : "Altera��o realizada") + " com sucesso!");
			
			return posCadastro();
			
		} catch (Exception e) {
			tratamentoErroPadrao(e);
			return null;
		} 
	}
	
	/** Verifica se a imagem anexada do usu�rio est� de acordo com o esperado */
	private boolean validarImagemCadastro(){
		if (usuario.getArquivo() != null && ValidatorUtil.isNotEmpty(usuario.getArquivo().getFileName())
				&& usuario.getArquivo().getSize() != -1){
			//Verificando extens�o
			
			String[] nomes = usuario.getArquivo().getFileName().split("\\.");
			String extensao = nomes[nomes.length - 1].toLowerCase();
			
			List<String> formatos = Arrays.asList(FORMATOS_PERMITIDOS);
			
			if (!formatos.contains(extensao)){
				String msg = "O formato de arquivo que voc� anexou n�o � suportado. Por favor, ";
				msg += "envie em um dos seguintes formatos: png ou jpg.";
				
				addMsgError(msg);
				return false;
			} 
			
			//Verificando tamanho arquivo
			if (usuario.getArquivo().getSize() > TAM_MAXIMO_ARQUIVO) {
				addMsgError("O tamanho m�ximo para upload de arquivo � de 2 MB.");
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
	
