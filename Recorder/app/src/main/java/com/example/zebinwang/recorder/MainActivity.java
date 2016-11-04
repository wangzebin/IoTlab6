package com.example.zebinwang.recorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.ArrayList;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;


public class MainActivity extends Activity implements OnClickListener{

    private Button sender, record,twitter;
    private TextView txtSpeechInput,weatherInput;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private String request;
    private String previous;
    public MainActivity() {

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 200:
                    Toast.makeText(getApplicationContext(),
                            "Send Success!",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    private void initView() {
        record = (Button) findViewById(R.id.btn2);
        sender = (Button) findViewById(R.id.btn1);
        twitter = (Button) findViewById(R.id.btn3);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        weatherInput = (TextView) findViewById(R.id.weatherInput);

        record.setOnClickListener(this);
        sender.setOnClickListener(this);
        twitter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn2:
                promptSpeechInput();
                break;
            case R.id.btn1:
                try {
                    // td.sendGet(); //send HTTP GET Request
                    sender_message(); // send HTTP POST Request
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.btn3:
                try {
                    // td.sendGet(); //send HTTP GET Request
                    sender_twitter(); // send HTTP POST Request
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sender_twitter() throws IOException {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //code to do the HTTP request
                try {
                    if (request.equals("show last Twitter")) {
                        String url1 = "http://209.2.214.74";
                        URL obj1 = new URL(url1);
                        HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();

                        //add reuqest header
                        con1.setRequestMethod("POST");
                        con1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                        String urlParameters = URLEncoder.encode(previous, "UTF-8");

                        // Send post request
                        con1.setDoOutput(true);
                        DataOutputStream wr1 = new DataOutputStream(con1.getOutputStream());
                        wr1.writeBytes(urlParameters);
                        wr1.flush();
                        int responseCode1 = con1.getResponseCode();
                        System.out.println("Response Code : " + responseCode1);
                        mHandler.sendEmptyMessage(responseCode1);
                        wr1.close();
                    } else {
                        String url = "http://ec2-54-186-203-85.us-west-2.compute.amazonaws.com/twitter";
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        //add reuqest header
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        //String urlParameters = URLEncoder.encode("hahaha", "UTF-8");
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("description", request);
                        // Send post request
                        con.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                        wr.write(jsonParam.toString());
                        wr.flush();
                        int responseCode = con.getResponseCode();
                        System.out.println("Response Code : " + responseCode);
                        mHandler.sendEmptyMessage(responseCode);
                        wr.close();

                        String url1 = "http://209.2.214.74";
                        URL obj1 = new URL(url1);
                        HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();

                        //add reuqest header
                        con1.setRequestMethod("POST");
                        con1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                        String urlParameters = URLEncoder.encode(request, "UTF-8");

                        // Send post request
                        con1.setDoOutput(true);
                        DataOutputStream wr1 = new DataOutputStream(con1.getOutputStream());
                        wr1.writeBytes(urlParameters);
                        wr1.flush();
                        int responseCode1 = con1.getResponseCode();
                        System.out.println("Response Code : " + responseCode1);
                        mHandler.sendEmptyMessage(responseCode1);
                        wr1.close();
                        previous = request;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }

    private void sender_message() throws IOException {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //code to do the HTTP request
                try {
                    if (request.equals("weather") ) {
                        String url = "http://ec2-54-186-203-85.us-west-2.compute.amazonaws.com/weather";
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        //add reuqest header
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");

                        //String urlParameters = URLEncoder.encode("hahaha", "UTF-8");
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("description", "hahaha");
                        // Send post request
                        con.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                        wr.write(jsonParam.toString());
                        wr.flush();
                        int responseCode = con.getResponseCode();
                        System.out.println("Response Code : " + responseCode);
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine ;
                        while ((inputLine = in.readLine()) != null)
                            System.out.println(inputLine);
                        in.close();
                        mHandler.sendEmptyMessage(responseCode);
                        wr.close();
                    } else {
                        String url = "http://209.2.214.74";
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        //add reuqest header
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                        String urlParameters = URLEncoder.encode(request, "UTF-8");

                        // Send post request
                        con.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(urlParameters);
                        wr.flush();
                        int responseCode = con.getResponseCode();
                        System.out.println("Response Code : " + responseCode);
                        mHandler.sendEmptyMessage(responseCode);
                        wr.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    request = result.get(0);
                }
                break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

}