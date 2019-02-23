package com.example.genia.gostee.Controllers.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.genia.gostee.Controllers.Adapters.RecyclerAddAdapter;
import com.example.genia.gostee.Controllers.ConnToDB.ConnDB;
import com.example.genia.gostee.Controllers.Objects.Card;
import com.example.genia.gostee.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddCard extends AppCompatActivity {

    RecyclerAddAdapter recyclerAdapter;
    ArrayList<String> arrayList;

    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private ConnDB connDB;
    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Thread thread = new Thread(new MyClass());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerAdapter = new RecyclerAddAdapter(cards, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAddCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getCards() {
        arrayList = new ArrayList<>();

        input = SERVER_NAME + "/gostee.php?action=getCards";
        connDB = new ConnDB();
        ansver = "[" +connDB.sendRequest(input) + "]";

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

    public void goBack(View view){finish();}
}
