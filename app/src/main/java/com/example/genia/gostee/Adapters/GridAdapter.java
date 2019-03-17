package com.example.genia.gostee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.genia.gostee.Controllers.Main2Activity;
import com.example.genia.gostee.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> images;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Main2Activity context, ArrayList<Integer> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imVAddCard);
            imageView.setImageResource(images.get(i));
        }

        return view;
    }
}