package com.example.lab3_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText[] input = new EditText[5];
    Button startButton;
    TextView res, execTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    @Override
    public void onClick(View v) {

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

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            public Object call() throws InterruptedException {
                Genetic genetic = new Genetic(
                        inputInteger[0],
                        inputInteger[1],
                        inputInteger[2],
                        inputInteger[3],
                        inputInteger[4]
                );
                String result = genetic.calculate();
                return result;
            }
        };
        Future<Object> future = executor.submit(task);

        try {
            Object result = future.get(Integer.valueOf(execTimeout.getText().toString()), TimeUnit.SECONDS);
            res.setText(result.toString());
        } catch (TimeoutException ex) {
            res.setText("Execution took too long");
        } catch (InterruptedException e) {
            res.setText("Something went wrong");
        } catch (ExecutionException e) {
            res.setText("Something went wrong");
        } finally {
            future.cancel(true);
        }
    }

    public void setup() {
        input[0] = findViewById(R.id.x1);
        input[1] = findViewById(R.id.x2);
        input[2] = findViewById(R.id.x3);
        input[3] = findViewById(R.id.x4);
        input[4] = findViewById(R.id.y);
        execTimeout = findViewById(R.id.editTimeout);
        res = findViewById(R.id.result);
        startButton = findViewById(R.id.button);
        startButton.setOnClickListener(this);
    }
}
