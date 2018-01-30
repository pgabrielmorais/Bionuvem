package br.unb.cic.bionimbuz.prediction;
 
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

 
@ManagedBean(name="PredictionTableView")
@ViewScoped
public class TableView implements Serializable {
     
    private List<Instance> instances;
     
    @ManagedProperty("#{InstaceServicePrediction}")
    private InstanceService service;
 
    @PostConstruct
    public void init() {
        instances = service.returnInstances();
    }
     
    public List<Instance> getInstances() {
        return instances;
    }
 
    public void setService(InstanceService service) {
        this.service = service;
    }
}