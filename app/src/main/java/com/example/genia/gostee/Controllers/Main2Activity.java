package com.example.genia.gostee.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.genia.gostee.Adapters.CardFragmentPagerAdapter;
import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.Objects.Card;
import com.example.genia.gostee.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rd.PageIndicatorView;
import com.rd.draw.controller.DrawController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity" ;
    private String mUserId = "";
    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private ConnDB connDB;
    private List<Card> cards = null;
    private ConstraintLayout constraintLayout;
    private TextView tvNoneCards;
    private SharedPreferences preferences;
    PageIndicatorView pageIndicatorView;
    ArrayList<Integer> images;
    //ArrayList<GridAdapter> listAdapters;
    //private CardsAdapter cardsAdapter;
    private Button createQR;
    //private ScrollView scrollView;
    //private int itemCount;
    //private int xhdpi;
    private ViewPager viewPager;

    //DisplayMetrics displaymetrics;
    //Activity activity;
    //private float value = 0;

    private ArrayList<ArrayList<Integer>> arrayLists;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        arrayLists = new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);

        //activity = Main2Activity.this;

        //gvCircle = (GridView) findViewById(R.id.gvCircle);

        //displaymetrics = new DisplayMetrics();

        //activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //value = getResources().getDisplayMetrics().density;

        //Log.i(TAG, "onCreate: Screen Density = " + String.valueOf(value));


        //scrollView = findViewById(R.id.scrollView5);



        preferences = getSharedPreferences("info",MODE_PRIVATE);
        String id = preferences.getString("userId", "");
        Log.i("preferences", id + " preferences");

        //listAdapters =  new ArrayList<>();
        pageIndicatorView =  findViewById(R.id.pageIndicatorView);
        mUserId = preferences.getString("userId", "");;
        //tvUserName.setText(preferences.getString("userName", ""));
        Log.i("Main2Activity", "userId = " + mUserId);
        constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutCards);
        tvNoneCards = (TextView) findViewById(R.id.tvNoneCards);
        createQR = (Button)findViewById(R.id.btnCreateQR);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("onPostResume", "onPostResume");
        Log.i("statusADD", preferences.getBoolean("statusADD", false) + "");

        if (cards != null){
            cards.clear();
        }

        Thread thread = new Thread(new MyClass());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ansver != null) {
            //initRecyclerView();
            createImageViewList();

            CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter( cards, arrayLists,
                    getSupportFragmentManager(), dpToPixels(2, this));


            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setPageMargin((int) dpToPixels(30, Main2Activity.this));

            pageIndicatorView =  findViewById(R.id.pageIndicatorView);
            pageIndicatorView.setCount(pagerAdapter.getCount());
            pageIndicatorView.setSelection(0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pageIndicatorView.setSelection(position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            pageIndicatorView.setClickListener(new DrawController.ClickListener() {
                @Override
                public void onIndicatorClicked(int position) {
                    viewPager.setCurrentItem(position);
                }
            });
            constraintLayout.setVisibility(View.VISIBLE);
            createQR.setVisibility(View.VISIBLE);
            tvNoneCards.setVisibility(View.INVISIBLE);
            Log.i("Main2Activity: onCreate", "Answer != null");
        }else {
            Log.i("Main2Activity: onCreate", "Answer = null");
            constraintLayout.setVisibility(View.INVISIBLE);
            createQR.setVisibility(View.INVISIBLE);
            tvNoneCards.setText("У вас еще нет карточек.");
            tvNoneCards.setVisibility(View.VISIBLE);
            Editor editor = preferences.edit();
            editor.putString("idsCards", "");
            editor.apply();
        }

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    /*private void initRecyclerView(){

        /*LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        CustomLinearLayoutManager layoutManager1 = new CustomLinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager1);
        createImageViewList();
        cardsAdapter = new CardsAdapter(this, cards, listAdapters, value);

        Log.i("Main2Activity", "cardsAdapter.getItemCount() = "
                + cardsAdapter.getItemCount());
        recyclerView.setAdapter(cardsAdapter);
        pageIndicatorView.setCount(cardsAdapter.getItemCount()-2);
        pageIndicatorView.setSelection(0);
        itemCount = cardsAdapter.getItemCount();

        Log.i("Main2Activity", "computeHorizontalScrollOffset = " +
                "" + recyclerView.computeHorizontalScrollOffset());

        if (cardsAdapter.getItemCount()-2 == 1) layoutManager1.setScrollEnabled(false);
        else layoutManager1.setScrollEnabled(true);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                scrollView.scrollTo(0, 0);
                int x = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();



                Log.i("Main2Activity", "dx = " + dx);
                //Log.i("Main2Activity", "first = " + first);
                //Log.i("Main2Activity", "last = " + last);
               // Log.i(TAG, "onScrolled: cardsAdapter.getItemCount = " + cardsAdapter.getItemCount());

                if (x >= 0) {
                    //recyclerView.stopScroll();
                    pageIndicatorView.setSelection(x-1);

                }
            }
        });


    }*/

    private void getUserCards() {
        try {
            input = SERVER_NAME
                    + "/gostee.php?action=getUserCards&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connDB = new ConnDB();


        ansver = connDB.sendRequest(input, this, 10000);
        if (ansver.equals("null")) ansver = null;


        if (ansver != null && !ansver.isEmpty()) {
            ansver = "[" + ansver + "]";

            Log.i("getUserCards", "+ Connect ---------- reply contains JSON:" + ansver);

                Log.i("getUserCards", " - answer: " + ansver);
                ObjectMapper objectMapper = new ObjectMapper();
                //JsonNode jsonNode = null;
            try {
                List<Card> bufferCards = objectMapper.readValue(ansver, new TypeReference<List<Card>>(){});
                cards = new ArrayList<>();
                cards.addAll(bufferCards);

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
        startActivity(intent);
    }

    private void createImageViewList()    {
        //listAdapters.clear();
        arrayLists.clear();
        String idsCards = "";

        for (Card card:cards) {
            if (card.getIdMark() == null) continue;
            idsCards += card.getCard_id() + " ";
            images = new ArrayList<>();

            for (int i = 0; i < card.getCount(); i++)  images.add(R.drawable.logo_elips);
            if (card.getCircle_number()- card.getCount()== 1 && card.getType() == 1){
                for (int j = 0; j < card.getCircle_number()-card.getCount() - 1; j++)
                    images.add(R.drawable.gray_elipse);
                images.add(R.drawable.elips_present);
            }else {
                for (int j = 0; j < card.getCircle_number()-card.getCount(); j++)
                    images.add(R.drawable.gray_elipse);
            }

            arrayLists.add(images);
            //listAdapters.add(new GridAdapter(this, images));
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


}
