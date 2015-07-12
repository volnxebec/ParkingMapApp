package com.example.wind.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.app.SearchManager;

/**
 * Created by Wind on 7/7/2015.
 */
public class SearchableActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    //Search operation
    private void doMySearch(String query){
        //TODO: Fill this function...
    }
}
