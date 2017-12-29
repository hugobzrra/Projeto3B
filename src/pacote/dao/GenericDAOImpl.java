package pacote.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import pacote.dominio.ObjetoPersistivel;
import pacote.util.ValidatorUtil;

public class GenericDAOImpl implements IGenericDAO {
	

	private void change(ObjetoPersistivel c, OperacaoDatabase op){
		switch (op) {
			case INSERIR:
				getEm().persist(c);
				break;
			case ALTERAR:
				getEm().merge(c);
				break;
			case REMOVER:
				getEm().remove(c);
				break;
		}
	}


	@Override
	public EntityManager getEm() {
		return Database.getInstance().getEntityManager();
	}
	

	@Override
	public void create(ObjetoPersistivel c){
		change(c, OperacaoDatabase.INSERIR);
	}


	@Override
	public void update(ObjetoPersistivel c){
		change(c, OperacaoDatabase.ALTERAR);
	}
	

	@Override
	public void createOrUpdate(ObjetoPersistivel c){
		if (c.getId() == 0)
			change(c, OperacaoDatabase.INSERIR);
		else
			change(c, OperacaoDatabase.ALTERAR);
	}
	
	@Override
	public void delete(ObjetoPersistivel c){
		change(c, OperacaoDatabase.REMOVER);
	}

	@Override
	public void flush(){
		getEm().flush();
	}
	

	@Override
	public <T extends ObjetoPersistivel> T findByPrimaryKey(int id, Class<T> classe){
		EntityManager em = getEm();
		T c = em.find(classe, id);
		return c;
	}
	
	@Override
	public <T extends ObjetoPersistivel> List<T> findAll(Class<T> classe){
		EntityManager em = getEm();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(classe);
		TypedQuery<T> typedQuery = em.createQuery(query.select(query.from(classe)));
		List<T> c = typedQuery.getResultList();
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> List<T> findAllAtivos(Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where ativo = :ativo ";
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("ativo", true);
		List<T> retorno = q.getResultList();
		return retorno;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> List<T> findAllAtivos(Class<T> classe, String orderBy) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where ativo = :ativo order by " + orderBy;
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("ativo", true);
		
		List<T> retorno = q.getResultList();
		return retorno;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ObjetoPersistivel> List<T> findAllLike(String coluna,String valor, String orderby, Class<T> classe){
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where upper("+coluna+") like upper(:valor)";
		
		if (ValidatorUtil.isNotEmpty(orderby)){
			jpql += " order by " + orderby;
		}
		
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", "%"+valor+"%");
		List<T> retorno = q.getResultList();
		return retorno;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> List<T> findByExactField(String coluna, Object valor, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where "+coluna+" = :valor";
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		List<T> retorno = q.getResultList();
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> List<T> findByExactField(String coluna, Object valor, String orderBy, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where "+coluna+" = :valor";
		
		if (ValidatorUtil.isNotEmpty(orderBy)){
			jpql += " order by " + orderBy;
		}
		
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		List<T> retorno = q.getResultList();
		return retorno;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> List<T> findByExactFields(String[] colunas, Object[] valores, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where 1=1 ";
		
		for (int i = 0; i < colunas.length; i++){
			String coluna = colunas[i];
			jpql += " and " + coluna + " = :valor" + i + " ";
		}
		
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		
		for (int i = 0; i < valores.length; i++){
			q.setParameter("valor"+i, valores[i]);
		}
		
		List<T> retorno = q.getResultList();
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjetoPersistivel> T findByExactFields(String[] colunas, Object[] valores, 
			boolean limit, Class<T> classe) {
		String tabela = classe.getSimpleName();
		String jpql = "from "+tabela+ " where 1=1 ";
		
		for (int i = 0; i < colunas.length; i++){
			String coluna = colunas[i];
			jpql += " and " + coluna + " = :valor" + i + " ";
		}
		
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		
		if (limit)
			q.setMaxResults(1);
		
		for (int i = 0; i < valores.length; i++){
			q.setParameter("valor"+i, valores[i]);
		}
		
		List<T> results = q.getResultList();
		
		return ValidatorUtil.isNotEmpty(results) ? results.get(0) : null;
	}
	
	/** Atualiza uma coluna de uma tabela de qualquer entidade. */
	@Override
	public <T extends ObjetoPersistivel> void updateField(Class<T> classe, int id, String coluna, Object valor) {
		String tabela = classe.getSimpleName();
		String jpql = "update "+tabela+ " set " + coluna + " = :valor where id = :id ";
		EntityManager em = getEm();
		Query q = em.createQuery(jpql);
		q.setParameter("valor", valor);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	@Override
	public void clear() {
		getEm().clear();	
	}

	@Override
	public void detach(ObjetoPersistivel p) {
		getEm().detach(p);
	}
	

	@Override
	public void refresh(ObjetoPersistivel p) {
		getEm().refresh(p);
	}
	
	@Override
	public void update(String sql) {
		Session session = (Session) Database.getInstance().getEntityManager().getDelegate();
		SQLQuery q = session.createSQLQuery(sql);
		q.executeUpdate();
	}

}

enum OperacaoDatabase {
	INSERIR,ALTERAR,REMOVER;
}

