package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Pattern;

import fss.shopping.R;
import fss.shopping.service.LoginRequest;

public class LoginActivity extends AppCompatActivity implements LoginRequest.LoginListener, TextWatcher {
    private static final String TAG = LoginActivity.class.getName();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final int REGISTRATION_CODE = 0;
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
        etPassword.addTextChangedListener(this);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        validateEmailAndPassword();
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateEmailAndPassword();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etEmail.removeTextChangedListener(this);
        etPassword.removeTextChangedListener(this);
    }

    public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        new LoginRequest(email, password, this).process();
    }

    public void goToRegistration(View v) {
        startActivityForResult(new Intent(getApplicationContext(), RegistrationActivity.class), REGISTRATION_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_CODE) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra("email");
                etEmail.setText(email);
                String password = data.getStringExtra("password");
                etPassword.setText(password);
            }
        }
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
    public void onLoginComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AuthTestActivity.class));
            }
        });
    }

    @Override
    public void onLoginFailure(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLogin.setEnabled(true);
                tvResponse.setText("Error: " + errorMessage);
            }
        });
    }
}
