package botoes;

/**@author Hugo Bezerra e Matheus Bezerra *
 * 
 */


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
	 
@ManagedBean
	
	public class buttonView {
		
		/**M�todo que mostra uma mensagem mensagem com confirma��o 
		 * de cadastro efetivado.
		 * @return void*/
	
	    public void buttonAction(ActionEvent actionEvent) {
	        addMessage("Campos salvos com sucesso!");
	    }
	     
	    /**M�todo que mostra uma mensagem que j� tem na biblioteca do sistema
	     *@return void */
	    	    
	    public void addMessage(String summary) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	}