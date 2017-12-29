package botoes;

/**@author Hugo Bezerra e Matheus Bezerra  */

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
@ManagedBean
public class GrowlView {
 
	/** M�todo que pega a mensagem */
	
    private String message;
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    /**M�todo que salva a String insirida pelo usu�rio
     *  concatenado com a messagem contida no m�todo */
    
    public void saveMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
         
        context.addMessage(null, new FacesMessage("Sucesso, sua palavra foi: " + message + " !"));
    }
}