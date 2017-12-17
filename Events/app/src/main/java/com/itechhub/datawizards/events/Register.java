package com.itechhub.datawizards.events;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegister;
    private EditText etUsername, etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initialize();
    }

    void initialize() {
        etUsername = findViewById(R.id.etUserName);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Login.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (validateInpuut()) {
                    try {
                        new PostData().execute(ConstantVariables.HOST_MACHINE + "app/api/register/");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean validateInpuut() {

        return true;
    }

    public class PostData extends AsyncTask<String, String, String> {
        // This is the JSON body of the post
        JSONObject postData;

        // This is a constructor that allows you to pass in the JSON body
        // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection con = null;
            StringBuffer response = new StringBuffer();

            try {
                url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = "username=" + etUsername.getText().toString() +"firstname=" + etFirstName.getText().toString()
                        + "&lastname=" + etLastName.getText().toString() + "&emailaddress=" + etEmail.getText().toString()
                        + "password=" + etPassword.getText().toString();

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //print result
                System.out.println(response.toString());
                //
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return response.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Intent intent = new Intent(getApplicationContext(), Events.class);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "User successfully created..", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "User not created..", Toast.LENGTH_LONG).show();
            }
        }
    }
}
