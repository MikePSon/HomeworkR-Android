package com.example.mikepatterson.homeworkr_actual;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.mikepatterson.homeworkr_actual.JSONParser;


public class MainActivity extends ActionBarActivity {
    ListView list;
    TextView assign_title_view;
    TextView assign_due_view;
    TextView assign_description;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "https://enigmatic-wave-8305.herokuapp.com/assignments.json";
//    private static String url = "http://10.0.2.2:3000/assignments.json";

    //JSON Node Names
    private static final String TAG_TYPE = "tasks";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "name";
    private static final String TAG_DESC = "notes";
    private static final String TAG_COMPLETE = "complete";
    private static final String TAG_DUE = "start";
    private static final String TAG_END = "end";
    private static final String TAG_URL = "url";


    JSONArray assignArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new JSONParse().execute();

        setContentView(R.layout.activity_main);
        oslist = new ArrayList<HashMap<String, String>>();

//        Btngetdata = (Button)findViewById(R.id.getdata);
//        Btngetdata.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                new JSONParse().execute();
//
//            }
//        });

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            assign_title_view = (TextView)findViewById(R.id.assign_title);
            assign_due_view = (TextView)findViewById(R.id.assign_due_date);
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Getting Assignments ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
//            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                assignArr = json.getJSONArray(TAG_TYPE);
                for(int i = 0; i < assignArr.length(); i++){
                    JSONObject c = assignArr.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String assign_title_str = c.getString(TAG_TITLE);
                    String assign_due_str = c.getString(TAG_DUE);
                    String assign_desc_str = c.getString(TAG_DESC);
                    String assign_complete_bool = c.getString(TAG_COMPLETE);

                    String assign_complete_str = c.getString(TAG_COMPLETE);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_TITLE, assign_title_str);
                    map.put(TAG_DUE, assign_due_str);
                    map.put(TAG_DESC, assign_desc_str);
                    map.put(TAG_COMPLETE, assign_complete_bool);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_TITLE,TAG_DUE, TAG_DESC }, new int[] {
                            R.id.assign_title,R.id.assign_due_date,R.id.assign_description});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Toast.makeText(MainActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                            String assignName = oslist.get(+position).get("name");
                            String assignDue = oslist.get(+position).get("start");
                            String assignNotes = oslist.get(+position).get("notes");
                            String assignComplete = oslist.get(+position).get("complete");
                            String assignID = oslist.get(+position).get("id");
                            Intent i = new Intent(getApplicationContext(),DetailActivity.class);
                            i.putExtra("assignName",assignName);
                            i.putExtra("assignDue",assignDue);
                            i.putExtra("assignNotes",assignNotes);
                            i.putExtra("assignComplete",assignComplete);
                            i.putExtra("assignID",assignID);
                            startActivity(i);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}