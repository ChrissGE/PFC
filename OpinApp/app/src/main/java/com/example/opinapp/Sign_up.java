package com.example.opinapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Sign_up extends Fragment {

    private Spinner sexSpinner;
    private EditText datePicker, emailEditText, passwordEditText,usernameEditText;
    private Button signUpBtn;
    private CheckBox showPasswordCB;
    private FragmentManager fragmentManager;
    private String selectedDate,gender;
    private ApiService apiService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        usernameEditText=view.findViewById(R.id.usernameEditText);
        sexSpinner = view.findViewById(R.id.sexSpinner);
        datePicker = view.findViewById(R.id.dataPicker);
        signUpBtn = view.findViewById(R.id.buttonRegister);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        showPasswordCB = view.findViewById(R.id.showPasswordCheckBox);
        fragmentManager = getParentFragmentManager();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getActivity(), "invalid email", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.isEmpty() || password.length() < 8) {
                        Toast.makeText(getActivity(), "invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = auth.getCurrentUser();
                                            if (user != null) {
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            try {
                                                                HacerLlamadaApi();
                                                            } catch (InterruptedException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                            fragmentManager.popBackStack();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Send email error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Sign in error", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                }
            }
        });

        showPasswordCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSex = parent.getItemAtPosition(position).toString();
                gender = selectedSex.equals("Hombre") ? "Male" : (selectedSex.equals("Mujer") ? "Female" : "Other");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPicker();
            }
        });
        return view;
    }

    private void HacerLlamadaApi() throws InterruptedException {
        JsonObject userData = new JsonObject();
        userData.addProperty("email", emailEditText.getText().toString());
        Log.d("email", emailEditText.getText().toString());
        userData.addProperty("username", usernameEditText.getText().toString());
        Log.d("username", usernameEditText.getText().toString());
        userData.addProperty("gender", gender);
        Log.d("gender", gender);
        userData.addProperty("birth_date", selectedDate);
        Log.d("birth_date", selectedDate);
        Call<Void> call = apiService.setUser(userData);
        Thread hilo = new Thread(new LlamadaApi<>(call));
        hilo.start();
        hilo.join();
    }

    private void showDataPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                        datePicker.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
}