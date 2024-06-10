package com.example.opinapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opinapp.ui.home.HomeViewModel;
import com.example.opinapp.ui.reviews.ReviewModel;
import com.example.opinapp.ui.reviews.ReviewsViewModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Questionary extends Fragment {

    private LinearLayout questionsLayout;
    private ArrayList<RadioGroup> radioGroups = new ArrayList<>();
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private ApiService apiService;
    private ReviewsViewModel reviewsViewModel;
    private HomeViewModel homeViewModel;
    private String companyName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questionary, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        questionsLayout = view.findViewById(R.id.questionLayout);
        reviewsViewModel = new ViewModelProvider(requireActivity()).get(ReviewsViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        String jsonString = getArguments().getString("jsonArray");
        String company_code=getArguments().getString("company_code");
        String email=sharedPreferences.getString("email","def");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        try {
            String currentQuestionaryName = "";
            TextView currentQuestionaryTextView = null;

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String questionaryName = jsonObject.getString("menu_text");
                String pregunta = jsonObject.getString("question_text");
                String questionType = jsonObject.getString("question_type");
                companyName = jsonObject.getString("company_name");
                if (!currentQuestionaryName.equals(questionaryName)) {
                    currentQuestionaryName = questionaryName;
                    currentQuestionaryTextView = new TextView(getContext());
                    currentQuestionaryTextView.setText(currentQuestionaryName);
                    currentQuestionaryTextView.setTextSize(24);
                    currentQuestionaryTextView.setTextColor(Color.parseColor("#bcc1cb"));
                    currentQuestionaryTextView.setTypeface(null, Typeface.BOLD);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(40, 20, 0, 10);
                    currentQuestionaryTextView.setLayoutParams(layoutParams);
                    questionsLayout.addView(currentQuestionaryTextView);

                    View separator = new View(getContext());
                    separator.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                    ));
                    separator.setBackgroundColor(Color.parseColor("#bcc1cb"));
                    questionsLayout.addView(separator);
                }

                TextView textViewPregunta = new TextView(getContext());
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                textParams.setMargins(80, 10, 0, 10);
                textViewPregunta.setText(pregunta);
                textViewPregunta.setTextColor(Color.parseColor("#bcc1cb"));
                textViewPregunta.setTextSize(18);
                textViewPregunta.setTypeface(null, Typeface.NORMAL);

                if (questionType.equals("Text")) {
                    EditText editTextRespuesta = new EditText(getContext());
                    editTextRespuesta.setHint("Conteste...");
                    editTextRespuesta.setTextColor(Color.parseColor("#bcc1cb")); // Color del texto amarillo
                    editTextRespuesta.setHintTextColor(Color.parseColor("#bcc1cb")); // Color del texto de sugerencia amarillo
                    editTextRespuesta.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#bcc1cb"))); // Color del borde amarillo

                    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    editTextParams.setMargins(60, 0, 20, 0);
                    editTextRespuesta.setId(jsonObject.getInt("id_questions"));
                    questionsLayout.addView(textViewPregunta, editTextParams);
                    questionsLayout.addView(editTextRespuesta);
                    editTexts.add(editTextRespuesta);
                } else {
                    RadioGroup radioGroup = new RadioGroup(getContext());
                    radioGroup.setOrientation(LinearLayout.HORIZONTAL);

                    RadioButton radioButtonSi = new RadioButton(getContext());
                    radioButtonSi.setText("Sí");
                    radioButtonSi.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#bcc1cb")));

                    RadioButton radioButtonNo = new RadioButton(getContext());
                    radioButtonNo.setText("No");
                    radioButtonNo.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#bcc1cb")));

                    LinearLayout.LayoutParams radioButtonParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    radioButtonParams.setMargins(60, 0, 20, 0);
                    radioGroup.setId(jsonObject.getInt("id_questions"));
                    radioGroup.addView(radioButtonSi, radioButtonParams);
                    radioGroup.addView(radioButtonNo);

                    questionsLayout.addView(textViewPregunta, textParams);
                    questionsLayout.addView(radioGroup);
                    radioGroups.add(radioGroup);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //TODO:Texto libre
        //POR QUE ME DICE ESOOOO NENAAAA
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areAllRadioButtonsChecked()&&areAllEditTextsIsNotEmpty()){
                    JsonObject jsonObject = new JsonObject();
                    try {
                        jsonObject.addProperty("company_code", company_code);
                        jsonObject.addProperty("email", sharedPreferences.getString("email","def"));
                        JsonArray questionsArray = new JsonArray();
                        for (RadioGroup radioGroup : radioGroups) {
                            RadioButton radioButton = getView().findViewById(radioGroup.getCheckedRadioButtonId());
                            int respuesta = (radioButton.getText().toString().equals("Sí")) ? 1 : 0;
                            JsonObject questionObject = new JsonObject();
                            questionObject.addProperty("id_question", radioGroup.getId());
                            questionObject.addProperty("respuesta", respuesta);
                            questionsArray.add(questionObject);
                        }
                        for (EditText editText : editTexts) {
                            String respuesta = editText.getText().toString();
                            JsonObject questionObject = new JsonObject();
                            questionObject.addProperty("id_question", editText.getId());
                            questionObject.addProperty("respuesta", respuesta);
                            questionsArray.add(questionObject);
                        }
                        jsonObject.add("questions",questionsArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Call<JsonObject> call = apiService.setReview(jsonObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                JsonObject json = response.body();
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                String fechaActual = dayOfMonth + "/" + month + "/" + year;
                                ReviewModel review=new ReviewModel(fechaActual,companyName,json.get("points_reward").getAsString());
                                review.SetImage(homeViewModel.getRegistro(review.getBusinessName()));
                                int points= sharedPreferences.getInt("points",0);
                                sharedPreferences.edit().putInt("points",points+json.get("points_reward").getAsInt()).apply();
                                reviewsViewModel.anhadirReview(review);
                                getParentFragmentManager().beginTransaction().remove(Questionary.this).commit();
                            }
                            else {
                                Log.e("LlamadaApi", "Request failed with error: " + response.errorBody());
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("LlamadaApi", "Error executing the network request: " + t.getMessage());
                        }
                    });
                    Toast.makeText(getActivity(),"Sumbit",Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("CHECKS", "NO CHECKS CHECKED");
                    Toast.makeText(getActivity(),"Empty questions",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private boolean areAllEditTextsIsNotEmpty() {
        boolean allNotEmpty=true;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                allNotEmpty=false;
            }
        }
        return allNotEmpty;
    }

    private boolean areAllRadioButtonsChecked() {
        boolean allChecked=true;
        for (RadioGroup radioGroup : radioGroups) {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                allChecked=false;
            }
        }
        return allChecked;
    }
}