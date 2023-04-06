package com.example.spotify_group4.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Presenter.AuthPresenter;
import com.example.spotify_group4.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginFragment extends Fragment {
    FragmentLoginBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> myActivityResultLauncher;
    AuthPresenter authPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentLoginBinding.inflate(getLayoutInflater(), null, false);
        return layoutBinding.getRoot();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        authPresenter = new AuthPresenter(context);
        replaceFragmentListener = (ReplaceFragmentListener) context;
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            task.getResult(ApiException.class);
                            GoogleSignInAccount account = task.getResult();
                            String userId = account.getId();
                            authPresenter.insertUser(userId);
                            authPresenter.goHomeActivity();
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                });

        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setEvent();
        super.onViewCreated(view, savedInstanceState);
    }

    void setEvent() {
        layoutBinding.btnPhoneAth.setOnClickListener(v ->
                replaceFragmentListener.replaceFragment(new PhoneNumberAthFragment()));
        layoutBinding.btnLoginAccount1.setOnClickListener(v ->
                replaceFragmentListener.replaceFragment(new LoginEmailFragment())
        );
        layoutBinding.btnLoginGoogle.setOnClickListener(v -> SignInGoogle());
        layoutBinding.btnRegisterAccount.setOnClickListener(v -> replaceFragmentListener.replaceFragment(new FragmentRegisterAccount()));
    }

    private void SignInGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        myActivityResultLauncher.launch(intent);
    }
}