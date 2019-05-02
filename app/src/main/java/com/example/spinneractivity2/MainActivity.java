package com.example.spinneractivity2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequest;
    private RequestQueue mRequest2;
    private ArrayList<String> estadosSpinner;
    private ArrayList<String> cidadesSpinner;
    private Spinner spinner;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        estadosSpinner = new ArrayList<>();
        cidadesSpinner = new ArrayList<>();
        spinner = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        mRequest = Volley.newRequestQueue(this);
        mRequest2 = Volley.newRequestQueue(this);

        jsonEstado();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estado = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), estado, Toast.LENGTH_LONG).show();
                jsonCidade(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cidade = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), cidade, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void jsonEstado() {
        String url = "https://api.myjson.com/bins/11tvog";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            estadosSpinner.clear();
                            JSONArray jsonArray = response.getJSONArray("estados");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject estados = jsonArray.getJSONObject(i);
                                String sigla = estados.getString("sigla");
                                String nome = estados.getString("nome");


                                estadosSpinner.add(nome + "," + sigla);

                            }
                            spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, estadosSpinner));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequest.add(request);
    }

    private void jsonCidade(final int position) {
        String url = "https://api.myjson.com/bins/11tvog";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cidadesSpinner.clear();
                            JSONArray jsonArray = response.getJSONArray("estados");
                            JSONObject cidades = jsonArray.getJSONObject(position);
                            String cidade = cidades.getString("cidades");

                            String cidadesIntermediario;
                            cidadesIntermediario = cidade.replace("[", "").replace("\"", "").replace("]", "");

                            String[] cidadesFinal;
                            cidadesFinal = cidadesIntermediario.split(",");


                            cidadesSpinner.addAll(Arrays.asList(cidadesFinal));

                            spinner2.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, cidadesSpinner));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequest2.add(request);
    }
}
