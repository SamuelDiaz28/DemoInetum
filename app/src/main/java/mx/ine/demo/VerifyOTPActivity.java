package mx.ine.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import mx.ine.demo.Requests.Model.UsuarioService;
import mx.ine.demo.Requests.RetrofitRequest;
import mx.ine.demo.Util.SharedPreferencesHelper;
import mx.ine.demo.Util.Util;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private final int OTP_SMS = 10;
    private final int OTP_EMAIL = 11;

    private EditText inputCode1;
    private EditText inputCode2;
    private EditText inputCode3;
    private EditText inputCode4;
    private EditText inputCode5;
    private EditText inputCode6;

    private TextView txtMobile;
    private Button btnVerify;
    private ProgressBar progressBar;

    private int verificationId;
    private String emailUser;

    private SharedPreferencesHelper sharedPreferencesHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        sharedPreferencesHelper = new SharedPreferencesHelper(VerifyOTPActivity.this);

        startComponents();
    }

    private void startComponents() {
        inputCode1 = findViewById(R.id.imputCode1);
        inputCode2 = findViewById(R.id.imputCode2);
        inputCode3 = findViewById(R.id.imputCode3);
        inputCode4 = findViewById(R.id.imputCode4);
        inputCode5 = findViewById(R.id.imputCode5);
        inputCode6 = findViewById(R.id.imputCode6);

        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.prgressBarVerify);

        txtMobile = findViewById(R.id.txtMobile);

        int opc = getIntent().getIntExtra("opc", 0);

        if (OTP_EMAIL == opc) {
            emailUser = getIntent().getStringExtra("email");
            txtMobile.setText(emailUser);
        } else if (OTP_SMS == opc) {
            txtMobile.setText(String.format(getIntent().getStringExtra("code") +
                    "- " + getIntent().getStringExtra("mobile")
            ));
        }


        verificationId =  getIntent().getIntExtra("codeVerification", 0);

        setupInputs();

        btnVerify.setOnClickListener(this);
    }

    private void setupInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVerify) {
            btnVerify.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            if (inputCode1.getText().toString().trim().isEmpty() ||
                    inputCode2.getText().toString().trim().isEmpty() ||
                    inputCode3.getText().toString().trim().isEmpty() ||
                    inputCode4.getText().toString().trim().isEmpty() ||
                    inputCode5.getText().toString().trim().isEmpty() ||
                    inputCode6.getText().toString().trim().isEmpty()) {
                Toast.makeText(VerifyOTPActivity.this, "Por favor igresa el codigo correcto", Toast.LENGTH_LONG).show();

                btnVerify.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                return;
            }

            String code = inputCode1.getText().toString().trim() +
                    inputCode2.getText().toString().trim() +
                    inputCode3.getText().toString().trim() +
                    inputCode4.getText().toString().trim() +
                    inputCode5.getText().toString().trim() +
                    inputCode6.getText().toString().trim();

            sendCodeVerification(Integer.parseInt(code));
        }
    }

    private void sendCodeVerification(int code) {

        String data = null;
        try {
            JSONObject dataJS = new JSONObject();
            dataJS.put("Correo", emailUser);
            dataJS.put("Code", code);

            data = dataJS.toString();
        }catch (JSONException e){
            Log.e("MainActivity", "Error al convertir en json los datos\n" + e.getMessage());
        }

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(data);
        Call<String> response = service.validation(body);
        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d("Codigo de respuesta", String.valueOf(response.code()));
                if (response.code() != 200) {
                    Toast.makeText(VerifyOTPActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    Boolean res = jres.getBoolean("OK");
                    if (res) {

                        sharedPreferencesHelper.putToken(jres.getJSONObject("data").getString("JWT"));
                        Util.TOKEN = jres.getJSONObject("data").getString("JWT");
                        sharedPreferencesHelper.onUserLogin(jres.getJSONObject("data").getInt("ID"));

                        Toast.makeText(VerifyOTPActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        btnVerify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        goToMain();

                    } else {
                        Toast.makeText(VerifyOTPActivity.this, "Error: " + jres.getString("message"), Toast.LENGTH_LONG).show();
                        btnVerify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }




                } catch (JSONException e) {
                    Log.e("VerificationOTPActivity", "Error al convertir en json los datos\n" + e.getMessage());
                    btnVerify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                btnVerify.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void goToMain() {
        Intent i = new Intent(VerifyOTPActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}