package pacote.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import pacote.negocio.ProcessadorInativar;
import pacote.util.CriptografiaUtils;
import pacote.util.ValidatorUtil;


@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class BuscaUsuariosMBean extends AbstractController {
	
	/** Opção que será exibida na busca de usuários para ordenar a busca aleatoriamente */
	private static final int OPCAO_ORDENAR_QUALQUER = 0;
	
	/** 
	 * Opção que será exibida na busca de usuários para ordenar a busca pelo nome,
	 * em ordem crescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_ASC = 1;
	
	/** 
	 * Opção que será exibida na busca de usuários para ordenar a busca pelo nome,
	 * em ordem decrescente. 
	 * */
	private static final int OPCAO_ORDENAR_NOME_DESC = 2;

	/** Armazena as informações preenchidas na página de busca de usuários. */
	private Usuario usuarioBusca;
	
	/** Armazena os usuários encontrados no banco de acordo com os parâmetros de busca. */
	private List<Usuario> usuariosEncontrados;
	
	/** Permite o acesso ao banco. */
	private UsuarioDAO dao;
	
	/** Indica qual opção de ordenamento foi selecionada pelo usuário. */
	private Integer opcaoOrdenar;
	
	/** Inicialização do MBean */
	@PostConstruct
	private void init() {
		usuarioBusca = new Usuario();
		usuarioBusca.setPessoa(new Pessoa());
		usuariosEncontrados = new ArrayList<>();
		opcaoOrdenar = null;
		
		dao = new UsuarioDAO();
	}
	
	/** Entra na página de busca de usuários */
	public String entrarBuscarUsuarios(){
		return Paginas.BUSCAR_USUARIO;
	}
	
	/** Realiza a busca de usuários no banco. */
	public String buscar(){
		dao = new UsuarioDAO();
		
		String ordenar = opcaoOrdenar == OPCAO_ORDENAR_NOME_ASC ? "u.pessoa.nome ASC" : 
							opcaoOrdenar == OPCAO_ORDENAR_NOME_DESC ? "u.pessoa.nome DESC" : null;
		
		usuariosEncontrados = dao.findUsuarioGeral(usuarioBusca.getEmail(),
				usuarioBusca.getPessoa().getNome(),
				usuarioBusca.getPessoa().getSobrenome(),
				ordenar);
			
		return null;
	}
	
	/** 
	 * Inativa um usuário do banco de dados. Não o remove, apenas inativa, porém
	 * tem o mesmo efeito, já que ele não pode mais fazer login. É útil para quando
	 * o administrador não quer perder as informações do registro, por diversos motivos.
	 *  
	 * */
	public String removerUsuario(Usuario usuario) throws Exception{
		ProcessadorInativar p = new ProcessadorInativar();
		p.setObj(usuario);
		//Define a operação. Se estiver ativo, inativa o registro; se estiver inativo, reativa.
		p.setOperacao(usuario.isAtivo() ? ProcessadorInativar.INATIVAR : ProcessadorInativar.REATIVAR);
		p.execute();
		
		return buscar();
	}
	
	public Usuario getUsuarioBusca() {
		return usuarioBusca;
	}

	public void setUsuarioBusca(Usuario usuarioBusca) {
		this.usuarioBusca = usuarioBusca;
	}

	public List<Usuario> getUsuariosEncontrados() {
		return usuariosEncontrados;
	}

	public void setUsuariosEncontrados(List<Usuario> usuariosEncontrados) {
		this.usuariosEncontrados = usuariosEncontrados;
	}

	public Integer getOpcaoOrdenar() {
		return opcaoOrdenar;
	}

	public void setOpcaoOrdenar(Integer opcaoOrdenar) {
		this.opcaoOrdenar = opcaoOrdenar;
	}

	public int getOpcaoOrdenarQualquer() {
		return OPCAO_ORDENAR_QUALQUER;
	}

	public int getOpcaoOrdenarNomeAsc() {
		return OPCAO_ORDENAR_NOME_ASC;
	}

	public int getOpcaoOrdenarNomeDesc() {
		return OPCAO_ORDENAR_NOME_DESC;
	}

}
	
