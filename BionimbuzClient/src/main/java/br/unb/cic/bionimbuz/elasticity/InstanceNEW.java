package br.unb.cic.bionimbuz.elasticity;

import java.io.Serializable;

public class InstanceNEW implements Serializable {
    
    public String id;
    public String name;
    public String state;
    public String provider;
    public String CPUutilization;
    
    public InstanceNEW() {}

    public InstanceNEW(String id, String name, String state, String provider, String CPUutilization) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.provider = provider;
        this.CPUutilization = CPUutilization;
    }

    public String getCPUutilization() {
        return CPUutilization;
    }

    public void setCPUutilization(String CPUutilization) {
        this.CPUutilization = CPUutilization;
    }
    
 
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    
}