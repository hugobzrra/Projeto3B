package pacote.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import pacote.util.ValidatorUtil;




/** 
 * Entidade que armazena os dados de uma pessoa no sistema.
 * @author Hugo Bezerra e Matheus Bezerra
 */
@SuppressWarnings("serial")
@Entity
public class Pessoa implements ObjetoPersistivel {
	
	@Id
    @GeneratedValue  
	@Column(name="id_pessoa", nullable = false)
	private int id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String sobrenome;
	
	@Column(nullable = false, length = 1)
	private char sexo;
	
	@Transient
	private String nomeSobrenome;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/** Retorna uma descri��o, de forma leg�vel a humanos, referente
	 * ao sexo da pessoa em quest�o. */
	public String getDescricaoSexo(){
		if (ValidatorUtil.isEmpty(sexo))
			return "-";
		else if (sexo == 'M'){
			return "Masculino";
		} else {
			return "Feminino";
		}
	}
	
	/** Identifica se o sexo da pessoa em quest�o � masculino ou n�o */
	public boolean isSexoMasculino(){
		if (ValidatorUtil.isNotEmpty(sexo) && sexo == 'M'){
			return true;
		} 
		return false;
	}
	
	/** Identifica se o sexo da pessoa em quest�o � feminino ou n�o */
	public boolean isSexoFeminino(){
		if (ValidatorUtil.isNotEmpty(sexo) && sexo == 'F'){
			return true;
		} 
		return false;
	}

	public String getNome() {
		return nome;
	}
	
	/** Concatena o nome com o sobrenome do usu�rio. */
	public String getNomeSobrenome() {
		if (ValidatorUtil.isEmpty(nomeSobrenome)){
			if (ValidatorUtil.isEmpty(nome) && ValidatorUtil.isEmpty(sobrenome))
				return null;
			
			return nome + " " + sobrenome;
		}
		
		return nomeSobrenome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public void setNomeSobrenome(String nomeSobrenome) {
		this.nomeSobrenome = nomeSobrenome;
	}
	
}
