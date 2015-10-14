package com.enterpaper.comepenny.tab.t2booth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-09-26.
 */
public class BoothFragment extends Fragment {
    View rootView;
    GridView main_list;
    // Adapter
    BoothAdapter adapter;
    // ArrayList
    ArrayList<BoothItem> arr_list = new ArrayList<BoothItem>();

    public static Fragment newInstance() {
        Fragment fragment = new BoothFragment();
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

        initMainList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booth,container,false);

        // frgment 폰트 설정
        SetFont.setGlobalFont(rootView.getContext(), rootView);

        main_list = (GridView)rootView.findViewById(R.id.gv_main_booth);

        // Adapter 생성
        adapter = new BoothAdapter(rootView.getContext(), R.layout.row_booth, arr_list);

        // Adapter와 GirdView를 연결
        main_list.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent company = new Intent(rootView.getContext(), BoothDetailActivity.class);
                startActivity(company);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        main_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        return rootView;
    }

    // 리스트 추가
    private void initMainList(){

        for(int i=0; i<16;i++){

            arr_list.add(new BoothItem());
        }

    }
}
