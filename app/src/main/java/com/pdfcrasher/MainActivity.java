package com.pdfcrasher;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener {

    PDFView pdfView;
    TextView pageNumberTextView;
    EditText pageNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pdfView = (PDFView) findViewById(R.id.pdfview);
        pageNumberTextView = (TextView) findViewById(R.id.pageNumberTextView);
        pageNumberEditText = (EditText) findViewById(R.id.pageNumberEditText);

        pdfView.fromAsset("sample.pdf")
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                .swipeVertical(true)
                .onPageChange(this) // Set the page change listener
                .load();

        pageNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        int pageNumber = Integer.parseInt(s.toString());
                        if (pageNumber > 0 && pageNumber <= pdfView.getPageCount()) {
                            pdfView.jumpTo(pageNumber);
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid page number", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid page number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumberTextView.setText(String.format("%s / %s", page, pageCount));
    }

}