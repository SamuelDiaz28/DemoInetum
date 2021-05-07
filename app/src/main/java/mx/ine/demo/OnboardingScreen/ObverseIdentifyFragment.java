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
import java.text.ParseException;

import mx.ine.demo.MainActivity;
import mx.ine.demo.R;
import mx.ine.demo.RoomData.AppDatabase;
import mx.ine.demo.RoomData.entities.UserInfo;
import mx.ine.demo.Util.CommonMethods;
import mx.ine.demo.Util.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 */

public class ObverseIdentifyFragment extends Fragment implements View.OnClickListener {

    private Button btnScan;
    private ProgressBar progressBar;

    private MaterialButton btnNext;

    private String surname_and_give_names;
    private String birth_date;
    private String address;
    private String voter_key;
    private String curp;
    private String validity_date;
    private String issue_year;
    private String registry_year;

    private Bitmap documentImage;
    private Bitmap portrait;

    private CommonMethods commonMethods = new CommonMethods();
    private SharedPreferencesHelper preferencesHelper;

    private SaveDocumentData saveDocumentData = new SaveDocumentData();


    public ObverseIdentifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_overse_identify, container, false);

        btnScan = root.findViewById(R.id.btnScan);
        progressBar = root.findViewById(R.id.prgressBarID);
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
        if (v.getId() == R.id.btnScan) {
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

            surname_and_give_names = results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME_AND_GIVEN_NAMES);
            address = (results.getTextFieldValueByType(eVisualFieldType.FT_ADDRESS));

            voter_key = (results.getTextFieldValueByType(eVisualFieldType.FT_VOTER_KEY));
            curp = (results.getTextFieldValueByType(eVisualFieldType.FT_PERSONAL_NUMBER));

            birth_date = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_BIRTH), 3);
            validity_date = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_EXPIRY), 1);
            issue_year = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_ISSUE), 2);
            registry_year = commonMethods.convertDate(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_REGISTRATION), 2);

            documentImage = results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE);
            // Get portrait image
            portrait = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT);

        }catch (Exception e){
            e.printStackTrace();
        }

        checkResults();
    }

    private void checkResults() {
        if (surname_and_give_names == null ||
                birth_date == null ||
                address == null ||
                voter_key == null ||
                curp == null ||
                validity_date == null ||
                issue_year == null ||
                registry_year == null) {

            String msg = "Un dato es erroneo, volver a escanear";
            commonMethods.showErrorDialog(msg, getActivity());
        } else {

            try {
                if (!commonMethods.validityDate(validity_date)) {
                    String msg = "El documento escaneado esta vencido";
                    commonMethods.showWarningDialog(msg, getActivity());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            saveDocumentData.execute();
            Toast.makeText(getActivity().getApplicationContext(), "Escaneo completado", Toast.LENGTH_LONG).show();
        }
    }

    public class SaveDocumentData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                UserInfo userInfo = new UserInfo();
                userInfo.setSurname_and_give_names(surname_and_give_names);
                userInfo.setBirth_day(birth_date);
                userInfo.setAddress(address);
                userInfo.setVoter_key(voter_key);
                userInfo.setCurp(curp);
                userInfo.setValidity_date(validity_date);
                userInfo.setRegistry_year(registry_year);
                userInfo.setIssue_year(issue_year);
                userInfo.setImg_document(commonMethods.getEncoded64(documentImage));

                preferencesHelper.putImagePortrait(commonMethods.getEncoded64(portrait));
                AppDatabase.getInstance(getActivity()).userInfoDao().insert(userInfo);


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