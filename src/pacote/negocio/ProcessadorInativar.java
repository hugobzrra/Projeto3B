package pacote.negocio;

import pacote.dao.GenericDAOImpl;
import pacote.dao.IGenericDAO;
import pacote.dominio.ObjetoPersistivel;

/** 
 * Classe capaz de realizar inativação/ativação lógica de quaisquer entidades do sistema
 * que possuam um atributo chamado "ativo".
 * @author Hugo Bezerra e Matheus Bezerra
 */
public class ProcessadorInativar extends ProcessadorComando {
	
	public static final int INATIVAR = 1;
	public static final int REATIVAR = 2;
	
	/** Indica a operação a ser realizada. Deve ser INATIVAR ou ser REATIVAR. */
	private int operacao;
	
	/** 
	 * Objeto que se quer inativar/reativar no banco. 
	 */
	private ObjetoPersistivel obj;
	
	@Override
	protected void iniciarExecucao() throws Exception {
		IGenericDAO dao = new GenericDAOImpl();
		
		if (operacao == INATIVAR)
			//Atualiza a coluna "ativo" do objeto em questão 
			dao.updateField(obj.getClass(), obj.getId(), "ativo", false);
		else if (operacao == REATIVAR)
			//Atualiza a coluna "ativo" do objeto em questão
			dao.updateField(obj.getClass(), obj.getId(), "ativo", true);
		else
			throw new Exception("Operação inválida!");
		
		dao.detach(obj);
	}

	@Override
	protected Object getResultado() {
		return obj;
	}

	public void setObj(ObjetoPersistivel obj) {
		this.obj = obj;
	}

	public void setOperacao(int operacao) {
		this.operacao = operacao;
	}
	
}
