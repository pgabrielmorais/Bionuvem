/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbuz.storage.bucket;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.configuration.BionimbuzClientConfig;
import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.storage.bucket.CloudStorageMethods.StorageProvider;
import br.unb.cic.bionimbuz.storage.bucket.methods.CloudMethodsAmazonGoogle;

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
        LOGGER.info("[PeriodicChecker] Starting");
        //Instance methods object
        methodsInstance = new CloudMethodsAmazonGoogle();
        
        //Getting parameters from config file
        bucketsFolder = config.getBucketsFolder();
        methodsInstance.setAuthFolder(config.getBucketsAuthFolder());
        methodsInstance.setGcloudFolder(config.getGcloudFolder());
        methodsInstance.setMyId("client");
        
        //Instance all buckets
        LOGGER.info("[PeriodicChecker] Instancing Buckets");
        InstanceBuckets();
        
        // Cleaning possible mounted buckets from last execution
        for (BioBucket aux : bucketList) {
            try {
                methodsInstance.StorageUmount(aux);
            } catch (Throwable t) {
                // Ignore
            }
        }
        
        try {
            //Authenticate providers
            LOGGER.info("[PeriodicChecker] Authenticating Providers");
            for (StorageProvider aux : StorageProvider.values()) {
                methodsInstance.StorageAuth(aux);
            }

            //Mount all buckets
            LOGGER.info("[PeriodicChecker] Mounting Buckets");
            for (BioBucket aux : bucketList) {
                methodsInstance.StorageMount(aux);
            }
        } catch (Throwable t) {
            LOGGER.error("[PeriodicChecker] Exception: " + t.getMessage());
            t.printStackTrace();
        }
        
        scheduler.scheduleAtFixedRate(this, 0, 15, TimeUnit.MINUTES);
    }
    
    private void InstanceBuckets() {
        
        BioBucket aux;
        
        //AMAZON
        //Brazil
//        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-br", (bucketsFolder + "bionimbuz-a-br"));
//        aux.setEndPoint("s3-sa-east-1.amazonaws.com");
//        bucketList.add(aux);
//
//        //US
//        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-us", (bucketsFolder + "bionimbuz-a-us"));
//        bucketList.add(aux);
//
//        //EU
//        aux = new BioBucket(StorageProvider.AMAZON, "bionimbuz-a-eu", (bucketsFolder + "bionimbuz-a-eu"));
//        aux.setEndPoint("s3.eu-central-1.amazonaws.com");
//        bucketList.add(aux);
        
        
        //GOOGLE
        //US
        aux = new BioBucket(StorageProvider.GOOGLE, "bionimbuz-g-us", (bucketsFolder + "bionimbuz-g-us"));
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
    
    
}
