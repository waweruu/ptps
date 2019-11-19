package com.isproject.ptps.mpesa;

import android.content.Context;
import android.os.AsyncTask;

import com.isproject.ptps.mpesa.Mode;
import com.isproject.ptps.mpesa.interfaces.AuthListener;
import com.isproject.ptps.mpesa.interfaces.MpesaListener;
import com.isproject.ptps.mpesa.models.STKPush;
import com.isproject.ptps.mpesa.network.NetworkHandler;
import com.isproject.ptps.mpesa.utils.Config;
import com.isproject.ptps.mpesa.utils.Pair;
import com.isproject.ptps.mpesa.utils.Preferences;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by miles on 18/11/2017.
 */

public class Mpesa {
    private static AuthListener authListener;
    private static Mpesa instance;
    private static MpesaListener mpesaListener;
    private static com.isproject.ptps.mpesa.Mode mode = com.isproject.ptps.mpesa.Mode.SANDBOX;
    private Mpesa(){}

    public static void with (Context context, String key, String secret){
        if (! (context instanceof AuthListener)){
            throw new RuntimeException("Context must implement AuthListener");
        }
        if (instance == null){
            instance = new Mpesa();
            Preferences.getInstance().setKey(key);
            Preferences.getInstance().setSecret(secret);
            authListener = (AuthListener)context;

            String url = Config.BASE_URL + Config.ACCESS_TOKEN_URL;
            if (mode == com.isproject.ptps.mpesa.Mode.PRODUCTION)
                url = Config.PRODUCTION_BASE_URL + Config.ACCESS_TOKEN_URL;
            new AuthService().execute(url);
        }

    }
    public static void with (Context context, String key, String secret, com.isproject.ptps.mpesa.Mode m){
        if (! (context instanceof AuthListener)){
            throw new RuntimeException("Context must implement AuthListener");
        }
        mode = m;
        instance = null;
        if (instance == null){
            instance = new Mpesa();
            Preferences.getInstance().setKey(key);
            Preferences.getInstance().setSecret(secret);
            authListener = (AuthListener)context;

            String url = Config.BASE_URL + Config.ACCESS_TOKEN_URL;
            if (mode == com.isproject.ptps.mpesa.Mode.PRODUCTION)
                url = Config.PRODUCTION_BASE_URL + Config.ACCESS_TOKEN_URL;
            new AuthService().execute(url);
        }

    }

    public static Mpesa getInstance(){
        if (instance == null){
            throw new RuntimeException("Mpesa must be initialized with key and secret");
        }
        return instance;
    }

    public static void pay(Context context, STKPush push){
        //TODO pay
        if (! (context instanceof AuthListener)){
            throw new RuntimeException("Context must implement MpesaListener");
        }
        if (push == null){
            throw new RuntimeException("STKPush cannot be null");
        }
        mpesaListener = (MpesaListener) context;
        JSONObject postData = new JSONObject();
        try {
            postData.put("BusinessShortCode", push.getBusinessShortCode());
            postData.put("Password", push.getPassword());
            postData.put("Timestamp", push.getTimestamp());
            postData.put("TransactionType", push.getTransactionType());
            postData.put("Amount", push.getAmount());
            postData.put("PartyA", push.getPartyA());
            postData.put("PartyB", push.getPartyB());
            postData.put("PhoneNumber", push.getPhoneNumber());
            postData.put("CallBackURL", push.getCallBackURL());
            postData.put("AccountReference", push.getAccountReference());
            postData.put("TransactionDesc", push.getTransactionDesc());

            String url = Config.BASE_URL + Config.PROCESS_REQUEST_URL;
            if (mode == Mode.PRODUCTION)
                url = Config.PRODUCTION_BASE_URL + Config.PROCESS_REQUEST_URL;
            new PayService().execute(url, postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static class AuthService extends AsyncTask<String, Void, Pair<Integer, String> > {

        @Override
        protected Pair<Integer, String> doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + Preferences.getInstance().getAuthorization());
            return NetworkHandler.doGet(strings[0], headers);
        }

        protected void onPostExecute(Pair<Integer, String> result){
            if (result == null){
                Mpesa.getInstance().authListener.onAuthError(new Pair<>(418, Config.ERROR)); //User is a teapot :(
                return;
            }
            if (result.code == 400){
                Mpesa.getInstance().authListener.onAuthError(new Pair<>(result.code, "Invalid credentials"));
                return;
            }
            try {
                JsonParser jsonParser = new JsonParser();
                JsonObject jo = (JsonObject) jsonParser.parse(result.message).getAsJsonObject();
                if (result.code / 100 != 2) {
                    //Error occurred
                    String message = jo.get("errorMessage").getAsString();
                    Mpesa.getInstance().authListener.onAuthError(new Pair<>(result.code, message));
                    return;
                }
                String access_token = jo.get("access_token").getAsString();
                Preferences.getInstance().setAccessToken(access_token);
                Mpesa.getInstance().authListener.onAuthSuccess();
                return;
            }catch (Exception e) {
                String message = "Error completing fetching token.Please try again.";
                Mpesa.getInstance().authListener.onAuthError(new Pair<>(result.code, message));
            }
        }
    }
    static class PayService extends AsyncTask<String, Void, Pair<Integer, String> >{
        @Override
        protected Pair<Integer, String> doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + Preferences.getInstance().getAccessToken());
            return NetworkHandler.doPost(strings[0],strings[1], headers);
        }

        protected void onPostExecute(Pair<Integer, String> result) {
            if (result == null) {
                Mpesa.mpesaListener.onMpesaError(new Pair<>(418, Config.ERROR)); //User is a teapot :(
                return;
            }
            try {
                JsonParser jsonParser = new JsonParser();

                JsonObject jo = jsonParser.parse(result.message).getAsJsonObject();
                if (result.code / 100 != 2) {
                    //Error occurred
                    if (jo.has("errorMessage")) {
                        String message = jo.get("errorMessage").getAsString();
                        Mpesa.mpesaListener.onMpesaError(new Pair<>(result.code, message));
                        return;
                    }
                    String message = "Error completing payment.Please try again.";
                    Mpesa.mpesaListener.onMpesaError(new Pair<>(result.code, message));
                    return;
                }
                Mpesa.mpesaListener.onMpesaSuccess(jo.get("MerchantRequestID").getAsString(), jo.get("CheckoutRequestID").getAsString(), jo.get("CustomerMessage").getAsString());
            } catch (Exception e) {
                String message = "Error completing payment.Please try again.";
                Mpesa.mpesaListener.onMpesaError(new Pair<>(result.code, message));
            }
        }
    }

}
