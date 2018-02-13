package fss.shopping.web.service;

import android.util.Log;

import java.io.IOException;

import fss.shopping.web.ConnectionManager;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 */

public class SignOutRequest extends AbstractRequest {
    private static final String TAG = SignOutRequest.class.getName();
    private final Listener listener;

    public SignOutRequest(Listener listener) {
        super(null);
        this.listener = listener;
    }

    @Override
    protected String getRequestUrl() {
        return "logout";
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, e.getMessage(), e);
        listener.onSignOutFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.i(TAG, "Response body: " + response.body().string());
        Log.i(TAG, "Response headers: " + response.headers().toString());
        ConnectionManager.getInstance().clearCookies();
        listener.onSignOutSuccess();
    }

    public interface Listener {
        public void onSignOutSuccess();

        public void onSignOutFailure(String errorMessage);
    }
}
