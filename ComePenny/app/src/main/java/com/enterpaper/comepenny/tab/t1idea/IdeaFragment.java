package com.enterpaper.comepenny.tab.t1idea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activity.IdeaDetailActivity;
import com.enterpaper.comepenny.activity.WriteActivity;
import com.enterpaper.comepenny.util.SetFont;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-09-26.
 */
public class IdeaFragment extends Fragment {
    View rootView;
    ListView lvMainIdea;
    FloatingActionButton fab;
    private Intent intent = new Intent();
    IdeaAdapter adapters;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();

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

        addItemsidea();

        // Adapter 생성
        adapters = new IdeaAdapter(rootView.getContext(), R.layout.row_idea, dataList);

        // Adapter와 GirdView를 연결
        lvMainIdea.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        lvMainIdea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                intent.setClass(rootView.getContext(), IdeaDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }

        });

        lvMainIdea.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //testestttest

                ///testesttest

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                fab.attachToListView(lvMainIdea);
            }
        });

        return rootView;
    }

    // 리스트 아이템 추가
    private void addItemsidea() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new IdeaListItem(1234, "IdeaTitle", "jihoon1234", "1233", "4321"));

        }
    }
}
