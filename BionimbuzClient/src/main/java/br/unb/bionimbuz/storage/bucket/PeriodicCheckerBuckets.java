/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.bionimbuz.storage.bucket;

import br.unb.bionimbuz.storage.bucket.methods.CloudMethodsAmazonGoogle;
import br.unb.bionimbuz.storage.bucket.CloudStorageMethods.*;
import br.unb.cic.bionimbuz.configuration.BionimbuzClientConfig;
import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lucas
 */
public class PeriodicCheckerBuckets implements Runnable {
    
    Logger LOGGER = LoggerFactory.getLogger(PeriodicCheckerBuckets.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static private List<BioBucket> bucketList = new ArrayList<>();
    
    private static String bucketsFolder;
    static private CloudStorageMethods methodsInstance;
    
    protected BionimbuzClientConfig config = ConfigurationRepository.getConfig();
    
    @Override
    public void run () {
        LOGGER.info("[PeriodicChecker] Starting Latency/Bandiwth check on buckets ");
        try {
            for (BioBucket aux : bucketList) {

                LOGGER.info("[PeriodicChecker] Checking Latency for bucket " + aux.getName());
                methodsInstance.CheckStorageLatency(aux);
                LOGGER.info("[PeriodicChecker] Latency for bucket " + aux.getName() + ": " + aux.getLatency());

                LOGGER.info("[PeriodicChecker] Checking Bandwith for bucket " + aux.getName());
                methodsInstance.CheckStorageBandwith(aux);
                LOGGER.info("[PeriodicChecker] Upload for bucket " + aux.getName() + ": " + (aux.getUpBandwith()/1024)/1024 + " MB/s" );
                LOGGER.info("[PeriodicChecker] Download for bucket " + aux.getName() + ": " + (aux.getDlBandwith()/1024)/1024 + " MB/s" );
            }
        } catch (Throwable t) {
            LOGGER.error("[PeriodicChecker] Exception: " + t.getMessage());
            t.printStackTrace();
        }
        LOGGER.info("[PeriodicChecker] Finishing Latency/Bandiwth check on buckets ");        
    }
    
    public void start () {

        //Instance methods object
        methodsInstance = new CloudMethodsAmazonGoogle();
        
        //Getting parameters from config file
        methodsInstance.setAuthFolder(config.getBucketsAuthFolder());
        methodsInstance.setGcloudFolder(config.getGcloudFolder());
        methodsInstance.setMyId("client");
        
        //Instance all buckets
        LOGGER.info("[PeriodicChecker] Instancing Buckets");
        InstanceBuckets();
        
    }
    
    private void InstanceBuckets() {
        
        BioBucket aux;
        
        //AMAZON
        //Brazil
        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-br2", (bucketsFolder + "bionimbuz-a-br2"));
        aux.setEndPoint("s3-sa-east-1.amazonaws.com");
        bucketList.add(aux);
//
//        //US
//        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-us", (bucketsFolder + "bionimbuz-a-us"));
//        bucketList.add(aux);
//
//        //EU
//        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-eu", (bucketsFolder + "bionimbuz-a-eu"));
//        aux.setEndPoint("s3.eu-central-1.amazonaws.com");
//        bucketList.add(aux);
        
        
//        //GOOGLE
//        //US
            aux = new BioBucket(StorageProvider.GOOGLE, "bustling-cosmos-151913.appspot.com", (bucketsFolder + "bustling-cosmos-151913.appspot.com"));
            bucketList.add(aux);

        //EU
//        aux = new BioBucket(StorageProvider.GOOGLE, "bionimbuz-g-eu", (bucketsFolder + "bionimbuz-g-eu"));
//        bucketList.add(aux);

    }   
    
    public static BioBucket getBestBucket (List<BioBucket> buckets) {
        BioBucket best = null;
        
        for (BioBucket aux : buckets) {
            
            if (best == null || (aux.getAvgBandwith() > best.getAvgBandwith())) 
                best = aux;
            
        }
        
        return best;
    }
    
    static public BioBucket findFile (String fileName) {
        
        
        List<BioBucket> buckets = new ArrayList<>();
        
        for (BioBucket bucket : bucketList) {

            File dataFolder = new File (bucket.getMountPoint() + "/data-folder/");

            for (File file : dataFolder.listFiles()) {
                if (fileName.equals(file.getName()))
                    buckets.add(bucket);
            }
        }

        if (buckets.isEmpty())
        {
            return null;
        }
        
        return getBestBucket(buckets);
    }

    public static List<BioBucket> getBucketList() {
        return bucketList;
    }
    
    public static BioBucket getBucket (String name) {
        for (BioBucket aux : bucketList) {
            if (aux.getName().equals(name)) {
                return aux;
            }
        }
        return null;
    }
    
    
}
