package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddPatientActivity extends AppCompatActivity {

    private TextView status;
    private EditText name;
    private EditText surname;
    private EditText date;
    private EditText email;
    private EditText address;

    private RequestQueue requestQueue;
    private String url = "https://healthcare-dsgkcuetdvfxaae6.italynorth-01.azurewebsites.net/api/v1/patients";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        name = (EditText) findViewById(R.id.teName);
        surname = (EditText) findViewById(R.id.teSurname);
        date = (EditText) findViewById(R.id.teDate);
        status = (TextView) findViewById(R.id.status);
        email = (EditText) findViewById(R.id.teMail);
        address = (EditText) findViewById(R.id.teAddress);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public void addStudent(View view){
        this.status.setText("Posting to " + url);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("firstMidName", name.getText());
            jsonBody.put("lastName", surname.getText());
            jsonBody.put("dateOfBirth", date.getText());
            jsonBody.put("email", email.getText());
            jsonBody.put("address", address.getText());
            jsonBody.put("fullName", name.getText() + " " + surname.getText());

            final String mRequestBody = jsonBody.toString();

            status.setText(mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        status.setText(responseString);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "NePoznasGesla");
                    params.put("Content-Type", "application/json");
                    return params;
                }

            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


