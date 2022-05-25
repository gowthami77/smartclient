package ai.supervue.smartclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onClickAddDetails(View view) {

        validate();

    }

    private void validate() {
        if(!imageFile(((EditText) findViewById(R.id.textUrl)).getText().toString())){
            Toast.makeText(MainActivity.this,"Please enter valid Url",Toast.LENGTH_LONG).show();
        }else if(!imageFile(((EditText) findViewById(R.id.textName)).getText().toString())){
            Toast.makeText(MainActivity.this,"Please enter valid Name with extension",Toast.LENGTH_LONG).show();
        }else{
            addValues();
        }
    }

    private void addValues() {
        ContentValues values = new ContentValues();

        values.put(MyContentProvider.name, ((EditText) findViewById(R.id.textName)).getText().toString());
        values.put(MyContentProvider.fileUrl, ((EditText) findViewById(R.id.textUrl)).getText().toString());
        // inserting into database through content URI


        Cursor cursor = getContentResolver().query(Uri.parse("content://com.demo.user.provider/users"), null, null, null, null);

        Toast.makeText(MainActivity.this,"Download request send",Toast.LENGTH_LONG).show();
        if (cursor.moveToFirst()) {
            Log.e("update", "........");
            getContentResolver().update(MyContentProvider.CONTENT_URI, values, null, null);

        } else {
            Log.e("insert", "........");

            getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

        }
    }

    @SuppressLint("Range")
    public void onClickShowDetails(View view) {
        // inserting complete table details in this text field
        TextView resultView= (TextView) findViewById(R.id.res);

        // creating a cursor object of the
        // content URI
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.demo.user.provider/users"), null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if(cursor.moveToFirst()) {
            StringBuilder strBuild=new StringBuilder();
            while (!cursor.isAfterLast()) {
                strBuild.append("\n"+cursor.getString(cursor.getColumnIndex("id"))+ "-"+ cursor.getString(cursor.getColumnIndex("name"))+"-"+cursor.getString(cursor.getColumnIndex("fileUrl")));
                cursor.moveToNext();
            }
            resultView.setText(strBuild);
        }
        else {
            resultView.setText("No Records Found");
        }
    }
    public static boolean imageFile(String str)
    {
        // Regex to check valid image file extension.
        String regex
                = "([^\\s]+(\\.(?i)(jpe?g|png|gif|mp4|html))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
}