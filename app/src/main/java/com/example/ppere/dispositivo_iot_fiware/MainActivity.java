package com.example.ppere.dispositivo_iot_fiware;

import android.os.AsyncTask;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;
    public int FragmentControlId=-1;
    public int FragmentStatusId=-1;

    String LOG;

    //Addressing

    InetAddress IPL=null;
    // int puerto_de_escucha = 54545;
    int IPPORT=6070;
    ServerSocket sk;

    // autenticate
    String usertoken = "";
    String userdomain ="";




    public abstract static class command_handler
    {
        String name_handler;
        String lastvalue;

        public command_handler(String name) {
            name_handler=name;

        }

        abstract int execute(ArrayList<String> arguments);


        public String getName() {
            return name_handler;
        }
    };

    class handler_list 
    {
        ArrayList<command_handler> lista_handler = new ArrayList<command_handler>();

        public void add(command_handler cd)
        {
            lista_handler.add(cd);
        }
        
        public int execute(String name,ArrayList<String> params)
        { 
            int res;
            res=-1;
            for (int t=0;t<lista_handler.size();t++)
            {
                if (((command_handler) lista_handler.get(t)).getName().contains(name))
                    res=((command_handler) lista_handler.get(t)).execute(params);
            }
            return res; // error no existe el handler
        }
    };
    
    
    public handler_list hl=new handler_list();

    public void setCommand(command_handler cm) {hl.add(cm);}

    class ULParser {

     String line;


     public String getDevice()
     {

         return line.substring(0,line.indexOf("@"));

     }

        public void setLine(String line) {
            this.line = line;
        }

        public String getCommand()
        {
            return line.substring(1+line.indexOf("@"),line.indexOf("|"));
        }
        public List<String> getParams()
        {
            int f;
            String aux=line.substring(1+line.indexOf("|"));

            ArrayList<String> wholeList = new ArrayList<String>();

            do {
                f=aux.indexOf("|");
                if (f==-1)
                {
                    wholeList.add(aux);
                    aux="";
                }
                else {
                    String cad=aux.substring(0,f);
                    wholeList.add(cad);
                    aux=aux.substring(1+f);

                }

            } while(aux.length()>0);

            return wholeList;
        }

        public String getParamsValue(ArrayList<String> lista,String parametro){

            int ni=lista.size();
            for (int i=0;i<(ni);i++)
            {
                if (lista.get(i).contains(parametro+":"))
                    return lista.get(i).substring(lista.get(i).indexOf(":")+1);

            }
            return null;
        }

        public String getParamsNameAtIndex(ArrayList<String> lista,int i){

            int ni=lista.size();

            if (i<ni)
                if ((lista.get(i).indexOf(":"))>=0)
                    return lista.get(i).substring(0,lista.get(i).indexOf(":"));
                else // parametro sin nombre
                    return "unnamed";


            else 
                return null;
            
        }



    }



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG="";




// thread para almacenar la IP asignada
        new Thread(new Runnable() {
            public void run() {

                int i=0;
                while(true)
                {
                    IPL = getLocalAddress();
                    try {
                        // imprimirln("Linea "+i);
                        i++;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        ).start();

        //acceso a lazy attributes

// thread para commands y lectura de atributos bajo demanda
        new Thread(new Runnable() {
            public void run() {

                while (true) {
                    try {
                        while (true) {
                            InetAddress g1 = getLocalAddress();
                            sk = new ServerSocket(IPPORT, 0, g1);
                            StringBuilder sb = null, sbj = null;

                            while (!sk.isClosed()) {
                                sbj = null;
                                sb = null;
                                imprimirln("LISTEN ON "+IPPORT);
                                Socket cliente = sk.accept();

                                imprimirln("new conection");

                                BufferedReader entrada = new BufferedReader(
                                        new InputStreamReader(cliente.getInputStream()));

                                                                PrintWriter salida = new PrintWriter(
                                        new OutputStreamWriter(cliente.getOutputStream()), true);
                                String line = null;
                                sb = new StringBuilder();
                                try {
                                    do {
                                        line = entrada.readLine();
                                        sb.append(line + "\n");
                                        //hacking
                                        if ((sbj == null) && (line.contains("{"))) // date
                                            sbj = new StringBuilder();
                                        if (sbj != null) {
                                            sbj.append(line + "\n");
                                        }
                                        System.out.println("" + sb);
                                    }
                                    while (entrada.ready());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                //System.out.println("Fin de lectura");

                                ULParser parser=new ULParser();
                                parser.setLine(line);

                                //parser.setLine("weatherStation167@ping|param1:1924|param2:2785");

                                String dv=parser.getDevice();
                                String command=parser.getCommand();
                                ArrayList<String> lista= (ArrayList<String>) parser.getParams();

                                boolean valid;
                                int res;
                                valid=false;
                                for (int t=0;t<lista.size();t++)
                                {
                                    String cm=parser.getParamsNameAtIndex(lista,t);
                                    res=hl.execute(command,lista);
                                    if (res>=0) {valid=true; break;}
                                }

                                salida.print("HTTP/1.1 200 OK\r\n");
                                salida.print("Content-Length: "+line.length()+"\r\n");
                                salida.print("Connection: Closed\r\n");
                                salida.print("\r\n");
                                salida.println(line);
                                salida.flush();

                                cliente.close();
                            }
                            Thread.sleep(1000);

                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }

        }).start();



        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);


        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Controls"), TABControlFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Status"), TABStatusFragment.class, null);


        tabHost.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View v) {}

            @Override
            public void onViewAttachedToWindow(View v) {
                tabHost.getViewTreeObserver().removeOnTouchModeChangeListener(tabHost);
            }
        });



    }



String SouthBridge_SendMeasure(String ServerName,int port, String IP, String device,String apikey,String Attribute,String Value)
{
    Socket socket;
    String res;
    try {

        /*
        DELETE /iot/devices/ULSensor08 HTTP/1.1**Host: pperez2.disca.upv.
                es:4000**User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/2010
        0101 Firefox/55.0**Accept: application/json**Accept-Language: null**Accept-Encod
        ing: gzip, deflate**fiware-servicepath: /Alumbrado**fiware-service: Electricidad
                **Content-Length: 0**Content-Type: text/plain;charset=UTF-8**Cookie: UPV_IDIOMA=
                es; __utma=253516877.95715475.1466164257.1498826435.1499265046.19; __utmz=253516
        877.1466164257.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)**Connection: ke
        ep-alive****:1*/

        final String EndPoint="/iot/d?"+"k="+apikey+"&i="+device+"&d="+Attribute+"|"+Value;



        InetAddress serverAddr = InetAddress.getByName(ServerName);
        socket = new Socket(serverAddr, port);
        if (socket.isConnected()) {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),true);
            out.print("GET "+EndPoint+" HTTP/1.1\r\n");
            out.print("Host: "+IP+"\r\n");
            out.print("Connection: keep-alive\r\n");

            out.print("\r\n\r\n");
            out.flush();
            out.close();
            socket.close();

        }
        else res="Imposible abrir socket";


    } catch (UnknownHostException e1) {
        e1.printStackTrace();
    } catch (IOException e1) {
        e1.printStackTrace();
    }
    return "";

}



String NorthBridge_DELETE_VERB(String IP,String ServerName,int port, String EndPoint,String Headers) {
 Socket socket;
    String res;
    try {

        /*
        DELETE /iot/devices/ULSensor08 HTTP/1.1**Host: pperez2.disca.upv.
                es:4000**User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/2010
        0101 Firefox/55.0**Accept: application/json**Accept-Language: null**Accept-Encod
        ing: gzip, deflate**fiware-servicepath: /Alumbrado**fiware-service: Electricidad
                **Content-Length: 0**Content-Type: text/plain;charset=UTF-8**Cookie: UPV_IDIOMA=
                es; __utma=253516877.95715475.1466164257.1498826435.1499265046.19; __utmz=253516
        877.1466164257.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)**Connection: ke
        ep-alive****:1*/

        InetAddress serverAddr = InetAddress.getByName(ServerName);
        socket = new Socket(serverAddr, port);
        if (socket.isConnected()) {
            PrintWriter out = new PrintWriter(new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream())),true);
            out.print("DELETE "+EndPoint+" HTTP/1.1\r\n");
            out.print("Host: "+IP+"\r\n");
            out.print("Connection: keep-alive\r\n");
            out.print(Headers);
            out.print("\r\n\r\n");
            out.flush();
            out.close();
            socket.close();

        }
        else res="Imposible abrir socket";


    } catch (UnknownHostException e1) {
        e1.printStackTrace();
    } catch (IOException e1) {
        e1.printStackTrace();
    }
return "";
}

    class RegisterTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;
            int pos;

            pos = 0;
            String operation = (String) urls[0];
            String user_name = (String) urls[1];
            String user_passwd = (String) urls[2];
            String device_name = (String) urls[3];
            String service = (String) urls[4];
            String subservice = (String) urls[5];
            String IP = (String) urls[6];
            String PORT = (String) urls[7];

//            probar el registro que debería lanzar las server socket

            String protocol = "HTTP";
            String svrname = "pperez-seu-or.disca.upv.es";
            String svrport = "4061";

/*            svrname = "pperez2.disca.upv.es";
            svrport = "4000";
            operation="Unregister";
*/
            String device_id = device_name;
            String entity_name = device_id;
            String entity_type = "sensoractuator";

            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";
            String leng = null;
            String resp = "none";

            if (operation.contentEquals("Register")) {

                String svrendpoint = "iot/devices";
                String payload = "{\n" +
                        "    \"devices\": [\n" +
                        "      {\n" +
                        "        \"device_id\": \"" + device_id + "\",\n" +
                        "        \"entity_name\": \"" + entity_name + "\",\n" +
                        "        \"entity_type\": \"" + entity_type + "\",\n" +
                        "        \"endpoint\": \"http://" + IP + ":" + PORT + "\",\n" +
                        "        \"protocol\": \"HTTP_UL\",\n" +
                        "        \"attributes\": [\n" +
                        "            {\n" +
                        "                \"name\": \"Temperature\",\n" +
                        "                \"type\": \"celsius\"\n" +
                        "            } " +
                        "        ],\n" +
                        "        \"lazy\": [\n" +
                        "            {\n" +
                        "                \"name\": \"Humidity\",\n" +
                        "                \"type\": \"percentage\"\n" +
                        "            }" +
                        "        ],\n" +
                        "        \"commands\": [\n" +
                        "            {\n" +
                        "                \"name\": \"Velocity\",\n" +
                        "                \"type\": \"rpm\"\n" +
                        "            }, " +
                        "            {\n" +
                        "                \"name\": \"LEDS\",\n" +
                        "                \"type\": \"binary\"\n" +
                        "            } " +
                        "        ]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "}";

                imprimir("EE ");
                for (int t = 0; t < 7; t++) imprimir(urls[t] + " * ");
                imprimirln("OO ");
                imprimirln(payload);


                runOnUiThread(new Runnable() {
                    public void run() {
                        ToggleButton bt = (ToggleButton)  findViewById(R.id.buttonRegister);
                        bt.setChecked(false);
                    }
                });

                try {
                    leng = Integer.toString(payload.getBytes("UTF-8").length);

                    OutputStreamWriter wr = null;
                    BufferedReader rd = null;
                    StringBuilder sb = null;

                    URL url = null;
                    url = new URL(protocol + "://" + svrname + ":" + svrport + "/" + svrendpoint);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000); // miliseconds
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");

                    conn.setRequestProperty("fiware-service", service);
                    conn.setRequestProperty("fiware-servicepath", subservice);
                    conn.setRequestProperty("Accept", HeaderAccept);
                    conn.setRequestProperty("Content-type", HeaderContent);
                    conn.setRequestProperty("Content-Length", leng);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(payload.getBytes("UTF-8"));
                    os.flush();
                    os.close();


                    int rc = conn.getResponseCode();


                    if (rc != 200) // serrvidor no contesta
                    {
                        if ((rc==201)||(rc==409)) { // created
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ToggleButton bt = (ToggleButton)  findViewById(R.id.buttonRegister);
                                    bt.setChecked(true);
                                }
                            });
                        }

                        is = conn.getErrorStream();
                        if (is == null) is = conn.getInputStream();
                        if (is != null) {


                            rd = new BufferedReader(new InputStreamReader(is));
                            sb = new StringBuilder();

                            String line = null;
                            while ((line = rd.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            String result = sb.toString();
                            imprimirln("respuesta: " + result);
                        } else {
                            imprimirln("respuesta: SINRESPUESTA");
                        }
                    } else {
                        is = conn.getInputStream();
                        rd = new BufferedReader(new InputStreamReader(is));
                        sb = new StringBuilder();

                        String line = null;
                        while ((line = rd.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        String result = sb.toString();
                        imprimirln("respuesta: " + result);

                    }

                    // String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    imprimirln(e.getMessage());

                    e.printStackTrace();
                }

                return null;
            } else {//unregister
                String svrendpoint = "/iot/devices/"+device_id;



                imprimirln("Unregister");
                try {
                    leng = Integer.toString("".getBytes("UTF-8").length);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ToggleButton bt = (ToggleButton)  findViewById(R.id.buttonRegister);
                            bt.setChecked(true);
                        }
                    });


                    String Respuesta=NorthBridge_DELETE_VERB(IP,svrname,(new Integer(svrport)).intValue() , svrendpoint,"fiware-service: "+ service+"\r\n"+"fiware-servicepath: "+subservice+"\r\n"+"Accept: "+HeaderAccept+"\r\n");
                    if (Respuesta!=null)
                    {
                    imprimirln("respuesta: " + Respuesta);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ToggleButton bt = (ToggleButton)  findViewById(R.id.buttonRegister);
                                bt.setChecked(false);
                            }
                        });
                    }

                    // String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    imprimirln(e.getMessage());


                    e.printStackTrace();
                }

                return null;



            }


     }
    }


    class SouthBridgeUpdateTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;
            int pos;

            pos = 0;
            String operation = (String) urls[0];
            String user_name = (String) urls[1];
            String user_passwd = (String) urls[2];
            String device_name = (String) urls[3];
            String service = (String) urls[4];
            String subservice = (String) urls[5];
            String IP = (String) urls[6];
            String PORT = (String) urls[7];
            String Attribute=(String) urls[8];
            String Value=(String) urls[9];

            String protocol = "HTTP";
            String svrname = "pperez-seu-or.disca.upv.es";
            String svrport = "7896";

/*            svrname = "pperez2.disca.upv.es";
            svrport = "4000";
            operation="Unregister";
*/
            String device_id = device_name;
            String entity_name = "SensorSEU_" + device_id;
            String entity_type = "SensorActuator";

            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";
            String leng = null;
            String resp = "none";


            SouthBridge_SendMeasure(svrname,(new Integer(svrport)).intValue() , IP, device_id,"TEF",Attribute,Value);

        return null;
        }
    }



    // Funciones utiles



    // + * Returns a valid InetAddress to use for RMI communication. + * If the
    // system property java.rmi.server.hostname is set it is used. + * Secondly
    // InetAddress.getLocalHost is used. + * If neither of these are
    // non-loopback all network interfaces + * are enumerated and the first
    // non-loopback ipv4 + * address found is returned. If that also fails null
    // is returned.

    private static InetAddress getLocalAddress() {
        InetAddress inetAddr = null;

        //
        // 1) If the property java.rmi.server.hostname is set and valid, use it
        //

        try {
            //System.out.println("Attempting to resolve java.rmi.server.hostname");
            String hostname = System.getProperty("java.rmi.server.hostname");
            if (hostname != null) {
                inetAddr = InetAddress.getByName(hostname);
                if (!inetAddr.isLoopbackAddress()) {
                    return inetAddr;
                } else {
                    //System.out                     .println("java.rmi.server.hostname is a loopback interface.");
                }

            }
        } catch (SecurityException e) {
            System.out                    .println("Caught SecurityException when trying to resolve java.rmi.server.hostname");
        } catch (UnknownHostException e) {
            System.out
                    .println("Caught UnknownHostException when trying to resolve java.rmi.server.hostname");
        }

        // 2) Try to use InetAddress.getLocalHost
        try {
            //System.out                    .println("Attempting to resolve InetADdress.getLocalHost");
            InetAddress localHost = null;
            localHost = InetAddress.getLocalHost();
            if (!localHost.isLoopbackAddress()) {
                return localHost;
            } else {
                //System.out                        .println("InetAddress.getLocalHost() is a loopback interface.");
            }

        } catch (UnknownHostException e1) {
            System.out                    .println("Caught UnknownHostException for InetAddress.getLocalHost()");
        }

        // 3) Enumerate all interfaces looking for a candidate
        Enumeration ifs = null;
        try {
            //System.out                    .println("Attempting to enumerate all network interfaces");
            ifs = NetworkInterface.getNetworkInterfaces();

            // Iterate all interfaces
            while (ifs.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) ifs.nextElement();

                // Fetch all IP addresses on this interface
                Enumeration ips = iface.getInetAddresses();

                // Iterate the IP addresses
                while (ips.hasMoreElements()) {
                    InetAddress ip = (InetAddress) ips.nextElement();
                    if ((ip instanceof Inet4Address) && !ip.isLoopbackAddress()) {
                        return (InetAddress) ip;
                    }
                }
            }
        } catch (SocketException se) {
            System.out.println("Could not enumerate network interfaces");
        }

        // 4) Epic fail
        //System.out                .println("Failed to resolve a non-loopback ip address for this host.");
        return null;
    }





    synchronized void ponTextoTextView(final String cad, final int id) {
        final String g = cad;

        runOnUiThread(new Runnable() {
            public void run() {
                TextView ll = (TextView) findViewById(id);
                if (ll != null) ll.setText(cad);
            }
        });
    }

    synchronized void imprimir(final String cad) {
        final String g = cad;

        LOG=LOG+cad;

    }

    public synchronized  String getLOG() {return LOG;};


    public void imprimirln(final String cad) {
        imprimir(cad + "\r\n");
    }


}

