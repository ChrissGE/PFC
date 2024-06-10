package com.example.opinapp;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.opinapp.ui.home.HomeViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Log_in extends Fragment {

    private final int RC_SIGN_IN = 200;
    private ImageView logoImageView;
    private EditText emailEditText, passwordEditText;
    private CheckBox showPasswordCB;
    private LinearLayout buttonsLayout;
    private Button signInBtn, signUpBtn;
    private SignInButton loginGoogleButton;
    private TextView accederConLbl, forgotPasswordLbl;
    private int screenHeight;
    private FragmentManager fragmentManager;
    private int displacement;
    private GoogleSignInAccount googleSignInClient;
    private HomeViewModel homeViewModel;
    private FirebaseAuth auth;
    private String email;
    private String username;
    private  SharedPreferences sharedPreferences;
    private ApiService apiService;
    public Log_in() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
         auth= FirebaseAuth.getInstance();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fragmentManager = getParentFragmentManager();
        logoImageView = view.findViewById(R.id.logoImageView);
        emailEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        buttonsLayout = view.findViewById(R.id.buttonsLayout);
        signUpBtn = buttonsLayout.findViewById(R.id.buttonRegister);
        signInBtn = buttonsLayout.findViewById(R.id.buttonLogin);
        loginGoogleButton = view.findViewById(R.id.buttonGoogleLogin);
        accederConLbl = view.findViewById(R.id.accedaConTextView);
        showPasswordCB = view.findViewById(R.id.showPasswordCheckBox);
        forgotPasswordLbl = view.findViewById(R.id.forgotPasswordTextView);

        // Get screen height
        screenHeight = getResources().getDisplayMetrics().heightPixels;
         displacement = (int) (screenHeight * 0.3f);

        Animation animation = new TranslateAnimation(0, 0, 0, -displacement);
        animation.setDuration(500);
        animation.setFillAfter(true);

        // Delayed animation
        sharedPreferences = getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn) {
                    //cargarFragmento(new main_screen());
                    cargarActividadMainScreen();
                } else {


//                    logoImageView.startAnimation(animation);
//                    usernameEditText.startAnimation(animation);
//                    passwordEditText.startAnimation(animation);
//                    buttonsLayout.startAnimation(animation);
//                    loginGoogleButton.startAnimation(animation);
//                    accederConLbl.startAnimation(animation);
//                    showPasswordCB.startAnimation(animation);
//                    forgotPasswordLbl.startAnimation(animation);
//
                    animarVista(logoImageView);
                    animarVista(emailEditText);
                    animarVista(passwordEditText);
                    animarVista(buttonsLayout);
                    animarVista(loginGoogleButton);
                    animarVista(accederConLbl);
                    animarVista(showPasswordCB);
                    animarVista(forgotPasswordLbl);
                    emailEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    buttonsLayout.setVisibility(View.VISIBLE);
                    loginGoogleButton.setVisibility(View.VISIBLE);
                    accederConLbl.setVisibility(View.VISIBLE);
                    showPasswordCB.setVisibility(View.VISIBLE);
                    forgotPasswordLbl.setVisibility(View.VISIBLE);
                }

            }
        }, 2000);


        // Show/hide password
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

        // Forgot password action
        forgotPasswordLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new Forgot_passwordV2()).addToBackStack(null).commit();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new Sign_up()).addToBackStack(null).commit();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                if(email.isEmpty()||password.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getActivity(), "Invalid fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        if(user!=null){
                                            if(user.isEmailVerified()){
                                                Toast.makeText(getActivity(),"Welcome",Toast.LENGTH_SHORT).show();
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("isLoggedIn", true);
                                                editor.apply();
                                                //fragmentManager.beginTransaction().replace(R.id.fragment_container, new main_screen()).addToBackStack(null).commit();
                                                cargarActividadMainScreen();
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Check your email and verify", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"Failed login",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configurar las opciones de inicio de sesión con Google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        //.requestIdToken(getString(R.string.default_web_client_id))
                        .requestIdToken("670051286413-i1jknnu90toncovc9ja2qn7rnvi0do5u.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                // Construir el cliente de inicio de sesión de Google
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

                // Iniciar la actividad de inicio de sesión de Google
                startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.e("Google","entra al try");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("Google","hace el account");
                if (account != null) {
                    Log.v("nombre", account.getPhotoUrl().toString());
                    sharedPreferences.edit().putString("profile_pic",account.getPhotoUrl().toString()).apply();
                    email = account.getEmail();
                    sharedPreferences.edit().putString("email",email).apply();
                    username=account.getGivenName();
                    sharedPreferences.edit().putString("usermane",username).apply();
                    LlamarGetUser();
                    firebaseAuthWithGoogle(account.getIdToken());
                } else {
                    Toast.makeText(getActivity(), "Error 404", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Toast.makeText(getActivity(), "Login error " , Toast.LENGTH_SHORT).show();
                Log.e("Google", String.valueOf(e));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void LlamarGetUser() throws InterruptedException {
        LlamadaApiGetUser(new ApiResponseCallback()     {
            @Override
            public void onResponse(JsonArray jsonArray) {
            }

            @Override
            public void onResponse(JsonObject jsonResponse) {

                String email = jsonResponse.get("email").getAsString();
                if(email.equals("NOK")){
                    sharedPreferences.edit().putInt("points",0).apply();
                    LlamadaApiSetUser();
                }
                else{
                    sharedPreferences.edit().putInt("points",jsonResponse.get("points").getAsInt()).apply();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el error aquí
                Log.e("LlamadaApi", errorMessage);
            }

            @Override
            public void onResponse(Bitmap imagen) {

            }
        });
    }

    private void LlamadaApiGetUser(ApiResponseCallback callback) throws InterruptedException {
        JsonObject userData = new JsonObject();
        userData.addProperty("email", email);

        Call<JsonObject> call = apiService.getUser(userData);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    callback.onResponse(jsonObject);
                } else {
                    callback.onFailure("Request failed with error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onFailure("Error executing the network request: " + t.getMessage());
            }
        });
    }

    private void LlamadaApiSetUser() {
        JsonObject userData = new JsonObject();
        userData.addProperty("email", email);
        userData.addProperty("username", username);
        userData.addProperty("gender", (String) null);
        userData.addProperty("birth_date", (String) null);

        Call<Void> call = apiService.setUser(userData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // La llamada se completó exitosamente
                    // No se requiere procesamiento adicional de la respuesta
                } else {
                    // La llamada no fue exitosa, manejar el error si es necesario
                    Log.e("LlamadaApi", "Request failed with error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejar el error en caso de que falle la llamada
                Log.e("LlamadaApi", "Error executing the network request: " + t.getMessage());
            }
        });
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Usuario autenticado correctamente
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // El correo electrónico está verificado y asociado con una cuenta, iniciar sesión
                            Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();

                            // Guardar el estado de inicio de sesión
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            // Cargar la pantalla principal
                            //cargarFragmento(new main_screen());
                            cargarActividadMainScreen();
                        } else {
                            // El correo electrónico no está verificado o no está asociado con una cuenta, abrir el fragmento Sign_up para registrar
                            cargarFragmento(new Sign_up());
                        }
                    } else {
                        // Error en la autenticación con Firebase
                        Toast.makeText(getActivity(), "Google login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void cargarFragmento(Fragment fragmento) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragmento);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void animarVista(View vista) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(vista, "translationY", -displacement);
        animator.setDuration(500);
        animator.start();
    }
    private void cargarActividadMainScreen() {
        Intent intent = new Intent(getActivity(), Main_screen.class);
        startActivity(intent);
    }
}
