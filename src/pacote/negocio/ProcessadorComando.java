package pacote.negocio;

import javax.persistence.EntityManager;

import pacote.dao.Database;


/**
 * Classe que representa um processador de dados. Deve ser estendido
 * por todos os outros processadores do sistema.
 * É esta class quem realiza operações cruciais do sistema, como
 * cadastro, edição, remoção, etc.
 * 
 * @author Hugo Bezerra e Matheus Bezerra
 *
 */
public abstract class ProcessadorComando {
	
	/** 
	 * Método principal que irá iniciar a execução dos processamentos 
	 * dos processadores filhos. É o método que deve ser chamado para 
	 * iniciar o processamento.
	 * @throws Exception 
	 * 
	 * @throws ArqException 
	 * @throws NegocioException 
	 */
	public final Object execute() throws Exception {
		EntityManager em = null;
		
		try {
			em = Database.getInstance().getEntityManager();
			
			em.getTransaction().begin();
			
			iniciarExecucao();
			
			em.getTransaction().commit();
			
			return getResultado();
			
		} catch (Exception e){
			e.printStackTrace();
			
			em = Database.getInstance().getEntityManager();
			
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			throw new Exception(e);
		} finally {
			//Limpando caches
//			if (em != null)
//				em.clear();
		}
	}
	
	/** Método que os processadores filhos devem implementar para realizar as operações necessárias. */
	protected abstract void iniciarExecucao() throws Exception;
	
	/** 
	 * Método que deve ser implementado pelos processadores filhos para retornar algum dado
	 * para quem chamou o processador. 
	 */
	protected abstract Object getResultado();
	
}
