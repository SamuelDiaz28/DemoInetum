package mx.ine.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView cardMX;
    private CardView cardArg;
    private CardView cardCol;
    private CardView cardPeru;

    private CheckBox checkPassport;
    private CheckBox checkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().hide();
        startComponents();
    }

    private void startComponents() {
        cardMX = findViewById(R.id.cardMX);
        cardArg = findViewById(R.id.cardArg);
        cardCol = findViewById(R.id.cardCol);
        cardPeru = findViewById(R.id.cardPeru);

        checkPassport = findViewById(R.id.checkPassport);
        checkID = findViewById(R.id.checkID);

        cardMX.setOnClickListener(this);
        cardArg.setOnClickListener(this);
        cardCol.setOnClickListener(this);
        cardPeru.setOnClickListener(this);

        checkPassport.setOnClickListener(this);
        checkID.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(DashboardActivity.this, MainActivity.class);
        switch (v.getId()){
            case R.id.cardMX:
                if (checkPassport.isChecked() || checkID.isChecked())
                    startActivity(i);
                else
                    Toast.makeText(DashboardActivity.this, "Seleccione una opción debajo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cardArg:
                if (checkPassport.isChecked() || checkID.isChecked())
                    startActivity(i);
                else
                    Toast.makeText(DashboardActivity.this, "Seleccione una opción debajo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cardCol:
                if (checkPassport.isChecked() || checkID.isChecked())
                    startActivity(i);
                else
                    Toast.makeText(DashboardActivity.this, "Seleccione una opción debajo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cardPeru:
                if (checkPassport.isChecked() || checkID.isChecked())
                    startActivity(i);
                else
                    Toast.makeText(DashboardActivity.this, "Seleccione una opción debajo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.checkPassport:
                if (checkID.isChecked()) {
                    checkPassport.setChecked(true);
                    checkID.setChecked(false);
                }
                break;

            case R.id.checkID:
                if (checkPassport.isChecked()) {
                    checkID.setChecked(true);
                    checkPassport.setChecked(false);
                }
                break;
        }
    }
}