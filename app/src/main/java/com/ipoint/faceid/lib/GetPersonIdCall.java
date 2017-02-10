package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by spe on 07.02.2017.
 */

public class GetPersonIdCall extends AbstractFaceIdCall {

    public static class GetPersonIdResult extends CallResult{
        String id;

        public GetPersonIdResult(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public GetPersonIdCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        super(token, params, url, queue, callback);
    }


    @Override
    protected CallResult processResponse(JSONObject response) throws FaceIdCallException {
        try {
            if (response.has("id")) {
                return  new GetPersonIdResult(response.getString("id"));
            } else {
                throw  new FaceIdCallException(response.getString("error"));
            }
        } catch (JSONException e) {
            throw  new FaceIdCallException(e.getMessage(), e);
        }
    }
}
