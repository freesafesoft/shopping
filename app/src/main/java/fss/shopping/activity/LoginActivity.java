package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fss.shopping.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvResponse = (TextView) findViewById(R.id.text_error_message_log_in);
        etEmail = (EditText) findViewById(R.id.edit_text_mail_enter);
        etPassword = (EditText) findViewById(R.id.edit_password_enter);
    }

    public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_email);
            return;
        }

        if (password.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_pass);
            return;
        }
    }

    public void goToRegistration(View v) {

        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
    }

    public void forgot_password(View w) {

        Button button = (Button) w;
        startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
    }
}
