package com.ipoint.faceid.lib;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spe on 07.02.2017.
 */

public class FaceIdRequest extends JsonObjectRequest {

    interface TokenExpiredListener {
        void onTokenExpired();
    }

    private String token;

    public FaceIdRequest(String url, JSONObject jsonRequest, String token, Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener, final TokenExpiredListener tokenExpiredListener) {
        super(url, jsonRequest, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401) {
                    tokenExpiredListener.onTokenExpired();
                } else {
                    errorListener.onErrorResponse(error);
                }
            }
        });
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "bearer " + token);
        headers.putAll(super.getHeaders());
        return headers;
    }

}
