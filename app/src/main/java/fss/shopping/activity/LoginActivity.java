package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import fss.shopping.R;
import fss.shopping.service.ServerRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements Callback, TextWatcher {
    private static final String TAG = LoginActivity.class.getName();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvResponse;

    private Pattern emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvResponse = (TextView) findViewById(R.id.text_error_message_log_in);
        etEmail = (EditText) findViewById(R.id.edit_text_mail_enter);
        etPassword = (EditText) findViewById(R.id.edit_password_enter);
        btnLogin = (Button) findViewById(R.id.button_login);

        etEmail.addTextChangedListener(this);
        etEmail.removeTextChangedListener(this);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        validateEmailAndPassword();
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateEmailAndPassword();
    }

    public void login(View view) {
        btnLogin.setEnabled(false);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        ServerRequest.login(this, email, password);
    }

    public void goToRegistration(View v) {

        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
    }

    public void forgotPassword(View w) {
        startActivity(new Intent(getApplicationContext(), PasswordResetActivity.class));
    }

    private void validateEmailAndPassword() {
        String email = etEmail.getText().toString();
        boolean emailValid = emailPattern.matcher(email).matches();

        String password = etPassword.getText().toString();
        boolean passwordValid = password.length() > 0;
        if (emailValid && passwordValid) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validateEmailAndPassword();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLogin.setEnabled(true);
                tvResponse.setText("Error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText(response.headers().toString());
                if(response.header("Set-Cookie").indexOf("JSESSIONID") != -1 )
                    startActivity(new Intent(getApplicationContext(), AuthTestActivity.class));
                else
                    Log.e(TAG, "No JSESSION IN COOKIES!!");
            }
        });
    }
}
