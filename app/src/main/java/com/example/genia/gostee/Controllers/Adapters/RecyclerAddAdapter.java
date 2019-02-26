package com.example.genia.gostee.Controllers.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.Controllers.ConnToDB.ConnDB;
import com.example.genia.gostee.Controllers.Objects.Card;
import com.example.genia.gostee.R;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class RecyclerAddAdapter extends RecyclerView.Adapter<RecyclerAddAdapter.ViewHolder>{
    private List<Card> cardList;
    private Context context;
    private ConnDB connDB;
    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private String mUserId = null, mIdCard = null;
    private Boolean status = false;
    private String[] ids;
    private ImageButton imageButton;

    public RecyclerAddAdapter(List<Card> cardList, Context context, String userId, String idsCards) {
        this.cardList = cardList;
        this.context = context;
        this.mUserId = userId;
        this.ids = idsCards.split("|");
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_of_add_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAddAdapter.ViewHolder holder, final int position) {

        Glide.with(context)
                .load(getUrlWithHeaders(cardList.get(position).getIndividual_icon()))
                .into(holder.imageView);

        holder.textView.setText(cardList.get(position).getName());

        for (String str:ids) {
            Log.i("RecyclerAddAdapter","str " + str);
            if (cardList.get(position).getCard_id().equals(str)){
                holder.imageButton.setImageResource(R.drawable.greentick);
                holder.imageButton.setEnabled(false);
            }
        }



        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("RecyclerAddAdapter","onClick" + cardList.get(position).getName());
                imageButton  = (ImageButton) view;
                mIdCard = cardList.get(position).getCard_id();
                Thread thread = new Thread(new MyClass());
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("RecyclerAddAdapter","refreshDrawableState");
                imageButton.setImageResource(R.drawable.greentick);
                //imageButton.refreshDrawableState();
                imageButton.setEnabled(false);
            }
        });
    }

    private GlideUrl getUrlWithHeaders (String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .build());
    }

    public Boolean getStatus() {
        return status;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewAddCard);
            imageView = itemView.findViewById(R.id.imageViewAddCard);
            imageButton = itemView.findViewById(R.id.imageButtonAddCard);
        }
    }

    private void addCard() {

        try {
            input = SERVER_NAME
                    + "/gostee.php?action=addCard&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8")
                    +"&idCard="
                    + URLEncoder.encode(mIdCard, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connDB = new ConnDB();


        ansver = connDB.sendRequest(input);
        if (ansver.equals("300")) status = true;
        else ansver = null;


    }

    private class MyClass implements Runnable{

        @Override
        public void run() {
            addCard();
        }
    }
}
