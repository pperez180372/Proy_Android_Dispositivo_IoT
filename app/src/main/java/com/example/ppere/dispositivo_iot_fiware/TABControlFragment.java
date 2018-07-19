package com.example.ppere.dispositivo_iot_fiware;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ppere on 19/09/2017.
 */

public class TABControlFragment extends Fragment {




    //GUI

    ToggleButton BotonRegister;
    SeekBar SeekBarTemperature;
    Button BotonActualiza;
    ToggleButton BotonAUTO;

    ToggleButton BotonLOGIN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.control, container, false);


        /* definición de comandos */

        MainActivity.command_handler ch1=new MainActivity.command_handler("LEDS") {
            @Override
            int execute(ArrayList<String> params) {
                final boolean s1,s2,s3,s4;
                final ToggleButton bt1,bt2,bt3,bt4;

                final String value=params.get(0);

                s1=s2=s3=s4=false;
                bt1=(ToggleButton)view.findViewById(R.id.buttonLED1);
                bt2=(ToggleButton)view.findViewById(R.id.buttonLED2);
                bt3=(ToggleButton)view.findViewById(R.id.buttonLED3);
                bt4=(ToggleButton)view.findViewById(R.id.buttonLED4);

                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    public void run() {


                        if (value.charAt(0)=='1')  bt1.setChecked(true); else  bt1.setChecked(false);
                        if (value.charAt(1)=='1')  bt2.setChecked(true); else  bt2.setChecked(false);
                        if (value.charAt(2)=='1')  bt3.setChecked(true); else  bt3.setChecked(false);
                        if (value.charAt(3)=='1')  bt4.setChecked(true); else  bt4.setChecked(false);

                    }
                });
                return 0;
            }
        };

        MainActivity.command_handler ch2=new MainActivity.command_handler("Velocity") {
            @Override
            int execute(ArrayList<String> params) {

                final String value=params.get(0);

                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    public void run() {
                        TextView tv = (TextView) view.findViewById(R.id.textView_Value_Entity_Velocity);
                        tv.setText(value);

                        SeekBar sb = (SeekBar) view.findViewById(R.id.seekBar_Velocity);
                        sb.setProgress(Integer.parseInt(value));
                    }

                    ;
                });
                return 0;
            }
        };

        ((MainActivity)getActivity()).setCommand(ch1);
        ((MainActivity)getActivity()).setCommand(ch2);

        SeekBarTemperature = (SeekBar) view.findViewById(R.id.seekBar_Temperature);
        SeekBarTemperature.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener() {

                                                            @Override
                                                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                                                System.out.println("progress"+progress);
                                                                TextView tv = (TextView) view.findViewById(R.id.textView_Value_Entity_Temperature);
                                                                tv.setText(""+progress);

                                                            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("progress"+seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("progress"+seekBar);
            }
        });

//////////////////////////// Login
////////////////////////////////////////////////////
        BotonRegister = (ToggleButton) view.findViewById(R.id.buttonLOGIN);
        BotonRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

             String user_name = ((TextView) (view.findViewById(R.id.textView_user_name))).getText().toString();
             String user_passwd = ((TextView) (view.findViewById(R.id.textView_passwd))).getText().toString();

             if (((MainActivity)getActivity()).IPL!=null) {

                 String IPs = ((MainActivity)getActivity()).IPL.toString();
                 IPs = IPs.substring(1);
                 MainActivity.Register.RegisterTask ay;
                    ay =((MainActivity)getActivity()).new RegisterTask();
                    if (BotonRegister.isChecked()) {
                        ay.execute("Register", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT);
                    } else {
                        ay.execute("Unregister", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT);
                    }
                }
                else {

                    ((MainActivity)getActivity()).imprimirln("No hay IP asignada (compruebe la conexión IP)");

                }
                // cuando termine de ejecutarse la clase será destruida si ya no tiene referencias, por ejemplo la primera de dos ejecuciones.
            } // del if


            ;
        });



        BotonRegister = (ToggleButton) view.findViewById(R.id.buttonRegister);
        BotonRegister.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {




                String user_name = ((TextView) (view.findViewById(R.id.textView_user_name))).getText().toString();
                String user_passwd = ((TextView) (view.findViewById(R.id.textView_passwd))).getText().toString();
                String device_name = ((TextView) (


                        view.findViewById(R.id.textView_device_name))).getText().toString();
                String service = ((TextView) (view.findViewById(R.id.textView_service))).getText().toString();
                String subservice = ((TextView) (view.findViewById(R.id.textView_subservice))).getText().toString();

                if (((MainActivity)getActivity()).IPL!=null) {
                    String IPs = ((MainActivity)getActivity()).IPL.toString();
                    IPs = IPs.substring(1);
                    MainActivity.RegisterTask ay;
                    ay =((MainActivity)getActivity()).new RegisterTask();
                    if (BotonRegister.isChecked()) {
                        ay.execute("Register", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT);
                    } else {
                        ay.execute("Unregister", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT);
                    }
                }
                else {

                    ((MainActivity)getActivity()).imprimirln("No hay IP asignada (compruebe la conexión IP)");

                }
                // cuando termine de ejecutarse la clase será destruida si ya no tiene referencias, por ejemplo la primera de dos ejecuciones.
            } // del if


            ;
        });

        BotonActualiza= (Button) view.findViewById(R.id.buttonActualize);
        BotonActualiza.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                String user_name = ((TextView) (view.findViewById(R.id.textView_user_name))).getText().toString();
                String user_passwd = ((TextView) (view.findViewById(R.id.textView_passwd))).getText().toString();
                String device_name = ((TextView) (view.findViewById(R.id.textView_device_name))).getText().toString();
                String service = ((TextView) (view.findViewById(R.id.textView_service))).getText().toString();
                String subservice = ((TextView) (view.findViewById(R.id.textView_subservice))).getText().toString();


                String Value = ((TextView) (view.findViewById(R.id.textView_Value_Entity_Temperature))).getText().toString();
                String Attribute = ((TextView) (view.findViewById(R.id.textView_rr_name))).getText().toString();;


                if (((MainActivity)getActivity()).IPL!=null) {
                    String IPs = ((MainActivity)getActivity()).IPL.toString();
                    IPs = IPs.substring(1);
                    MainActivity.SouthBridgeUpdateTask ay;
                    ay =((MainActivity)getActivity()).new SouthBridgeUpdateTask();
                    ay.execute("SBUpdate", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT,Attribute,Value);

                }
                else {

                    ((MainActivity)getActivity()).imprimirln("No hay IP asignada (compruebe la conexión IP)");

                }
                // cuando termine de ejecutarse la clase será destruida si ya no tiene referencias, por ejemplo la primera de dos ejecuciones.
            } // del if


            ;
        });


        new Thread(new Runnable() {
            public void run() {
                int t=0;
                String Value;
                try {
                    while (true) {
                        t++;
                        ToggleButton r=(ToggleButton) (view.findViewById(R.id.AUTO_ACT));

                        if (r.isChecked())
                        {
                            String user_name = ((TextView) (view.findViewById(R.id.textView_user_name))).getText().toString();
                            String user_passwd = ((TextView) (view.findViewById(R.id.textView_passwd))).getText().toString();
                            String device_name = ((TextView) (view.findViewById(R.id.textView_device_name))).getText().toString();
                            String service = ((TextView) (view.findViewById(R.id.textView_service))).getText().toString();
                            String subservice = ((TextView) (view.findViewById(R.id.textView_subservice))).getText().toString();

                            ToggleButton rr=(ToggleButton) (view.findViewById(R.id.AUTO_RANDOM));

                            if (rr.isChecked())
                            {
                                final String f;
                                Value = ""+(25+10*Math.sin((float)t/10.0*2.0*3.1415927)+3.0*Math.random());
                                t=t%10;
                                f=Value;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //stuff that updates ui
                                        ((TextView) (view.findViewById(R.id.textView_Value_Entity_Temperature))).setText(f);
                                    }
                                });
                            }else
                                Value = ((TextView) (view.findViewById(R.id.textView_Value_Entity_Temperature))).getText().toString();
                            String Attribute = ((TextView) (view.findViewById(R.id.textView_rr_name))).getText().toString();;

                            if (((MainActivity)getActivity()).IPL!=null) {
                                String IPs = ((MainActivity)getActivity()).IPL.toString();
                                IPs = IPs.substring(1);
                                MainActivity.SouthBridgeUpdateTask ay;
                                ay =((MainActivity)getActivity()).new SouthBridgeUpdateTask();
                                ay.execute("SBUpdate", user_name, user_passwd, device_name, service, subservice, IPs,""+((MainActivity)getActivity()).IPPORT,Attribute,Value);
                            }
                            else  ((MainActivity)getActivity()).imprimirln("No hay IP asignada (compruebe la conexión IP)");
                        };
                        Thread.sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();





        return view;
    }


}


