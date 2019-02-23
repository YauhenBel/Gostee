package com.example.genia.gostee.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.Controllers.Objects.Card;
import com.example.genia.gostee.R;

import java.util.List;

public class RecyclerAddAdapter extends RecyclerView.Adapter<RecyclerAddAdapter.ViewHolder>{
    private List<Card> cardList;
    private Context context;

    public RecyclerAddAdapter(List<Card> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull RecyclerAddAdapter.ViewHolder holder, final int position) {

        Glide.with(context)
                .load(getUrlWithHeaders(cardList.get(position).getIndividual_icon()))
                .into(holder.imageView);

        holder.textView.setText(cardList.get(position).getName());

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("RecyclerAddAdapter","onClick" + cardList.get(position).getName());
            }
        });

    }

    private GlideUrl getUrlWithHeaders (String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .build());
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
}
