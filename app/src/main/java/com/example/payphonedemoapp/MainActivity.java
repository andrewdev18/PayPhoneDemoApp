package com.example.payphonedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnAcceder;
    EditText txtPhone;

    String userId;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAcceder = findViewById(R.id.btnAcceder);
        txtPhone = findViewById(R.id.txtPhone);

        queue = Volley.newRequestQueue(getApplicationContext());
        userId = "#";

        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessSistem();
                /*if(ValidateNumber()){
                    AccessSistem();
                }else{
                    Toast.makeText(getApplicationContext(), "No existe el usuario", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }

    private void AccessSistem(){
        String number = txtPhone.getText().toString().trim();
        if(number.equals("")){
            number = "0968472213";
        }

        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.putExtra("phone", number);
        startActivity(intent);
    }

    private boolean ValidateNumber(){
        String number = txtPhone.getText().toString().trim();
        userId = "#";

        if(number.equals("")){
            number = "9999999999";
        }

        String urlApi = "https://pay.payphonetodoesposible.com/api/Users/" + number + "/region/593";
        String headerKey = "Authorization";
        String headerValue = "Bearer 6Y5CN8y4zbiMj4HLkny2N0u7PiMAYsHPKk3XebrXo28qHNQIpyTD3ZIBte05IFouyrWZy7dZBBgWc7WLwT__QbNJn6_5TwXdcvIpemUHv7uespG--qivdauagks_LoztHUi1lEnJVJxnvwxI-oEGj6clzNI9tMi6RlTSdaXtHPs-HeupL9Cr13iUduGlwjerlLXr-4sKW37qLCZ3zhVD2FyWNBwt9z_d2qIf9mz0zM0KqMWjG-7Q72KFuBctYGcM14fXDkzpPI2-6sZdxU3COl2v7faZkPJaykfBTmlPY9hnuysdmSM_CsxyPhyPujMGrl5Y5l5Zb_7ZgmseMME_YES28T4";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MainActivity.this.userId = response.getString("documentId");
                            System.out.println("Usuario: " + response.getString("documentId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }//Fin Response Listener
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorRespone: " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put(headerKey, headerValue);
                return headers;
            }
        };

        queue.add(request);

        return !userId.equals("#");
    }
}