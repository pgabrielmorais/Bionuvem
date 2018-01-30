package br.unb.cic.bionimbuz.elasticity;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
 
@ManagedBean
public class InstanceView implements Serializable {
     
    private List<InstanceNEW> instances;
    private List<String> CPU;
     
    private InstanceNEW selectedInstance;
     
    @ManagedProperty("#{instanceService}")
    private InstanceService service;
 
    @PostConstruct
    public void init() {        
        instances = service.getInstances();
        
    }

    public List<String> getCPU() {
        return CPU;
    }

    public void setCPU(List<String> CPU) {
        this.CPU = CPU;
    }

    public List<InstanceNEW> getInstances() {
        return instances;
    }

    public void setInstances(List<InstanceNEW> instances) {
        this.instances = instances;
    }

    public InstanceNEW getSelectedInstance() {
        return selectedInstance;
    }

    public void setSelectedInstance(InstanceNEW selectedInstance) {
        this.selectedInstance = selectedInstance;
    }

    public InstanceService getService() {
        return service;
    }

    public void setService(InstanceService service) {
        this.service = service;
    }
    
    

     

}