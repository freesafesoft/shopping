package fss.shopping.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 */

public class LogoutRequest extends BaseRequest {
    private static final String TAG = LogoutRequest.class.getName();
    private final Call call;
    private final LogoutListener listener;

    public LogoutRequest(LogoutListener listener) {
        Request request = new Request.Builder()
                .url(HOST + "logout")
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
        Log.e(TAG, e.getMessage(), e);
        listener.onLogoutFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.i(TAG, "Response body: " + response.body().string());
        Log.i(TAG, "Response headers: " + response.headers().toString());
        ClientHolder.getInstance().clearCookies();
        listener.onLogoutComplete();
    }

    public interface LogoutListener {
        public void onLogoutComplete();

        public void onLogoutFailure(String errorMessage);
    }
}
