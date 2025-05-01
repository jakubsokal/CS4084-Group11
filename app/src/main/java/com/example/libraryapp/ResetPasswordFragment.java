package com.example.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.libraryapp.db.DatabaseHelper;

public class ResetPasswordFragment extends Fragment {

    private EditText emailEditText, newPasswordEditText;
    private DatabaseHelper db;

    public ResetPasswordFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        emailEditText = view.findViewById(R.id.reset_email);
        newPasswordEditText = view.findViewById(R.id.new_password);
        Button changePasswordBtn = view.findViewById(R.id.change_password_btn);
        Button exitBtn = view.findViewById(R.id.exit_btn);

        db = new DatabaseHelper(getContext());

        changePasswordBtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updatePasswordByEmail(email, newPassword);
            if (updated) {
                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                // Optionally, remove the fragment after success
                closeFragment();
            } else {
                Toast.makeText(getContext(), "Failed to update password. Email may not exist.", Toast.LENGTH_SHORT).show();
            }
        });

        exitBtn.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void closeFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(ResetPasswordFragment.this).commit();
    }
}
