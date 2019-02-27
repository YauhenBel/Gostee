package com.example.genia.gostee.Controllers.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.genia.gostee.Controllers.Adapters.CardsAdapter;
import com.example.genia.gostee.Controllers.Adapters.GridAdapter;
import com.example.genia.gostee.Controllers.Adapters.RecyclerAddAdapter;
import com.example.genia.gostee.Controllers.ConnToDB.ConnDB;
import com.example.genia.gostee.Controllers.Objects.Card;
import com.example.genia.gostee.Controllers.Views.ExpandableHeightGridView;
import com.example.genia.gostee.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rd.PageIndicatorView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private Integer idCard = null;
    private String mUserId = "";
    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private ConnDB connDB;
    private List<Card> cards = null;
    private TextView tvDescription;
    private TextView tvName;
    private TextView tvTime;
    private ConstraintLayout constraintLayout;
    private TextView tvNoneCards;
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    PageIndicatorView pageIndicatorView;
    //GridView gvCircle;
    ArrayList<Integer> images;
    GridAdapter gridAdapter;
    ExpandableHeightGridView mGridView;
    ArrayList<GridAdapter> listAdapters;
    private CardsAdapter cardsAdapter;
    private Button createQR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //gvCircle = (GridView) findViewById(R.id.gvCircle);

        preferences = getSharedPreferences("info",MODE_PRIVATE);
        String id = preferences.getString("userId", "");
        Log.i("preferences", id + " preferences");

        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        mGridView = (ExpandableHeightGridView) findViewById(R.id.spotsView);
        mGridView.setExpanded(true);
        listAdapters =  new ArrayList<>();
        pageIndicatorView =  findViewById(R.id.pageIndicatorView);
        tvName = findViewById(R.id.tvName);
        tvTime = findViewById(R.id.tvTime);
        tvDescription = findViewById(R.id.tvDescription);
        mUserId = preferences.getString("userId", "");;
        tvUserName.setText(preferences.getString("userName", ""));
        Log.i("Main2Activity", "userId = " + mUserId);
        constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutCards);
        tvNoneCards = (TextView) findViewById(R.id.tvNoneCards);
        createQR = (Button)findViewById(R.id.btnCreateQR);


        /*new Thread(new Runnable() {
            @Override public void run() {


            }
        }).start();*/

        Thread thread = new Thread(new MyClass());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ansver != null) {
            initRecyclerView();
            createImageViewList();
            mGridView.setAdapter(listAdapters.get(0));
        }else {
            Log.i("Main2Activity: onCreate", "Answer = null: ");
            constraintLayout.setVisibility(View.INVISIBLE);
            createQR.setVisibility(View.INVISIBLE);
            tvNoneCards.setText("У вас еще нет карточек.");
            tvNoneCards.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("onPostResume", "onPostResume");
        Log.i("statusADD", preferences.getBoolean("statusADD", false) + "");

        if (preferences.getBoolean("statusADD", false) && cards == null){
            Thread thread = new Thread(new MyClass());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (ansver != null) {
                initRecyclerView();
                createImageViewList();
                mGridView.setAdapter(listAdapters.get(0));

                constraintLayout.setVisibility(View.VISIBLE);
                createQR.setVisibility(View.VISIBLE);

                tvNoneCards.setVisibility(View.INVISIBLE);
            }else {
                Log.i("Main2Activity: onCreate", "Answer = null: ");

            }
            return;
        }

        if (preferences.getBoolean("statusADD", false)){
            Thread thread = new Thread(new GetNewCard());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cardsAdapter = new CardsAdapter(this, cards);
            recyclerView.setAdapter(cardsAdapter);
            pageIndicatorView.setCount(cardsAdapter.getItemCount());
            createImageViewList();
            mGridView.setAdapter(listAdapters.get(0));
        }

    }

    private void initRecyclerView(){

        LinearLayoutManager layoutManager =  new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        cardsAdapter = new CardsAdapter(this, cards);
        recyclerView.setAdapter(cardsAdapter);
        pageIndicatorView.setCount(cardsAdapter.getItemCount());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int x = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                //Log.d(TAG, "findFirstCompletelyVisibleItemPosition" + x);


                if (x>=0) {

                    pageIndicatorView.setSelection(x);
                    tvName.setText(cardsAdapter.getItem(x).getName());
                    tvTime.setText(cardsAdapter.getItem(x).getWorking_hours() + " / " +
                    cardsAdapter.getItem(x).getWorking_days());
                    tvDescription.setText(cardsAdapter.getItem(x).getDescription());
                    mGridView.setAdapter(listAdapters.get(x));
                    idCard = cardsAdapter.getItem(x).getCard_id();

                }

            }
        });


    }

    private void getUserCards() {
        try {
            input = SERVER_NAME
                    + "/gostee.php?action=getUserCards&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connDB = new ConnDB();


        ansver = connDB.sendRequest(input);
        if (ansver.equals("null")) ansver = null;


        if (ansver != null && !ansver.isEmpty()) {
            ansver = "[" + ansver + "]";

            Log.i("getUserCards", "+ Connect ---------- reply contains JSON:" + ansver);

                Log.i("getUserCards", " - answer: " + ansver);
                ObjectMapper objectMapper = new ObjectMapper();
                //JsonNode jsonNode = null;
            try {
                cards = objectMapper.readValue(ansver, new TypeReference<List<Card>>(){});

                for (Card card:cards) {
                    Log.i("getUserCards", "Id Mark: " + card.getIdMark());
                    Log.i("getUserCards", "Id карты: " + card.getCard_id());
                    Log.i("getUserCards", "Название заведения: " + card.getName());
                    Log.i("getUserCards", "Время работы: " + card.getWorking_hours());
                    Log.i("getUserCards", "Дни работы: " + card.getWorking_days());
                    Log.i("getUserCards", "Описание: " + card.getDescription());
                    Log.i("getUserCards", "Ссылка на картинку: " + card.getIndividual_icon());
                    Log.i("getUserCards", "Тип карты: " + card.getType());
                    Log.i("getUserCards", "Количество отметок: " + card.getCount());
                    Log.i("getUserCards", "Количество кружочков: " + card.getCircle_number());
                    Log.i("getUserCards", "-------------------------------------------------");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.i("getUserCards", "answer = null");
        }
    }

    public void goBack(View view) {
        finish();
    }

    public void addNewCard(View view){
        Intent intent = new Intent(this, AddCard.class);
        startActivity(intent);
    }

    public void createQRCode(View view) {
        Intent intent = new Intent(this, CreateQRCode.class);
        intent.putExtra("idUser", mUserId);
        intent.putExtra("idCard", idCard);
        startActivity(intent);
    }

    private void getAddedCard(){
        String lastIdMark = cards.get(cards.size()-1).getIdMark();
        try {
            input = SERVER_NAME
                    + "/gostee.php?action=getAddedCard&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8") +
                    "&lastIdMark="
                    + URLEncoder.encode(lastIdMark, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connDB = new ConnDB();

        ansver = connDB.sendRequest(input);
        if (ansver.equals("null")) ansver = null;


        if (ansver != null && !ansver.isEmpty()) {
            ansver = "[" + ansver + "]";

            Log.i("getAddedCard", "+ Connect ---------- reply contains JSON:" + ansver);

            Log.i("getAddedCard", " - answer: " + ansver);
            ObjectMapper objectMapper = new ObjectMapper();
            //JsonNode jsonNode = null;
            try {
                List<Card> cardsBuffer = objectMapper.readValue(ansver, new TypeReference<List<Card>>(){});

                cards.addAll(cardsBuffer);

                for (Card card:cards) {
                    Log.i("getAddedCard", "Id карты: " + card.getCard_id());
                    Log.i("getAddedCard", "Название заведения: " + card.getName());
                    Log.i("getAddedCard", "Время работы: " + card.getWorking_hours());
                    Log.i("getAddedCard", "Дни работы: " + card.getWorking_days());
                    Log.i("getAddedCard", "Описание: " + card.getDescription());
                    Log.i("getAddedCard", "Ссылка на картинку: " + card.getIndividual_icon());
                    Log.i("getAddedCard", "Тип карты: " + card.getType());
                    Log.i("getAddedCard", "Количество отметок: " + card.getCount());
                    Log.i("getAddedCard", "Количество кружочков: " + card.getCircle_number());
                    Log.i("getAddedCard", "-------------------------------------------------");
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.i("getUserCards", "answer = null");
        }
    }

    private void createImageViewList()    {
        listAdapters.clear();
        String idsCards = "";

        for (Card card:cards) {
            idsCards += card.getCard_id() + " ";
            images = new ArrayList<>();

            //for (int i = 0; i < card.getCircle_number(); i++) images.add(R.drawable.gray_elipse);

            for (int i = 0; i < card.getCount(); i++)  images.add(R.drawable.logo_elips);
            if (card.getCircle_number()- card.getCount()== 1 && card.getType() == 1){
                for (int j = 0; j < card.getCircle_number()-card.getCount() - 1; j++)
                    images.add(R.drawable.gray_elipse);
                images.add(R.drawable.elips_present);
            }else {
                for (int j = 0; j < card.getCircle_number()-card.getCount(); j++)
                    images.add(R.drawable.gray_elipse);
            }

            listAdapters.add(new GridAdapter(this, images));
        }

        Editor editor = preferences.edit();
        editor.putString("idsCards", idsCards);
        editor.apply();

    }

    private class MyClass implements Runnable{

        @Override
        public void run() {
            getUserCards();
        }
    }

    private class GetNewCard implements Runnable{

        @Override
        public void run() {
            getAddedCard();
        }
    }


}
