package com.dnext.virido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {
     EditText searchBar;
     ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        Id Assign
        searchBar = findViewById(R.id.search_searchbar);
        back = findViewById(R.id.search_back);
//        Ui Designs
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

// Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        SearchBar
        searchBar.requestFocus();
        searchBar.getShowSoftInputOnFocus();


    }
}