package pacote.negocio;

import pacote.dao.GenericDAOImpl;
import pacote.dao.IGenericDAO;
import pacote.dominio.ObjetoPersistivel;

/** 
 * Classe capaz de realizar remoção de quaisquer entidades do sistema da base de dados.
 * @author Hugo Bezerra e Matheus Bezerra
 */
public class ProcessadorRemocao extends ProcessadorComando {
	
	/** 
	 * Objeto que se quer remover do banco. 
	 */
	protected ObjetoPersistivel obj;
	
	@Override
	protected void iniciarExecucao() throws Exception {
		IGenericDAO dao = new GenericDAOImpl();
		obj = dao.findByPrimaryKey(obj.getId(), obj.getClass());
		dao.delete(obj);
	}

	@Override
	protected Object getResultado() {
		return obj;
	}

	public void setObj(ObjetoPersistivel obj) {
		this.obj = obj;
	}

}
