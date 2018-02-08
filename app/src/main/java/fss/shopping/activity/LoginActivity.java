package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import fss.shopping.R;
import fss.shopping.service.LoginRequest;

public class LoginActivity extends AppCompatActivity implements LoginRequest.LoginListener, TextWatcher {
    private static final String TAG = LoginActivity.class.getName();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final int REGISTRATION_CODE = 0;

    private EditText etEmail;
    private TextInputLayout tilEmail;
    private EditText etPassword;
    private TextInputLayout tilPassword;
    private Button btnLogin;
    private Pattern emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.edit_text_mail_enter);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        etPassword = (EditText) findViewById(R.id.edit_password_enter);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        btnLogin = (Button) findViewById(R.id.button_login);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
    }

    public void onStart() {
        super.onStart();
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateFields(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etEmail.removeTextChangedListener(this);
        etPassword.removeTextChangedListener(this);
    }

    public void login(View view) {
        if (!validateFields(true))
            return;
        btnLogin.setEnabled(false);
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
                Toast.makeText(this, "Registration complete", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void forgotPassword(View w) {
        startActivity(new Intent(getApplicationContext(), PasswordResetActivity.class));
    }

    @Override
    public void onLoginComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AuthTestActivity.class));
                btnLogin.setEnabled(true);
            }
        });
    }

    @Override
    public void onLoginFailure(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validateFields(false);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private boolean validateFields(boolean onClick) {
        boolean allFieldsOk = true;

        String email = etEmail.getText().toString();
        if (!emailPattern.matcher(email).matches()) {
            allFieldsOk = false;
            if (onClick)
                tilEmail.setError("You provided invalid email");
        } else {
            tilEmail.setErrorEnabled(false);
        }

        String password = etPassword.getText().toString();
        if (password.length() < 3) {
            allFieldsOk = false;
            if (onClick)
                tilPassword.setError("Password is too short");
        } else {
            tilPassword.setErrorEnabled(false);
        }
        return allFieldsOk;
    }

    private void setError(TextInputLayout layout, int id, boolean onClick) {
        if (onClick)
            layout.setError(getResources().getText(id));
    }


}
