package pacote.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import pacote.dominio.Pessoa;
import pacote.dominio.Usuario;
import pacote.util.ValidatorUtil;

/**
 * DAO (Data Access Object - Objeto de Acesso a Dados) com 
 * métodos relativos à entidade {@link Usuario}.
 * @author Hugo Bezerra e Matheus Bezerra
 */
public class UsuarioDAO extends GenericDAOImpl {

	/** Encontra um usuário a partir de seu login e senha. */
	public Usuario findUsuarioByLoginSenha(String login, String senha){
		EntityManager em = getEm();
		
		String hql = "SELECT usuario ";
		hql += " FROM Usuario usuario WHERE usuario.email = :login and usuario.senha = :senha ";
		
		Query q = em.createQuery(hql);
		q.setParameter("login", login);
		q.setParameter("senha", senha);
		
		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			return usuario;
		} catch (NoResultException e){
			return null;
		}
	}
	
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Usuario> findUsuarioGeral(String email, String nome, String sobrenome, 
			String ordenarPor){
		EntityManager em = getEm();
		
		String hql = "SELECT u ";
		hql += " FROM Usuario u WHERE 1=1";
		
		if (ValidatorUtil.isNotEmpty(email)){
			hql += " AND u.email = :email ";
		}
		if (ValidatorUtil.isNotEmpty(nome)){
			hql += " AND upper(u.pessoa.nome) like :nome ";
		}
		if (ValidatorUtil.isNotEmpty(sobrenome)){
			hql += " AND upper(u.pessoa.sobrenome) like :sobrenome ";
		}
		if (ValidatorUtil.isNotEmpty(ordenarPor)){
			hql += " ORDER BY :ordenarPor ";
		}
		
		Query q = em.createQuery(hql);
		
		if (ValidatorUtil.isNotEmpty(email)){
			q.setParameter("email", email);
		}
		if (ValidatorUtil.isNotEmpty(nome)){
			q.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		if (ValidatorUtil.isNotEmpty(sobrenome)){
			q.setParameter("sobrenome", "%" + sobrenome.toUpperCase() + "%");
		}
		if (ValidatorUtil.isNotEmpty(ordenarPor)){
			q.setParameter("ordernarPor", ordenarPor);
		}
		
		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	
}
