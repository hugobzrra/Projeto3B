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
public class UsuarioMBean {
	
	public String cadastraUsuario() throws SQLException {
		
		String sql = "SELECT * FROM Usuario;";
		
		Connection con = ConnectionFactory.criarConexao();
		
		if (con.prepareStatement(sql) != null) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso!", "Usuário cadastrado com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!", "Usuário já cadastrado!"));
		}
		
		con.close();
		return sql;
		
	}
	
	public String removeUsuario() throws SQLException {
		
			EntityManagerFactory fabrica =
					Persistence.createEntityManagerFactory("Trabalho3B");
			EntityManager gerenciador = fabrica.createEntityManager();
			
			Usuario u = gerenciador.find(Usuario.class, 1L);
			gerenciador.remove(u);
			
			gerenciador.getTransaction().begin();
			gerenciador.getTransaction().commit();
			
			gerenciador.close();
			fabrica.close();
			return null;
		}

	}