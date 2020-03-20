package com.example.roomregistration_mandatoryassignment.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomregistration_mandatoryassignment.R;
import com.example.roomregistration_mandatoryassignment.RecyclerViewSimpleAdapter;
import com.example.roomregistration_mandatoryassignment.Reservation;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        final RecyclerView recyclerView = root.findViewById(R.id.singleRoomRecyclerView);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        pageViewModel.getList().observe(this, new Observer<List<Reservation>>() {
            @Override
            public void onChanged(List<Reservation> reservations) {
                populateRecyclerView(recyclerView, reservations);
            }
        });

        return root;
    }

    private void populateRecyclerView(RecyclerView recyclerView, List<Reservation> allReservations) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter(getActivity(), allReservations);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}