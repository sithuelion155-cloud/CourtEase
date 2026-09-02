package com.sithu.courtease.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCourts;
    private TextInputEditText etSearch;
    private Spinner spinnerSport;
    private Spinner spinnerLocation;
    private TextView tvEmptyState;
    private CourtAdapter courtAdapter;
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
                R.layout.fragment_home,
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

        recyclerViewCourts = view.findViewById(R.id.recyclerViewCourts);
        etSearch = view.findViewById(R.id.etSearch);
        spinnerSport = view.findViewById(R.id.spinnerSport);
        spinnerLocation = view.findViewById(R.id.spinnerLocation);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recyclerViewCourts.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        allCourts = CourtData.getCourts();

        filteredCourts = new ArrayList<>(allCourts);

        courtAdapter = new CourtAdapter(
                filteredCourts,
                court -> openCourtDetails(court)
        );

        recyclerViewCourts.setAdapter(courtAdapter);

        setupSpinners();
        setupSearch();

        updateEmptyState();
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
                "Ang Mo Kio",
                "Bishan",
                "Choa Chu Kang",
                "Clementi",
                "Dover",
                "Evans Road",
                "Geylang",
                "Hougang",
                "Jurong East",
                "Jurong West",
                "Kallang",
                "Pasir Ris",
                "Segar",
                "Tampines",
                "Tiong Bahru"
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
                new TextWatcher() {

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
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }

    private void applyFilters() {

        String query = etSearch.getText() != null ? etSearch.getText().toString() : "";
        String searchQuery = query
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

            boolean matchesSearch = searchQuery.isEmpty()
                    || court.getName().toLowerCase(Locale.getDefault()).contains(searchQuery)
                    || court.getSport().toLowerCase(Locale.getDefault()).contains(searchQuery)
                    || court.getLocation().toLowerCase(Locale.getDefault()).contains(searchQuery);

            boolean matchesSport = selectedSport.equals("All Sports")
                    || court.getSport().equalsIgnoreCase(selectedSport);

            boolean matchesLocation = selectedLocation.equals("All Locations")
                    || court.getLocation().equalsIgnoreCase(selectedLocation);

            if (matchesSearch && matchesSport && matchesLocation) {
                filteredCourts.add(court);
            }
        }

        courtAdapter.updateList(filteredCourts);

        updateEmptyState();
    }

    private void updateEmptyState() {

        if (filteredCourts.isEmpty()) {

            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerViewCourts.setVisibility(View.GONE);

        } else {

            tvEmptyState.setVisibility(View.GONE);
            recyclerViewCourts.setVisibility(View.VISIBLE);
        }
    }

    private void openCourtDetails(Court court) {

        CourtDetailsFragment fragment =
                CourtDetailsFragment.newInstance(court);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}