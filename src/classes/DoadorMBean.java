package classes;

import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
public class DoadorMBean {

	public String cadastraDoador() throws SQLException {

		String sql = "SELECT * FROM Doador;";

		Connection con = ConnectionFactory.criarConexao();

		if (con.prepareStatement(sql) != null) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso!", "Doação feita com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!", "Usuário já fez doação!"));
		}

		con.close();
		return sql;

	}
	
	public String removeDoador() throws SQLException {
	
			EntityManagerFactory fabrica =
					Persistence.createEntityManagerFactory("Trabalho3B");
			EntityManager gerenciador = fabrica.createEntityManager();
			
			Doador d = gerenciador.find(Doador.class, 1L);
			gerenciador.remove(d);
			
			gerenciador.getTransaction().begin();
			gerenciador.getTransaction().commit();
			
			gerenciador.close();
			fabrica.close();
			return null;
		}

	}
