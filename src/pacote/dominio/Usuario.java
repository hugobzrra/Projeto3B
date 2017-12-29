package pacote.dominio;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.primefaces.model.UploadedFile;

import pacote.util.CriptografiaUtils;
import pacote.util.Formatador;
import pacote.util.UsuarioUtil;



/** 
 * Entidade que armazena os dados de um usu�rio do sistema.
 * @author Hugo Bezerra e Matheus Bezerra
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Usuario extends EntidadeRastreada {
	
	@Id
    @GeneratedValue  
	@Column(name="id_usuario", nullable = false)
	private int id;
	
	@Column(nullable = false)
	private String senha;
	
	@Column(nullable = false)
	private String email;
	
	/** Pessoa associada ao usu�rio do sistema. */
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", nullable=false)
	private Pessoa pessoa;
	
	/** Tipo do usu�rio. */
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TipoUsuario tipoUsuario;
	
	/** Utilizado para remo��o l�gica. */
	@Column(nullable = false)
	private boolean ativo = true;
	
	@Column(name="id_foto")
	private Integer idFoto;
	
	/** 
	 * Atributo n�o persisitido que armazena uma foto que o usu�rio deseja
	 * para seu perfil.
	 * */
	@Transient
	private UploadedFile arquivo;
	
	/** Confirma��o da nova senha. */
	@Transient
	private String novaSenhaConfirmacao;
	
	/** Obt�m uma descri��o do usu�rio em quest�o. No caso, exibe seu nome. */
	@Override
	public String toString() {
		return pessoa != null && pessoa.getNome() != null ? pessoa.getNome() : email;
	}
	
	/** Cria um c�digo que identifica unicamente um usu�rio. Serve para descobrir
	 * se um usu�rio � igual a outro ou n�o. */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/** Identifica se um usu�rio � igual a outro ou n�o. */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/** Obt�m a URL atrav�s da qual a foto do usu�rio pode ser carregada. */
	public String getUrlFotoUsuario(){
		return "/verArquivo?"
				+ "idArquivo=" + getIdFoto() //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescricaoTipoUsuario(){
		return tipoUsuario.toString();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	public boolean isAdministrador(){
		return tipoUsuario != null && tipoUsuario == TipoUsuario.ADMINISTRADOR;
	}
	
	public boolean isUsuarioComum(){
		return tipoUsuario != null && tipoUsuario == TipoUsuario.COMUM;
	}

	public String getNovaSenhaConfirmacao() {
		return novaSenhaConfirmacao;
	}

	public void setNovaSenhaConfirmacao(String novaSenhaConfirmacao) {
		this.novaSenhaConfirmacao = novaSenhaConfirmacao;
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}

}
