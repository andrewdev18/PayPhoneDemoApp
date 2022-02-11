package com.example.payphonedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    String phone;

    RecyclerView recyclerView;
    List<Curso> cursos;
    RequestQueue queue;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        phone = getIntent().getStringExtra("phone");
        Toast.makeText(getApplicationContext(), "Numero: " + phone, Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.listRecyclerView);
        cursos = new ArrayList<>();
        queue = Volley.newRequestQueue(getApplicationContext());

        LoadCourses();
    }

    private void LoadCourses(){
        String urlApi = "https://my-json-server.typicode.com/andrewdev18/PayPhoneDemoApp/db";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = response.getJSONArray("cursos");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);

                                Curso cursoTemp = new Curso();
                                cursoTemp.setIdCurso(json.getInt("id"));
                                cursoTemp.setNombre(json.getString("nombre"));
                                cursoTemp.setDescripcion(json.getString("descripcion"));
                                cursoTemp.setPrecio(json.getDouble("precio"));
                                cursoTemp.setImpuesto(json.getDouble("impuesto"));
                                cursoTemp.setUrlImagen(json.getString("urlImagen"));

                                cursos.add(cursoTemp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



                        adapter = new Adapter(getApplicationContext(), cursos, phone);
                        recyclerView.setAdapter(adapter);

                    }//Fin Response Listener
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorRespone: " + error.getMessage());
            }
        });

        queue.add(request);
    }

}