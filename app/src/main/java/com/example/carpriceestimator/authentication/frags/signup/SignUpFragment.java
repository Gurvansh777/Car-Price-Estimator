package com.example.carpriceestimator.authentication.frags.signup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carpriceestimator.MainActivity;
import com.example.carpriceestimator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This fragment is used for sign up new user
 */
public class SignUpFragment extends Fragment {
    FirebaseAuth auth;
    EditText signUpEmail, signUpPassword;
    Button buttonSignUp;
    boolean connected = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        buttonSignUp = view.findViewById(R.id.signUpFragmentButtonToSignUp);
        signUpEmail = view.findViewById(R.id.edittextSignUpUsername);
        signUpPassword = view.findViewById(R.id.signUppasswordEditText);


        buttonSignUp.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (checkConnection()) {
                if (signUpPassword.getText() != null) {
                    SignUp();
                } else {
                    Toast.makeText(getContext(), "Please enter password.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please connect to the Internet!", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.buttonbacktologinFromSignUp).setOnClickListener(view1 -> NavHostFragment.findNavController(SignUpFragment.this)
                .navigate(R.id.action_signUpFragment_to_usernameEmailFragment));
        view.findViewById(R.id.textViewBacktoLoginFromSignUp).setOnClickListener(view1 -> NavHostFragment.findNavController(SignUpFragment.this)
                .navigate(R.id.action_signUpFragment_to_usernameEmailFragment));
    }

    /**
     * Utility method to check network connection
     * @return - isConnected
     */
    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    /**
     * Get user details, validate and sign up the user
     */
    public void SignUp() {
        String email, password;
        email = signUpEmail.getText().toString();
        password = signUpPassword.getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            if (email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                if (password.length() >= 9) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), task -> {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getActivity(), "There's an existing account with same Email, go to log in instead.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Password should at least have 9 characters.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter a valid email address.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
        }
    }
}