package com.example.spreadsheettest;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<Reading> readingList;
    ListView listView;
    Reading reading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontent_layout);

        myDB = new DatabaseHelper(this);
        readingList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        if (numRows == 0) {
            Toast.makeText(ViewListContents.this, "No data recorded.", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                reading = new Reading(data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                readingList.add(reading);
            }
       /*    ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view, readingList);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
*/
        }
    }
}