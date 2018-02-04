package fss.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fss.shopping.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){

    }

    public void goToRegistration(View v){

        Button button=(Button) v;
        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
    }
}
