package com.example.opinapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_passwordV2 extends Fragment {

    private Button sendEmailBtn;
    private EditText emailEditText;

    public Forgot_passwordV2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_passwordv2, container, false);

        sendEmailBtn = view.findViewById(R.id.buttonEnviarCorreo);
        emailEditText = view.findViewById(R.id.emailEditText);

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return view;
    }

    private void validate() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo inválido");
        } else {
            sendEmail(email);
        }
    }

    private void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email sent!", Toast.LENGTH_SHORT).show();
                            requireActivity().onBackPressed();
                        } else {
                            Toast.makeText(getActivity(), "Invalid mail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
