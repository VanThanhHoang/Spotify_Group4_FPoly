package com.example.spotify_group4.View.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.View.Activity.HomeActivity;
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
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentLoginBinding.inflate(getLayoutInflater(),null,false);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(),gso);
        layoutBinding.btnLoginGoogle.setOnClickListener(v -> SignInGoogle());
        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setEvent();
        super.onViewCreated(view, savedInstanceState);
    }
    void setEvent(){
        layoutBinding.btnLoginAccount.setOnClickListener(v->{
            startActivity(new Intent(getContext(), HomeActivity.class));
        });
        layoutBinding.btnPhoneAth.setOnClickListener(v->
                replaceFragmentListener.replaceFragment(new PhoneNumberAthFragment()));
    }
    private void SignInGoogle(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(getContext(),HomeActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}