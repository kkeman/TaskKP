package com.service.codingtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.service.codingtest.model.response.ItemsEntity

class SharedViewModel : ViewModel() {

    private var selected = MutableLiveData<Int>()

    fun select(item: ItemsEntity, position: Int) {
        selected.value = position
    }

    fun getSelected(): LiveData<Int> {
        return selected
    }
}

