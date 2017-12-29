package classes;

/** @author Hugo Bezerra e Matheus Bezerra 
 * */

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AtualizaDoador {
	
	/**
	 *  Método que quando o usuário (Doador) digitar o novo nome irá alterar na tabela,
	 *  assim como o valor.
	 * */
	
	public static void main(String[] args) {
		EntityManagerFactory fabrica =
				Persistence.createEntityManagerFactory("Trabalho3B");
		EntityManager gerenciador = fabrica.createEntityManager();
		
		Doador d = gerenciador.find(Doador.class, 1L);
		d.setNome("Editora Alterada");
		d.setValor("emailalterado@teste.com.br");
		
		gerenciador.getTransaction().begin();
		gerenciador.getTransaction().commit();
		
		gerenciador.close();
		fabrica.close();
	}

}