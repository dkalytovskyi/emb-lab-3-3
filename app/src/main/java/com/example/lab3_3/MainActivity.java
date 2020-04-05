package com.example.lab3_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText[] input = new EditText[5];
    Button startButton;
    TextView res;
    ProgressDialog progressBar;
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    @Override
    public void onClick(View v) {

        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        res.setText("");
        final Integer[] inputInteger = new Integer[5];

        for (int i = 0; i < input.length; i++) {
            if (input[i].getText().toString().trim().length() == 0) {
                res.setText("Not all parameters are provided!");
                return;
            }
            try {
                inputInteger[i] = Integer.valueOf(input[i].getText().toString().trim());
            } catch (Exception e) {
                res.setText("Decrease the values");
                return;
            }
        }

        for (int i = 0; i < input.length - 1; i++) {
            if (inputInteger[i] > inputInteger[inputInteger.length - 1]) {
                res.setText("Will require negative values,\n decrease the parameters!");
                return;
            }

        }
        Thread th = new Thread(new Runnable() {
            public void run() {

                Genetic genetic = new Genetic(
                        inputInteger[0],
                        inputInteger[1],
                        inputInteger[2],
                        inputInteger[3],
                        inputInteger[4]
                );
                String result = genetic.calculate();
                res.setText(result);

            }
        });
        Thread th2 = new Thread(new Runnable() {
            public void run() {
                myHandler.post(new Runnable() {
                    public void run() {
                        progressBar.cancel();

                    }
                });
            }
        });

        myHandler.postDelayed(th, 330);

        progressBar.show();

        myHandler.postDelayed(th2, 900);


    }

    public void setup() {
        input[0] = findViewById(R.id.x1);
        input[1] = findViewById(R.id.x2);
        input[2] = findViewById(R.id.x3);
        input[3] = findViewById(R.id.x4);
        input[4] = findViewById(R.id.y);
        res = findViewById(R.id.result);
        startButton = findViewById(R.id.button);
        startButton.setOnClickListener(this);
    }
}
