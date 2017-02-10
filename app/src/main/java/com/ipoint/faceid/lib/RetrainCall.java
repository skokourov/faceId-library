package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by spe on 08.02.2017.
 */

public class RetrainCall extends AbstractFaceIdCall {

    public static class RetrainResult extends CallResult{
        String message;

        public RetrainResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public RetrainCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        super(token, params, url, queue, callback);
    }

    @Override
    protected CallResult processResponse(JSONObject response) throws FaceIdCallException {
        try {
            if (response.has("message")) {
                return  new RetrainCall.RetrainResult(response.getString("message"));
            } else {
                throw  new FaceIdCallException(response.getString("err"));
            }
        } catch (JSONException e) {
            throw  new FaceIdCallException(e.getMessage(), e);
        }
    }
}
