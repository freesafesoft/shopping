package fss.shopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fss.shopping.R;
import fss.shopping.service.ServerRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Callback {
    private EditText etName;
    private EditText etSurname;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private CheckBox cbTerms;
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.edit_text_name);
        etSurname = (EditText) findViewById(R.id.edit_text_surname);
        etEmail = (EditText) findViewById(R.id.edit_text_mail);
        etPassword = (EditText) findViewById(R.id.edit_password);
        etPasswordConfirm = (EditText) findViewById(R.id.edit_second_password);
        cbTerms = (CheckBox) findViewById(R.id.check_box);
        tvResponse = (TextView) findViewById(R.id.text_error_message);
    }

    public void controlRegistration(View view) {
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();

        // trim удаляет пробелы в начале слова и в конце слова
        if (name.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_name);
            return;
        }

        if (surname.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_surname);
            return;
        }

        if (email.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_email);
            return;
        }

        if (password.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_pass);
            return;
        }

        if (passwordConfirm.trim().equals("")) {
            tvResponse.setText(R.string.reg_tv_empty_pass2);
            return;
        }

        boolean passwords = password.equals(passwordConfirm);
        if (passwords == false) {
            tvResponse.setText(R.string.reg_tv_pass_not_same);
            return;
        }

        boolean checkBoxes = cbTerms.isChecked();
        if (checkBoxes == false) {
            tvResponse.setText(R.string.reg_tv_terms);
            return;
        }
        tvResponse.setText(R.string.reg_tv_registration_process);
        ServerRequest.registration(this, name, surname, password, passwordConfirm,
                email, checkBoxes);
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        final String errorMessage = getResources().getString(R.string.reg_tv_error) + ": " + e.getMessage();
        Log.e(getClass().getName(), errorMessage, e);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText(errorMessage);
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        final String body = response.body().string();
        try {
            JSONObject json = new JSONObject(body);
            final String message = json.getString("message");
            Log.i(getClass().getName(), "message: " + message);
            final String error = json.getString("error");
            Log.i(getClass().getName(), "error: " + error);
            if (error != null) {
                final String errorMsg = getResources().getString(R.string.reg_tv_error) + ": " + error;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResponse.setText(errorMsg);
                    }
                });
            } else if ("OK".equals(message)) {
                final String errorMsg = getResources().getString(R.string.reg_tv_unexpected_response) + ": " + message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResponse.setText(errorMsg);
                    }
                });
            } else {
                // open login activity and enter login and password to login fields
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResponse.setText(message);
                    }
                });
            }
        } catch (JSONException e) {
            final String errorMsg = getResources().getString(R.string.reg_tv_cant_parse_response) + ": " + body;
            Log.e(getClass().getName(), errorMsg, e);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvResponse.setText(errorMsg);
                }
            });
        }
    }
}
