/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioe_testing;

//import com.qualcomm.ioeplatform.internal.Log;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import com.qualcomm.ioeplatform.peripheral.*;
import java.util.Map;
import javax.microedition.cellular.*;

/**
 *
 * @author Nick
 */
public class IoE_Testing extends MIDlet {
    
    private boolean bInitialized = false; 
    
    protected void destroyApp(boolean unconditional) {
        bInitialized = false;
    }    
    
    protected void startApp() {
        // check to make sure board is not already initialized
        if (bInitialized) {
            System.out.println("IoE_Testing:  app is already running");
        } else {
            System.out.println("IoE_Testing:  start app");
            bInitialized = true;
        }
        
//        // print out network subscribers
//        System.out.println("Subscribers");
//        for (Subscriber s : Subscriber.getSubscribers()){
//            for (Map.Entry<String, String> entrySet : s.getProperties().entrySet()) {
//                    String key = entrySet.getKey();
//                    String value = entrySet.getValue();
//                    System.out.println(key + "= " + value);
//            }
//            System.out.println();
//        }
//        
//        System.out.println("Registered Networks Before");
//        for (CellularNetwork cn : CellularNetwork.getAvailableNetworks()){
//            System.out.println(cn.getName());
//        }
//        System.out.println();
//        
//        System.out.println("Available Networks");
//        for (CellularNetwork cn : CellularNetwork.getAvailableNetworks()){
//            System.out.println(cn.getName());
//            
//            try{
//                // attempt to auto register
//                cn.register(null);
//                
//                // try to print out network information
//                for (Map.Entry<String, String> entrySet : cn.getCellProperties().entrySet()) {
//                    String key = entrySet.getKey();
//                    String value = entrySet.getValue();
//                    System.out.println(key + "= " + value);
//                    
//                }
//                System.out.println(cn.getSignalStrength());
//                System.out.println();
//            }catch(NetworkDisconnectedException e){
//                System.out.println("NetworkDisconnectedException");
//            }catch(IOException e){
//                System.out.println("IOException");
//            }
//        }
//        
//        System.out.println("Registered Networks After");
//        for (CellularNetwork cn : CellularNetwork.getAvailableNetworks()){
//            try{
//                System.out.println(cn.getName());
//                // try to print out network information
//                for (Map.Entry<String, String> entrySet : cn.getCellProperties().entrySet()) {
//                    String key = entrySet.getKey();
//                    String value = entrySet.getValue();
//                    System.out.println(key + "= " + value);
//                }
//                NetworkInterface ni = cn.getNetworkInterface();
//                AccessPoint[] aps = ni.getConnectedAccessPoints();
//                System.out.println("# APs: " + aps.length);
//                System.out.println("Status: " + cn.getStatus());
//                System.out.println("Signal Strengh: " + cn.getSignalStrength());
//            }catch(NetworkDisconnectedException e){
//                System.out.println("NetworkDisconnectedException");
//            }
//        }
        
        // print out all information about connected APs
        System.out.println("=== CONNECTED DEVICES ===");
        AccessPoint aps[] = AccessPoint.getAccessPoints(true); // if true, then only connected APs returned
        String[] propNames;
        int wifi = -1;
        int cellular = -1;
        System.out.println("# APs: " + aps.length);
        if(aps.length == 0){
            System.out.println("No APs to connect to");
            notifyDestroyed();
            return;
        }
        for (int i = 0; i < aps.length; i++) {
            System.out.println("Name: " + aps[i].getName());
            System.out.println("Type: " + aps[i].getType());
            if(aps[i].getType().equals("3GPP")){
                cellular = i; 
            }else if(aps[i].getType().equals("WIFI")){
                wifi = i;
            }
            propNames = aps[i].getPropertyNames();
            for (String p : propNames){
                System.out.println(p + ": " + aps[i].getProperty(p));
            }
            System.out.println();
        }
        
        System.out.println("Wifi #: " + wifi);
        System.out.println("Cellular #: " + cellular);
        
        // attempt to set up connection to cellular
//        ConnectionOption<String> apn = new ConnectionOption<>("apn", "c2.korem2m.com");
//        ConnectionOption<String> apnOperator = new ConnectionOption<>("apnoperator", null);
//        AccessPoint ap = AccessPoint.of("3GPP", apn, apnOperator);
//        System.out.println("Name: " + ap.getName() + ", Connected?: " + ap.isConnected());
//        try{
//            System.out.println(ap.connect());
//        }catch(IOException e){
//            System.out.println("IOException");
//        }

        
        // need the try to catch any IO exceptions
        try{
            // attempt to set up LED control
//            LEDControl ledControl = LEDControl.getInstance();
//            
//            // turn off all LEDs
//            ledControl.turnOff(LEDControl.LED_ALL);
           
            // attempt to connect to xively
//            ConnectionOption<AccessPoint> apOption = new ConnectionOption<>("AccessPoint", aps[0]);
//            if(wifi >=0){
//                System.out.println("Connect to Wifi");
//                apOption = new ConnectionOption<>("AccessPoint", aps[wifi]);
//            }else if(cellular >=0){
//                System.out.println("Connect to Cellular");
//                apOption = new ConnectionOption<>("AccessPoint", aps[cellular]);
//            }else{
//                System.out.println("No wireless networks to connect to");
//                notifyDestroyed();
//                return;        
//            }
//            SocketConnection sc = (SocketConnection) Connector.open("socket://api.xively.com:8081", apOption);
            SocketConnection sc = (SocketConnection) Connector.open("socket://api.xively.com:8081");
//            SocketConnection sc = (SocketConnection) Connector.open("socket://146.148.44.124:8888", apOption);

            // create input and output stream
            InputStream is = sc.openInputStream();
            OutputStream os = sc.openOutputStream();
            
            // check what you're connected to
            System.out.println("=== CONNECTED TO ===");
            if(sc.getAccessPoint().isConnected()){
                System.out.println(sc.getAccessPoint().getName());
                propNames = sc.getAccessPoint().getPropertyNames();
                for (String p : propNames){
                    System.out.println(p + ": " + sc.getAccessPoint().getProperty(p));
                }
                System.out.println();
            // if not connected, terminate 
            }else{
                notifyDestroyed();
                return;
            }
           
            System.out.println("=== START SENDING ===");
            // send data to xivley
            sendToXively(sc, is, os);
            
        // catch exceptions 
//        }catch(SensorException ex){
//            System.out.println("SensorException");
        }catch(IOException e){
            e.printStackTrace();
        }
        
        // terminate
        notifyDestroyed();
    }
    
    
    public void sendToXively(SocketConnection sc, InputStream is, OutputStream os){
        
        // set up variables for sending
        boolean done = false;
        String out = null;
        int pulse = -1;
        int spo2 = -1;
        int sigStr = -1;
        String s = null;   
        int i = 0;
        
        // start main sending loop
        while(!done){
            try{   


                // create sin wave for testing
                pulse = (int)((int) 25*Math.sin(Math.PI/6*i)+50);
//                spo2 = (int)((int) 25*Math.sin(Math.PI/6*i)+50);
                if(sc.getAccessPoint().isConnected()){
                    spo2 = 1;
                }else{
                    spo2 = 0;
                }
//                sigStr = (int)((int) 25*Math.sin(Math.PI/6*i)+50);
                sigStr = Integer.parseInt(sc.getAccessPoint().getProperty("signalstrength"));

                // create string to send 
                s = "{\"method\": \"put\","
                        + "\"resource\": \"/feeds/909656253\","
                        + "\"params\": {},"
                        + "\"headers\": {\"X-PachubeApiKey\":\"JhxKaGOzgOozj0LqDVReCJWyPU8Vg3xEgK9aDYRt3YJyKc9n\"},"
                        + "\"body\":{"
                            + "\"version\": \"1.0.0\","
                            + "\"datastreams\":["
                                + "{\"id\": \"LivePulse\",\"current_value\": \"" + pulse + "\"},"
                                + "{\"id\": \"LiveSpO2\",\"current_value\": \"" + spo2 + "\"},"
                                + "{\"id\": \"SignalStrength\",\"current_value\": \"" + sigStr + "\"}"
                            + "]"
                        + "},\"token\": \"n\"}";

                // write data to output stream
                os.write(s.getBytes());

                // flush output stream to ensure all data was sent
                os.flush();

                // get the acknowledgement that was returned
                out = getOutput(is);

                // check if acknowledgement says the send was a success
                if(out.contains("\"status\":200")){
                    System.out.println("Send Success: Pulse= " + pulse + ", SpO2= " + spo2 + ", SigStrength= " + sigStr);
    //                    ledControl.turnOn(LEDControl.LED0);  // turn on LED 0 if successful send
                //TODO if send failed, resend
                }else{
                    System.out.println("Send Failed");
                    System.out.println(out);
    //                    ledControl.turnOn(LEDControl.LED1);  // turn on LED 1 if failed send
                }

                // pause 10 seconds
                Thread.sleep(10000);

                // turn off all LEDs
    //                ledControl.turnOff(LEDControl.LED_ALL);

                // increment i up to the max value, then start over at 0
                i = (i+1)%Integer.MAX_VALUE;

                // shut down connection
                //sc.close();
            }catch(IOException e){
                e.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }  
        }
    } 
    
    // logs the returned status of the send
    // Parameters:
    // is - accepts input stream to get the output from
    // Output: 
    // String that was buffered from output stream
    public String getOutput(InputStream is){
        try{
            // create string buffer to read input stream into
            StringBuffer strBuf = new StringBuffer();
            
            // pause to wait for response to be sent
            Thread.sleep(1500);
            
            // loop while input stream has data buffered, add to string buffer
            while(is.available()>0){
                int value = is.read();
                char c = (char) value;
                strBuf.append(c);
            }
            
            // convert string buffer to string
            String retStr = strBuf.toString();
            
            // return output of output stream if available
            return retStr; 
            
        // catch IO exceptions    
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        // return null if nothing is being output
        return null;  
    }
}
