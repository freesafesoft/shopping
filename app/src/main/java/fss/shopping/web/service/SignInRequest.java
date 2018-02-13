package fss.shopping.web.service;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 * Login request
 */
public class SignInRequest extends AbstractRequest {
    private static final String TAG = SignInRequest.class.getName();
    private final Listener listener;

    private SignInRequest(RequestBody requestBody, Listener listener) {
        super(requestBody);
        this.listener = listener;
    }

    @Override
    protected String getRequestUrl() {
        return "login";
    }

    @Override
    public void onFailure(Call call, IOException e) {
        listener.onSignInFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            String cookie = response.header(SET_COOKIE);
            if (cookie != null && cookie.contains(SESSION)) {
                listener.onSignInSuccess();
            } else {
                final String body = response.body().string();
                JSONObject json = new JSONObject(body);
                Log.i(TAG, json.toString());
                String errorMessage = json.getString("error");
                Log.e(TAG, "Error message: " + errorMessage);
                listener.onSignInFailure(errorMessage);
            }
        } catch (Exception e) {
            Log.e(TAG, "onResponse exception: " + e.getMessage(), e);
            listener.onSignInFailure(e.getMessage());
        }
    }

    /**
     * Login request builder
     */
    public static class Builder extends AbstractRequest.Builder {
        private Listener listener;

        public Builder addEmail(String email) {
            builder.add("username", email);
            return this;
        }

        public Builder addPassword(String password) {
            builder.add("password", password);
            return this;
        }

        public Builder addSignInListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public SignInRequest build() {
            return new SignInRequest(builder.build(), listener);
        }
    }

    /**
     * Listener interface allows to receive notifications about login success and login failure events
     */
    public interface Listener {
        public void onSignInSuccess();

        public void onSignInFailure(String errorMessage);
    }
}