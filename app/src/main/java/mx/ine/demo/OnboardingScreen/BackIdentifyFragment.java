package mx.ine.demo.OnboardingScreen;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderInitCompletion;
import com.regula.documentreader.api.enums.DocReaderAction;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.documentreader.api.enums.eGraphicFieldType;
import com.regula.documentreader.api.enums.eVisualFieldType;
import com.regula.documentreader.api.errors.DocumentReaderException;
import com.regula.documentreader.api.results.DocumentReaderResults;

import java.io.InputStream;

import mx.ine.demo.R;
import mx.ine.demo.RoomData.AppDatabase;
import mx.ine.demo.RoomData.entities.UserInfoIdBack;
import mx.ine.demo.Util.CommonMethods;
import mx.ine.demo.Util.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackIdentifyFragment extends Fragment implements View.OnClickListener {

    private Button btnScan;
    private ProgressBar progressBar;

    private MaterialButton btnNext;

    private String MRZ;
    private String CIC;
    private String OCR;

    private String mrzDateBirth;
    private String mrzYearExpiry;

    private Bitmap documentImage;

    private CommonMethods commonMethods = new CommonMethods();
    private SharedPreferencesHelper preferencesHelper;
    private SaveDocumentIdData saveDocumentData = new SaveDocumentIdData();


    public BackIdentifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_back_identify, container, false);

        btnScan = root.findViewById(R.id.btnScanBack);
        progressBar = root.findViewById(R.id.prgressBarIDBack);

        btnNext = (MaterialButton) getActivity().findViewById(R.id.btnOnboardingAction);

        btnScan.setOnClickListener(this);
        btnNext.setEnabled(false);

        preferencesHelper = new SharedPreferencesHelper(getActivity().getApplicationContext());

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE);
        btnScan.setVisibility(View.INVISIBLE);

        try {
            Log.i(">>REGULA INIT<<", "ENTRO 1");
            InputStream licInput = getResources().openRawResource(R.raw.regula);
            int available = licInput.available();
            byte[] license = new byte[available];
            //noinspection ResultOfMethodCallIgnored
            licInput.read(license);

            DocumentReader.Instance().initializeReader(getActivity().getApplicationContext(), license, new IDocumentReaderInitCompletion() {
                @Override
                public void onInitCompleted(boolean success, DocumentReaderException e) {

                    DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply();

                    //Initialization successful
                    if (success) {
                        progressBar.setVisibility(View.GONE);
                        btnScan.setVisibility(View.VISIBLE);

                        // set the scenario value
                        DocumentReader.Instance().processParams().scenario = Scenario.SCENARIO_LOCATE_VISUAL_AND_MRZ_OR_OCR;
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Init failed:" + e, Toast.LENGTH_LONG).show();
                        Log.i(">>REGULA INIT<<", "ENTRO 3");
                    }
                }
            });

            licInput.close();

        } catch (Exception e) {
            Log.i(">>REGULA INIT<<", "ENTRO 4");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnScanBack) {
            DocumentReader.Instance().showScanner(getActivity(), completionScanId);
        }
    }

    private IDocumentReaderCompletion completionScanId = new IDocumentReaderCompletion() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCompleted(int action, DocumentReaderResults results, DocumentReaderException e) {
            if (action == DocReaderAction.COMPLETE) {
                displayResults(results);
            } else {
                if (action == DocReaderAction.CANCEL) {
                    Toast.makeText(getActivity(), "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else if (action == DocReaderAction.ERROR) {
                    Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_LONG);
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayResults(DocumentReaderResults results) {

        try {
            MRZ = results.getTextFieldValueByType(eVisualFieldType.FT_MRZ_STRINGS);
            CIC = results.getTextFieldValueByType(eVisualFieldType.FT_DOCUMENT_NUMBER);
            OCR = results.getTextFieldValueByType(eVisualFieldType.FT_OPTIONAL_DATA);


            mrzDateBirth = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_BIRTH), 3);
            mrzYearExpiry = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_EXPIRY), 1);

            documentImage = results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        checkResults();
    }

    private void checkResults() {
        if (MRZ == null ||
                CIC == null ||
                OCR == null) {

            String msg = "Un dato es erroneo, volver a escanear";
            commonMethods.showErrorDialog(msg, getActivity());

        } else {

            String frontDateOfBirth = preferencesHelper.getDateBirth();
            String frontExpiry = preferencesHelper.getExpiryYear();
            String codeMRZ = MRZ.trim().replaceAll("\\s", "");


            Log.i(">>>Tamaño MRZ<<<", String.valueOf(codeMRZ.length()) + " --- " + codeMRZ);

            if (mrzDateBirth.equals(frontDateOfBirth) && mrzYearExpiry.equals(frontExpiry) && codeMRZ.length() == 90) {
                Toast.makeText(getActivity().getApplicationContext(), "Escaneo completado", Toast.LENGTH_SHORT).show();
                saveDocumentData.execute();
            } else {
                String msg = "El documento no coincide con la parte frontal";
                commonMethods.showWarningDialog(msg, getActivity());
            }
        }
    }

    public class SaveDocumentIdData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                UserInfoIdBack userInfoIdBack = new UserInfoIdBack();
                userInfoIdBack.setMRZ(MRZ);
                userInfoIdBack.setCIC(CIC);
                userInfoIdBack.setOCR(OCR);
                userInfoIdBack.setImg_document(commonMethods.getEncoded64(documentImage));

                AppDatabase.getInstance(getActivity()).userInfoIdBackDao().insert(userInfoIdBack);

                return true;
            } catch (Exception e){
                Log.e("ObverseIdentifyFragment", "Error al almacenar reporte", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            Toast.makeText(getActivity(), ok ? "Datos almacenados" : "Ocurrio un error al intentar almacenar los datos", Toast.LENGTH_LONG).show();
            if (ok)
                btnNext.setEnabled(true);
        }
    }
}