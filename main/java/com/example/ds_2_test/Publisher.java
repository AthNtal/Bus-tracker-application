package com.example.ds_2_test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Publisher  {

    static List<Bus> buses = new ArrayList<Bus>();
    static List<Topic> topics = new ArrayList<Topic>();
    static List<Value> values = new ArrayList<Value>();

    static ArrayList<Topic> registeredTopics=new ArrayList<Topic>();
    static ArrayList<Broker> listOfBrokers=new ArrayList<Broker>();

    static ObjectOutputStream output;
    static ObjectInputStream input;
    static Socket connection;

    static int port;
    static String ip;

    public static void main(String[] args) throws IOException {
        //local broker
        String Ip1 ="192.168.1.102";

        // 2nd's broker ip
        String Ip2 = "192.168.1.100";

        // 3rd's broker ip
        String Ip3 = "192.168.1.143";

        int  port =1111;

        // reading everything from files
        loadFile();

        //connections with brokers
        push(Ip1,port);
        //push(Ip2,port);
        // push(Ip3,port);


    }

    public Publisher(){}
    public Publisher(String ip,int port){
        this.ip=ip;
        this.port=port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public static void push(String IpOfBroker,int port) throws IOException {
        try {

            Socket connection = new Socket(IpOfBroker, port);
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());

            // reading the topics from broker
            registeredTopics =(ArrayList<Topic>)input.readObject();


            output.reset();

            //sending values to brokers
            for(Topic t: registeredTopics){
                for (Value value : values) {
                    if(t.getBusline().equals(value.bus.getBuslineId())) {
                        output.writeObject(value);
                        System.out.println(value.toString());
                    }
                }
            }
            output.writeObject(null);
            output.flush();

            Thread.sleep((int)( Math.random() * 500));

            connection.close();

        } catch (UnknownHostException e) {
            System.out.println("Wrong host!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error!");
            e.printStackTrace();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void loadFile() {
        File f = null;
        File f1 = null;
        BufferedReader reader = null;
        BufferedReader reader1 = null;

        Bus bus = new Bus();
        Topic topic = new Topic();
        Value value;

        File file = new File(new File("/Users/thanasisntales/Desktop/rt 2/app/src/main/java/com/example/rt/busPositionsNew.txt").getAbsolutePath());

        try {

            Scanner fileInput = null;

            fileInput = new Scanner(file);

            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] tokens = line.split(",");

                value = new Value();
                bus = new Bus();
                bus.setLineNumber(tokens[0]);
                bus.setRouterCode(tokens[1]);
                bus.setVehicleId(tokens[2]);
                bus.setInfo(tokens[5]);
                buses.add(bus);

                value.setLatitute(Double.parseDouble(tokens[3]));
                value.setLongitude(Double.parseDouble(tokens[4]));
                value.bus = bus;

                values.add(value);


            }
            fileInput.close();

            File file2 = new File(new File("/Users/thanasisntales/Desktop/rt 2/app/src/main/java/com/example/rt/busLinesNew.txt").getAbsolutePath());
            fileInput = new Scanner(file2);

            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] tokens = line.split(",");

                topic = new Topic();

                topic.setBusline(tokens[1]);
                topics.add(topic);

                for (Bus buss : buses) {
                    if (buss.getLineNumber().equals(tokens[0])) {
                        buss.setBuslineId(tokens[1]);
                        buss.setLineName(tokens[2]);
                    }
                }

            }
            fileInput.close();

            File file3 = new File(new File("/Users/thanasisntales/Desktop/rt 2/app/src/main/java/com/example/rt/RouteCodesNew.txt").getAbsolutePath());
            fileInput = new Scanner(file3);

            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] tokens = line.split(",");

                for (Bus buss : buses) {
                    if (buss.getLineNumber().equals(tokens[1])) {
                        buss.setRouteType(tokens[2]);
                    }
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
