package com.example.wilsonzhu.searchengine.Activity.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;


import com.example.wilsonzhu.searchengine.Activity.SignUpPageActivity;
import com.example.wilsonzhu.searchengine.R;

public class SearchPageActivity extends AppCompatActivity {

    private String[] TestStrs = {"cscc01", "project", "problem", "airline", "combine"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_page);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TestStrs));
        mListView.setTextFilterEnabled(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
     }


     // ******************* added by Alan for basic UI connection, can be deleted or modified later
     public void signUpClicked(View view) {
        Intent i = new Intent(this, SignUpPageActivity.class);
        startActivity(i);
     }

     public void loginClicked(View view) {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
     }
    public void searchClicked(View view) {
        Intent i = new Intent(this, SearchResultDisplayActivity.class);
        startActivity(i);
    }
    public void advSearchClicked(View view) {
        Intent i = new Intent(this, AdvancedSearchActivity.class);
        startActivity(i);
    }

     //********************************************************************************************

}
