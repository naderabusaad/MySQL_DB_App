package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] urls, restaurants;
    Button InsertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getJSON("http://192.168.1.100/Android/get_resturants.php");
        listView = findViewById(R.id.listView);
        InsertBtn = findViewById(R.id.InsertBtn);

        InsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),InsertActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSONObj = new GetJSON();
        getJSONObj.execute();
    }

    private void loadIntoListView(String s) throws JSONException {
        JSONObject jsnobject = new JSONObject(s);
        JSONArray jsonArray = jsnobject.getJSONArray("restaurants");
        restaurants = new String[jsonArray.length()];
        urls = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            String strName = explrObject.getString("name");
            //String str2 = explrObject.getString("url");
            restaurants[i]=strName;
            //urls[i]=str2;
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurants){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the current item from ListView
                        TextView view = (TextView)super.getView(position,convertView,parent);
                        // Initialize a TextView for ListView each Item

                        view.setTextColor(Color.parseColor("#6600FF"));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                        if(position %2 == 1) {
                            // Set a background color for ListView regular row
                            view.setBackgroundColor(Color.parseColor("#D2B4DE"));
                        }
                        else {
                            // Set the background color for alternate row
                            view.setBackgroundColor(Color.parseColor("#F5CBA7"));
                        }
                        return view;
                    }
                };

        listView.setAdapter(itemsAdapter);
    }
}
