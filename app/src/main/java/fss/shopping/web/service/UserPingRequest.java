package fss.shopping.web.service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Alex on 08.02.2018.
 */

public class UserPingRequest extends AbstractRequest {
    private static final String TAG = SignOutRequest.class.getName();
    private final Listener listener;

    public UserPingRequest(Listener listener) {
        super(null);
        this.listener = listener;
    }

    @Override
    protected String getRequestUrl() {
        return "user/ping";
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, e.getMessage(), e);
        listener.onPingFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            JSONObject json = new JSONObject(response.body().string());
            int status = json.getInt("status");
            if (status != 200)
                listener.onPingFailure("Unexpected result from server");
            listener.onPingSuccess();
        } catch (JSONException e) {
            Log.e(TAG, "Exception during result parsing", e);
            listener.onPingFailure(e.getMessage());
        }
    }

    public interface Listener {
        public void onPingSuccess();

        public void onPingFailure(String errorMessage);
    }
}
