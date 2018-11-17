package com.example.wilsonzhu.searchengine.Activity.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wilsonzhu.searchengine.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AdvancedSearchActivity extends AppCompatActivity {

    /*
    // constants for JSON keys
    private static final String KEY_SEARCH_CONTENT = "searchContent";
    private static final String KEY_FILE_TYPE = "fileType";
    private static final String KEY_TAG = "tag";
    private static final String KEY_ACCESS_GROUP = "accessGroup";

    // constants for search content and option values
    private EditText searchContentEdit = (EditText) findViewById(R.id.adv_search_edit);
    private EditText fileTypeEdit = (EditText) findViewById(R.id.adv_search_file_type_edit);
    private EditText tagEdit = (EditText) findViewById(R.id.adv_search_tag_edit);
    private EditText accessGroupEdit = (EditText) findViewById(R.id.adv_search_access_group_edit);
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        /* setting up for drop down list options
        // create drop down list for file type selection
        Spinner fileTypeSpinner = (Spinner) findViewById(R.id.adv_search_file_type);

        ArrayAdapter<String> fileTypeAdapter = new ArrayAdapter<String>(AdvancedSearchActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.adv_search_file_type_values));
        fileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileTypeSpinner.setAdapter(fileTypeAdapter);

        // create drop down list for tag selection
        Spinner tagSpinner = (Spinner) findViewById(R.id.adv_search_tag);

        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(AdvancedSearchActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.test_adv_search_tag_values));  //TODO change test_adv_search_tag_values to the actual tags that the the current user actually can access
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);
        */
    }

    public void searchButtonOnClick(View view) {

        /*
        // get all user inputs
        final String searchContent = searchContentEdit.getText().toString().trim();
        final String fileType = fileTypeEdit.getText().toString().trim();
        final String tag = tagEdit.getText().toString().trim();
        final String accessGroup = accessGroupEdit.getText().toString().trim();

        // TODO: check user inputs




        // TODO: using volley to talk to backend and get search results back in JSON
        // TODO: change the url to the actual backend url
        String searchUrl = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AdvancedSearchActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdvancedSearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            protected JSONObject getInputs() {
                // pack user inputs in JSON
                JSONObject userInputs = new JSONObject();
                try {
                    userInputs.put(KEY_SEARCH_CONTENT, searchContent);
                    userInputs.put(KEY_FILE_TYPE, fileType);
                    userInputs.put(KEY_TAG, tag);
                    userInputs.put(KEY_ACCESS_GROUP, accessGroup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userInputs;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        */

        // TODO: get back results from backend as a JSON obj



        // pass results in JSON to search result display activity and redirect to it
        Intent i = new Intent(this, SearchResultDisplayActivity.class);
        // TODO: pass the results in JSON
        startActivity(i);

    }

}
