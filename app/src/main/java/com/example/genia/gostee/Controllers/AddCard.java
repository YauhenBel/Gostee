package com.example.genia.gostee.Controllers;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.example.genia.gostee.Adapters.RecyclerAddAdapter;
import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.Objects.Card;
import com.example.genia.gostee.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCard extends AppCompatActivity {

    RecyclerAddAdapter recyclerAdapter;
    ArrayList<String> arrayList;

    private List<Card> cards;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        SearchOperator();


        preferences = getSharedPreferences("info", MODE_PRIVATE);
        String idsCards = preferences.getString("idsCards", "");

        Thread thread = new Thread(new MyClass());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerAdapter = new RecyclerAddAdapter(cards, this, preferences.getString("userId", ""), idsCards);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAddCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getCards() {
        arrayList = new ArrayList<>();

        String SERVER_NAME = "http://r2551241.beget.tech";
        String input = SERVER_NAME + "/gostee.php?action=getCards";
        ConnDB connDB = new ConnDB();
        String ansver = "[" + connDB.sendRequest(input, this) + "]";

        if (ansver != null && !ansver.isEmpty()) {
            Log.i("ConnDB", "+ Connect ---------- reply contains JSON:" + ansver);

            Log.i("userLogIn", " - answer: " + ansver);
            ObjectMapper objectMapper = new ObjectMapper();
            //JsonNode jsonNode = null;
            try {
                cards = objectMapper.readValue(ansver, new TypeReference<List<Card>>(){});

                for (Card card:cards) {
                    Log.i("Main2Activity", "Id карты: " + card.getCard_id());
                    Log.i("Main2Activity", "Название заведения: " + card.getName());
                    Log.i("Main2Activity", "Ссылка на картинку: " + card.getIndividual_icon());
                    Log.i("Main2Activity", "-------------------------------------------------");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyClass implements Runnable{

        @Override
        public void run() {
            getCards();
        }
    }

    public void goBack(View view){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("statusADD", recyclerAdapter.getStatus());
        editor.apply();
        finish();}

    private void SearchOperator(){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.getBackground();

        // отслеживаем изменения текста в поисковом поле
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // фильтруем recycler view при окончании ввода
                    recyclerAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // фильтруем recycler view при изменении текста
                    recyclerAdapter.getFilter().filter(query);
                    return false;
                }
            });
        }
}
