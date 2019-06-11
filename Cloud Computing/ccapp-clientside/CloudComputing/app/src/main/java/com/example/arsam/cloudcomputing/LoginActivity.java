package com.example.arsam.cloudcomputing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Title Bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Hide the Status Bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pDialog = new ProgressDialog(this);

        Button btn_login, btn_signup;

        btn_login = (Button) findViewById(R.id.btn_login_login);
        btn_signup = (Button) findViewById(R.id.btn_signup_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = (EditText) findViewById(R.id.et_email_login),
                        passwordEditText = (EditText) findViewById(R.id.et_password_login);

                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                //loginCall(usernameEditText.getText().toString(),passwordEditText.getText().toString());

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void loginCall(String email, String password){

        String url = Server.IP + "/login?email=" + email + "&pass=" + password;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("user not found")){
                    Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject user = new JSONObject(response);
//                        JSONArray products = user.getJSONArray("user");
//                        JSONObject us = products.getJSONObject(0);
//
//                        SharedPref sp = new SharedPref(LoginActivity.this);
//                        sp.putIntPref("user_id",us.getInt("id"));
//                        sp.putPref("user_name",us.getString("name"));
//                        sp.putPref("user_email",us.getString("email"));
//                        sp.putIntPref("user_role_id",us.getInt("role_id"));

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Exception in login", Toast.LENGTH_SHORT).show();
                    }
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
