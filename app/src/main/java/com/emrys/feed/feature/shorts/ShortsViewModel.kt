package com.emrys.feed.feature.shorts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrys.feed.data.FeedDummyData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShortsViewModel: ViewModel() {

    val shorts = MutableLiveData<List<ShortUiModel>>()

    init {
        viewModelScope.launch {
            shorts.value = FeedDummyData.feedList
        }
    }

    fun refresh() {
        viewModelScope.launch {
            delay(500)
            shorts.value = FeedDummyData.feedList.shuffled()
        }
    }


}