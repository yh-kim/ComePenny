package com.enterpaper.comepenny.tab.t1idea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.WriteActivity;
import com.enterpaper.comepenny.util.SetFont;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-26.
 */
public class IdeaFragment extends Fragment {
    int row_cnt = 8;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;
    View rootView, popular_view;
    ListView lvMainIdea;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    private Intent intent = new Intent();
    IdeaAdapter adapters;
    IdeaPopularAdapter adapter;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();
    LinearLayout recycler_info;
    List<IdeaPopularListItem> items = new ArrayList<>();
    LinearLayoutManager layoutmanager;

    public static Fragment newInstance() {
        Fragment fragment = new IdeaFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_idea, container, false);

        // frgment 폰트 설정
        SetFont.setGlobalFont(rootView.getContext(), rootView);

        lvMainIdea = (ListView) rootView.findViewById(R.id.lv_main_idea);


        //헤더 생성
        popular_view = inflater.inflate(R.layout.fragment_idea_header, null, false);
        recycler_info = (LinearLayout) popular_view.findViewById(R.id.recycler_info);
        recyclerView = (RecyclerView)popular_view.findViewById(R.id.recyclerview);

        // 헤더레이아웃 객체 생성
        inithearLayout();


        //헤더설정
        lvMainIdea.addHeaderView(popular_view);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(lvMainIdea);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itWrite = new Intent(rootView.getContext(), WriteActivity.class);
                startActivity(itWrite);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        addItemsIdea();

        // Adapter 생성
        adapters = new IdeaAdapter(rootView.getContext(), R.layout.row_idea, dataList);

        // Adapter와 GirdView를 연결
        lvMainIdea.setAdapter(adapters);
        adapters.notifyDataSetChanged();//값이 변경됨을 알려줌

        lvMainIdea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                intent.setClass(rootView.getContext(), IdeaDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }

        });

        lvMainIdea.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                fab.attachToListView(lvMainIdea);



            }
        });

        return rootView;
    }



    // 리스트 아이템 추가
    private void addItemsIdea() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new IdeaListItem("1234", "IdeaTitle", "jihoon1234", "1233", "4321"));

        }
    }

    // layout
    private void inithearLayout() {
        recycler_info = (LinearLayout) popular_view.findViewById(R.id.recycler_info);
        recyclerView = (RecyclerView) popular_view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popular_view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        adapter = new IdeaPopularAdapter(popular_view.getContext(), items, R.layout.row_idea_popular);
        recyclerView.setAdapter(adapter);


        for (int i = 0; i < 10; i++) {
            items.add(new IdeaPopularListItem(R.drawable.ex1, "Lego"));

        }
        adapter.notifyDataSetChanged();
    }



}
