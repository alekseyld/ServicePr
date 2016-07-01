package com.alekseyld.servicepr;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.concurrent.TimeUnit;

import static com.raizlabs.android.dbflow.sql.language.Method.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.amount) TextView amount;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.number) TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
        ButterKnife.bind(this);

        long a = SQLite.select(count())
                .from(DataTable.class)
                .count();
        amount.setText("Count lines - "+String.valueOf(a));

        try {
            // if no lines in base
            if (a != 0l) {
                Where<DataTable> i = SQLite.select(DataTable_Table.name, DataTable_Table.number)
                        .from(DataTable.class)
                        .where(DataTable_Table.id.eq(SQLite.select(max(DataTable_Table.id)).from(DataTable.class)));
                name.setText("name is "+i.querySingle().name);
                number.setText("number is "+String.valueOf(i.querySingle().number));
            }
        }catch (NullPointerException e){
            e.printStackTrace();

            name.setText("Error");
            number.setText("Error");
        }
    }

//    @BindView(R.id.eTime) EditText eTime;
//    @BindView(R.id.eName) EditText eName;
//    @BindView(R.id.eNumber) EditText eNum;

    @OnClick(R.id.button)
    public void sync(){
        if(DataService.isRunning){
            Toast.makeText(this, "Service is already running", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Service start", Toast.LENGTH_SHORT).show();
//            startService(new Intent(this, DataService.class)
//                    .putExtra("time", Integer.parseInt(eTime.getText().toString()))
//                    .putExtra("name", eName.getText().toString())
//                    .putExtra("number", Integer.parseInt(eNum.getText().toString())));
            startService(new Intent(this, DataService.class));
        }
    }

}
