package com.example.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Resource
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1 // 1 for test

    init {
        getBreakingNews("us")
    }

    // executes API from the repository
    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading()) // before the network call

        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage) // actual response

        // when 21 line is finished, the coroutine will just continue with the next line, and here
        // there must be confidence that current network response is saved in "val response"
        // in this case response can be handled (and for that there is separate function - handleBreakingNewsResponse
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    // in this function we decide whether we want to omit the success state in breakingNews LivaData, or the error state
    // if a response is successful - return Success state, if not - return Error state
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            // if the body of response is not equal to null
            response.body()?.let { resultResponse -> return Resource.Success(resultResponse) }
        }
        return Resource.Error(response.message())
    }


}