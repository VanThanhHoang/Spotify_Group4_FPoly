package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Helper.KeyBroadHelper;
import com.example.spotify_group4.databinding.FragmentEnterAthCodeBinding;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EnterAthCodeFragment extends Fragment {
    FragmentEnterAthCodeBinding layoutBinding;
    EditText[] edAthCodes;
    int indexAthCodeFocus = 0;
    private final int UP_ANM_FLAG = 0;
    private final int DOW_ANM_FLAG = 1;
    StringBuilder athCode = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentEnterAthCodeBinding.inflate(getLayoutInflater(), null, false);
        countDownToEnableButtonGetAthCodeAgain();
        layoutBinding.btnGetAthCode.setOnClickListener(v -> countDownToEnableButtonGetAthCodeAgain());
        if (this.getActivity() != null) {
            KeyboardVisibilityEvent.setEventListener(
                    this.getActivity(),
                    isOpen -> {
                        if (isOpen) {

                            startTranslateAnimation(createAnimation(UP_ANM_FLAG));
                            layoutBinding.parentLayout.setGravity(Gravity.TOP);
                        } else {
                            startTranslateAnimation(createAnimation(DOW_ANM_FLAG));
                            layoutBinding.parentLayout.setGravity(Gravity.CENTER);
                        }
                    });
        }
        return layoutBinding.getRoot();
    }

    void startTranslateAnimation(Animation animation) {
        layoutBinding.tvTittle.startAnimation(animation);
        layoutBinding.athCodeContainer.startAnimation(animation);
        layoutBinding.btnGetAthCode.startAnimation(animation);
        layoutBinding.tvGetCodeAgain.startAnimation(animation);
    }

    TranslateAnimation createAnimation(int flag) {
        TranslateAnimation animation;
        if (flag == UP_ANM_FLAG) {
            animation = new TranslateAnimation(0, 0, 200, 0);
        } else {
            animation = new TranslateAnimation(0, 0, -150, 0);
        }
        animation.setDuration(500);
        animation.setFillAfter(false);
        return animation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edAthCodes = new EditText[]{
                layoutBinding.athCode0,
                layoutBinding.athCode2,
                layoutBinding.athCode3,
                layoutBinding.athCode4,
                layoutBinding.athCode5,
                layoutBinding.athCode6,
        };
        if (getContext() != null) {
            edAthCodes[indexAthCodeFocus].requestFocus();
            KeyBroadHelper.showKeyBroad(this.getContext(), edAthCodes[indexAthCodeFocus]);
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
                if (indexAthCodeFocus == edAthCodes.length - 1 && getContext() != null) {
                    KeyBroadHelper.hideKeyBroad(getContext(), edAthCodes[indexAthCodeFocus]);
                    for (EditText edT : edAthCodes) {
                        athCode.append(edT.getText().toString());
                    }
                } else if (count == 1) {
                    edAthCodes[++indexAthCodeFocus].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        for (int i = 0 ;i<edAthCodes.length;i++) {
            int finalI = i;
            edAthCodes[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        indexAthCodeFocus = finalI;
                    }
                }
            });
            edAthCodes[i].addTextChangedListener(textWatcher);
            edAthCodes[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (indexAthCodeFocus == 0) {
                        return false;
                    }
                    edAthCodes[--indexAthCodeFocus].requestFocus();
                    return true;
                }
                return false;
            });

        }
    }

    void countDownToEnableButtonGetAthCodeAgain() {
        int secondsInMinis = 60;
        layoutBinding.btnGetAthCode.setEnabled(false);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (int i = secondsInMinis; i > 0; i--) {
                layoutBinding.btnGetAthCode.setText(i + "");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    layoutBinding.btnGetAthCode.setText("Gửi lại mã xác minh");
                    layoutBinding.btnGetAthCode.setEnabled(true);
                });
            }
        });
    }
}
