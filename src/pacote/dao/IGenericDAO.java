package pacote.dao;

import java.util.List;

import javax.persistence.EntityManager;

import pacote.dominio.ObjetoPersistivel;

/**
 * Interface que contém os métodos que os DAO (Data Access Object) devem ter.
 * @author Hugo Bezerra e Matheus Bezerra
 */
public interface IGenericDAO {

	/** Obtém o EntityManager para poder fazer operações no banco. */
	public abstract EntityManager getEm();
	
	/** 
	 * Limpa o cache do Hibernate. Força o recarregamento de todas as entidades, quando necessário.
	 * Todas as alterações não salvas serão perdidas. */
	public abstract void clear();
	
	/** Limpa uma determinada entidade da memória do Hibernate. */
	public abstract void detach(ObjetoPersistivel p);
	
	/** Atualiza as informações de um determinado registro (objeto) de entidade.
	 * Sincroniza com o banco. */
	public abstract void refresh(ObjetoPersistivel p);

	/** 
	 * Este método pode ser chamado quando for preciso salvar um novo registro
	 * no banco de dados (insert). Funciona para qualquer entidade.
	 * */
	public abstract void create(ObjetoPersistivel c);

	/** 
	 * Este método pode ser chamado quando for preciso atualizar (update) um registro
	 * no banco de dados. Funciona para qualquer entidade.
	 * */
	public abstract void update(ObjetoPersistivel c);
	
	/** 
	 * Cria ou atualiza um registro do banco, dependendo se ele
	 * já existe ou não. 
	 * */
	public abstract void createOrUpdate(ObjetoPersistivel c);

	/**
	 * Remove um registro do banco de dados (delete). Funciona
	 * para qualquer entidade.
	 */
	public abstract void delete(ObjetoPersistivel c);
	
	/** Força a sincronização de suas entidades com o banco de dados. */
	public abstract void flush();
	
	/** Realiza um update através de um SQL simples. */
	public abstract void update(String sql);
	
	/** Obtém qualquer entidade através do ID dela. */
	public abstract <T extends ObjetoPersistivel> T findByPrimaryKey(int id, Class<T> classe);

	/** Obtém todos os registros de uma tabela no banco de dados. Basta informar o .class da entidade. */
	public abstract <T extends ObjetoPersistivel> List<T> findAll(Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que estão ativos, ou seja, que
	 * possuem a coluna "ativo" igual a true. */
	public abstract <T extends ObjetoPersistivel> List<T> findAllAtivos(Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que estão ativos, ou seja, que
	 * possuem a coluna "ativo" igual a true. Adiciona possibilidade de ordenação
	 * dos resultados. */
	public abstract <T extends ObjetoPersistivel> List<T> findAllAtivos(Class<T> classe, String orderBy);

	/** 
	 * Retorna os registros de qualquer entidade do banco que possuam
	 * um determinado valor de coluna. Não precisa ser valor exato. */
	public abstract <T extends ObjetoPersistivel> List<T> findAllLike(String coluna, String valor, String orderby, Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que possuam um valor exato de coluna. */
	public abstract <T extends ObjetoPersistivel> List<T> findByExactField(String coluna, Object valor, Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que possuam um valor exato de coluna. */
	public abstract <T extends ObjetoPersistivel> List<T> findByExactField(String coluna, Object valor, String orderBy, Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que possuam valores específicos de algumas colunas. */
	public <T extends ObjetoPersistivel> List<T> findByExactFields(String[] colunas, Object[] valores, Class<T> classe);
	
	/** Retorna todos os registros de uma entidade que possuam valores específicos de algumas colunas. */
	public <T extends ObjetoPersistivel> T findByExactFields(String[] colunas, Object[] valores, boolean limit, Class<T> classe);

	/** Atualiza uma coluna de uma tabela de qualquer entidade. */
	public <T extends ObjetoPersistivel> void updateField(Class<T> classe, int id, String coluna, Object valor);

}