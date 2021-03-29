package mx.ine.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSend;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtPhoneNumber;

    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.prgressBar);

        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (edtPhoneNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegistryActivity.this, "Digite el numero de telefono", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.INVISIBLE);
            /*Intent i = new Intent(RegistryActivity.this, VerifyOTPActivity.class);
            i.putExtra("mobile", edtPhoneNumber.getText().toString());
            startActivity(i);*/

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+52" + edtPhoneNumber.getText().toString(),
                    60,
                    TimeUnit.SECONDS,
                    RegistryActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            btnSend.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            btnSend.setVisibility(View.VISIBLE);
                            Toast.makeText(RegistryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(View.GONE);
                            btnSend.setVisibility(View.VISIBLE);

                            Intent i = new Intent(RegistryActivity.this, VerifyOTPActivity.class);
                            i.putExtra("mobile", edtPhoneNumber.getText().toString());
                            i.putExtra("verificationID", verificationID);
                            startActivity(i);
                        }
                    }
            );
        }
    }
}