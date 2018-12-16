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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.helpers.CommonHelper;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;


public class ScanQRFragment extends Fragment {

    private CodeScanner checkScanner;

    private final String SUCCESS = "Успешно";
    private final String FAIL = "Ошибка";

    private final String QR_PARSING_FAIL = "Ошибка при распознании QR кода чека";
    private final String QR_PARSING_SUCCESS = "QR код чека успешно распознан";

    private final String DEBUG_TAG = "DebugSendQR";

    private final String RESPONSE_ON_201 = "Чек распознан и добавлен в вашу библиотеку";
    private final String RESPONSE_ON_202 = "Чек не обнаружен на сервере ФНС, добавлен в список ожидания";
    private final String RESPONSE_ON_406 = "Чек не обнаружен на сервере ФНС";
    private final String RESPONSE_ON_FAIL = "Произошла ошибка при добавлении чека";


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
                Toast.makeText(activity, QR_PARSING_SUCCESS, Toast.LENGTH_LONG).show();
                Log.d("DebugSendQR", json.toString(1));

                try {
                    RestClient.post(activity, RestClient.SEND_QR_URL, json,
                                    activity.getUsername(), activity.getPassword(),
                                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            String message;
                            switch (statusCode) {
                                case 201:
                                    message = RESPONSE_ON_201;
                                    break;
                                default:
                                    message = SUCCESS;
                            }
                            Log.d(DEBUG_TAG, String.format("%d: %s", statusCode, response.toString()));
                            showAlert(activity, SUCCESS, message);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            String message;
                            switch (statusCode) {
                                case 202:
                                    message = RESPONSE_ON_202;
                                    break;
                                case 406:
                                    message = RESPONSE_ON_406;
                                    break;
                                default:
                                    message = RESPONSE_ON_FAIL;
                            }
                            Log.d(DEBUG_TAG, String.format("%d: %s", statusCode, FAIL));
                            showAlert(activity, FAIL, message);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    showAlert(activity, FAIL, e.getMessage());
                    Log.d(DEBUG_TAG, e.getMessage());
                }
            } catch (JSONException e) {
                showAlert(activity, FAIL, QR_PARSING_FAIL);
            } catch (Exception e) {
                showAlert(activity, FAIL, e.getMessage());
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}
