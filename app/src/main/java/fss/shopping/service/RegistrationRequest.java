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

public class RegistrationRequest extends BaseRequest {
    private static final String TAG = RegistrationRequest.class.getName();
    private final Call call;
    private final RegistrationListener listener;

    public RegistrationRequest(String name, String surname, String password,
                               String passwordConfirm, String email, boolean terms, String token, RegistrationListener listener) {

        RequestBody requestBody = new FormBody.Builder()
                .add("firstName", name)
                .add("lastName", surname)
                .add("password", password)
                .add("confirmPassword", passwordConfirm)
                .add("email", email)
                .add("confirmEmail", email)
                .add("terms", String.valueOf(terms))
                .add("g-recaptcha-response", token)
                .build();

        Request request = new Request.Builder()
                .url(HOST + "registration")
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
        listener.onRegistrationFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            final String body = response.body().string();
            JSONObject json = new JSONObject(body);
            int status = json.getInt("status");
            Log.i(TAG, "status: " + status);
            if (status == 200) {
                listener.onRegistrationComplete();
                return;
            }
            String errorMessage = json.getString("error");
            Log.e(TAG, "Error message: " + errorMessage);
            listener.onRegistrationFailure(errorMessage);
        } catch (Exception e) {
            Log.e(TAG, "onResponse exception: " + e.getMessage());
            listener.onRegistrationFailure(e.getMessage());
        }
    }

    public interface RegistrationListener {
        public void onRegistrationComplete();

        public void onRegistrationFailure(String errorMessage);
    }
}
