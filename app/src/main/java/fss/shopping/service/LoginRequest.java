package fss.shopping.service;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 */
public class LoginRequest extends BaseRequest {
    private static final String TAG = LoginRequest.class.getName();
    private final Call call;
    private final LoginListener listener;

    public LoginRequest(String email, String password, LoginListener listener) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", email)
                .add("password", password)

                .build();
        Request request = new Request.Builder()
                .url(HOST + "login")
                .post(requestBody)
                .build();

        call = client.newCall(request);
        this.listener = listener;
    }

    @Override
    public void process() {
        call.enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        listener.onLoginFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
           // final String body = response.body().string();
           // JSONObject json = new JSONObject(body);
            //Log.i(TAG, json.toString());
            String cookie = response.header("Set-Cookie");
            if (cookie != null && cookie.indexOf("JSESSIONID") != -1) {
                listener.onLoginComplete();
            } else {
                final String body = response.body().string();
                JSONObject json = new JSONObject(body);
                Log.i(TAG, json.toString());
                String errorMessage = json.getString("error");
                Log.e(TAG, "Error message: " + errorMessage);
                listener.onLoginFailure(errorMessage);
            }
        } catch (Exception e) {
            Log.e(TAG, "onResponse exception: " + e.getMessage(), e);
            listener.onLoginFailure(e.getMessage());
        }
    }

    public interface LoginListener {
        public void onLoginComplete();

        public void onLoginFailure(String errorMessage);
    }
}