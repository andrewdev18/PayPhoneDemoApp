package com.example.payphonedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Random;

public class ConfirmationActivity extends AppCompatActivity {

    TextView lblNombreCurso;
    TextView lblDescripcionCurso;
    TextView lblSubtotal;
    TextView lblImpuestos;
    TextView lblTotal;

    Button btnEnviar;

    RequestQueue queue;
    int currentId = 0;
    Curso curso;
    String currentPhone;
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        queue = Volley.newRequestQueue(getApplicationContext());
        curso = new Curso();
        currentPhone = getIntent().getStringExtra("phone");

        AsignarElementos();
        GetCourseInfo(getIntent().getStringExtra("idCurso"));

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData();
            }
        });
    }

    private void AsignarElementos(){
        lblNombreCurso = findViewById(R.id.lblNombreCurso);
        lblDescripcionCurso = findViewById(R.id.lblDescripcionCurso);
        lblSubtotal = findViewById(R.id.lblSubtotal);
        lblImpuestos = findViewById(R.id.lblImpuestos);
        lblTotal = findViewById(R.id.lblTotal);
        btnEnviar = findViewById(R.id.btnEnviar);
    }

    private void SendData(){
        String urlApi = "https://pay.payphonetodoesposible.com/api/Sale";
        String headerKey = "Authorization";
        String headerValue = "Bearer 6Y5CN8y4zbiMj4HLkny2N0u7PiMAYsHPKk3XebrXo28qHNQIpyTD3ZIBte05IFouyrWZy7dZBBgWc7WLwT__QbNJn6_5TwXdcvIpemUHv7uespG--qivdauagks_LoztHUi1lEnJVJxnvwxI-oEGj6clzNI9tMi6RlTSdaXtHPs-HeupL9Cr13iUduGlwjerlLXr-4sKW37qLCZ3zhVD2FyWNBwt9z_d2qIf9mz0zM0KqMWjG-7Q72KFuBctYGcM14fXDkzpPI2-6sZdxU3COl2v7faZkPJaykfBTmlPY9hnuysdmSM_CsxyPhyPujMGrl5Y5l5Zb_7ZgmseMME_YES28T4";

        Random rnd = new Random();
        int next = rnd.nextInt(500);

        JSONObject jsonData = new JSONObject();
        try {
            ValidateNumber();
            jsonData.put("phoneNumber", currentPhone);
            jsonData.put("countryCode", 593);
            jsonData.put("clientUserId", clientId);
            jsonData.put("reference", "Transacción de testeo");
            jsonData.put("responseUrl", "https:/localhost/response.php");
            jsonData.put("amount", Integer.valueOf((int) ((curso.getPrecio()*100) + (curso.getImpuesto()*100))));
            jsonData.put("amountWithTax", Integer.valueOf((int) (curso.getPrecio()*100)));
            jsonData.put("amountWithoutTax", 0);
            jsonData.put("tax", Integer.valueOf((int) (curso.getImpuesto()*100)));
            jsonData.put("clientTransactionId", String.valueOf(next));
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                urlApi, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
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
    }

    private void GetCourseInfo(String identifier){
        String urlApi = "https://my-json-server.typicode.com/andrewdev18/PayPhoneDemoApp/cursos/" + identifier;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            curso.setIdCurso(response.getInt("id"));
                            curso.setNombre(response.getString("nombre"));
                            curso.setDescripcion(response.getString("descripcion"));
                            curso.setPrecio(response.getDouble("precio"));
                            curso.setImpuesto(response.getDouble("impuesto"));
                            curso.setUrlImagen(response.getString("urlImagen"));

                            lblNombreCurso.setText(curso.getNombre());
                            lblDescripcionCurso.setText(curso.getDescripcion());
                            lblSubtotal.setText(String.valueOf(curso.getPrecio()));
                            lblImpuestos.setText(String.valueOf(curso.getImpuesto()));
                            lblTotal.setText(String.valueOf(curso.getPrecio() + curso.getImpuesto()));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }//Fin Response Listener
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorRespone: " + error.getMessage());
            }
        });

        queue.add(request);
    }

    private void ValidateNumber(){

        String urlApi = "https://pay.payphonetodoesposible.com/api/Users/" + currentPhone + "/region/593";
        String headerKey = "Authorization";
        String headerValue = "Bearer 6Y5CN8y4zbiMj4HLkny2N0u7PiMAYsHPKk3XebrXo28qHNQIpyTD3ZIBte05IFouyrWZy7dZBBgWc7WLwT__QbNJn6_5TwXdcvIpemUHv7uespG--qivdauagks_LoztHUi1lEnJVJxnvwxI-oEGj6clzNI9tMi6RlTSdaXtHPs-HeupL9Cr13iUduGlwjerlLXr-4sKW37qLCZ3zhVD2FyWNBwt9z_d2qIf9mz0zM0KqMWjG-7Q72KFuBctYGcM14fXDkzpPI2-6sZdxU3COl2v7faZkPJaykfBTmlPY9hnuysdmSM_CsxyPhyPujMGrl5Y5l5Zb_7ZgmseMME_YES28T4";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            clientId = response.getString("documentId");
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
    }
}