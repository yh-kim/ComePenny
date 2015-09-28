package com.enterpaper.comepenny.tab.t1idea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.enterpaper.comepenny.R;
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

    MenuAdapter adapters;
    ArrayList<MenuListItem> dataList;

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
        rootView = inflater.inflate(R.layout.fragment_idea,container,false);

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
                getActivity().overridePendingTransition(0,0);
            }
        });


        addItemsMenu();

        // Adapter 생성
        adapters = new MenuAdapter(rootView.getContext(), R.layout.row_menu, dataList);

        // Adapter와 GirdView를 연결
        lvMainIdea.setAdapter(adapters);

        adapters.notifyDataSetChanged();

        return rootView;
    }

    // 리스트 아이템 추가
    private void addItemsMenu(){
        dataList = new ArrayList<>();
        for(int i=0; i<20;i++){
            dataList.add(new MenuListItem("내 정보"));
        }
    }
}
