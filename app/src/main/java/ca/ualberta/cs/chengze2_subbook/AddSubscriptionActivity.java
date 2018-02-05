package ca.ualberta.cs.chengze2_subbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddSubscriptionActivity extends AppCompatActivity {

    String SubscriptionName;
    int year, month, day;
    String SubscriptionCharge;
    String SubscriptionComment;
    Double DoubleCharge;
    Button chooseDate;
    Button addSubscription;
    Button discardSubscription;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        // button to choose date
        chooseDate = (Button) findViewById(R.id.inputDateButton);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(1);
            }
        });

        // initialize the intent and bundle
        Intent intentCase = getIntent();
        Bundle bundleCase;
        bundleCase = intentCase.getExtras();
        // put requestCode and the position of item in arrayList into a bundle
        final String requestCase = bundleCase.getString("Case");
        final int pos = bundleCase.getInt("Pos");
        // requestCode = 1
        if (requestCase.equals("Edit")){
            // get the current information of subscription and set them as a default value
            year = bundleCase.getInt("Year");
            month = bundleCase.getInt("Month");
            day = bundleCase.getInt("Day");
            chooseDate.setText(year + "-" + month + "-" + day);
            EditText editTextName = (EditText) findViewById(R.id.inputName);
            editTextName.setText(bundleCase.getString("Name"));
            EditText editTextCharge = (EditText) findViewById(R.id.inputCharge);
            editTextCharge.setText(Double.toString(bundleCase.getDouble("Charge")));
            EditText editTextComment = (EditText) findViewById(R.id.inputComment);
            editTextComment.setText(bundleCase.getString("Comment"));
        }
        // requestCode = 0
        else if (requestCase.equals("Add")){
            // initialize a new value for data(current date)
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            chooseDate.setText(year + "-" + (month + 1) + "-" + day);
        }


        // button to click add
        addSubscription = (Button) findViewById(R.id.addSubscriptionButton);
        addSubscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // initialize the default values after click finish button
                EditText editTextName = (EditText) findViewById(R.id.inputName);
                SubscriptionName = editTextName.getText().toString();
                EditText editTextCharge = (EditText) findViewById(R.id.inputCharge);
                SubscriptionCharge = editTextCharge.getText().toString();
                EditText editTextComment = (EditText) findViewById(R.id.inputComment);
                SubscriptionComment = editTextComment.getText().toString();
                // make sure the input text from name and charge are not empty
                if ((SubscriptionName.length() < 1) || (SubscriptionCharge.length() < 1)) {
                    showDialog(2);
                }
                else {
                    // a flag to show the value gotten from charge is double or not
                    Boolean getNum;
                    getNum = true;
                    try {
                        DoubleCharge = Double.parseDouble(SubscriptionCharge);
                    }
                    catch(Exception e){
                        getNum = false;
                        showDialog(3);
                    }

                    if (getNum){
                        // initialize a intent and bundle to pass necessary value back to
                        // previous activity (the object would be instantiated in MainActivity)
                        Intent intent = new Intent();
                        if (requestCase.equals("Add")){
                            Bundle bundle = new Bundle();
                            bundle.putString("Name", SubscriptionName);
                            bundle.putInt("Year", year);
                            bundle.putInt("Month", month);
                            bundle.putInt("Day", day);
                            bundle.putDouble("Charge", DoubleCharge);
                            bundle.putString("Comment", SubscriptionComment);
                            intent.putExtras(bundle);
                            setResult(0, intent);
                            finish();
                        }
                        else if (requestCase.equals("Edit")){
                            Bundle bundle = new Bundle();
                            bundle.putString("Name", SubscriptionName);
                            bundle.putInt("Year", year);
                            bundle.putInt("Month", month);
                            bundle.putInt("Day", day);
                            bundle.putDouble("Charge", DoubleCharge);
                            bundle.putString("Comment", SubscriptionComment);
                            bundle.putInt("Pos", pos);
                            intent.putExtras(bundle);
                            setResult(0, intent);
                            finish();
                        }
                    }
                }
            }
        });

        // button to discard
        discardSubscription = (Button) findViewById(R.id.discardSubscription);
        discardSubscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // initialize a intent
                Intent intent = new Intent();
                if (requestCase.equals("Add")) {
                    setResult(1, intent);
                    finish();
                } else if (requestCase.equals("Edit")) {
                    // initialize a bundle to record position of subscription which would be discard
                    Bundle bundle = new Bundle();
                    bundle.putInt("Pos", pos);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }


    @Override
    // learn and took from https://stackoverflow.com/questions/39916178/how-to-show-datepickerdialog-on-button-click
    //                      https://stackoverflow.com/questions/4954130/center-message-in-android-dialog-box
    // 2018-02-04
    protected Dialog onCreateDialog(int id) {
        // Build dialogs
        switch (id) {
            case 1:
                // show date picker dialog
                return new DatePickerDialog(this, dateListener, year, month, day);

            case 2:
                // show a alert message to warn for empty lines
                AlertDialog.Builder emptyAlert = new AlertDialog.Builder(this);
                emptyAlert.setTitle("Warning");
                emptyAlert.setMessage("Please fill all lines!");
                emptyAlert.setPositiveButton("OK", null);
                emptyAlert.show();

            case 3:
                // show a alert message to warn for unrecognizable charge(double)
                AlertDialog.Builder chargeAlert = new AlertDialog.Builder(this);
                chargeAlert.setTitle("Warning");
                chargeAlert.setMessage("Cannot recognize your charge!");
                chargeAlert.setPositiveButton("OK", null);
                chargeAlert.show();
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        // get the date (year, month, day) from date picker dialog and show them on button
        public void onDateSet(DatePicker view, int chosenYear, int chosenMonth, int chosenDay) {
            year = chosenYear;
            month = chosenMonth + 1;
            day = chosenDay;
            chooseDate.setText(year + "-" + month + "-" + day);
        }
    };


}
