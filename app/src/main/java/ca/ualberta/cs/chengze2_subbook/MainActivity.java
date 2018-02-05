package ca.ualberta.cs.chengze2_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "subscription.sav";
    private ListView oldSubscriptionList;

    private ArrayList<Subscription> SubscriptionList;
    private ArrayAdapter<Subscription> adapter;

    private double summaryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize the oldSubscriptionList and listen the click on items in the list
        oldSubscriptionList = (ListView) findViewById(R.id.oldSubscriptionList);
        oldSubscriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddSubscriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Case", "Edit");
                bundle.putString("Name", SubscriptionList.get(position).getName());
                bundle.putInt("Year", SubscriptionList.get(position).getDate().getYear());
                bundle.putInt("Month", SubscriptionList.get(position).getDate().getMonth());
                bundle.putInt("Day", SubscriptionList.get(position).getDate().getDay());
                bundle.putDouble("Charge", SubscriptionList.get(position).getCharge());
                bundle.putString("Comment", SubscriptionList.get(position).getComment());
                bundle.putInt("Pos", position);
                // pass the position by bundle to edit/discard subscriptions easily
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void toAdd(View view){
        // new activity by click add button
        // pass the bundle to record the requestCode
        Intent intent = new Intent(this, AddSubscriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Case", "Add");
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    // learn and took from https://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android
    // 2018-02-04
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // detect the repuestCode and result for each case
        if (requestCode == 0){
            Bundle bundle = data.getExtras();
            // resultCode == 0 : add-add
            if (resultCode == 0){
                String name = bundle.getString("Name");
                int year = bundle.getInt("Year");
                int month = bundle.getInt("Month");
                int day = bundle.getInt("Day");
                double charge = bundle.getDouble("Charge");
                String comment = bundle.getString("Comment");
                MyDate mydate = new MyDate(year, month, day);
                Subscription subscription = new Subscription(name, mydate, charge, comment);
                // add new-added subscription
                SubscriptionList.add(subscription);
                saveInFile();
            }
            // resultCode == 0 : add-discard
        }
        else if (requestCode == 1){
            Bundle bundle = data.getExtras();
            // discard current subscription
            SubscriptionList.remove(bundle.getInt("Pos"));
            // resultCode == 0 : edit-edit
            if (resultCode == 0){
                String name = bundle.getString("Name");
                int year = bundle.getInt("Year");
                int month = bundle.getInt("Month");
                int day = bundle.getInt("Day");
                double charge = bundle.getDouble("Charge");
                String comment = bundle.getString("Comment");
                MyDate mydate = new MyDate(year, month, day);
                Subscription subscription = new Subscription(name, mydate, charge, comment);
                // add edited subscription
                SubscriptionList.add(subscription);
            }
            // resultCode == 1 : edit-discard
            saveInFile();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // load arrayList from file
        loadFromFile();
        // use for each loop to get the charge sum
        summaryAmount = 0;
        for (Subscription s :  SubscriptionList){
            summaryAmount = summaryAmount + s.getCharge();
        }
        TextView editSummaryAmount = (TextView) findViewById(R.id.summaryAmount);
        DecimalFormat twoDig = new DecimalFormat("#.00");
        editSummaryAmount.setText("Summary Amount: $ " + twoDig.format(summaryAmount));

        adapter = new ArrayAdapter<Subscription>(this,
                R.layout.list_item, SubscriptionList);
        oldSubscriptionList.setAdapter(adapter);


    }
    // learn from lab lonely twitter
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            // load arrayList from gson file
            SubscriptionList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            // create a new subscriptionList if cannot find a file
            SubscriptionList = new ArrayList<Subscription>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
    // learn from lab lonely twitter
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            // transfer from arrayList to gson file
            gson.toJson(SubscriptionList, out);
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
