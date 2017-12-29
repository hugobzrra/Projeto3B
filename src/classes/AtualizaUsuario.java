package classes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AtualizaUsuario {
	
	public static void main(String[] args) {
		EntityManagerFactory fabrica =
				Persistence.createEntityManagerFactory("Trabalho3B");
		EntityManager gerenciador = fabrica.createEntityManager();
		
		Usuario u = gerenciador.find(Usuario.class, 1L);
		u.setNome("Editora Alterada");
		u.setEmail("emailalterado@teste.com.br");
		
		gerenciador.getTransaction().begin();
		gerenciador.getTransaction().commit();
		
		gerenciador.close();
		fabrica.close();
	}

}
