package mx.ine.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import mx.ine.demo.Util.SharedPreferencesHelper;

public class IdiomsActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardEnglish;
    private CardView cardSpanish;
    private CardView cardFrench;
    private CardView cardPortuguese;

    private SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioms);
        sharedPreferencesHelper = new SharedPreferencesHelper(IdiomsActivity.this);

        skipLogin();

        getSupportActionBar().hide();
        startComponents();
    }

    private void startComponents() {
        cardEnglish = findViewById(R.id.cardViewEnglish);
        cardSpanish = findViewById(R.id.cardViewSpanish);
        cardFrench = findViewById(R.id.cardViewFrench);
        cardPortuguese = findViewById(R.id.cardViewPortuguese);

        cardEnglish.setOnClickListener(this);
        cardSpanish.setOnClickListener(this);
        cardFrench.setOnClickListener(this);
        cardPortuguese.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(IdiomsActivity.this, LoginActivity.class);
        switch (v.getId()){
            case R.id.cardViewEnglish :
                startActivity(i);
                break;

            case R.id.cardViewSpanish :
                startActivity(i);
                break;

            case R.id.cardViewFrench :
                startActivity(i);
                break;

            case R.id.cardViewPortuguese:
                startActivity(i);
                break;
        }

    }

    public void skipLogin(){
        if (sharedPreferencesHelper.isUserLogged()){
            Intent intent = new Intent(IdiomsActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }
}