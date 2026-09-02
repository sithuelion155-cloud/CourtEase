package com.sithu.courtease.fragments;

import android.widget.AdapterView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.sithu.courtease.R;
import com.sithu.courtease.adapters.CourtAdapter;
import com.sithu.courtease.models.Court;
import com.sithu.courtease.utils.CourtData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private TextInputEditText etSearch;

    private Spinner spinnerSport;
    private Spinner spinnerLocation;

    private RecyclerView recyclerViewCourts;

    private TextView tvEmptyState;

    private CourtAdapter adapter;

    private List<Court> allCourts;

    private List<Court> filteredCourts;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_search,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        etSearch =
                view.findViewById(R.id.etSearch);

        spinnerSport =
                view.findViewById(R.id.spinnerSport);

        spinnerLocation =
                view.findViewById(R.id.spinnerLocation);

        recyclerViewCourts =
                view.findViewById(R.id.recyclerViewCourts);

        tvEmptyState =
                view.findViewById(R.id.tvEmptyState);

        allCourts =
                CourtData.getCourts();

        filteredCourts =
                new ArrayList<>(allCourts);

        recyclerViewCourts.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        adapter =
                new CourtAdapter(
                        filteredCourts,
                        court -> openCourtDetails(court)
                );

        recyclerViewCourts.setAdapter(adapter);

        setupSpinners();

        setupSearch();
    }

    private void setupSpinners() {

        String[] sports = {
                "All Sports",
                "Badminton",
                "Basketball",
                "Pickleball",
                "Multi-Sport"
        };

        String[] locations = {
                "All Locations",
                "Geylang",
                "Bishan",
                "Tiong Bahru",
                "Evans Road",
                "Dover",
                "Kallang",
                "Segar",
                "Jurong East"
        };

        ArrayAdapter<String> sportAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        sports
                );

        sportAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSport.setAdapter(sportAdapter);

        spinnerSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> locationAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        locations
                );

        locationAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerLocation.setAdapter(locationAdapter);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setupSearch() {

        etSearch.addTextChangedListener(
                new android.text.TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        applyFilters();
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s
                    ) {
                    }
                }
        );
    }

    private void applyFilters() {

        String search =
                etSearch.getText()
                        .toString()
                        .trim()
                        .toLowerCase(Locale.getDefault());

        String selectedSport =
                spinnerSport.getSelectedItem()
                        .toString();

        String selectedLocation =
                spinnerLocation.getSelectedItem()
                        .toString();

        filteredCourts.clear();

        for (Court court : allCourts) {

            boolean matchesSearch =
                    search.isEmpty()
                            || court.getName()
                            .toLowerCase(Locale.getDefault())
                            .contains(search)
                            || court.getSport()
                            .toLowerCase(Locale.getDefault())
                            .contains(search)
                            || court.getLocation()
                            .toLowerCase(Locale.getDefault())
                            .contains(search);

            boolean matchesSport =
                    selectedSport.equals("All Sports")
                            || court.getSport()
                            .equalsIgnoreCase(selectedSport);

            boolean matchesLocation =
                    selectedLocation.equals("All Locations")
                            || court.getLocation()
                            .equalsIgnoreCase(selectedLocation);

            if (matchesSearch
                    && matchesSport
                    && matchesLocation) {

                filteredCourts.add(court);
            }
        }

        adapter.updateList(filteredCourts);

        updateEmptyState();
    }

    private void updateEmptyState() {

        if (filteredCourts.isEmpty()) {

            recyclerViewCourts.setVisibility(
                    View.GONE
            );

            tvEmptyState.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerViewCourts.setVisibility(
                    View.VISIBLE
            );

            tvEmptyState.setVisibility(
                    View.GONE
            );
        }
    }

    private void openCourtDetails(Court court) {

        CourtDetailsFragment fragment =
                CourtDetailsFragment.newInstance(court);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .addToBackStack(null)
                .commit();
    }

    public abstract static class SimpleItemSelectedListener
            implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onNothingSelected(
                android.widget.AdapterView<?> parent
        ) {
        }
    }
}