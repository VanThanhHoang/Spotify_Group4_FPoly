package com.example.spotify_group4.Presenter;

import android.app.Activity;
import android.content.Intent;

import com.example.spotify_group4.View.Activity.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragmentPresenter {
    Activity context;

    public AccountFragmentPresenter(Activity context) {
        this.context = context;
    }

    public boolean signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            startLoginAtv();
            return true;
        } else if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN);
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(context, task -> startLoginAtv());
            return true;
        }
        return false;
    }

    void startLoginAtv() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.finish();
        context.startActivity(intent);
    }
}
