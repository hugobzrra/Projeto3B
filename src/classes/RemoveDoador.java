package classes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RemoveDoador {
	
	public static void main(String[] args) {
		EntityManagerFactory fabrica =
				Persistence.createEntityManagerFactory("Trabalho3B");
		EntityManager gerenciador = fabrica.createEntityManager();
		
		Doador d = gerenciador.find(Doador.class, 1L);
		gerenciador.remove(d);
		
		gerenciador.getTransaction().begin();
		gerenciador.getTransaction().commit();
		
		gerenciador.close();
		fabrica.close();
	}

}