package mx.ine.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.ine.demo.Adapters.SpinnerCountriesAdapter;
import mx.ine.demo.Requests.Model.UsuarioService;
import mx.ine.demo.Requests.RetrofitRequest;
import mx.ine.demo.Util.SpinnerCountriesModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = "RegistryActivity";

    private final int OTP_SMS = 10;
    private final int OTP_EMAIL = 11;

    private Button btnSend;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtPhoneNumber;
    private CheckBox checkEmail;
    private CheckBox checkMSM;

    private ProgressBar progressBar;
    private Spinner spinnerCountries;

    private String phoneCode;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPhone;

    private ArrayList<SpinnerCountriesModel> listCountries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        startComponents();
    }

    private void startComponents() {
        btnSend = findViewById(R.id.btnSend);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhoneNumber = findViewById(R.id.edtNumberPhone);

        checkEmail = findViewById(R.id.checkEmail);
        checkMSM = findViewById(R.id.checkMSJ);

        progressBar = findViewById(R.id.prgressBar);
        spinnerCountries = findViewById(R.id.spnCountries);

        textInputLayoutEmail = findViewById(R.id.text_input_email);
        textInputLayoutPassword = findViewById(R.id.text_input_password);
        textInputLayoutPhone = findViewById(R.id.text_input_phone);

        btnSend.setOnClickListener(this);
        checkEmail.setOnClickListener(this);
        checkMSM.setOnClickListener(this);


        listCountries.add(new SpinnerCountriesModel("+52", R.drawable.ic_mexico));
        listCountries.add(new SpinnerCountriesModel("+54", R.drawable.ic_argentina));
        listCountries.add(new SpinnerCountriesModel("+57", R.drawable.ic_colombia));
        listCountries.add(new SpinnerCountriesModel("+51", R.drawable.ic_panama));
        listCountries.add(new SpinnerCountriesModel("+507", R.drawable.ic_peru));

        SpinnerAdapter adapter = new SpinnerCountriesAdapter(RegistryActivity.this, R.layout.spinner_countries, R.id.txtCountry, listCountries);
        spinnerCountries.setAdapter(adapter);
        spinnerCountries.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                if (checkForm()) {
                    sendData();
                } else {
                    return;
                }
                break;

            case R.id.checkEmail:
                if (checkMSM.isChecked()) {
                    checkMSM.setChecked(false);
                    checkEmail.setChecked(true);
                }
                break;

            case R.id.checkMSJ:
                if (checkEmail.isChecked()) {
                    checkEmail.setChecked(false);
                    checkMSM.setChecked(true);
                }
                break;
        }

    }

    private void sendData() {

        progressBar.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.INVISIBLE);

        String dataPrepared = null;

        try {
            JSONObject data = new JSONObject();
            data.put("Telefono", phoneCode + edtPhoneNumber.getText().toString().trim());
            data.put("Correo", edtEmail.getText().toString().trim());
            data.put("Password", edtPassword.getText().toString().trim());

            dataPrepared = data.toString();

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(dataPrepared);
        Call<String> response = service.registrar(body);
        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Codigo de respuesta", String.valueOf(response.code()));
                if (response.code() != 200) {
                    Toast.makeText(RegistryActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    Boolean res = jres.getBoolean("OK");
                    if (res) {
                        JSONObject jsData = jres.getJSONObject("data");
                        int codeValidation = jsData.getInt("codeValidation");
                        Toast.makeText(RegistryActivity.this, "PIN enviado", Toast.LENGTH_LONG).show();

                        if (checkEmail.isChecked())
                            goToVerification(OTP_EMAIL, codeValidation);
                        else if (checkMSM.isChecked())
                            goToVerification(OTP_SMS, codeValidation);


                    } else {
                        Toast.makeText(RegistryActivity.this, "Error: " + jres.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    Log.i(">>>Data<<<", res.toString());

                    progressBar.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SpinnerCountriesModel spinnerModel = listCountries.get(position);
        String item = spinnerModel.getTxtCountry();
        phoneCode = item;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        phoneCode = "+52";
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);
    }

    private boolean checkForm() {
        boolean mail = false;
        boolean pass = false;
        boolean phone = false;
        boolean check = false;
        boolean response = false;

        if (!edtPassword.getText().toString().trim().isEmpty()) {
            pass = true;
        } else {
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError("Vacío");

            edtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    edtPassword.setError(null);
                    textInputLayoutPassword.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            pass = false;
        }


        if (!edtEmail.getText().toString().trim().isEmpty()) {
            if (!validateEmail(edtEmail.getText().toString().trim())) {
                mail = true;
            } else {
                textInputLayoutEmail.setErrorEnabled(true);
                textInputLayoutEmail.setError("Verificar el fortmato de correo");

                edtEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        edtEmail.setError(null);
                        textInputLayoutEmail.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                mail = false;
            }
        } else {
            textInputLayoutEmail.setErrorEnabled(true);
            textInputLayoutEmail.setError("Vacío");

            edtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    edtEmail.setError(null);
                    textInputLayoutEmail.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mail = false;
        }

        if (!edtPhoneNumber.getText().toString().trim().isEmpty()) {
            phone = true;
        } else {
            textInputLayoutPhone.setErrorEnabled(true);
            textInputLayoutPhone.setError("Vacío");

            edtPhoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    edtPhoneNumber.setError(null);
                    textInputLayoutPhone.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            phone = false;
        }

        if (checkMSM.isChecked() || checkEmail.isChecked()) {
            if (checkMSM.isChecked()) {
                Toast.makeText(RegistryActivity.this, "SMS in process", Toast.LENGTH_SHORT).show();
                checkMSM.setChecked(false);
            } else {
                check = true;
            }
        } else {
            Toast.makeText(RegistryActivity.this, "Seleccione una opcion para recibir su PIN", Toast.LENGTH_LONG).show();
        }

        if (pass && mail && phone && check) {
            response = true;
        }

        return response;
    }

    private boolean validateEmail(String email) {

        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            Log.e(TAG, "Email false->correct");
            return false;
        }
        Log.e(TAG, "Email true->incorrect");
        return true;
    }

    private void goToVerification(int option, int code) {

        Intent i;
        switch (option){
            case OTP_EMAIL:
                i =  new Intent(RegistryActivity.this, VerifyOTPActivity.class);
                i.putExtra("codeVerification", code);
                i.putExtra("opc", OTP_EMAIL);
                i.putExtra("email", edtEmail.getText().toString().trim());
                startActivity(i);
                break;

            case OTP_SMS:
                i = new Intent(RegistryActivity.this, VerifyOTPActivity.class);
                i.putExtra("opc", OTP_SMS);
                i.putExtra("mobile", edtPhoneNumber.getText().toString().trim());
                i.putExtra("code", phoneCode);
                i.putExtra("codeVerification", code);
                startActivity(i);
                break;
        }
    }
}