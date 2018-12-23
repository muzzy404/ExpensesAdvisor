package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String LOGIN_FAILED = "login failed: %s";

    EditText usernameField;
    EditText passwordField;

    public static final String DEBUG_TAG = "DebugLogin";

    private static final String RESPONSE_ON_403 = "Неправильный логин или пароль. Попробуйте еще раз.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpLink = findViewById(R.id.signup_link);
        signUpLink.setText(R.string.title_sign_up_link);

        usernameField = findViewById(R.id.username_field);
        passwordField = findViewById(R.id.password_field);

        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            try {
                login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void login() {
        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put(USERNAME, username);
            json.put(PASSWORD, password);
            Log.d(DEBUG_TAG, json.toString());
        } catch (JSONException e) {
            final String failMessage = "json creation failed";
            Log.d(DEBUG_TAG, failMessage);
            Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LoginActivity context = this;
            RestClient.post(this, RestClient.LOGIN_URL, json, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(USERNAME, username);
                    intent.putExtra(PASSWORD, password);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    String failMessage;
                    switch (statusCode) {
                        case 403:
                            failMessage = RESPONSE_ON_403;
                        break;
                        default:
                            failMessage = String.format(LOGIN_FAILED, errorResponse.toString());
                            break;
                    }
                    Log.d(DEBUG_TAG, failMessage);
                    Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            String failMessage = String.format(LOGIN_FAILED, e.getMessage());
            Log.d(DEBUG_TAG, failMessage);
            Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show();
        }

    }

}
