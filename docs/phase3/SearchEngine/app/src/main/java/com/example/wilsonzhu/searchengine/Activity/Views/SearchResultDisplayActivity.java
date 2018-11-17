package com.example.wilsonzhu.searchengine.Activity.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.wilsonzhu.searchengine.R;

public class SearchResultDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_display);

        String[] testContents = {"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10", "test11", "test1"};
        ListAdapter contentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testContents);
        ListView resultList = findViewById(R.id.result_list);
        resultList.setAdapter(contentsAdapter);

        resultList.setOnItemClickListener(new resultListItemClick());

    }



    class resultListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            resultListItemClicked(adapterView, view, position, id);
        }
    }

    private void resultListItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
        // Just for testing
        String testContent = String.valueOf(adapterView.getItemAtPosition(position));
        Toast.makeText(SearchResultDisplayActivity.this, testContent, Toast.LENGTH_SHORT).show();
    }
}
