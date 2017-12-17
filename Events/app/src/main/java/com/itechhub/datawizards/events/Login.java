package com.itechhub.datawizards.events;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnRegister;
    private EditText etUsername, etPassword;
    private String username, password;
    private ArrayList<User> users = new ArrayList<>();
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize();
    }

    void initialize() {
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.login);
        btnRegister = findViewById(R.id.loginRegister);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (validateInpuut()) {
                    username = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    try {
                        new PostData().execute(ConstantVariables.HOST_MACHINE + "app/api/api_auth/" + username + "/" + password + "/");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.loginRegister:
                startActivity(new Intent(getBaseContext(), Register.class));
                finish();
                break;
            default:
                break;
        }
    }

    private class PostData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String jsonObject = buffer.toString();
                if (!jsonObject.isEmpty()) {
                    String finalObject = "{" + '"' + "user" + '"' + ": " + jsonObject + "}";
                    JSONObject jsonParent = new JSONObject(finalObject);
                    JSONArray jsonArray = jsonParent.getJSONArray("user");

                    JSONObject object = jsonArray.getJSONObject(0);
                    System.out.println(object.toString());

                    String field = object.getString("fields");
                    jsonParent = new JSONObject("{user:[" + field + "]}");

                    jsonArray = jsonParent.getJSONArray("user");
                    object = jsonArray.getJSONObject(0);

                    String name = object.getString("first_name");
                    String surname = object.getString("last_name");
                    String address = object.getString("email");
                    String username = object.getString("username");

                    //(String name, String surname, String address, String username)
                    user = new User(name, surname, address, username);
                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (user != null) {
                Intent intent = new Intent(getApplicationContext(), Events.class);
                intent.putExtra("_user", user);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Access Granted", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Wrong login details", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateInpuut() {


        return true;
    }

}
