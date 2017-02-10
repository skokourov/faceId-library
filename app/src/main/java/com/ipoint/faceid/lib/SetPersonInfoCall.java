package com.ipoint.faceid.lib;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by spe on 08.02.2017.
 */

public class SetPersonInfoCall extends AbstractFaceIdCall {

    public static class SetPersonInfoResult extends CallResult{
        JSONObject json;

        public SetPersonInfoResult(JSONObject json) {
            this.json = json;
        }

        public JSONObject getResult() {
            return json;
        }
    }


    public SetPersonInfoCall(Token token, JSONObject params, String url, RequestQueue queue, Callback callback) {
        super(token, params, url, queue, callback);
    }

    @Override
    protected CallResult processResponse(JSONObject response) throws FaceIdCallException {
        try {
            if (response.has("personId")) {
                return  new SetPersonInfoCall.SetPersonInfoResult(response);
            } else {
                throw  new FaceIdCallException(response.getString("err"));
            }
        } catch (JSONException e) {
            throw  new FaceIdCallException(e.getMessage(), e);
        }
    }
}
