package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.databinding.FragmentPhonenumberAthBinding;

public class PhoneNumberAthFragment extends Fragment {
    FragmentPhonenumberAthBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;

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
        initCountyCodePicker();
        setHasOptionsMenu(true);
        layoutBinding.btnGetAthCode.setOnClickListener(v->
                replaceFragmentListener.replaceFragment(new EnterAthCodeFragment()));
        super.onViewCreated(view, savedInstanceState);
    }

    void initCountyCodePicker() {
        layoutBinding.countryCodePicker.registerCarrierNumberEditText(layoutBinding.edPhoneNumber);
        layoutBinding.countryCodePicker.setOnCountryChangeListener(() -> {
            String countryCode = layoutBinding.countryCodePicker.getSelectedCountryCode();
            layoutBinding.tilPhoneNumber.setPrefixText("+" + countryCode);
        });
    }

}
