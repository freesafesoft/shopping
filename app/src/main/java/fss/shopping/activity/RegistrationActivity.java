package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import fss.shopping.R;
import fss.shopping.service.RegistrationRequest;

public class RegistrationActivity extends AppCompatActivity implements RegistrationRequest.RegistrationListener {
    private static final String TAG = RegistrationActivity.class.getName();
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
        setContentView(R.layout.activity_registration);
        etName = (EditText) findViewById(R.id.edit_text_name);
        etSurname = (EditText) findViewById(R.id.edit_text_surname);
        etEmail = (EditText) findViewById(R.id.edit_text_mail);
        etPassword = (EditText) findViewById(R.id.edit_password);
        etPasswordConfirm = (EditText) findViewById(R.id.edit_second_password);
        cbTerms = (CheckBox) findViewById(R.id.check_box);
        tvResponse = (TextView) findViewById(R.id.text_error_message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void controlRegistration(View view) {
        final String name = etName.getText().toString();
        final String surname = etSurname.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordConfirm = etPasswordConfirm.getText().toString();

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

        final boolean checkBoxes = cbTerms.isChecked();
        if (checkBoxes == false) {
            tvResponse.setText(R.string.reg_tv_terms);
            return;
        }
        tvResponse.setText(R.string.reg_tv_registration_process);

        Log.i(TAG, "Registration request");
        // Android
        SafetyNet.getClient(this).verifyWithRecaptcha("6LfmCEUUAAAAAEV2BlBvgfklqwUkedW0ueIj1uhK")
        // test
       // SafetyNet.getClient(this).verifyWithRecaptcha("6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            new RegistrationRequest(name, surname, password, passwordConfirm,
                                    email, checkBoxes, response.getTokenResult(), RegistrationActivity.this).process();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Token: " + e.getMessage());
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onRegistrationComplete() {
        Intent data = new Intent();
        data.putExtra("email", etEmail.getText().toString());
        data.putExtra("password", etPassword.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onRegistrationFailure(String errorMessage) {
        final String errorMsg = getResources().getString(R.string.reg_tv_error) + ": " + errorMessage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText(errorMsg);
            }
        });
    }
}
