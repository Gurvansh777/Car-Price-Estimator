package com.example.carpriceestimator.authentication.frags.forgetpassword;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.carpriceestimator.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordFragment extends Fragment {
    private static final String TAG = "Email :";
    private FirebaseAuth auth;
    Button resetButton;
    EditText resetPasswordEmailTextView;
    boolean connected = false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forget_password_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        resetButton = view.findViewById(R.id.buttonResetPassword);
        resetPasswordEmailTextView = view.findViewById(R.id.edittextResetEmail);

        resetPasswordEmailTextView.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(checkConnection()){
                ResetPassword();
            }
            else {
                Toast.makeText(getContext(), "Please connect to the Internet!", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.textViewbacktoSignUpFromReset).setOnClickListener(view1 -> NavHostFragment.findNavController(ForgetPasswordFragment.this)
                .navigate(R.id.action_forgetPasswordFragment_to_signUpFragment));
        view.findViewById(R.id.buttonbacktologinFromReset).setOnClickListener(view1 -> NavHostFragment.findNavController(ForgetPasswordFragment.this)
                .navigate(R.id.action_forgetPasswordFragment_to_usernameEmailFragment));
    }

    public void ResetPassword() {
        String emailAddress = resetPasswordEmailTextView.getText().toString();
        if(emailAddress.length()>0){
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getActivity(), "Email sent!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Please enter valid email!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please enter valid email!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

}