package mx.ine.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.ine.demo.Requests.Model.UsuarioService;
import mx.ine.demo.Requests.RetrofitRequest;
import mx.ine.demo.Util.SharedPreferencesHelper;
import mx.ine.demo.Util.Util;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "LoginActivity";

    private Button btnRegistry;
    private Button btnStart;
    private LinearLayout linearLayoutButtons;

    private EditText edtEmail;
    private EditText edtPassword;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private ProgressBar progressBar;

    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        startComponents();
    }

    private void startComponents() {
        sharedPreferencesHelper = new SharedPreferencesHelper(LoginActivity.this);

        btnRegistry = findViewById(R.id.btnRegister);
        btnStart = findViewById(R.id.btnStart);

        linearLayoutButtons = findViewById(R.id.linButtonsLogin);
        progressBar = findViewById(R.id.prgressBarLogin);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);

        textInputLayoutEmail = findViewById(R.id.text_input_email_log);
        textInputLayoutPassword = findViewById(R.id.text_input_password_log);

        btnStart.setOnClickListener(this);
        btnRegistry.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnStart :
                if (checkForm()){
                    linearLayoutButtons.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    sendLog();
                }
                break;

            case R.id.btnRegister:
                Intent i = new Intent(this, RegistryActivity.class);
                startActivity(i);
        }
    }

    private void sendLog(){
        String data = null;

        try {
            JSONObject dataJS = new JSONObject();
            dataJS.put("Correo", edtEmail.getText().toString().trim());
            dataJS.put("Password", edtPassword.getText().toString().trim());
            data = dataJS.toString();
        }catch (JSONException e){
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(data);
        Call<String> resp = service.login(body);

        resp.enqueue( new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.code() != 200) {
                    Toast.makeText( LoginActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    Boolean res = jres.getBoolean("OK");
                    if (res) {

                        sharedPreferencesHelper.putToken(jres.getJSONObject("data").getString("JWT"));
                        Util.TOKEN = jres.getJSONObject("data").getString("JWT");
                        sharedPreferencesHelper.onUserLogin(jres.getJSONObject("data").getInt("ID"));


                        linearLayoutButtons.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        goToMain();

                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + jres.getString("message"), Toast.LENGTH_LONG).show();
                        linearLayoutButtons.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }




                } catch (JSONException e) {
                    Log.e("VerificationOTPActivity", "Error al convertir en json los datos\n" + e.getMessage());
                    linearLayoutButtons.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    private void goToMain() {
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    private boolean checkForm(){
        boolean email = false;
        boolean pass = false;
        boolean response = false;


        if (!edtEmail.getText().toString().trim().isEmpty()) {
            if (!validateEmail(edtEmail.getText().toString().trim())) {
                email = true;
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
                email = false;
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
            email = false;
        }

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

        if (email && pass) {
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
}