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
		
		/**Método que mostra uma mensagem mensagem com confirmação 
		 * de cadastro efetivado.
		 * @return void*/
	
	    public void buttonAction(ActionEvent actionEvent) {
	        addMessage("Campos salvos com sucesso!");
	    }
	     
	    /**Método que mostra uma mensagem que já tem na biblioteca do sistema
	     *@return void */
	    	    
	    public void addMessage(String summary) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	}