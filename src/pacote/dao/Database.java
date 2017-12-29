package pacote.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

public class Database {

	private static Database singleton = new Database();
	private static EntityManager em;

	private Database() {
		criarEM();
	}
	
	private void criarEM(){
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("Projeto3B");
		em = emf.createEntityManager();
	}

	public static Database getInstance() {
		return singleton;
	}

	public EntityManager getEntityManager() {
		if (!em.isOpen()){
			criarEM();
		}
		
		return em;
	}
	
	public Session getSession(){
		return (Session) em.getDelegate();
	}

}

