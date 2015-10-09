package com.enterpaper.comepenny.tab.t1idea;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.enterpaper.comepenny.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimmiri on 2015. 10. 8..
 */
public class IdeaPopularView extends FragmentActivity {

    List<IdeaPopularListItem> items;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_idea_header);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        //
        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(new IdeaPopularListItem(R.drawable.ex1, "Lego"));


            recyclerView.setAdapter(new IdeaPopularAdapter(getApplicationContext(), items, R.layout.fragment_idea_header));
        }
    }
}