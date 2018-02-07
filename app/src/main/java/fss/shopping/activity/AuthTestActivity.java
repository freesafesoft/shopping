package fss.shopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import fss.shopping.R;
import fss.shopping.service.LogoutRequest;

/**
 * Created by Alex on 06.02.2018.
 */

public class AuthTestActivity extends AppCompatActivity implements LogoutRequest.LogoutListener {
    private static final String TAG = AuthTestActivity.class.getName();
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_test);
        tvResponse = (TextView) findViewById(R.id.test_tv_message);
    }

    public void logout(View v) {
        Log.i(TAG, "Logout");
        tvResponse.setText("Logout");
        new LogoutRequest(this).process();
    }

    @Override
    public void onLogoutComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText("Logout complete");
                finish();
            }
        });
    }

    @Override
    public void onLogoutFailure(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText("Logout failed: " + errorMessage);
            }
        });
    }
}
