package mx.ine.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegistry;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        startComponents();
    }

    private void startComponents() {
        btnRegistry = findViewById(R.id.btnRegister);
        btnStart = findViewById(R.id.btnStart);


        btnStart.setOnClickListener(this);
        btnRegistry.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnStart :
                Toast.makeText(this, "In Process", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnRegister:
                Intent i = new Intent(this, RegistryActivity.class);
                startActivity(i);
        }
    }
}