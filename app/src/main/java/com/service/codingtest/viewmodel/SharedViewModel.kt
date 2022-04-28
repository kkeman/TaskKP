package com.service.codingtest.viewmodel

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.service.codingtest.model.response.FavoriteEntity
import com.service.codingtest.model.response.ItemsEntity

class SharedViewModel : ViewModel() {

//    var selected: LiveData<ItemsEntity> = liveData { }

//    private var selected = MutableLiveData<ItemsEntity>()
    private var selected = MutableLiveData<Int>()

    public fun select(item: ItemsEntity, position: Int) {
        selected.setValue(position)
    }

    public fun getSelected(): LiveData<Int> {
        return selected
    }
}

