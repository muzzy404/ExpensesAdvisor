package spbpu.ponzelkoch.expensesadvisor;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
        checkScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
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
                            // TODO: messages from statusCode
                            String message = "success";
                            try {
                                message = response.getString("message");
                            } catch (JSONException ignored) { }
                            Log.d("DebugSendQR", response.toString());
                            showAlert(activity, "Success", message);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            // TODO: messages from statusCode
                            String failMessage;
                            try {
                                failMessage = errorResponse.toString();
                            } catch (Exception e) {
                                failMessage = e.getMessage();
                            }
                            Log.d("DebugSendQR", failMessage);
                            showAlert(activity, "Fail", failMessage);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    Log.d("DebugSendQR", e.getMessage());
                }
            } catch (JSONException e) {
                showAlert(activity, "Fail", QR_PARSING_FAIL);
            } catch (Exception e) {
                showAlert(activity, "Fail", e.getMessage());
            }
            // go back to Checks List Fragment
            activity.navigation.setSelectedItemId(R.id.navigation_checks_list);
        }));

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

    private void showAlert(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
