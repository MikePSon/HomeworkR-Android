package com.example.mikepatterson.homeworkr_actual;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import android.app.ProgressDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class DetailActivity extends ActionBarActivity {

    //Heroku Deployment
    private static String url = "https://enigmatic-wave-8305.herokuapp.com/assignments.json";

    //Local Host Url
//    private static String url = "http://10.0.2.2/assignments.json";
    static OutputStreamWriter is = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Intent thisIntent = getIntent();
        initVals(thisIntent);

        final CheckBox thisCompleteBox = (CheckBox)findViewById(R.id.assignmentComplete);
        thisCompleteBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String newCompleteVal;
                if(thisCompleteBox.isChecked()){
                    newCompleteVal = "true";
                }else{
                    newCompleteVal = "false";
                }
                completePost(thisIntent, newCompleteVal);
            }
        });
    }

    private class sendComplete extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... thisID) {

            try {
//                String thisURL = "http://10.0.2.2:3000/assignments/" + 1 + "/toggleComplete";
                String thisURL = "enigmatic-wave-8305.herokuapp.com/assignments" + 1 + "/toggleComplete";
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(thisURL);
                HttpResponse response = httpClient.execute(httpPut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean complete) {
            Toast.makeText(DetailActivity.this, "Assignment has been updated" , Toast.LENGTH_SHORT).show();
        }
    }


    public void completePost(Intent thisIntent, String newVal){
        String thisName = thisIntent.getStringExtra("assignName");
        String thisDesc = thisIntent.getStringExtra("assignNotes");
        String thisDue = thisIntent.getStringExtra("assignDue");
        String thisCompleteStr = newVal;
        String thisID = thisIntent.getStringExtra("assignID");
        new sendComplete().execute(thisID);

    }

    public void initVals(Intent thisIntent){
        String thisName = thisIntent.getStringExtra("assignName");
        TextView assignmentNameTV = (TextView)findViewById(R.id.assignmentName);
        assignmentNameTV.setText(thisName);

        String thisDesc = thisIntent.getStringExtra("assignNotes");
        TextView assignmentNotesTV = (TextView)findViewById(R.id.assignmentNotes);
        assignmentNotesTV.setText(thisDesc);

        String thisDue = thisIntent.getStringExtra("assignDue");
        thisDue = thisDue.substring(0,10);
        TextView assignmentDueTV = (TextView)findViewById(R.id.assignmentDue);
        assignmentDueTV.setText("Due: " + thisDue);

        String thisCompleteStr = thisIntent.getStringExtra("assignComplete");
        CheckBox thisCompleteView = (CheckBox)findViewById(R.id.assignmentComplete);
        Integer boolCheck = thisCompleteStr.indexOf("t");
        if(boolCheck >= 0){
            thisCompleteView.setChecked(true);
        } else{
            thisCompleteView.setChecked(false);

            //Reminder Toast for students
//            Toast.makeText(DetailActivity.this, "Due in x days", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}