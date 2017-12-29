package botoes;

import java.util.Date;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class BotoesMBean {

	private Date date1;
	private Integer rating2;
	private String option;
	private String console;

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	
	public Integer getRating2() {
		return rating2;
	}

	public void setRating2(Integer rating2) {
		this.rating2 = rating2;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}
	
}
