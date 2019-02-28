package com.example.genia.gostee.Controllers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.Controllers.Objects.Card;
import com.example.genia.gostee.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private List<Card> cards;
    private Context context;

    public CardsAdapter(Context context, List<Card> cards){
        this.cards = cards;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "position = " + position);
        return super.getItemId(position);
    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");


        Glide.with(context)
                .load(getUrlWithHeaders(cards.get(position).getIndividual_icon()))
                .into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image" + cards.get(position).getName());
                Toast.makeText(context, cards.get(position).getName(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private GlideUrl getUrlWithHeaders (String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .build());
    }

    public Card getItem(int position){
        Log.d(TAG, "position= " + position);
        return cards.get(position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_View);
        }
    }
}
