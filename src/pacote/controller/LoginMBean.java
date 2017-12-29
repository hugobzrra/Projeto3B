package pacote.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import pacote.arq.AbstractController;
import pacote.dao.UsuarioDAO;
import pacote.dominio.Pessoa;
import pacote.dominio.TipoUsuario;
import pacote.dominio.Usuario;
import pacote.util.CriptografiaUtils;
import pacote.util.ValidatorUtil;

/**
 * MBean que controla o login no sistema. 
 * @author Hugo Bezerra e Matheus Bezerra
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class LoginMBean extends AbstractController {
	
	/** Armazena os dados informados na tela de login. */
	private Usuario usuario;
	
	/** Armazena os dados iniciais de cadastro do usuário. */
	private Usuario usuarioCadastro;
	
	@PostConstruct
	private void init(){
		usuario = new Usuario();
		usuarioCadastro = new Usuario();
		usuarioCadastro.setPessoa(new Pessoa());
		usuarioCadastro.setTipoUsuario(TipoUsuario.COMUM);
	}
	
	/** Autentica o usuário e faz login no sistema. */
	public String autenticar(){
		if (!validarLogin()){
			return null;
		}
		
		try {
			UsuarioDAO dao = new UsuarioDAO();
			usuario = dao.findUsuarioByLoginSenha(usuario.getEmail(), CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			
			if (!ValidatorUtil.isEmpty(usuario)){
				if (!usuario.isAtivo()){
					addMsgError("Este usuário foi desabilitado e não possui mais acesso ao sistema.");
					return null;
				}
			} else {
				init();
				addMsgError("Usuário/Senha incorretos.");
				return null;
			}
			
			//Salva o usuário permanentemente na memória do sistema 
			getCurrentSession().setAttribute("usuarioLogado", usuario);
			return Paginas.PORTAL_INICIO;
			
		} catch (Exception e) {
			tratamentoErroPadrao(e);
			return null;
		} 
	}
	
	/** Verifica se os dados para login estão corretos */
	public boolean validarLogin(){
		if (usuario == null || (ValidatorUtil.isEmpty(usuario.getEmail()) && 
				ValidatorUtil.isEmpty(usuario.getSenha()))){
			addMsgError("Usuário/senha não informados.");
			return false;
		}
		
		if (ValidatorUtil.isEmpty(usuario.getEmail())){
			addMsgError("Usuário: campo obrigatório não informado.");
			return false;
		}
		
		if (ValidatorUtil.isEmpty(usuario.getSenha())){
			addMsgError("Senha: campo obrigatório não informado.");
			return false;
		}
		
		return true;
	}
	
	/** Realiza logoff do sistema. */
	public String logoff(){
		getCurrentSession().invalidate();
		return Paginas.LOGIN_PAGE;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

}
