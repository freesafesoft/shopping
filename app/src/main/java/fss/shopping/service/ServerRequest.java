package fss.shopping.service;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 26.01.2018.
 */

public class ServerRequest {

    public static Call registration(Callback callback, String name, String surname, String password1,
                                    String password2, String email, boolean terms) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("firstName", name)
                .add("lastName", surname)
                .add("password", password1)
                .add("confirmPassword", password2)
                .add("email", email)
                .add("confirmEmail", email)
                .add("terms", String.valueOf(terms))
                .build();

        Request registrationRequest = new Request.Builder()
                .url("http://192.168.0.80:8080/registration")
                .post(requestBody)
                .build();
        Call call  = client.newCall(registrationRequest);
        call.enqueue(callback);
        return call;
    }
}
