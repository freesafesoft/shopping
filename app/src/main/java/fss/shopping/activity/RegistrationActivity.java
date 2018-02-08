package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Pattern;

import fss.shopping.R;
import fss.shopping.service.RegistrationRequest;

public class RegistrationActivity extends AppCompatActivity implements RegistrationRequest.RegistrationListener, TextWatcher {
    private static final String TAG = RegistrationActivity.class.getName();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private EditText etName;
    private TextInputLayout tilName;
    private EditText etSurname;
    private TextInputLayout tilSurname;
    private EditText etEmail;
    private TextInputLayout tilEmail;
    private EditText etPassword;
    private TextInputLayout tilPassword;
    private EditText etPasswordConfirm;
    private TextInputLayout tilPasswordConfirm;
    private CheckBox cbTerms;
    private Button btnRegistration;
    private Pattern emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etName = (EditText) findViewById(R.id.edit_text_name);
        tilName = (TextInputLayout) findViewById(R.id.til_name);
        etSurname = (EditText) findViewById(R.id.edit_text_surname);
        tilSurname = (TextInputLayout) findViewById(R.id.til_surname);
        etEmail = (EditText) findViewById(R.id.edit_text_mail);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        etPassword = (EditText) findViewById(R.id.edit_password);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        etPasswordConfirm = (EditText) findViewById(R.id.edit_password_confirm);
        tilPasswordConfirm = (TextInputLayout) findViewById(R.id.til_password_confirm);
        cbTerms = (CheckBox) findViewById(R.id.check_box);
        btnRegistration = (Button) findViewById(R.id.btn_registration);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        etName.addTextChangedListener(this);
        etSurname.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etPasswordConfirm.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateFields(false);
    }

    protected void onStop() {
        super.onStop();
        etName.removeTextChangedListener(this);
        etSurname.removeTextChangedListener(this);
        etEmail.removeTextChangedListener(this);
        etPassword.removeTextChangedListener(this);
        etPasswordConfirm.removeTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registration(View view) {
        if (!validateFields(true))
            return;
        btnRegistration.setEnabled(false);
        final String name = etName.getText().toString();
        final String surname = etSurname.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordConfirm = etPasswordConfirm.getText().toString();
        Log.i(TAG, "Registration request");

        Toast.makeText(RegistrationActivity.this, "Verifying you are not a robot =)", Toast.LENGTH_SHORT).show();
        SafetyNet.getClient(this).verifyWithRecaptcha("6LfmCEUUAAAAAEV2BlBvgfklqwUkedW0ueIj1uhK")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            Toast.makeText(RegistrationActivity.this, "Registration in process", Toast.LENGTH_LONG).show();
                            new RegistrationRequest(name, surname, password, passwordConfirm,
                                    email, true, response.getTokenResult(), RegistrationActivity.this).process();
                        } else
                            btnRegistration.setEnabled(true);
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
                            btnRegistration.setEnabled(true);
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
                btnRegistration.setEnabled(true);
                Toast.makeText(RegistrationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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

        String name = etName.getText().toString();
        if (name.equals("")) {
            setError(tilName, R.string.reg_tv_empty_name, onClick);
            allFieldsOk = false;
        } else {
            tilName.setErrorEnabled(false);
        }

        String surname = etSurname.getText().toString();
        if (surname.equals("")) {
            setError(tilSurname, R.string.reg_tv_empty_surname, onClick);
            allFieldsOk = false;
        } else {
            tilSurname.setErrorEnabled(false);
        }

        String email = etEmail.getText().toString();
        if (email.equals("")) {
            setError(tilEmail, R.string.reg_tv_empty_email, onClick);
            allFieldsOk = false;
        } else if (!emailPattern.matcher(email).matches()) {
            setError(tilEmail, R.string.reg_tv_empty_email, onClick);
            allFieldsOk = false;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        String password = etPassword.getText().toString();
        if (password.equals("")) {
            setError(tilPassword, R.string.reg_tv_empty_pass, onClick);
            allFieldsOk = false;
        } else {
            tilPassword.setErrorEnabled(false);
        }

        String passwordConfirm = etPasswordConfirm.getText().toString();
        if (passwordConfirm.equals("")) {
            setError(tilPasswordConfirm, R.string.reg_tv_empty_pass2, onClick);
            allFieldsOk = false;
        } else {
            tilPasswordConfirm.setErrorEnabled(false);
        }

        boolean samePasswords = password.equals(passwordConfirm);
        if (!samePasswords) {
            setError(tilPasswordConfirm, R.string.reg_tv_pass_not_same, onClick);
            allFieldsOk = false;
        } else {
            Log.e(TAG, "Passwords are same");
            tilPasswordConfirm.setErrorEnabled(false);
        }

        if (!cbTerms.isChecked()) {
            if (onClick)
                Toast.makeText(this, getResources().getText(R.string.reg_tv_terms), Toast.LENGTH_SHORT).show();
            allFieldsOk = false;
        }
        return allFieldsOk;
    }

    private void setError(TextInputLayout layout, int id, boolean onClick) {
        if (onClick)
            layout.setError(getResources().getText(id));
    }
}
