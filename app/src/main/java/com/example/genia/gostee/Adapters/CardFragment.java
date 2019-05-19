package com.example.genia.gostee.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.genia.gostee.Controllers.Main2Activity;
import com.example.genia.gostee.Objects.Card;
import com.example.genia.gostee.R;
import com.example.genia.gostee.Views.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.Objects;


public class CardFragment extends Fragment {

    private ConstraintLayout constraintLayout;

    public static Fragment getInstance(Card card, ArrayList<Integer> images) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putString("name", card.getName());
        args.putString("description", card.getDescription());
        args.putString("worker_time", card.getWorking_hours() + " / " +
                card.getWorking_days());
        if (!card.getIndividual_icon().isEmpty()){
            args.putString("icon", card.getIndividual_icon());
        }else {
            args.putString("icon", "http://r2551241.beget.tech/icons/standartIcon.png");
        }
        args.putIntegerArrayList("images", images);
        f.setArguments(args);
        return f;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.layout_listitem, container, false);
        CardView cardView = view.findViewById(R.id.cardView1);
        cardView.getLayoutParams().height= (int) dpToPixels(220, getActivity());

        constraintLayout = (ConstraintLayout) view.findViewById(R.id.relLayout);
        ImageView image = view.findViewById(R.id.image_View);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvTime = view.findViewById(R.id.tvTime);
        ExpandableHeightGridView mGridView = view.findViewById(R.id.spotsView);
        mGridView.setExpanded(true);
        mGridView.setEnabled(false);


        Glide.with(Objects.requireNonNull(getActivity()))
                .load(getUrlWithHeaders(getArguments().getString("icon")))
                .into(image);




        tvName.setText(getArguments().getString("name"));
        tvTime.setText(getArguments().getString("worker_time"));
        tvDescription.setText(getArguments().getString("description"));
        mGridView.setAdapter(new GridAdapter((Main2Activity) getActivity(), getArguments().getIntegerArrayList("images")));


        return view;
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private GlideUrl getUrlWithHeaders (String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .build());
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }
}
