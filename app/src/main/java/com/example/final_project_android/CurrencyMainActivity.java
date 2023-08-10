package com.example.final_project_android;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.final_project_android.databinding.ActivityCurrencymainBinding;
import com.example.final_project_android.databinding.ActivityHistoryRoomBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CurrencyMainActivity extends AppCompatActivity {
    HistoryDatabase myDB ;
   static HistoryDAO myDAO;

    ArrayList<History> allstuff = new ArrayList<>();

    private @NonNull ActivityCurrencymainBinding binding;
    private TextView resultTextView;
    private SharedPreferences sharedPreferences;
    private static final String TEXT_KEY = "text_key";

    ActivityHistoryRoomBinding HRbinding;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_currency, menu);

        super.onCreateOptionsMenu(menu);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HistoryDatabase db = Room.databaseBuilder(getApplicationContext(), HistoryDatabase.class, "database-name").build();
        // myDAO = myDB.HDAO();
        myDB = db; // Assign the created database instance to myDB
        myDAO = myDB.HDAO();






        binding = ActivityCurrencymainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Currency Converter");



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



            String cc= binding.ConvertedMoneyType.getText().toString();
            String on =binding.Amount.getText().toString();
            String oc =binding.MoneyType.getText().toString();
            String cn =binding.result.getText().toString();


            History history = new History(cc,on,oc,cn);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                //myDAO.deleteAllHistory();
                myDAO.Insert(history);
                // myDAO.deleteAllHistory();


            });






            Snackbar snackbar = Snackbar.make(saveButton, "@string/You choose to save", Snackbar.LENGTH_SHORT);
//            ColorStateList colorStateList = getResources().getColorStateList(R.color.purple);
//            snackbar.setBackgroundTintList(colorStateList);
            snackbar.show();
        });





        historyButton.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyMainActivity.this);

            builder.setMessage("@string/Do you want to see the history")
                    .setTitle("@string/See Hisytory?");
            builder.setNegativeButton("No",(dialog,cl)->{});
            builder.setPositiveButton("Yes",(dialog,cl)->{

                Intent nextPage = new Intent( this, HistoryRoom.class);



                startActivity( nextPage);



            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieve and set the text from SharedPreferences
        String savedText = sharedPreferences.getString(TEXT_KEY, "");
        resultTextView.setText(savedText);

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Save the text to SharedPreferences
        String textToSave = resultTextView.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT_KEY, textToSave);
        editor.apply();
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

   public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.help) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CurrencyMainActivity.this);
            builder.setMessage("Input the currency type(3 letters word) and the number of currency, then click the convert button to get the result" )
                    .setTitle("Instruction")
                    .setPositiveButton("yes", (dialog, cl) -> {

                    }).show();
        }
        else if (item.getItemId() == R.id.item_bear) {
            startActivity(new Intent(this, Bear.class));
        }
        else if (item.getItemId() == R.id.item_flight) {
            startActivity(new Intent(this, FlightTracker.class));
        }
        else if (item.getItemId() == R.id.item_trivia) {
            startActivity(new Intent(this, TopicSelection.class));
        }

        return true;
    }
}