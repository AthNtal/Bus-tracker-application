package com.example.ds_2_test;


import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Broker implements Serializable,Runnable{
    static String id,ip,port;

    private static final long serialVersionUID = 8422543761152527428L;

    static ServerSocket publisherServer;
    static ServerSocket subscriberServer;

    static List<Topic> topics= new ArrayList<Topic>();;
    static List<Value> values ;

    static ArrayList<Topic> registeredTopics=new ArrayList<Topic>();
    static ArrayList<Broker> listOfBrokers=new ArrayList<Broker>();

    static boolean exit;

    public static void main(String[] args) {

        readTopic();

        // each broker now will have its hash
        listOfBrokers.add(new Broker("1","192.168.1.102","5555"));

        for (Broker b : listOfBrokers){
            b.calculateKeys(b.getIp()); //hashing
            System.out.println("Broker is connected to this IP: "+b.getIp());
            // a thread for each broker
            Thread t1 = new Thread(b);
            t1.start();

        }
    }


    public Broker() {

    }

    public Broker(String id, String ip,String port) {
        this.id = id;
        this.ip=ip;
        this.port=port;

    }

    public String getid() {
        return id;
    }

    public String getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPort(String port) {
        this.port = port;
    }

    // Broker is up and sockets are opened and sub and pub are accepted
    public  void run(){
        try {
            publisherServer = new ServerSocket(1111);
            subscriberServer = new ServerSocket(2222);


            Socket publisher = publisherServer.accept();
            publisherConnect(publisher);

            while (!exit) {
                Socket subscriber = subscriberServer.accept();
                subscriberConnect(subscriber);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //via SHA-1 function calculates the first byte of ip-topic and it does the comparison
    public static void calculateKeys(String IpOfBroker){
        MessageDigest crypt;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(IpOfBroker.getBytes());
            byte hash = crypt.digest()[0];

            System.out.println("This broker is responsible for the following lines:");

            for (Topic topic : topics) {
                crypt.reset();
                crypt.update(topic.getBusline().getBytes());
                if (hash>=crypt.digest()[0]) {
                    registeredTopics.add(topic);
                    System.out.println(topic.toString());
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    // connection with publisher
    public static void publisherConnect(Socket connect) {
        values = new ArrayList<Value>();
        try {
            //calculateKeys(Broker.ip);
            ObjectInputStream input = new ObjectInputStream(connect.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(connect.getOutputStream());

            //sending the topics to the publisher
            output.writeObject(registeredTopics);
            output.flush();


            Value value = (Value) input.readObject();
            System.out.println("Data loaded");
            while (value != null) {
                values.add(value);
                value = (Value) input.readObject();
            }

            input.close();
            output.close();
            connect.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //connecting to the subscriber
    public static void subscriberConnect(Socket connect) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(connect.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(connect.getInputStream());

            //sending the topics
            output.writeUnshared(registeredTopics);
            output.flush();

            //sending the list of the connected brokers
            output.writeUnshared(listOfBrokers);
            output.flush();

            String in = (String) input.readObject();
            // System.out.println("Subscriber connected here for line "+in+".");

            int i=0;
            for (Value value : values) {
                if (value.bus.getBuslineId().equals(in)) {
                    output.writeObject(value);
                    i++;
                }
            }


            output.writeObject(null);
            output.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //reading the topics from busLinesNew.txt
    public static void readTopic() {
        Topic topic=new Topic();

        File file = new File(new File("/Users/thanasisntales/Desktop/rt 2/app/src/main/java/com/example/rt/busLinesNew.txt").getAbsolutePath());

        try {

            Scanner fileInput = null;

            fileInput = new Scanner(file);

            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] tokens = line.split(",");
                topic = new Topic();

                topic.setBusline(tokens[1]);
                topics.add(topic);

            }

            fileInput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
