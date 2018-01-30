package br.unb.cic.bionimbuz.prediction;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
import org.primefaces.event.SlideEndEvent;
 
@ManagedBean
public class SliderView {
        
    private int number2;   
 
    public int getNumber2() {
        return number2;
    }
 
    public void setNumber2(int number2) {
        this.number2 = number2;
    }
    
    public void onSlideEnd(SlideEndEvent event) {
        FacesMessage message = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        FacesContext.getCurrentInstance().addMessage(null, message);
    } 
}