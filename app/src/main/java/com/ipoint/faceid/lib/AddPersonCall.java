package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by spe on 08.02.2017.
 */

public class AddPersonCall extends AbstractFaceIdCall {

    public static class AddPersonResult extends CallResult{
        String id;

        public AddPersonResult(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public AddPersonCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        super(token, params, url, queue, callback);
    }

    @Override
    protected CallResult processResponse(JSONObject response) throws FaceIdCallException {
        try {
            if (response.has("id")) {
                return  new AddPersonCall.AddPersonResult(response.getString("id"));
            } else {
                throw  new FaceIdCallException(response.getString("error"));
            }
        } catch (JSONException e) {
            throw  new FaceIdCallException(e.getMessage(), e);
        }
    }
}
