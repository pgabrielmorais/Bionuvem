package br.unb.cic.bionimbuz.elasticity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import br.unb.cic.bionimbuz.elasticity.InstanceNEW;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

 
@ManagedBean(name = "instanceService")
@ApplicationScoped
public class InstanceService {
    
//    public void createInstance (String type){
//        
//        try {
//            AmazonAPI amazonapi = new AmazonAPI();
//            GoogleAPI googleapi = new GoogleAPI();
//
//            switch (type) {
//                case "t2.micro":
//                    System.out.println("amazon");
//                    amazonapi.createinstance("t2.micro");
//                    break;
//                case "n1-standard-1":
//                    System.out.println("google");
//                    googleapi.createinstance("n1-standard-1", "teste2");
//                    break;
//                default:
//                    System.out.println("Este não é um tipo válido!");
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(InstanceService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    }
   
    

    public List<InstanceNEW> getInstances() {
        List<InstanceNEW> list = new ArrayList<>();
         
        for (Instance instanceAWS : AmazonAPI.listinstances()) {
            InstanceNEW instance = new InstanceNEW();
            AmazonMonitoring api2 = new AmazonMonitoring();
            instance.setId(instanceAWS.getInstanceId());
            instance.setState(instanceAWS.getState().getName());
            int maxsize = instanceAWS.getInstanceId().length();
//            if (instanceAWS.getState().getName().equals("running") ) {
//                instance.setCPUutilization(api2.monitoring(instanceAWS.getInstanceId()).get(maxsize).toString());
//            } else {
//                instance.setCPUutilization("valor nao encontrado");
//            }
                instance.setCPUutilization("valor nao encontrado");

            list.add(instance);
        }
        return list;
    }

    
    public List<String> CPUutilization(){
    
        AmazonAPI api = new AmazonAPI();
        AmazonMonitoring api2 = new AmazonMonitoring();
        List<String> list = new ArrayList<>();
        
        for(int i = 0; i < api.listinstances().size(); i++){
            InstanceNEW instance = new InstanceNEW();
            
            String type = AmazonAPI.listinstances().get(i).getInstanceId();
            String teste = api2.monitoring(type).get(3).toString();
            instance.setCPUutilization(teste);
            list.add(teste);
    
        }
        return list;
    }
    
//    public String getCPUutilization(String type){
//    
////        AmazonMonitoring api2 = new AmazonMonitoring();
////        
////        String teste = api2.monitoring(type).get(3).toString();
////        
////        return teste;
//        
//        InstanceView teste = new InstanceView();
//        String id = teste.getSelectedInstance().id;
//        
//        AmazonMonitoring api2 = new AmazonMonitoring();
//        
//        String teste2 = api2.monitoring(type).get(3).toString();
//        
//    
//
//    }
    
    
         
}