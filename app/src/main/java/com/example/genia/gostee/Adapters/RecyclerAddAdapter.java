package com.example.genia.gostee.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.Objects.Card;
import com.example.genia.gostee.R;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class RecyclerAddAdapter extends RecyclerView.Adapter<RecyclerAddAdapter.ViewHolder>
        implements Filterable
{
    private List<Card> cardList;
    private List<Card> cardListFiltered;
    private Context context;
    private ConnDB connDB;
    private String ansver = "", input = "";
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private String mUserId = null;
    private Integer mIdCard = null;
    private Boolean status = false, status1 = false;

    private ArrayList<Integer> ids = new ArrayList<Integer>(){};
    private ImageButton imageButton;
    private String TAG = "RecyclerAddAdapter";

    public RecyclerAddAdapter(List<Card> cardList, Context context, String userId, String idsCards) {
        this.cardList = cardList;
        this.cardListFiltered = cardList;
        this.context = context;
        this.mUserId = userId;

        if (!idsCards.equals("")){
            String[] ids = idsCards.split(" ");
            for (String str: ids) {
                this.ids.add(Integer.parseInt(str));
            }
        }else this.ids = null;


    }

    @Override
    public int getItemCount() {
        return cardListFiltered.size();
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

        String icon = cardListFiltered.get(position).getIndividual_icon();
        if (!icon.isEmpty()){
            Glide.with(context)
                    .load(getUrlWithHeaders(icon))
                    .into(holder.imageView);
        }else {
            Glide.with(context)
                    .load(getUrlWithHeaders("http://r2551241.beget.tech/icons/standartIcon.png"))
                    .into(holder.imageView);
        }

        /*Glide.with(context)
                .load(getUrlWithHeaders(cardListFiltered.get(position).getIndividual_icon()))
                .into(holder.imageView);*/

        holder.textView.setText(cardListFiltered.get(position).getName());

        if (ids != null){
            for (Integer id: ids) {
                Log.i("RecyclerAddAdapter","str " + id);
                if (cardListFiltered.get(position).getCard_id() == id){
                    status1 = true;
                    break;
                }
            }

            if (!status1){
                holder.imageButton.setImageResource(R.drawable.plus_add);
                holder.imageButton.setEnabled(true);
            }else {
                holder.imageButton.setImageResource(R.drawable.greentick);
                holder.imageButton.setEnabled(false);
                status1 = false;
            }
        }else {
            holder.imageButton.setImageResource(R.drawable.plus_add);
            holder.imageButton.setEnabled(true);
        }







        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("RecyclerAddAdapter","onClick" + cardListFiltered.get(position).getName());
                imageButton  = (ImageButton) view;
                mIdCard = cardListFiltered.get(position).getCard_id();
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
        Log.i(TAG, "getUrlWithHeaders: url: " + url);
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .build());
    }

    public Boolean getStatus() {
        return status;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cardListFiltered = cardList;
                } else {
                    List<Card> filteredList = new ArrayList<>();
                    for (Card row : cardList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cardListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cardListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cardListFiltered = (ArrayList<Card>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
                    + URLEncoder.encode(String.valueOf(mIdCard), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connDB = new ConnDB();


        ansver = connDB.sendRequest(input, context);
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
