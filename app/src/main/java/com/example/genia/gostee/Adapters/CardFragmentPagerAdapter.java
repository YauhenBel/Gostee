package com.example.genia.gostee.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.genia.gostee.Objects.Card;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private float baseElevation;
    private List<Card> cards;
    private ArrayList<ArrayList<Integer>> listAdapters;


    public CardFragmentPagerAdapter(List<Card> cards,
                                    ArrayList<ArrayList<Integer>> listAdapters,
                                    FragmentManager fm, float baseElevation) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        this.cards = cards;
        this.listAdapters = listAdapters;

        for(int i = 0; i< cards.size(); i++){
            addCardFragment(new CardFragment());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public ConstraintLayout getConstraintLayoutwAt(int position) {
        return fragments.get(position).getConstraintLayout();
    }



    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.getInstance(cards.get(position), listAdapters.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}
