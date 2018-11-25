package spbpu.ponzelkoch.expensesadvisor;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String username;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean result = false;

            switch (item.getItemId()) {
                case R.id.navigation_checks_list:
                    setFragment(new ChecksFragment());
                    break;
                case R.id.navigation_qr_scanner:
                    setFragment(new ScanQRFragment());
                    result = true;
                    break;
                case R.id.navigation_statistics:
                    setFragment(new StatisticsFragment());
                    result = true;
                    break;
            }

            return result;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setFragment(new ChecksFragment());

        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container,
                                                               fragment).commit();
    }

}
