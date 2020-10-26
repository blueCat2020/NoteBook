package com.swufe.zbp.notebook.ui.theme_style;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ThemeStyleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ThemeStyleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is themeStyle fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}