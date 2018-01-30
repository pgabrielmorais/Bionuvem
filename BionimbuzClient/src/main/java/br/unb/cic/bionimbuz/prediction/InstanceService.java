package br.unb.cic.bionimbuz.prediction;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.joda.time.DateTime;
 
@ManagedBean(name = "InstaceServicePrediction")
@ApplicationScoped
public class InstanceService {
     
    private final static String[] provider;
    private final static String[] programa;
    private final static int[] delay;
    
     
    static {
        delay = new int [3];
        delay[0] = 5;
        delay[1] = 10;
        delay[2] = 30;
        
        provider = new String[3];
        provider[0] = "Amazon";
        provider[1] = "Azure";
        provider[2] = "Google";

         
        programa = new String[3];
        programa[0] = "sam2bad";
        programa[1] = "Bowtie";
        programa[2] = "Genome2Interval";
        
    }
    
    static List<Instance> list = new  ArrayList<Instance>();

    public InstanceService() {
    }
    
    public static void addInstances() {
    
        list.add(new Instance(getRandomId(), getRandomPrograma(), getRandomMem(), getRandomCpu(), getRandomPrice(), getRandomProvider(), getCreationTimer(), getDelay(),timetocreate(), isnow()));
    }
    
    public  List<Instance> returnInstances(){
        
        return list;
    }
    
    private static String getRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
     
    private static String getRandomPrograma() {
        return programa[(int) (Math.random() * 3)];
    }
    
    public static int getRandomMem() {
        return (int) (Math.random() * 10);
    }
    
    public static int getRandomCpu() {
        return (int) (Math.random() * 10);
    }
    
    public static int getRandomPrice() {
        return (int) (Math.random() * 100000);
    }
    
    private static String getRandomProvider() {
        return provider[(int) (Math.random() * 3)];
    }

    public static List<String> getProvider() {
        return Arrays.asList(provider);
    }
     
    public static List<String> getPrograma() {
        return Arrays.asList(programa);
    }

    private static int getDelay() {
        return delay[(int) (Math.random() * 3)];
    }

    public static DateTime getCreationTimer() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date()); // sets calendar time/date
//        
//        return cal.getTime();
    
            DateTime dt = new DateTime();
            return dt;
    }
    
    public static DateTime timetocreate(){
        DateTime dt = new DateTime();
        
        DateTime added = dt.plusHours(getDelay());
        
        
        
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date()); // sets calendar time/date
//        cal.add(Calendar.HOUR_OF_DAY, getDelay()); // adds one hour
//        System.out.println("delay"+getDelay());
//        //cal.getTime();
//        return cal.getTime();

        return added;
    }
    
    public static String isnow (){
        
        if (timetocreate().toString() == null ? getCreationTimer().toString() == null : timetocreate().toString().equals(getCreationTimer().toString())){
            return "sim";
        }else{
            return "nao";
        }
    
    }
    
}