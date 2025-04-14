package com.example.libraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {
    public ContactFragment() {
        super(R.layout.fragment_contact);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(v -> {

            Fragment homeFragment = new HomeFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, homeFragment)
                    .commit();


            if (requireActivity() instanceof INavbar) {
                ((INavbar) requireActivity()).bottomNavItemSelected(R.id.navbar_home);
            }

        });
    }
}