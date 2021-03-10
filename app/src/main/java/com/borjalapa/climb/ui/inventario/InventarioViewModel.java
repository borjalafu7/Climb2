package com.borjalapa.climb.ui.inventario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InventarioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InventarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("INVENTARIO");
    }

    public LiveData<String> getText() {
        return mText;
    }
}