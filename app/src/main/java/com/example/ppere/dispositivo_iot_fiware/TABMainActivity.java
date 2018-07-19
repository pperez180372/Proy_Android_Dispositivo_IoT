package com.example.ppere.dispositivo_iot_fiware;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/*

public class TABMainActivity extends Activity {

    TABMainActivity myself;

    //servers and services
    final String Protocol="HTTP";
    final String OrionSVRname="pperez-seu-or.disca.upv.es";



    String SubsID;
    String Subsnameentity;
    String Subsnameattribute;


    ServerSocket sk;

    float[] buffer = new float[3600];
    int puntero_vector = 0;
    int num_muestras = 0;
    Canvas grafica_1;
    Canvas grafica_2;
    int canvas_visible;


    Paint paintred;
    Paint paintwhite;
    BitmapDrawable BitmapD_1;
    BitmapDrawable BitmapD_2;


    int mx, Mx, my, My;
    int bmpdx = 200, bmpdy = 100;

    ToggleButton Botonsubscribe;
    Button Botonactualize;

    ToggleButton LED1, LED2, LED3, LED4;

    ToggleButton BotonSTEELSKIN;

    SeekBar seekValue;


    private void initializeVariables() {

        seekValue = (SeekBar) findViewById(R.id.seekBar1);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        myself = this;







        // crear los listeners

        initializeVariables();
        seekValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                TextView r=(TextView) (findViewById(R.id.textView_Value_Entity_Temperature));
                r.setText(""+progresValue);
                String nameresource = ((TextView) (findViewById(R.id.textView_rr_name))).getText().toString();
                String value_Temp = ((TextView) (findViewById(R.id.textView_Value_Entity_Temperature))).getText().toString();

                updateContextTask ay = new updateContextTask();
                ay.execute(nameresource,value_Temp);


            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        BotonSTEELSKIN = (ToggleButton) findViewById(R.id.LOGIN);
        BotonSTEELSKIN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (BotonSTEELSKIN.isChecked()) {
                    ToggleButton LOGIN = ((ToggleButton) (findViewById(R.id.LOGIN)));
                    String name = ((TextView) (findViewById(R.id.textView_user_name))).getText().toString();
                    String domain = "";//((TextView) (findViewById(R.id.textView_domain_name))).getText().toString();
                    String passwd = ((TextView) (findViewById(R.id.textView_passwd))).getText().toString();
                    LoginTask ay = new LoginTask();
                    ay.execute(name, domain, passwd);
                } else {
                    usertoken = "";
                    TextView tv = (TextView) (findViewById(R.id.textView_token));
                    tv.setText("");
                }

                // cuando termine de ejecutarse la clase será destruida si ya no tiene referencias, por ejemplo la primera de dos ejecuciones.
            }

            ;
        });




        Botonactualize = (Button) findViewById(R.id.buttonActualize);
        Botonactualize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nameresource = ((TextView) (findViewById(R.id.textView_rr_name))).getText().toString();
                String value_Temp = ((TextView) (findViewById(R.id.textView_Value_Entity_Temperature))).getText().toString();

                updateContextTask ay = new updateContextTask();
                ay.execute(nameresource,value_Temp);

                // cuando termine de ejecutarse la clase será destruida si ya no tiene referencias, por ejemplo la primera de dos ejecuciones.
            }

            ;
        });




        LED1 = (ToggleButton) findViewById(R.id.buttonLED1);
        LED1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String LED1_ST, LED2_ST, LED3_ST, LED4_ST;
                ToggleButton LED1 = ((ToggleButton) (findViewById(R.id.buttonLED1)));
                ToggleButton LED2 = ((ToggleButton) (findViewById(R.id.buttonLED2)));
                ToggleButton LED3 = ((ToggleButton) (findViewById(R.id.buttonLED3)));
                ToggleButton LED4 = ((ToggleButton) (findViewById(R.id.buttonLED4)));
                String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();

                if (LED1.isChecked())
                    LED1_ST = "1";
                else
                    LED1_ST = "0";

                if (LED2.isChecked())
                    LED2_ST = "1";
                else
                    LED2_ST = "0";
                if (LED3.isChecked())
                    LED3_ST = "1";
                else
                    LED3_ST = "0";
                if (LED4.isChecked())
                    LED4_ST = "1";
                else
                    LED4_ST = "0";

                updateContextTask ay = new updateContextTask();
                ay.execute(nameentity, LED1_ST, LED2_ST, LED3_ST, LED4_ST);

            }

            ;
        });

        LED2 = (ToggleButton) findViewById(R.id.buttonLED2);
        ((ToggleButton) findViewById(R.id.buttonLED2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String LED1_ST, LED2_ST, LED3_ST, LED4_ST;
                ToggleButton LED1 = ((ToggleButton) (findViewById(R.id.buttonLED1)));
                ToggleButton LED2 = ((ToggleButton) (findViewById(R.id.buttonLED2)));
                ToggleButton LED3 = ((ToggleButton) (findViewById(R.id.buttonLED3)));
                ToggleButton LED4 = ((ToggleButton) (findViewById(R.id.buttonLED4)));
                String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();

                if (LED1.isChecked())
                    LED1_ST = "1";
                else
                    LED1_ST = "0";

                if (LED2.isChecked())
                    LED2_ST = "1";
                else
                    LED2_ST = "0";
                if (LED3.isChecked())
                    LED3_ST = "1";
                else
                    LED3_ST = "0";
                if (LED4.isChecked())
                    LED4_ST = "1";
                else
                    LED4_ST = "0";

                updateContextTask ay = new updateContextTask();
                ay.execute(nameentity, LED1_ST, LED2_ST, LED3_ST, LED4_ST);

            }

            ;
        });
        LED3 = (ToggleButton) findViewById(R.id.buttonLED3);
        ((ToggleButton) findViewById(R.id.buttonLED3)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String LED1_ST, LED2_ST, LED3_ST, LED4_ST;
                ToggleButton LED1 = ((ToggleButton) (findViewById(R.id.buttonLED1)));
                ToggleButton LED2 = ((ToggleButton) (findViewById(R.id.buttonLED2)));
                ToggleButton LED3 = ((ToggleButton) (findViewById(R.id.buttonLED3)));
                ToggleButton LED4 = ((ToggleButton) (findViewById(R.id.buttonLED4)));
                String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();

                if (LED1.isChecked())
                    LED1_ST = "1";
                else
                    LED1_ST = "0";

                if (LED2.isChecked())
                    LED2_ST = "1";
                else
                    LED2_ST = "0";
                if (LED3.isChecked())
                    LED3_ST = "1";
                else
                    LED3_ST = "0";
                if (LED4.isChecked())
                    LED4_ST = "1";
                else
                    LED4_ST = "0";

                updateContextTask ay = new updateContextTask();
                ay.execute(nameentity, LED1_ST, LED2_ST, LED3_ST, LED4_ST);

            }

            ;
        });
        LED4 = (ToggleButton) findViewById(R.id.buttonLED4);
        ((ToggleButton) findViewById(R.id.buttonLED4)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String LED1_ST, LED2_ST, LED3_ST, LED4_ST;
                ToggleButton LED1 = ((ToggleButton) (findViewById(R.id.buttonLED1)));
                ToggleButton LED2 = ((ToggleButton) (findViewById(R.id.buttonLED2)));
                ToggleButton LED3 = ((ToggleButton) (findViewById(R.id.buttonLED3)));
                ToggleButton LED4 = ((ToggleButton) (findViewById(R.id.buttonLED4)));
                String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();

                if (LED1.isChecked())
                    LED1_ST = "1";
                else
                    LED1_ST = "0";

                if (LED2.isChecked())
                    LED2_ST = "1";
                else
                    LED2_ST = "0";
                if (LED3.isChecked())
                    LED3_ST = "1";
                else
                    LED3_ST = "0";
                if (LED4.isChecked())
                    LED4_ST = "1";
                else
                    LED4_ST = "0";

                updateContextTask ay = new updateContextTask();
                ay.execute(nameentity, LED1_ST, LED2_ST, LED3_ST, LED4_ST);

            }

            ;
        });

        // hilo de subscripción se crea y se queda dormido esperando conexiones la IP debe ser pública o accesible

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        ToggleButton r=(ToggleButton) (findViewById(R.id.AUTO_ACT));
                        String nameresource = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();

                        QueryContextTask ay = new QueryContextTask();
                        ay.execute(nameresource);

                        Thread.sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // hilo de actualizacion temperatura

        new Thread(new Runnable() {
            public void run() {

                while (true) {
                    try {
                        while (true) {

                            ToggleButton r=(ToggleButton) (findViewById(R.id.AUTO_ACT));
                            if (r.isChecked())
                            {
                                String nameresource = ((TextView) (findViewById(R.id.textView_rr_name))).getText().toString();
                                String value_Temp = ((TextView) (findViewById(R.id.textView_Value_Entity_Temperature))).getText().toString();

                                updateContextTask ay = new updateContextTask();
                                ay.execute(nameresource, value_Temp);
                            }
                            //imprimirln("TThread");
                            Thread.sleep(1000);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }




    @Override
    public void onStop() {
        super.onStop();
    }

    // *****************************************************************************************************************************************************************************************
    // * ****************************************************************************************************************************************************************************************
    // * Obtener el token de usuario una vez autenticado
    // * ****************************************************************************************************************************************************************************************
    // * ****************************************************************************************************************************************************************************************
    // * ****************************************************************************************************************************************************************************************
    // *

    class LoginTask extends AsyncTask<String, Void, String> {

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
            String username = (String) urls[0];
            String domainname = (String) urls[1];

            String passwd = (String) urls[2];
            imprimirln(username + "(" + domainname + ")=" + passwd);

            String payload = "{ \"auth\": {\"identity\": {\"methods\": [\"password\"],\"password\": {" +
                    "\"user\": {\"name\": \"" + username + "\",\"domain\": { \"name\": \"" + domainname + "\" }," +
                    "\"password\": \"" + passwd + "\"}}}}}";


            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";
            String leng = null;
            String resp = "none";

            try {
                leng = Integer.toString(payload.getBytes("UTF-8").length);

                OutputStreamWriter wr = null;
                BufferedReader rd = null;
                StringBuilder sb = null;

                URL url = null;
                url = new URL("http://pperez-seu-ks.disca.upv.es:5000/v3/auth/tokens");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // miliseconds
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept", HeaderAccept);
                conn.setRequestProperty("Content-type", HeaderContent);
                //conn.setRequestProperty("Fiware-Service", HeaderService);
                conn.setRequestProperty("Content-Length", leng);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.flush();
                os.close();


                int rc = conn.getResponseCode();

                resp = conn.getContentEncoding();
                is = conn.getInputStream();

                if (rc == 201) {
                    String token = conn.getHeaderField("X-Subject-Token");
                    usertoken = token;
                    userdomain=domainname;
                    System.out.println("Token: " + token);
                    ponTextoTextView(token, R.id.textView_token);

                } else {
                    usertoken = "";
                    userdomain="";
                    resp = "locaL: ERROR de conexión";
                    System.out.println("http response code error: " + rc + "\n");
                    ponTextoTextView("no hay token", R.id.textView_token);

                }

                // String nameentity = ((TextView) (findViewById(R.id.textView_rr_led_name))).getText().toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                usertoken = "";
                ponTextoTextView("IOExcep no hay token", R.id.textView_token);
                e.printStackTrace();
            }
            if (usertoken.length() == 0) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        ToggleButton bt = (ToggleButton) findViewById(R.id.LOGIN);
                        bt.setChecked(false);
                    }
                });
            }
            return resp;
        }


    }


    class Query_Orchestrator extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            String nameentity = (String) urls[0];

            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";

            String payload = "{\"entities\": [{\"type\": \"Sensor\",\"isPattern\": \"false\",\"id\": \"" + nameentity + "\"}]}";
            String leng = null;
            String resp = "none";
            imprimirln(payload);
            if (usertoken.length() == 0) {

                //imprimirln("acceso Directo a Orion");


                try {
                    leng = Integer.toString(payload.getBytes("UTF-8").length);

                    OutputStreamWriter wr = null;
                    BufferedReader rd = null;
                    StringBuilder sb = null;

                    URL url = null;

                    url = new URL("http://pperez-seu-or.disca.upv.es:1026/v1/queryContext");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000); // miliseconds
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");

                    conn.setRequestProperty("Accept", HeaderAccept);
                    conn.setRequestProperty("Content-type", HeaderContent);
                    //conn.setRequestProperty("Fiware-Service", HeaderService);
                    conn.setRequestProperty("Content-Length", leng);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(payload.getBytes("UTF-8"));
                    os.flush();
                    os.close();


                    int rc = conn.getResponseCode();

                    resp = conn.getContentEncoding();
                    is = conn.getInputStream();

                    if (rc == 200) {

                        resp = "OK";
                        //read the result from the server
                        rd = new BufferedReader(new InputStreamReader(is));
                        sb = new StringBuilder();

                        String line = null;
                        while ((line = rd.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        String result = sb.toString();


                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(result);

                            JSONArray jArray = jObject.getJSONArray("contextResponses");
                            JSONObject c = jArray.getJSONObject(0);
                            JSONObject jo = c.getJSONObject("contextElement");
                            JSONArray jArrayattr = jo.getJSONArray("attributes");
                            int nled=1;
                            int maxnled=4;



                            for (int i = 0; i < jArrayattr.length(); i++) {
                                String nameattribute="LED"+nled;
                                if (nled>=maxnled) break;
                                try {
                                    JSONObject ca = jArrayattr.getJSONObject(i);
                                    if (ca.getString("name").contains(nameattribute)) {

                                        final double va = ca.getDouble("value");

                                        final ToggleButton[] bt=new ToggleButton [4];
                                        bt[0]= (ToggleButton) findViewById(R.id.buttonLED1);
                                        bt[1]= (ToggleButton) findViewById(R.id.buttonLED2);
                                        bt[2]= (ToggleButton) findViewById(R.id.buttonLED3);
                                        bt[3]= (ToggleButton) findViewById(R.id.buttonLED4);
                                        // imprimirln(nameentity + "." + nameattribute + "=" + (float) va);
                                        final int nleds=nled;
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                                if (va>=0.5) {
                                                    bt[nleds].setChecked(true);
                                                    //imprimirln("LED"+nleds+" 1");
                                                }
                                                else
                                                {
                                                    bt[nleds].setChecked(false);
                                                    //imprimirln("LED"+nleds+" 0");
                                                }
                                            }
                                        });
                                        nled++;
                                        //nuevamuestra((float) va);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } // del for


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // cabeceras de recepcion
                        rr = conn.getHeaderFields();
                        System.out.println("headers: " + rr.toString());

                    } else {
                        resp = "ERROR de conexión";
                        System.out.println("http response code error: " + rc + "\n");

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return resp;
            }else{
                imprimirln("Not implemented");
                return "";
            }


        }
    }

    class QueryContextTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            String nameentity = (String) urls[0];

            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";

            String payload = "{\"entities\": [{\"type\": \"Sensor\",\"isPattern\": \"false\",\"id\": \"" + nameentity + "\"}]}";
            String leng = null;
            String resp = "none";
            imprimirln(payload);
            if (usertoken.length() == 0) {

                //imprimirln("acceso Directo a Orion");


                try {
                    leng = Integer.toString(payload.getBytes("UTF-8").length);

                    OutputStreamWriter wr = null;
                    BufferedReader rd = null;
                    StringBuilder sb = null;

                    URL url = null;

                    url = new URL("http://pperez-seu-or.disca.upv.es:1026/v1/queryContext");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000); // miliseconds
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");

                    conn.setRequestProperty("Accept", HeaderAccept);
                    conn.setRequestProperty("Content-type", HeaderContent);
                    //conn.setRequestProperty("Fiware-Service", HeaderService);
                    conn.setRequestProperty("Content-Length", leng);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(payload.getBytes("UTF-8"));
                    os.flush();
                    os.close();


                    int rc = conn.getResponseCode();

                    resp = conn.getContentEncoding();
                    is = conn.getInputStream();

                    if (rc == 200) {

                        resp = "OK";
                        //read the result from the server
                        rd = new BufferedReader(new InputStreamReader(is));
                        sb = new StringBuilder();

                        String line = null;
                        while ((line = rd.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        String result = sb.toString();


                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(result);

                            JSONArray jArray = jObject.getJSONArray("contextResponses");
                            JSONObject c = jArray.getJSONObject(0);
                            JSONObject jo = c.getJSONObject("contextElement");
                            JSONArray jArrayattr = jo.getJSONArray("attributes");
                            int nled=1;
                            int maxnled=4;



                            for (int i = 0; i < jArrayattr.length(); i++) {
                                String nameattribute="LED"+nled;
                                if (nled>=maxnled) break;
                                try {
                                    JSONObject ca = jArrayattr.getJSONObject(i);
                                    if (ca.getString("name").contains(nameattribute)) {

                                        final double va = ca.getDouble("value");

                                        final ToggleButton[] bt=new ToggleButton [4];
                                        bt[0]= (ToggleButton) findViewById(R.id.buttonLED1);
                                        bt[1]= (ToggleButton) findViewById(R.id.buttonLED2);
                                        bt[2]= (ToggleButton) findViewById(R.id.buttonLED3);
                                        bt[3]= (ToggleButton) findViewById(R.id.buttonLED4);
                                        // imprimirln(nameentity + "." + nameattribute + "=" + (float) va);
                                        final int nleds=nled;
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                                if (va>=0.5) {
                                                    bt[nleds].setChecked(true);
                                                    //imprimirln("LED"+nleds+" 1");
                                                }
                                                else
                                                {
                                                    bt[nleds].setChecked(false);
                                                    //imprimirln("LED"+nleds+" 0");
                                                }
                                            }
                                        });
                                        nled++;
                                        //nuevamuestra((float) va);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } // del for


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // cabeceras de recepcion
                        rr = conn.getHeaderFields();
                        System.out.println("headers: " + rr.toString());

                    } else {
                        resp = "ERROR de conexión";
                        System.out.println("http response code error: " + rc + "\n");

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return resp;
            }else{
                imprimirln("Not implemented");
                return "";
            }


        }
    }



    class updateContextTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;
            // View rootView = findViewById(R.layout.fragment_main);

            String name=urls[0];
            String value_Temp=urls[1];
            String NameEntity="";
            String NameAttrib="";

            // HttpUriRequest req = (HttpUriRequest) urls[0];
            int pos=name.indexOf(".");
            if (pos>=0) {
                NameEntity=name.substring(0,pos);
                NameAttrib=name.substring(pos+1,name.length());
                imprimirln("La entidad: --"+NameEntity+"--\n"+"El Attributo: --"+NameAttrib+"--");}

            String nameattribute_par = "temperature";
            float value=new Float(value_Temp).floatValue();
            String nameattribute;

            imprimirln("La entidad: --"+NameEntity+"--\n"+"El Attributo: --"+NameAttrib+"--");


            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";
            String payload_updateContext =
                    "{ \"contextElements\": [{\"type\": \"Sensor\", \"isPattern\": \"false\",\"id\": \""+NameEntity+"\",\"attributes\": [{\"name\":\""+NameAttrib+"\",\"type\": \"float\",\"value\": \""+value+" \"}]}],\"updateAction\": \"APPEND\"}";
            imprimirln(payload_updateContext);

            String leng = null;
            String resp=        "none";

            try {
                leng = Integer.toString(payload_updateContext.getBytes("UTF-8").length);

                OutputStreamWriter wr = null;
                BufferedReader rd = null;
                StringBuilder sb = null;


                URL url = null;

                url = new URL("http://pperez-seu-or.disca.upv.es:1026/v1/updateContext");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // miliseconds
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept", HeaderAccept);
                conn.setRequestProperty("Content-type", HeaderContent);
                //conn.setRequestProperty("Fiware-Service", HeaderService);
                conn.setRequestProperty("Content-Length", leng);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload_updateContext.getBytes("UTF-8"));
                os.flush();
                os.close();


                int rc = conn.getResponseCode();

                resp = conn.getContentEncoding();
                is = conn.getInputStream();

                if (rc == 200) {

                    resp = "OK";
                    //read the result from the server
                    rd = new BufferedReader(new InputStreamReader(is));
                    sb = new StringBuilder();

                    String line = null;
                    while ((line = rd.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();


                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(result);

                        JSONArray jArray = jObject.getJSONArray("contextResponses");
                        JSONObject c = jArray.getJSONObject(0);
                        JSONObject jo = c.getJSONObject("contextElement");
                        JSONArray jArrayattr = jo.getJSONArray("attributes");

                        for(int i = 0; i <  jArrayattr.length(); i++) {
                            try {
                                JSONObject ca = jArrayattr.getJSONObject(i);

                                if (ca.getString("name").contains(NameAttrib)) {

                                    final double va = ca.getDouble("value");
                                    // nuevamuestra((float) va);
                                    imprimirln(NameEntity + "." + NameAttrib + "=" + (float) va);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } // del for



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // cabeceras de recepcion
                    rr = conn.getHeaderFields();
                    System.out.println("headers: " + rr.toString());

                } else {
                    resp = "ERROR de conexión";
                    System.out.println("http response code error: " + rc + "\n");

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return resp;
        }
    }

    class unsubscribeContextTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            int len = 500;

            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";
            String payload =  "{\"subscriptionId\" : \""+urls[0]+"\"}";
            String leng = null;
            try {
                leng = Integer.toString(payload.getBytes("UTF-8").length);

                OutputStreamWriter wr = null;
                BufferedReader rd = null;
                StringBuilder sb = null;


                URL url = null;

                url = new URL("http://pperez-seu-or.disca.upv.es:1026/v1/unsubscribeContext");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 ); // miliseconds
                conn.setConnectTimeout(15000 );
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept", HeaderAccept);
                conn.setRequestProperty("Content-type", HeaderContent);
                //conn.setRequestProperty("Fiware-Service", HeaderService);
                conn.setRequestProperty("Content-Length", leng);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.flush();
                os.close();


                int rc = conn.getResponseCode();
                String resp = conn.getContentEncoding();
                is = conn.getInputStream();

                if (rc == 200) {

                    resp = "OK";


                    if (SubsID.matches(urls[0]))
                    {
                        SubsID=null;
                        Subsnameentity=null;
                        Subsnameattribute=null;

                    }

                    //read the result from the server
                    rd = new BufferedReader(new InputStreamReader(is));
                    sb = new StringBuilder();

                    String line = null;
                    while ((line = rd.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();


                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(result);

                        JSONObject jo = jObject.getJSONObject("statusCode");

                        final String err= jo.getString("reasonPhrase");
                        imprimirln(err);

                    } catch (JSONException e) {

                        imprimirln("Error parsing json");

                        e.printStackTrace();
                    }






                } else {
                    resp = "ERROR de conexión";
                    System.out.println("http response code error: " + rc + "\n");

                }



                return resp;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "error";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }
    }

    class subscribeContextTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            int len = 500;
            String nameresource=(String) urls[0];
            int pos=nameresource.indexOf(".");

            if (pos<0) {imprimirln("un recurso consiste en Entidad.Atributo");return "un recurso consiste en Entidad.Atributo";}

            String nameattribute = (String) urls[0].substring(pos+1);
            String nameentity= (String) urls[0].substring(0, pos);

            imprimirln(""+pos+" "+nameentity+"."+nameattribute);


            String HeaderAccept = "application/json";
            String HeaderContent = "application/json";

            if (sk == null)
                return "error socket no abierto";

            String urlEs = "http://" + sk.getInetAddress().getHostAddress() + ":" + sk.getLocalPort();
            String payload = "{\"entities\" : [{\"type\": \"Sensor\",\"isPattern\": \"false\",\"id\": \""+nameentity+"\"}],\"attributes\": [\"temperature\"], \"reference\": \"" + urlEs + "\", \"duration\": \"P1M\",\"notifyConditions\": [{    \"type\": \"ONCHANGE\", \"condValues\": [\"temperature\" ] } ], \"throttling\": \"PT5S\"}";
            imprimirln(payload);
            // String encodedData = URLEncoder.encode(payload, "UTF-8");
            // String encodedData = payload;
            String leng = null;
            try {
                try {
                    leng = Integer.toString(payload.getBytes("UTF-8").length);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                OutputStreamWriter wr = null;
                BufferedReader rd = null;
                StringBuilder sb = null;


                URL url = null;

                url = new URL("http://pperez-seu-or.disca.upv.es:1026/v1/subscribeContext");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // miliseconds
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept", HeaderAccept);
                conn.setRequestProperty("Content-type", HeaderContent);
                //conn.setRequestProperty("Fiware-Service", HeaderService);
                conn.setRequestProperty("Content-Length", leng);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.flush();
                os.close();


                int rc = conn.getResponseCode();
                String resp = conn.getContentEncoding();
                is = conn.getInputStream();

                if (rc == 200) {

                    resp = "OK";
                    //read the result from the server
                    rd = new BufferedReader(new InputStreamReader(is));
                    sb = new StringBuilder();

                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();


                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(result);

                        JSONObject jo = jObject.getJSONObject("subscribeResponse");
                        Subsnameentity= nameentity;
                        Subsnameattribute=nameattribute;
                        SubsID=jo.getString("subscriptionId");
                        final String subsIDl = jo.getString("subscriptionId");
                        imprimirln(subsIDl);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    resp = "ERROR de conexión";
                    System.out.println("http response code error: " + rc + "\n");

                }

                return resp;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "error";
        }
    }



}
*/