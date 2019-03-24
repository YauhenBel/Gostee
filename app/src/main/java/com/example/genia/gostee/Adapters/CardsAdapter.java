package com.example.genia.gostee.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.Objects.Card;
import com.example.genia.gostee.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private List<Card> cards;
    private Context context;
    private int width;

    public CardsAdapter(Context context, List<Card> cards, int width){
        this.cards = cards;
        this.context = context;
        this.width = width;

    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "position = " + position);
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (cards.get(position).getIdMark() == null && position == 0) return 1;
        if (cards.get(position).getIdMark() == null && position != 0) return 2;
        else return 0;

    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view = null;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_listitem, parent, false);
                //view.setLayoutParams(new RelativeLayout.LayoutParams(getWidth(), getHeight()));
                //view.setPadding(20,0,  20, 0);
                Log.i(TAG, "onCreateViewHolder: view.getWidth() = " + view.getLayoutParams().width);
                Log.i(TAG, "onCreateViewHolder: view.getHeight() = " + view.getLayoutParams().height);
                view.setLayoutParams(new RelativeLayout.LayoutParams(
                        (int) (view.getLayoutParams().width * getCipher()),
                        (int) (view.getLayoutParams().height * getCipher())));
                view.setPadding((int) (20 * getCipher()), 0, 0, 0);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fake_item, parent, false);
                view.setLayoutParams(new RelativeLayout.LayoutParams(
                        (int) (view.getLayoutParams().width * getCipherOne()),
                        (int) (view.getLayoutParams().height * getCipher())));
                view.setPadding(0, 0, 0, 0);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fake_item, parent, false);
                view.setLayoutParams(new RelativeLayout.LayoutParams(
                        (int) (view.getLayoutParams().width * getCipherTwo()),
                        (int) (view.getLayoutParams().height * getCipher())));
                view.setPadding((int) (20 * getCipher()), 0, 0, 0);
                break;
        }
        return new ViewHolder(view);
    }

    private double getCipher(){
        if (width >= 720  && width < 1080) return 1;
        if (width >= 1080) return 1.2;
        return 0;

    }

    private double getCipherOne(){
        if (width >= 720  && width < 1080) return 1.1;
        if (width >= 1080) return 1.15;

        return 0;

    }

    private double getCipherTwo(){
        if (width >= 720  && width < 1080) return 1.15;
        if (width >= 1080) return 1.25;

        return 0;

    }




    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");


        if (cards.get(position).getIdMark() != null){
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
