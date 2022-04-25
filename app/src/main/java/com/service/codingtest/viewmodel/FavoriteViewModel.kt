package com.service.codingtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.service.codingtest.model.response.FavoriteEntity

class FavoriteViewModel : ViewModel() {

    var list : LiveData<List<FavoriteEntity>> = liveData {  }
}