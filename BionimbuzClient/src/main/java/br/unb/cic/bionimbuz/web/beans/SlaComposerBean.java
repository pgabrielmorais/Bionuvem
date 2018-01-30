///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.unb.cic.bionimbuz.web.beans;
//
//import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
//import br.unb.cic.bionimbuz.model.Instance;
//import br.unb.cic.bionimbuz.model.User;
//import br.unb.cic.bionimbuz.rest.service.RestService;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.enterprise.context.SessionScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author brenokx
// */
//@Named
//@SessionScoped
//public class SlaComposerBean implements Serializable{
//    
//    @Inject
//    private SessionBean sessionBean;
//    
//    private final RestService restService;
//    private String panel1 = "Hide-Panel1";
//    private static final Logger LOGGER = LoggerFactory.getLogger(SlaComposerBean.class);
//    private boolean limitation;
//    private String limitationType;
//    private String limitationValue;
//    private List<Instance> instances;
//    private List<Instance> selectedInstances;
//    private Instance instance;
//    private String chosenInstanceId;
//    private int quantity;
//    private int objective;
//// Logged user
//    private User loggedUser;
//    
//     public SlaComposerBean() {
//        restService = new RestService();
//      //  instances = new ArrayList<>();
////        instances = ConfigurationRepository.getInstanceList();
//    }
//    @PostConstruct
//    public void init() {
//        
//        loggedUser = sessionBean.getLoggedUser();
//        selectedInstances=new ArrayList<>();
//        instances= new ArrayList<>();
//        instances.add(new Instance("Micro",0.03,10,"Brazil",1.0,3.3,"Xeon",1,20.0,"sata"));
//        instances.add(new Instance("Macro",0.24,5,"us-west",4.0,3.3,"Xeon",4,120.0,"sata"));
//        instances.add(new Instance("Large",0.41,3,"us-west",8.0,3.3,"Xeon",8,240.0,"sata"));
//
//    }
//    public void setPanel1(String panel1){
//    this.panel1 = panel1;
//    }
//
//    public String getPanel1(){
//    return this.panel1;
//    }
//
//    /**
//     * @return the limitation
//     */
//    public boolean isLimitation() {
//        return limitation;
//    }
//
//    /**
//     * @param limitation the limitation to set
//     */
//    public void setLimitation(boolean limitation) {
//        this.limitation = limitation;
//    }
//
//    /**
//     * @return the limitationType
//     */
//    public String getLimitationType() {
//        return limitationType;
//    }
//
//    /**
//     * @param limitationType the limitationType to set
//     */
//    public void setLimitationType(String limitationType) {
//        this.limitationType = limitationType;
//    }
//
//    /**
//     * @return the limitationValue
//     */
//    public String getLimitationValue() {
//        return limitationValue;
//    }
//
//    /**
//     * @param limitationValue the limitationValue to set
//     */
//    public void setLimitationValue(String limitationValue) {
//        this.limitationValue = limitationValue;
//    }
//
//    /**
//     * @return the instances
//     */
//    public List<Instance> getInstancies() {
//        return instances;
//    }
//
//    /**
//     * @param instancies the instances to set
//     */
//    public void setInstancies(List<Instance> instancies) {
//        this.instances = instancies;
//    }
//
//    /**
//     * @return the selectedInstances
//     */
//    public List<Instance> getSelectedInstancies() {
//        return selectedInstances;
//    }
//
//    /**
//     * @param selectedInstancies the selectedInstances to set
//     */
//    public void setSelectedInstancies(List<Instance> selectedInstancies) {
//        this.selectedInstances = selectedInstancies;
//    }
//    
//    public List<String> getListInstancesString(){
//        List<String> instancesString=new ArrayList<>();
//        instances.stream().forEach((i) -> {
//            instancesString.add(i.toString());
//        });
//        
//        return instancesString;   
//    }
//        
//    public void adSelectedInstance(ActionEvent actionEvent){
//        for(Instance i : instances)
//        {
//            
//            if(i.getId().equals(chosenInstanceId)&&!instances.isEmpty()){
//                    i.setQuantity(quantity);
//                    selectedInstances.add(i);
//                    instances.remove(i);
//                    System.out.println("Descrição: "+i.getDescription()+" quantidade: "+i.getQuantity());
//                    showMessage("Elemento " + i.getType() + " adicionado");
//                    break;
//            }else{
//                System.out.println("Not found!!");
//            }
//            
//        }
//       
//    }
//    
//    /**
//     * Removes an element from the selected instances list
//     *
//     * @param element
//     */
//    public void removeElement(Instance element) {
//        if(!selectedInstances.isEmpty()){
//            selectedInstances.remove(element);
//            instances.add(element);
//            showMessage("Elemento " + element.getType() + " removido");
//        }
//        else{
//            showMessage("Não existem elementos para ser removidos!");
//        }
//    }
//     /**
//     * Show message in growl component (View)
//     *
//     * @param msg
//     */
//    private void showMessage(String msg) {
//        FacesMessage message = new FacesMessage(msg, "");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    } 
//
//    /**
//     * @return the instance
//     */
//    public Instance getInstance() {
//        return instance;
//    }
//
//    /**
//     * @param instance the instance to set
//     */
//    public void setInstance(Instance instance) {
//        this.instance = instance;
//    }
//
//    /**
//     * @return the chosenInstanceId
//     */
//    public String getChosenInstanceId() {
//        return chosenInstanceId;
//    }
//
//    /**
//     * @param chosenInstanceId the chosenInstanceId to set
//     */
//    public void setChosenInstanceId(String chosenInstanceId) {
//        this.chosenInstanceId = chosenInstanceId;
//    }
//
//    /**
//     * @return the quantity
//     */
//    public int getQuantity() {
//        return quantity;
//    }
//
//    /**
//     * @param quantity the quantity to set
//     */
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    /**
//     * @return the objective
//     */
//    public int getObjective() {
//        return objective;
//    }
//
//    /**
//     * @param objective the objective to set
//     */
//    public void setObjective(int objective) {
//        this.objective = objective;
//    }
//}
