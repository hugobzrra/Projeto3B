package pacote.dominio;

import java.io.Serializable;

/** 
 * Interface que deve ser implementada por todas as entidades do sistema.
 * @author Hugo Bezerra e Matheus Bezerra
 */
public interface ObjetoPersistivel extends Serializable {

	public int getId();
	
	public void setId(int id);
}

