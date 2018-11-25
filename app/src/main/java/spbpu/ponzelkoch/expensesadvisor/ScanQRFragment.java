package spbpu.ponzelkoch.expensesadvisor;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment {

    private CodeScanner checkScanner;


    public ScanQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_scan_qr, container, false);
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_scan_qr, container, false);

        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        checkScanner = new CodeScanner(activity, scannerView);
        checkScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();

                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.navigation.setSelectedItemId(R.id.navigation_checks_list);
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkScanner.startPreview();
//        if (MainActivity.cameraPermission) {
//            checkScanner.startPreview();
//        }
    }

    @Override
    public void onPause() {
        checkScanner.releaseResources();
        super.onPause();
    }

}
