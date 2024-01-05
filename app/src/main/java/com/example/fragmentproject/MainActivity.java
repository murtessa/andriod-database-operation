package com.example.fragmentproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button insertButton = findViewById(R.id.insertButton);
        Button retrieveButton = findViewById(R.id.retrieveButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(editText.getText().toString());
            }
        });

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void insertData(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

        if (newRowId != -1) {
            editText.setText("");
            textView.setText("Data inserted successfully with ID: " + newRowId);
        } else {
            textView.setText("Error inserting data.");
        }

        db.close();
    }
    private void deleteData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete all data (you can modify the WHERE clause as needed)
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, null, null);

        if (rowsDeleted > 0) {
            textView.setText("All data deleted successfully.");
        } else {
            textView.setText("No data to delete.");
        }

        db.close();
    }

    private void retrieveData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder data = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            data.append("ID: ").append(id).append(", Name: ").append(name).append("\n");
        }

        cursor.close();
        textView.setText(data.toString());

        db.close();
    }
}
