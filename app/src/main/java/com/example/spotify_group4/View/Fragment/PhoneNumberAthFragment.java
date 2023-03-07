package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.View.Activity.HomeActivity;
import com.example.spotify_group4.View.Dialog.LoadingDialog;
import com.example.spotify_group4.databinding.FragmentPhonenumberAthBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PhoneNumberAthFragment extends Fragment {
    FragmentPhonenumberAthBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    FirebaseAuth mAuth;
    LoadingDialog loadingDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPhonenumberAthBinding.inflate(getLayoutInflater(), null, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        if (getContext() != null) {
            loadingDialog = new LoadingDialog(getContext());
        }
        initCountyCodePicker();
        setHasOptionsMenu(true);
        layoutBinding.btnGetAthCode.setOnClickListener(v -> {
            String phoneNumber = layoutBinding.countryCodePicker.getFullNumber();
            if (validPhoneNumber(phoneNumber)) {
                loadingDialog.show();
                sendOtp("+" + phoneNumber);
            } else {
                Toast.makeText(getContext(), "Số điện thoại bạn vừa nhập không hợp lệ !", Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    boolean validPhoneNumber(String phoneNumber) {
        String pattern = "^[0-9]{11}$";
        return Pattern.matches(pattern, phoneNumber);
    }

    void initCountyCodePicker() {
        layoutBinding.countryCodePicker.registerCarrierNumberEditText(layoutBinding.edPhoneNumber);
        layoutBinding.countryCodePicker.setOnCountryChangeListener(() -> {
            String countryCode = layoutBinding.countryCodePicker.getSelectedCountryCode();
            Toast.makeText(getContext(), layoutBinding.countryCodePicker.getFullNumber(), Toast.LENGTH_SHORT).show();
            layoutBinding.tilPhoneNumber.setPrefixText("+" + countryCode);
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this.getActivity()).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                Toast.makeText(getContext(), "ádasd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingDialog.hide();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                EnterAthCodeFragment enterAthCodeFragment = new EnterAthCodeFragment();
                Bundle bundle = new Bundle();
                loadingDialog.hide();
                bundle.putString("phoneNumber", phoneNumber);
                enterAthCodeFragment.setArguments(bundle);
                replaceFragmentListener.replaceFragment(enterAthCodeFragment);
            }
        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void goHomeActivity() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this.getActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                Toast.makeText(getContext(), "ádasd", Toast.LENGTH_SHORT).show();
                goHomeActivity();
            }
        });
    }

}
