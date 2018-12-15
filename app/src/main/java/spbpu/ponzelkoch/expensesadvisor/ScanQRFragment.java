package spbpu.ponzelkoch.expensesadvisor;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.helpers.CommonHelper;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;


public class ScanQRFragment extends Fragment {

    private CodeScanner checkScanner;

    private final String QR_PARSING_FAIL = "QR code content parsing failed";


    public ScanQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_scan_qr, container, false);

        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        checkScanner = new CodeScanner(activity, scannerView);
        checkScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // getting json from QR code string
                            JSONObject json = CommonHelper.QRStringToJSON(result.getText());
                            Toast.makeText(activity, json.toString(), Toast.LENGTH_LONG).show();
                            Log.d("DebugSendQR", json.toString(1));

                            try {
                                RestClient.post(activity, RestClient.SEND_QR_URL, json,
                                                activity.getUsername(), activity.getPassword(),
                                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Log.d("DebugSendQR", response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        try {
                                            Log.d("DebugSendQR", errorResponse.toString());
                                        } catch (Exception e) {
                                            Log.d("DebugSendQR", "null response: " + e.getMessage());
                                        }

                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(activity, QR_PARSING_FAIL, Toast.LENGTH_LONG).show();
                        }
                        // go back to Checks List Fragment
                        activity.navigation.setSelectedItemId(R.id.navigation_checks_list);
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
//        TODO: check for permisson
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
