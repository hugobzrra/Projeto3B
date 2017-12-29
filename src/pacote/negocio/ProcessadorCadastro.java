package pacote.negocio;

import pacote.dao.GenericDAOImpl;
import pacote.dao.IGenericDAO;
import pacote.dominio.ObjetoPersistivel;

/** 
 * Classe capaz de realizar cadastro/edição de quaisquer entidades do sistema.
 * @author Hugo Bezerra e Matheus Bezerra
 */
public class ProcessadorCadastro extends ProcessadorComando {
	
	/** 
	 * Objeto que se quer cadastrar/editar no banco. 
	 */
	protected ObjetoPersistivel obj;
	
	@Override
	protected void iniciarExecucao() {
		IGenericDAO dao = new GenericDAOImpl();
		
		if (obj.getId() == 0){
			dao.create(obj);
		} else {
			dao.update(obj);
		}
	}

	@Override
	protected Object getResultado() {
		return obj;
	}

	public void setObj(ObjetoPersistivel obj) {
		this.obj = obj;
	}
	
}
