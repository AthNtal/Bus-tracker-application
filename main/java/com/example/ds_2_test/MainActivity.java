package com.example.ds_2_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {


    private Intent Map;
    private String txt = null;
    ProgressDialog progressDialog2;

    private EditText text;

    private Button button;

    static ArrayList<Topic> registeredTopics=new ArrayList<Topic>();
    static ArrayList<Broker> listOfBrokers=new ArrayList<Broker>();
    static ArrayList <Value> values = new ArrayList<Value>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text=(EditText) findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscriber subscriber = new Subscriber();

                txt = text.getText().toString(); // the line the user entered
                if(txt.matches("^(\\d{1,3})$")) { // line validation
                    subscriber.execute(txt);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Map = new Intent(v.getContext(),MapsActivity.class);



                    double[] lat1 = new  double[values.size()];
                    double[] lon1=new  double[values.size()];
                    for (int i=0;i<values.size(); i++ ) {
                        lat1[i] = values.get(i).getLatitute();
                        lon1[i] = values.get(i).getLongitude();


                    }


                    if (values.size()==0) {

                        //Line not found
                        progressDialog2 = ProgressDialog.show(MainActivity.this, "Error", "No available info for this line. Please type another one.");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog2.dismiss();
                            }
                        }, 3000); // 3000 milliseconds delay

                    }else {
                        //sending info to the map
                        Map.putExtra("Bus", values.get(0).bus.getBuslineId() + " " + values.get(0).bus.getLineName());
                        Map.putExtra("Lat", lat1);
                        Map.putExtra("Lon", lon1);




                        startActivityForResult(Map, 0); // initiate MapsActivity


                    }


                }else{
                    Toast.makeText(getApplicationContext(),"Error: Bus format isn't valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class Subscriber extends AsyncTask<String, String ,String >{
        private String resp;

        ProgressDialog progressDialog;

        String Ip1="192.168.1.102";
        String Ip2 = "192.168.1.143";
        //String Ip3="";

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("Waiting..");//calls onprogressUpdate

            String in = strings[0];
            if (!in.equals("0")) {
                register(Ip1,2222,in);
                //register(Ip2,2222,in);
                // register(Ip3,2222,in);

            }

            resp="Data passed successfully";
            return resp;
        }

        private  void register(String Ip,int port,String in) {
            try {

                Socket connection = new Socket(Ip, port);
                ObjectOutputStream socketout = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream socketin = new ObjectInputStream(connection.getInputStream());

                registeredTopics = (ArrayList<Topic>) socketin.readObject();
                listOfBrokers = (ArrayList<Broker>) socketin.readObject();

                socketout.writeObject(in);
                socketout.flush();

                Value value = (Value) socketin.readObject();
                while (value != null && !values.contains(value)) {
                    values.add(value);
                    value = (Value) socketin.readObject();
                }

                connection.close();

            } catch (UnknownHostException e) {
                System.out.println("Wrong host!");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO error!");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found!");
                e.printStackTrace();
            }
        }

        protected void onPostExecute(String result){
            progressDialog.dismiss();
        }
        // the middle step between MainActivity and MapsActivity
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Searching for line " + txt+  ". Please wait.");

        }

    }

}




