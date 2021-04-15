package mx.ine.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import mx.ine.demo.Adapters.OnboardingPagerAdapter;
import mx.ine.demo.Requests.Model.UsuarioService;
import mx.ine.demo.Requests.RetrofitRequest;
import mx.ine.demo.Util.SharedPreferencesHelper;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private ViewPager2 viewPager;
    private LinearLayout layoutOnboardingIndicators;

    private OnboardingPagerAdapter pagerAdapter;
    private MaterialButton btnNext;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesHelper = new SharedPreferencesHelper(MainActivity.this);

        viewPager = findViewById(R.id.onboardingViewPager);
        layoutOnboardingIndicators = findViewById(R.id.layoutIndicators);
        btnNext = findViewById(R.id.btnOnboardingAction);

        pagerAdapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() + 1 < pagerAdapter.getItemCount()){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1 );
                } else {
                    startActivity(new Intent(getApplicationContext(), DataActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mn_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> response = service.logout();
        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(MainActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    sharedPreferencesHelper.onUserLogout();
                    sharedPreferencesHelper.removeToken();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[pagerAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i= 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    public void setCurrentOnboardingIndicator(int index){
        int childCount  = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView)layoutOnboardingIndicators.getChildAt(i);
            if (i == index ){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if (index == pagerAdapter.getItemCount() - 1 ){
            btnNext.setText("Terminar");
        } else {
            btnNext.setText("Siguiente");
        }
    }
}