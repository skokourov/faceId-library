package com.ipoint.faceid.lib;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ipoint.faceid.lib.FaceId.DEFAULT_SERVER_URL;

/**
 * Created by spe on 07.02.2017.
 */

public class Token {

    private static final String TOKEN_URL = "o/token/";

    public interface ReadyCallback {
        void onReady();
        void onError(String error);
    }

    private String accessToken;

    private String user;
    private String password;

    private String clientId = "demo";
    private String clientSecret = "demo";

    private RequestQueue queue;

    public Token(String user, String password, String clientId, String clientSecret, RequestQueue queue) {
        this.user = user;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.queue = queue;
    }


    public void refreshToken(final ReadyCallback readyCallback){
        StringRequest request = new StringRequest
                (Request.Method.POST, DEFAULT_SERVER_URL + TOKEN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            accessToken = jsonObject.getString("access_token");
                        } catch (JSONException e) {
                            readyCallback.onError(e.getMessage());
                            return;
                        }
                        readyCallback.onReady();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        readyCallback.onError(error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String s = clientId + ":" + clientSecret;
                headers.put("Authorization", "Basic " + Base64.encodeToString(s.getBytes(), Base64.DEFAULT));
                headers.putAll(super.getHeaders());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", user);
                params.put("password", password);
                return params;
            }
        };
        queue.add(request);

    }

    public String getAccessToken() {
        return accessToken;
    }
}
