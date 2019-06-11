package com.example.arsam.cloudcomputing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pDialog = new ProgressDialog(this);
        Button btn_login, btn_signup;

        btn_login = (Button) findViewById(R.id.btn_login_signup);
        btn_signup = (Button) findViewById(R.id.btn_signup_signup);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText nameEditText = findViewById(R.id.et_name_signup),
                        emailEditText = findViewById(R.id.et_email_signup),
                        passwordEditText = findViewById(R.id.et_password_signup),
                        confirmEditText = findViewById(R.id.et_c_password_signup),
                        idEditText = findViewById(R.id.et_id_signup);


                if (passwordEditText.getText().toString().length() < 5) {
                    passwordEditText.setError("Password cannot be less than 5");
                } else {
                    if (passwordEditText.getText().toString().equals(confirmEditText.getText().toString())) {

                        Intent i = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                        //signupCall(idEditText.getText().toString(),nameEditText.getText().toString(),emailEditText.getText().toString(),passwordEditText.getText().toString());
                        //signup(SignupActivity.this,idEditText.getText().toString(),nameEditText.getText().toString(),emailEditText.getText().toString(),passwordEditText.getText().toString());

                        // signupCall(,,);
                    } else {
                        confirmEditText.setError("Password does not match");
                    }
                }

            }
        });
    }

    //________________________________________________________________________________________________________________________
    //signup call



//queue.add(postRequest);






    private void signup(final Context context, final String id, final String name, final String email, final String password) {


}

    public void signupCall(String id, String name, final String email , final String password){

        String url = Server.IP + "/signup?id=" + id + "&name=" + name + "&email="+ email + "&password=" + password;
        url = url.replaceAll(" ", "%20");

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("1")){
                    Toast.makeText(SignupActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                    //loginCall(email,password);
                    Intent i = new Intent(SignupActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignupActivity.this, "Incorrect Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject user = new JSONObject(response);
                        JSONArray products = user.getJSONArray("user");
                        JSONObject us = products.getJSONObject(0);

                        SharedPref sp = new SharedPref(SignupActivity.this);
                        sp.putIntPref("user_id",us.getInt("id"));
                        sp.putPref("user_name",us.getString("name"));
                        sp.putPref("user_email",us.getString("email"));
                        sp.putIntPref("user_role_id",us.getInt("role_id"));

                        Intent i = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Exception in login", Toast.LENGTH_SHORT).show();
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
