package spbpu.ponzelkoch.expensesadvisor;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String username;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_checks_list:
                    mTextMessage.setText(R.string.title_checks_list);
                    return true;
                case R.id.navigation_qr_scanner:
                    mTextMessage.setText(R.string.title_qr_scanner);
                    return true;
                case R.id.navigation_statistics:
                    mTextMessage.setText(R.string.title_statistics);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    }

}
