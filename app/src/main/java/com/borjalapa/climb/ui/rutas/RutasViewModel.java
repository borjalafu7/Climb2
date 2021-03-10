package com.borjalapa.climb.ui.rutas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RutasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RutasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("RUTAS");
    }

    public LiveData<String> getText() {
        return mText;
    }
}