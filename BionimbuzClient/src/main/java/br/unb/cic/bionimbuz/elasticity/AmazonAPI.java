package br.unb.cic.bionimbuz.elasticity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class AmazonAPI {
    
    public static AmazonEC2 EC2;
    public static Scanner user_input = new Scanner(System.in);
    public static String instanceid;
    public static KeyPair keyPair;
    public static int count = 1;
    private String ipInstance;
    
    public void setup() {
        try {
            final String credentialsFile = "/opt/shared/BionimbuzClient/target/BionimbuzClient-0.0.1-SNAPSHOT/resources/apiCredentials/AwsCredentials.properties";
            InputStream is = null;
            is = new FileInputStream(credentialsFile);
            final PropertiesCredentials credentials = new PropertiesCredentials(is);
            
            EC2 = new AmazonEC2Client(credentials);
            EC2.setEndpoint("ec2.sa-east-1.amazonaws.com");
            
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }// , IllegalArgumentException
    }
    
    public String createinstance(String type, String imageID) throws IOException {
        // Acho que tem que mudar isso aqui em
        // imageID = "ami-912db2fd";
        this.setup();
        String IP = null;
        try {
            
            // CREATE EC2 INSTANCES
            final RunInstancesRequest runInstancesRequest = new RunInstancesRequest().withInstanceType(type).withImageId(imageID).withMinCount(1).withMaxCount(1).withSecurityGroupIds("default");
            
            runInstancesRequest.setMonitoring(Boolean.TRUE);
            
            // .withKeyName("xebia-france")
            // .withUserData(Base64.encodeBase64String(myUserData.getBytes()))
            
            // System.out.println("Criando nova maquina BioninbuZ");
            // //String imageId = "ami-6e3bb102";
            // String imageId = "ami-912db2fd";
            //
            // int minInstanceCount = 1; //
            // int maxInstanceCount = 1;
            // RunInstancesRequest rir = new RunInstancesRequest(imageId, minInstanceCount, maxInstanceCount);
            // rir.setInstanceType(type);
            // rir.withSecurityGroups("default");
            // //rir.setMonitoring(Boolean.TRUE);
            // RunInstancesResult result = AmazonAPI.EC2.runInstances(rir);
            final RunInstancesResult result = AmazonAPI.EC2.runInstances(runInstancesRequest);
            
            System.out.println("waiting");
            
            System.out.println("OK");
            
            final List<com.amazonaws.services.ec2.model.Instance> resultInstance = result.getReservation().getInstances();
            
            String createdInstanceId = null;
            int idx = 1;
            
            for (final com.amazonaws.services.ec2.model.Instance ins : resultInstance) {
                
                createdInstanceId = ins.getInstanceId();
                
                System.out.println("New instance has been created: " + createdInstanceId);// print the instance ID
                
                final CreateTagsRequest createTagsRequest = new CreateTagsRequest();
                createTagsRequest.withResources(createdInstanceId) //
                        .withTags(new Tag("Name", "BioNimbuZ-" + idx));
                EC2.createTags(createTagsRequest);
                
                idx++;
                
                final DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(ins.getInstanceId());
                final DescribeInstancesResult result2 = EC2.describeInstances(request);
                final List<Reservation> list = result2.getReservations();
                
                final long startTime = System.currentTimeMillis();
                while (IP == null) {
                    for (final Reservation res : list) {
                        final List<com.amazonaws.services.ec2.model.Instance> instanceList = res.getInstances();
                        
                        for (final com.amazonaws.services.ec2.model.Instance instance : instanceList) {
                            
                            IP = instance.getPublicIpAddress();
                            
                            this.setIpInstance(instance.getPublicIpAddress());
                        }
                    }
                }
                final long estimatedTime = System.currentTimeMillis() - startTime;
                
                System.out.println("Instance Public IP :" + IP + " com tempo:" + estimatedTime);
                
            }
        } catch (final AmazonServiceException ase) {
            System.out.println("Caught Exception: " + ase.getMessage());
            System.out.println("Reponse Status Code: " + ase.getStatusCode());
            System.out.println("Error Code: " + ase.getErrorCode());
            System.out.println("Request ID: " + ase.getRequestId());
            System.out.println("Give a valid input");
            System.out.println("");
            // AmazonAPI.enteroption();
        }
        return IP;
    }
    
    public static List<com.amazonaws.services.ec2.model.Instance> listinstances() {
        final AmazonAPI api = new AmazonAPI();
        api.setup();
        
        final DescribeInstancesResult describeInstancesRequest = EC2.describeInstances();
        final List<Reservation> reservations = describeInstancesRequest.getReservations();
        final List<com.amazonaws.services.ec2.model.Instance> instances = new ArrayList<>();
        
        for (final Reservation reservation : reservations) {
            instances.addAll(reservation.getInstances());
        }
        
        return instances;
        
    }
    
    public String getIpInstance() {
        return this.ipInstance;
    }
    
    public void setIpInstance(String ipInstance) {
        this.ipInstance = ipInstance;
    }
    
    public void terminate(String instanceid) {
        this.setup();
        final TerminateInstancesRequest tir = new TerminateInstancesRequest().withInstanceIds(instanceid);
        EC2.terminateInstances(tir);
        System.out.println("Terminating the instance : " + instanceid);
    }
    
    public String createami(String instanceid) throws IOException {
        this.setup();
        
        // CreateSnapshotRequest ssr = new CreateSnapshotRequest(instanceid, instanceid);
        final CreateImageRequest cir = new CreateImageRequest(instanceid, instanceid);
        final CreateImageResult cires = EC2.createImage(cir);
        String imageid;
        imageid = cires.getImageId();
        final String imageID = cires.getImageId();
        System.out.println("The imageid of the newly created AMI is " + imageid);
        
        try {
            Thread.sleep(120000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageID;
    }
    
    public void executeElasticity(String id) throws IOException {
        this.setup();
        // this.createami(id);
        this.createinstance("t2.micro", this.createami(id));
        this.terminate(id);
        
    }
    
} // main end
