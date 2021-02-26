package com.example.carpriceestimator.authentication.frags.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.MainActivity;
import com.example.carpriceestimator.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UsernameEmailFragment extends Fragment {
    //Firebase
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    //Views
    EditText email, password;
    ProgressBar progressBar;
    Button loginButton, backtoLogin, googleLogin;
    TextView signUpText, forgetPassword;

    //Sp
    SharedPreferences sharedpreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_username_email, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //firebase instance
        auth = FirebaseAuth.getInstance();

        //sp
        sharedpreferences = this.getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);

        //getting views
        email = (EditText) view.findViewById(R.id.edittextUsername);
        password = (EditText) view.findViewById(R.id.passwordEditText);
        forgetPassword = view.findViewById(R.id.textViewForgotPassword);
        backtoLogin = view.findViewById(R.id.buttonbacktoWelcome);
        signUpText = view.findViewById(R.id.textViewSignUpLoginScreen);
        googleLogin = view.findViewById(R.id.buttonGoogleLogin);
        loginButton = view.findViewById(R.id.buttonLogin);
        progressBar = view.findViewById(R.id.progressBar);

        //button events
        loginButton.setOnClickListener(v -> Login());
        googleLogin.setOnClickListener(v -> LoginWithGoogle());

        //google integration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        //navigation
        view.findViewById(R.id.textViewSignUpLoginScreen).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_signUpFragment));
        view.findViewById(R.id.buttonbacktoWelcome).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_loginFragment));
        view.findViewById(R.id.textViewForgotPassword).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_forgetPasswordFragment));
    }

    public void Login() {
        if (email.getText().length() > 0 && password.getText().length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                   // Toast.makeText(getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor spEditor = sharedpreferences.edit();
                    spEditor.putString(Constants.USER_EMAIL, auth.getCurrentUser().getEmail());
                    spEditor.apply();

                    Intent mainActivity = new Intent(getContext(), MainActivity.class);
                    startActivity(mainActivity);
                } else {
                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            });
        } else {
            Toast.makeText(getContext(), "Please enter email and password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void LoginWithGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        SharedPreferences.Editor spEditor = sharedpreferences.edit();
                        spEditor.putString(Constants.USER_EMAIL, user.getEmail());
                        spEditor.apply();
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());

                    }
                });
    }


}