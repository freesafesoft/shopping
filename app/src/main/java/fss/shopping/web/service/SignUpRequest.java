package fss.shopping.web.service;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 */

public class SignUpRequest extends AbstractRequest {
    private static final String TAG = SignUpRequest.class.getName();
    private final Listener listener;

    public SignUpRequest(RequestBody requestBody, Listener listener) {
        super(requestBody);
        this.listener = listener;
    }

    @Override
    protected String getRequestUrl() {
        return "registration";
    }

    @Override
    public void onFailure(Call call, IOException e) {
        listener.onSignUpFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            final String body = response.body().string();
            JSONObject json = new JSONObject(body);
            int status = json.getInt("status");
            Log.i(TAG, "status: " + status);
            if (status == 200) {
                listener.onSignUpSuccess();
                return;
            }
            String errorMessage = json.getString("error");
            Log.e(TAG, "Error message: " + errorMessage);
            listener.onSignUpFailure(errorMessage);
        } catch (Exception e) {
            Log.e(TAG, "onResponse exception: " + e.getMessage());
            listener.onSignUpFailure(e.getMessage());
        }
    }


    public static class Builder extends AbstractRequest.Builder {
        private Listener listener;

        public Builder addFirstName(String firstName) {
            builder.add("firstName", firstName);
            return this;
        }

        public Builder addFLastName(String lastName) {
            builder.add("lastName", lastName);
            return this;
        }

        public Builder addEmail(String email) {
            builder.add("email", email);
            return this;
        }

        public Builder addPassword(String password) {
            builder.add("password", password);
            return this;
        }

        public Builder addPasswordConfirm(String passwordConfirm) {
            builder.add("passwordConfirm", passwordConfirm);
            return this;
        }

        public Builder addTerms(boolean terms) {
            builder.add("terms", String.valueOf(terms));
            return this;
        }

        public Builder addCaptcha(String token) {
            builder.add("g-recaptcha-response", token);
            return this;
        }

        public Builder addRegistrationListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public SignUpRequest build() {
            return new SignUpRequest(builder.build(), listener);
        }
    }

    public interface Listener {
        public void onSignUpSuccess();

        public void onSignUpFailure(String errorMessage);
    }
}
