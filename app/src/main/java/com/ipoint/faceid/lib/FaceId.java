package com.ipoint.faceid.lib;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by spe on 02.02.2017.
 */

public class FaceId {

    public static final String DEFAULT_SERVER_URL = "http://develop.ipoint.ru:8000/";


    private static final String GET_PERSON_ID = "api/getPersonId/";
    private static final String GET_PERSON_INFO = "api/getPersonInfo/";
    private static final String SET_PERSON_INFO = "api/setPersonInfo/";
    private static final String ADD_PERSON = "api/addPerson/";
    private static final String RETRAIN = "api/retrain/";

    public interface ReadyCallback {
        void onReady(FaceId faceId);
        void onError(String error);
    }

    public interface PersonIdCallback {
        void onSuccess(String personId);
        void onError(String error);
    }

    public interface PersonInfoCallback {
        void onSuccess(JSONObject personInfo);
        void onError(String error);
    }

    public interface AddPersonCallback {
        void onSuccess(String personId);
        void onError(String error);
    }

    public interface RetrainCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    private static FaceId instance = null;

    private Context context;

    Token token;

    RequestQueue queue;

    private FaceId(Context context, String user, String password, String clientId, String clientSecret) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.token = new Token(user, password, clientId, clientSecret, queue);
    }

    public static void getInstance(Context context, String user, String password, String clientId, String clientSecret, final ReadyCallback callback) {
        if (instance == null) {
            instance = new FaceId(context, user, password, clientId, clientSecret);
        }
        instance.token.refreshToken(new Token.ReadyCallback() {
            @Override
            public void onReady() {
                callback.onReady(instance);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getPersonId(byte[] image, final PersonIdCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("image", new String(Base64.encode(image, Base64.DEFAULT)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetPersonIdCall call = new GetPersonIdCall(token, params, DEFAULT_SERVER_URL +
                GET_PERSON_ID, queue, new AbstractFaceIdCall.Callback() {
            @Override
            public void onSuccess(AbstractFaceIdCall.CallResult result) {
                callback.onSuccess(((GetPersonIdCall.GetPersonIdResult) result).getId());
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
        call.call();
    }

    public void getPersonInfo(String personId, final PersonInfoCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("stub", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetPersonInfoCall call = new GetPersonInfoCall(token, params, DEFAULT_SERVER_URL +
                GET_PERSON_INFO + personId, queue, new AbstractFaceIdCall.Callback() {
            @Override
            public void onSuccess(AbstractFaceIdCall.CallResult result) {
                callback.onSuccess(((GetPersonInfoCall.GetPersonInfoResult)result).getInfo());
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
        call.call();
    }

    public void addPerson(List<byte[]> images, boolean retrain,  final AddPersonCallback callback) {
        JSONObject params = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            int index = 0;
            for (byte[] image : images) {
                array.put(index, new String(Base64.encode(image, Base64.DEFAULT)));
                index++;
            }
            params.put("images", array);
            params.put("retrain", retrain);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AddPersonCall call = new AddPersonCall(token, params, DEFAULT_SERVER_URL +
                ADD_PERSON, queue, new AbstractFaceIdCall.Callback() {
            @Override
            public void onSuccess(AbstractFaceIdCall.CallResult result) {
                callback.onSuccess(((AddPersonCall.AddPersonResult) result).getId());
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
        call.call();
    }

    public void setPersonInfo(String personId, JSONObject info, final PersonInfoCallback callback) {
        SetPersonInfoCall call = new SetPersonInfoCall(token, info, DEFAULT_SERVER_URL +
                SET_PERSON_INFO + personId, queue, new AbstractFaceIdCall.Callback() {
            @Override
            public void onSuccess(AbstractFaceIdCall.CallResult result) {
                callback.onSuccess(((SetPersonInfoCall.SetPersonInfoResult)result).getResult());
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
        call.call();
    }

    public void retrain(final RetrainCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("stub", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrainCall call = new RetrainCall(token, params, DEFAULT_SERVER_URL +
                RETRAIN, queue, new AbstractFaceIdCall.Callback() {
            @Override
            public void onSuccess(AbstractFaceIdCall.CallResult result) {
                callback.onSuccess(((RetrainCall.RetrainResult)result).getMessage());
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
        call.call();
    }

}
