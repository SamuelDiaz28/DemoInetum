package mx.ine.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion;
import com.regula.documentreader.api.errors.DocumentReaderException;

import java.io.InputStream;

public class ProgressActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        textView = findViewById(R.id.txtLoading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!DocumentReader.Instance().getDocumentReaderIsReady()) {
            textView.setText("Inicializando...");
            //Reading the license from raw resource file
            try {
                InputStream licInput = getResources().openRawResource(R.raw.regula);
                int available = licInput.available();
                final byte[] license = new byte[available];
                licInput.read(license);

                //Preparing database files, it will be downloaded from network only one time and stored on user device
                DocumentReader.Instance().prepareDatabase(ProgressActivity.this, "Full", new IDocumentReaderPrepareCompletion() {
                    @Override
                    public void onPrepareProgressChanged(int i) {
                        textView.setText("Descargando DB " + i + "%");
                    }

                    @Override
                    public void onPrepareCompleted(boolean b, DocumentReaderException e) {
                        Intent i = new Intent(ProgressActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}