package com.example.libraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private INavbar selector;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        selector = (INavbar) getContext();
        Button bookButton = view.findViewById(R.id.bookSeat);
        bookButton.setOnClickListener(click -> selector.bottomNavItemSelected(R.id.navbar_book));
    }
}
