package com.example.soumyakalluri.mycontactapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editNumber;
    EditText editAddress;
    EditText editRelation;
    EditText editSearch;
    Button btnAddData;
    Button viewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        //add the layout vars
        editName = (EditText)findViewById(R.id.editText_name);
        editNumber = (EditText)findViewById(R.id.editText_number);
        editAddress = (EditText)findViewById(R.id.editText_address);
        editRelation = (EditText)findViewById(R.id.editText_relation);
        editSearch = (EditText)findViewById(R.id.editText_search);

        //btnAddData = (Button)findViewById(R.id.button_addData);
        //viewContacts = (Button)findViewById(R.id.button_viewContacts);
    }

    public void addData(View v)
    {
        boolean isInserted = myDb.insertData(editName.getText().toString(),
                editNumber.getText().toString(),
                editAddress.getText().toString(),
                editRelation.getText().toString());
        Context context = getApplicationContext();
        CharSequence text;

        if (isInserted)
        {
            Log.d("MyContact","Success inserting data");
            //insert toast message here...
            text = "Data inserted!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }

        else
        {
            Log.d("MyContact", "Failure inserting data");
            //insert toast message here...
            text = "Data couldn't be inserted";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void viewData(View v)
    {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0)
        {
            showMessage("Error","No data is found on the database");
            //output message using Log.d and Toast
            return;
        }

        StringBuffer buffer = new StringBuffer();
        //set up a loop with the Cursor (res) using moveToNext
        while (res.moveToNext()) {
            //append each COL to the buffer
            //display message using ShowMessage
            for (int i=0; i<res.getColumnCount(); i++)
            {
                buffer.append(res.getColumnName(i) + ": " + res.getString(i) + "\n");
            }
            buffer.append("\n");
        }
        showMessage("Contacts", buffer.toString());
    }

    private void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true); //cancel using back button
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void search(View v)
    {
        Cursor res = myDb.getAllData();
        Context context = getApplicationContext();

        if (res.getCount()==0)
        {
            showMessage("Error", "No data is found in the database");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext())
        {
            if (res.getString(1).toUpperCase().equals(editSearch.getText().toString().toUpperCase()))
            {
                for (int i=0; i<res.getColumnCount(); i++)
                {
                    buffer.append(res.getColumnName(i)+": "+res.getString(i)+"\n");
                }
                showMessage("Contact", buffer.toString());
            }

            else
            {
                showMessage("Contact not found", "Check your spelling or try another search.");
            }
        }
    }

}
