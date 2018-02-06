package fss.shopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fss.shopping.R;
import fss.shopping.service.ServerRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Alex on 06.02.2018.
 */

public class AuthTestActivity extends AppCompatActivity implements Callback {
    private static final String TAG = LoginActivity.class.getName();
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_test);
        tvResponse = (TextView) findViewById(R.id.test_tv_message);
    }

    public void write(View v) {
        Log.i(TAG, "write");
        tvResponse.setText("write request");
        ServerRequest.write(this);
    }

    public void read(View v) {
        Log.i(TAG, "read");
        tvResponse.setText("read request");
        ServerRequest.read(this);
    }

    public void check(View v) {
        Log.i(TAG, "check");
        tvResponse.setText("check request");
        ServerRequest.check(this);
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        Log.i(TAG, e.getMessage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText("ERROR" + e.getMessage());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        Log.i(TAG, response.headers().toString());
        try {
            final JSONObject jsonResult = new JSONObject(response.body().string());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tvResponse.setText("status: " + jsonResult.getInt("status") + "; Message: " + jsonResult.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, response.headers().toString(), e);
        }

    }
}
