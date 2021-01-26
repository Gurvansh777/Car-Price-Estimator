package com.example.carpriceestimator.authentication.frags.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carpriceestimator.MainActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.carpriceestimator.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UsernameEmailFragment extends Fragment {
    private FirebaseAuth auth;
    EditText email, password;
    private GoogleSignInClient mGoogleSignInClient;
    Button loginButton, backtoLogin, googleLogin;
    TextView signUpText, forgetPassword;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_username_email, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        email = (EditText) view.findViewById(R.id.edittextUsername);
        password = (EditText) view.findViewById(R.id.passwordEditText);
        forgetPassword = view.findViewById(R.id.textViewForgotPassword);
        backtoLogin = view.findViewById(R.id.buttonbacktoWelcome);
        signUpText = view.findViewById(R.id.textViewSignUpLoginScreen);
        googleLogin = view.findViewById(R.id.buttonGoogleLogin);
        loginButton = view.findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> Login());

        googleLogin.setOnClickListener(v -> LoginWithGoogle());


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        view.findViewById(R.id.textViewSignUpLoginScreen).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_signUpFragment));
        view.findViewById(R.id.buttonbacktoWelcome).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_loginFragment));
        view.findViewById(R.id.textViewForgotPassword).setOnClickListener(view1 -> NavHostFragment.findNavController(UsernameEmailFragment.this)
                .navigate(R.id.action_usernameEmailFragment_to_forgetPasswordFragment));
    }

    public void Login() {
        if (email.getText().length() > 0 && password.getText().length() > 0) {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    Intent main_acitvity = new Intent(getContext(), MainActivity.class);
                    startActivity(main_acitvity);
                } else {
                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please enter email and password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void LoginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
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
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());

                    }
                });
    }


}