package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Helper.KeyBroadHelper;
import com.example.spotify_group4.databinding.FragmentEnterAthCodeBinding;

public class EnterAthCodeFragment extends Fragment {
    FragmentEnterAthCodeBinding layoutBinding;
    EditText[] athCodes;
    int indexAthCodeFocus = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentEnterAthCodeBinding.inflate(getLayoutInflater(), null, false);

        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        athCodes = new EditText[]{
                layoutBinding.athCode0,
                layoutBinding.athCode2,
                layoutBinding.athCode3,
                layoutBinding.athCode4,
                layoutBinding.athCode5,
                layoutBinding.athCode6,
        };
        if (getContext() != null) {
            athCodes[indexAthCodeFocus].requestFocus();
            KeyBroadHelper.showKeyBroad(this.getContext(), athCodes[indexAthCodeFocus]);
        }
        initTextWatcher();
        super.onViewCreated(view, savedInstanceState);
    }

    void initTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    if(indexAthCodeFocus == 0){
                        return;
                    }
                    athCodes[--indexAthCodeFocus].requestFocus();
                } else if (indexAthCodeFocus == athCodes.length - 1 && getContext() != null) {
                    KeyBroadHelper.hideKeyBroad(getContext(), athCodes[indexAthCodeFocus]);

                } else if (count == 1) {
                    athCodes[++indexAthCodeFocus].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        for (EditText editText : athCodes) {
            editText.addTextChangedListener(textWatcher);
        }
    }
}
