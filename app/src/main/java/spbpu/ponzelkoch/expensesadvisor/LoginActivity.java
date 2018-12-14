package spbpu.ponzelkoch.expensesadvisor;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    EditText usernameField;
    EditText passwordField;

    private final int INTERNET_PERMISSION = 20;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);
            }
        }
    }

    protected void login() throws JSONException, UnsupportedEncodingException {
        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();

        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        Log.d("DebugLogin", json.toString());

        LoginActivity context = this;

        RestClient.post(this, RestClient.LOGIN_URL, json, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    response.get("message");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(USERNAME, username);
                    intent.putExtra(PASSWORD, password);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DebugLogin", errorResponse.toString());
                Toast.makeText(context, errorResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
