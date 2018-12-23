package spbpu.ponzelkoch.expensesadvisor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;

    private final int CAMERA_PERMISSION = 10;
    public static boolean cameraPermission = false;

    public BottomNavigationView navigation;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);
        password = intent.getStringExtra(LoginActivity.PASSWORD);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_checks_list);

        // camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        } else {
            cameraPermission = true;
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container,
                                                               fragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
                boolean result = false;

                switch (item.getItemId()) {
                    case R.id.navigation_checks_list:
                        setFragment(new ChecksFragment());
                        result = true;
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
            };

}
