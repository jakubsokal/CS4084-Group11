package com.example.libraryapp;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bookSeatButton = view.findViewById(R.id.bookSeat);
        bookSeatButton.setOnClickListener(v -> {
            // ✅ Use view to find the correct NavController
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.bookingFragment);
        });
    }
}
