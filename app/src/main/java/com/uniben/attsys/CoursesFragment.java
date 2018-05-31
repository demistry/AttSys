package com.uniben.attsys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoursesFragment extends Fragment {


    private RecyclerView recyclerView;


    public CoursesFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        recyclerView = view.findViewById(R.id.courses_recycler_view);
        recyclerView.addItemDecoration(new RecyclerViewItemDivider(this.getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new CoursesViewAdapter());
        return view;
    }

}
