package com.example.arta.cloudproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static String risk;
    static String bpm;
    Button btnSend;
    Spinner spRisk,spBpm;
    StringRequest stringRequest;
    TextView tvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner element
        spRisk = (Spinner) findViewById(R.id.spRisk);
        spBpm = (Spinner) findViewById(R.id.spBpm);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvTime = (TextView) findViewById(R.id.tvTime);


        // Spinner click listener
        spRisk.setOnItemSelectedListener(new itemListener1());
        spBpm.setOnItemSelectedListener(new itemListener2());

        // Spinner Drop down elements
        List<String> categories_risk = new ArrayList<String>();
        List<String> categories_bpm = new ArrayList<String>();

        categories_risk.add("0");
        categories_risk.add("1");
        int def = 10;
        for(int i=1;i<=30;i++)
        {
            String val = String.valueOf(def);
            categories_bpm.add(val);
            def+=5;
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_risk);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_bpm);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spRisk.setAdapter(dataAdapter1);
        spBpm.setAdapter(dataAdapter2);



        risk = "";
        bpm = "";


        // url dari server
        String requestUrl = "http://140.113.208.143/bridge.php";

        // untuk tentukan mw post apa put
        stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // cek apakah server respon / sudah terima data
                // bs print sesuatu biar detect klo emgn server sudah receive
                Log.d("Volley-Result", ""+response); //the response contains the result from the server, a json string or any other object returned by your server
                if(response.equals("b1"))
                {
                    Toast.makeText(MainActivity.this,"Successfully executed",Toast.LENGTH_LONG).show();
                }
                else if(response.equals("b0"))
                {
                    Toast.makeText(MainActivity.this,"Something Wrong in host OS",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Something Wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // print error
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                // Set up nama variable post dan setup value yang akan dikirim datanya ke server
                postMap.put("risk", risk);
                postMap.put("bpm", bpm);
                //            postMap.put("param2", value2);
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };

        // panggil fungsinya
        btnSend.setOnClickListener(new clicklistener());

    }


    public static String getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate=dateFormat.format(date);
        //System.out.println("Current time of the day using Calendar - 24 hour format: "+ formattedDate);
        return formattedDate;
    }

    public class clicklistener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnSend)
            {
                Log.d("asd","btn");
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                tvTime.setText("Send Time : "+getCurrentTimeUsingCalendar());
                Toast.makeText(MainActivity.this,"Send Data",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class itemListener1 implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Log.d("asd",view.getId()+" "+item);
                Log.d("asd","sp risk "+item);
                risk = item;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            risk = String.valueOf(0);
            bpm = String.valueOf(100);
        }
    }

    public class itemListener2 implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Log.d("asd",view.getId()+" "+item);

                Log.d("asd","sp bpm "+item);
                bpm = item;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            risk = String.valueOf(0);
            bpm = String.valueOf(100);
        }
    }


}
