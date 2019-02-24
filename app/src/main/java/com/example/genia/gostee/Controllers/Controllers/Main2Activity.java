package com.example.genia.gostee.Controllers.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.genia.gostee.Controllers.Adapters.CardsAdapter;
import com.example.genia.gostee.Controllers.Adapters.GridAdapter;
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

    private String idCard = "";
    private String mUserId = "";
    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private ConnDB connDB;
    private List<Card> cards;
    private TextView tvDescription;
    private TextView tvName;
    private TextView tvTime;
    private ConstraintLayout constraintLayout;
    private TextView tvNoneCards;
    private SharedPreferences preferences;
    PageIndicatorView pageIndicatorView;
    //GridView gvCircle;
    ArrayList<Integer> images;
    GridAdapter gridAdapter;
    ExpandableHeightGridView mGridView;
    ArrayList<GridAdapter> listAdapters;




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
        mUserId =preferences.getString("userId", "");;
        tvUserName.setText(preferences.getString("userName", ""));
        Log.i("Main2Activity", "userId = " + mUserId);


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
            constraintLayout = (ConstraintLayout)
                    findViewById(R.id.constraintLayoutCards);
            constraintLayout.setVisibility(View.INVISIBLE);
            tvNoneCards = (TextView) findViewById(R.id.tvNoneCards);
            tvNoneCards.setText("У вас еще нет карточек.");
            tvNoneCards.setVisibility(View.VISIBLE);





        }

        //gvCircle.setAdapter(gridAdapter);




    }

    private void initRecyclerView(){

        LinearLayoutManager layoutManager =  new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        final CardsAdapter cardsAdapter = new CardsAdapter(this, cards);
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
        finish();
    }

    public void createQRCode(View view) {
        goToCreateQRCode();
    }

    private class MyClass implements Runnable{

        @Override
        public void run() {
            getUserCards();
        }
    }

    private void createImageViewList()    {
        listAdapters.clear();

        for (Card card:cards) {
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

    }

    private void goToCreateQRCode(){
        Intent intent = new Intent(this, CreateQRCode.class);
        intent.putExtra("idUser", mUserId);
        intent.putExtra("idCard", idCard);
        startActivity(intent);
    }
}
