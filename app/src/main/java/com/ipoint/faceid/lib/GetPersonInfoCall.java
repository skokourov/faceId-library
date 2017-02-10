package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

/**
 * Created by spe on 08.02.2017.
 */

public class GetPersonInfoCall extends AbstractFaceIdCall {

    public static class GetPersonInfoResult extends CallResult{
        JSONObject json;

        public GetPersonInfoResult(JSONObject json) {
            this.json = json;
        }

        public JSONObject getInfo() {
            return json;
        }
    }


    public GetPersonInfoCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        super(token, params, url, queue, callback);
    }

    @Override
    protected CallResult processResponse(JSONObject response) throws FaceIdCallException {
        return null;
    }
}
