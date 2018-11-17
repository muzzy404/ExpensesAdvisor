package spbpu.ponzelkoch.expensesadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";

    EditText usernameField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpLink = findViewById(R.id.signup_link);
        signUpLink.setText(R.string.title_sign_up_link);

        usernameField = findViewById(R.id.username_field);
        passwordField = findViewById(R.id.password_field);

        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    protected void login() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        boolean success = true;  // TODO: send to server and get answer

        if (success) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USERNAME, username);
            startActivity(intent);
        }
    }
}
