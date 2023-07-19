package com.example.final_project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project_android.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button convertButton = binding.convert;
        Button saveButton = binding.save;
        Button historyButton = binding.history;
        resultTextView = binding.result;



        convertButton.setOnClickListener(v -> {
            long amount = Long.parseLong(binding.Amount.getText().toString());
            String moneyType = binding.MoneyType.getText().toString();
            String convertedMoneyType = binding.ConvertedMoneyType.getText().toString();
            String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_aRJQGlPUr3Lq9gizAnf2eCYjZ8TDGCJSHzPSLicC";
            new ConvertCurrencyTask().execute(apiUrl, moneyType, convertedMoneyType, String.valueOf(amount));
            CharSequence text = "Convert!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        });


        saveButton.setOnClickListener(v -> {


            Snackbar snackbar = Snackbar.make(saveButton, "You choose to save", Snackbar.LENGTH_SHORT);
            ColorStateList colorStateList = getResources().getColorStateList(R.color.purple);
            snackbar.setBackgroundTintList(colorStateList);
            snackbar.show();
        });


        historyButton.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Do you want to see the history")
                    .setTitle("See Hisytory?");
            builder.setNegativeButton("No",(dialog,cl)->{});
            builder.setPositiveButton("Yes",(dialog,cl)->{});
            AlertDialog dialog = builder.create();
            dialog.show();
        });



    }




    private class ConvertCurrencyTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            String apiUrl = params[0];
            String moneyType = params[1];
            String convertedMoneyType = params[2];
            String amount = params[3];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    inputStream.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                   // JSONObject rates = jsonObject.getJSONObject("data").getJSONObject("rates");
                    //JSONObject rates = jsonObject.getJSONObject("data").getJSONObject("rates");
                   //double rate1 = rates.getDouble(moneyType);
                    //double rate2 = rates.getDouble(convertedMoneyType);

                    JSONObject ratesObject = jsonObject.getJSONObject("data");
                    double rate1 = ratesObject.getDouble(moneyType);
                    double rate2 = ratesObject.getDouble(convertedMoneyType);

                    // Perform the currency conversion
                    double convertedAmount = (rate2 / rate1) * Double.parseDouble(amount);
                    return convertedAmount;
                } else {
                    // Handle API call error
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

            @Override
        protected void onPostExecute(Double convertedAmount) {
            if (convertedAmount != null) {
                resultTextView.setText(String.valueOf(convertedAmount));
            } else {
                resultTextView.setText("Error converting currency");
            }
        }
    }
}