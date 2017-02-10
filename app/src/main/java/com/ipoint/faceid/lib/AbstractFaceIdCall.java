package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by spe on 07.02.2017.
 */

public abstract class AbstractFaceIdCall {


    public static class FaceIdCallException extends Exception {

        public FaceIdCallException(String message) {
            super(message);
        }

        public FaceIdCallException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }

    interface Callback {
        void onSuccess(CallResult result);

        void onError(String error);
    }

    public abstract static class CallResult {

    }

    private Token token;

    private JSONObject params;

    private String url;

    private RequestQueue queue;

    private Callback callback;

    public AbstractFaceIdCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        this.token = token;
        this.params = params;
        this.url = url;
        this.queue = queue;
        this.callback = callback;
    }

    public void call() {
        callApi(true);
    }

    protected void callApi(final boolean firstCall) {
        FaceIdRequest request = new FaceIdRequest(url, params, token.getAccessToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(processResponse(response));
                        } catch (Exception e) {
                            callback.onError(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Http error. Code = " + error.networkResponse.statusCode +
                                ", data = " + String.valueOf(error.networkResponse.data));
                    }
                },
                new FaceIdRequest.TokenExpiredListener() {
                    @Override
                    public void onTokenExpired() {
                        if (firstCall) {
                            token.refreshToken(new Token.ReadyCallback() {
                                @Override
                                public void onReady() {
                                    callApi(false);
                                }

                                @Override
                                public void onError(String error) {
                                    callback.onError("Auth error token expired and refresh token failed.");
                                }
                            });
                        } else {
                            callback.onError("Auth error token expired after call refresh token.");
                        }
                    }
                });
        queue.add(request);
    }

    protected abstract CallResult processResponse(JSONObject response) throws FaceIdCallException;

}
