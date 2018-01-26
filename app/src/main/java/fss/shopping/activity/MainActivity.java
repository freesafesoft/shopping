package fss.shopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import fss.shopping.R;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText secondPassword;
    private CheckBox conditionCheckBox;
    /**
     * Создаем переменную messageTextView типа TextView , куда определяем поле из макета по id
     * В этом поле выводим сообщение
     */
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.edit_text_name);
        surname = (EditText) findViewById(R.id.edit_text_surname);
        email = (EditText) findViewById(R.id.edit_text_mail);
        password = (EditText) findViewById(R.id.edit_password);
        secondPassword = (EditText) findViewById(R.id.edit_second_password);
        conditionCheckBox = (CheckBox) findViewById(R.id.check_box);
        messageTextView = (TextView) findViewById(R.id.text_error_message);
    }

    public void controlRegistration(View view) {
        String strName = name.getText().toString();
        String strPassword = password.getText().toString();
        String strSecondPassword = secondPassword.getText().toString();
        boolean passwords = strPassword.equals(strSecondPassword);
        boolean checkBoxes = conditionCheckBox.isChecked();

        if (strName.equals("") || surname.getText().toString().equals("")
                || email.getText().toString().equals("") || password.getText().toString().equals("")
                || secondPassword.getText().toString().equals("")) {
            messageTextView.setText("Не все поля заполнены");
            return;
        }
        if (passwords == false) {
            messageTextView.setText("Пароли не совпадают");
            return;
        }
        if (checkBoxes == false) {
            messageTextView.setText("Необходимо согласиться с условиями использования приложения");
            return;
        }
        messageTextView.setText(strName + " спасибо за регистрацию!");
    }
}
