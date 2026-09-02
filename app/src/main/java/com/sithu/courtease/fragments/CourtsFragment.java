package com.sithu.courtease.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sithu.courtease.R;
import com.sithu.courtease.adapters.CourtAdapter;
import com.sithu.courtease.utils.CourtData;

public class CourtsFragment extends Fragment {

    private RecyclerView rvCourts;
    private CourtAdapter adapter;

    public CourtsFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_courts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCourts = view.findViewById(R.id.rvCourts);
        rvCourts.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CourtAdapter(CourtData.getCourts(), court -> {
            // Navigate to details
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, CourtDetailsFragment.newInstance(court))
                    .addToBackStack(null)
                    .commit();
        });

        rvCourts.setAdapter(adapter);
    }
}