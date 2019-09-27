package com.club.business;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class ChatFragment extends Fragment {

    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_chat,container,false);
        searchView=(SearchView)v.findViewById(R.id.search_view);

        searchView.setEnabled(false);
        searchView.setFocusableInTouchMode(false);
        searchView.setInputType(InputType.TYPE_NULL);
        //searchView.setVisibility(View.GONE);
        return v;
    }
}
