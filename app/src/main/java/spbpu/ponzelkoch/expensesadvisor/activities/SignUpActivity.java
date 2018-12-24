package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText passwordConfirmField;
    private ProgressBar progressBar;

    private static final String FAIL_PASSWORD_CONFIRMATION = "Введенные пароли не совпадают";
    private static final String FAIL_TRY_AGAIN = "Произошла ошибка. Попробуйте снова.";
    private static final String FAIL_FILL_ALL_FIELDS = "Все поля должны быть заполнены";

    private static final String RESPONSE_ON_201 = "Регистрация прошла успешно!";
    private static final String RESPONSE_ON_406 = "Аккаунт с таким именем уже существует";
    private static final String RESPONSE_ON_SUCCESS = "Успешно!";
    private static final String RESPONSE_ON_FAILURE = "При регистрации произошла ошибка";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameField = findViewById(R.id.signup_username_field);
        passwordField = findViewById(R.id.signup_password_field);
        passwordConfirmField = findViewById(R.id.signup_password_confirm_field);
        progressBar = findViewById(R.id.signup_progress_bar);

        final Button signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(v -> { signUp(); });
    }

    private void signUp() {
        SignUpActivity context = this;

        final String username = usernameField.getText().toString().trim();
        final String password = passwordField.getText().toString();
        final String passwordConfirm = passwordConfirmField.getText().toString();

        if (username.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0) {
            Toast.makeText(context, FAIL_FILL_ALL_FIELDS, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordConfirm.equals(password)) {
            Toast.makeText(context, FAIL_PASSWORD_CONFIRMATION, Toast.LENGTH_SHORT).show();
            passwordConfirmField.setText("");
            return;
        }

        // create JSONObject for request
        JSONObject json = new JSONObject();
        try {
            json.put(LoginActivity.USERNAME, username);
            json.put(LoginActivity.PASSWORD, password);
        } catch (JSONException e) {
            Toast.makeText(context, FAIL_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        try {
            RestClient.post(context, RestClient.SIGN_UP_URL, json, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String message = RESPONSE_ON_SUCCESS;
                    switch (statusCode) {
                        case 201:
                            message = RESPONSE_ON_201;
                            break;
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    clearAllInputs();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    String message = RESPONSE_ON_FAILURE;
                    switch (statusCode) {
                        case 406:
                            message = RESPONSE_ON_406;
                            break;
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    clearAllInputs();
                }
            });
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, FAIL_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void clearAllInputs() {
        usernameField.setText("");
        passwordField.setText("");
        passwordConfirmField.setText("");
    }
}
