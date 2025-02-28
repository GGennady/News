package com.example.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Resource
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1 // 1 for test
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    // executes API from the repository
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading()) // before the network call

        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage) // actual response

        // when prev line is finished, the coroutine will just continue with the next line, and here
        // there must be confidence that current network response is saved in "val response"
        // in this case response can be handled (and for that there is separate function - handleBreakingNewsResponse
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())

        val response = newsRepository.searchNews(searchQuery, searchNewsPage)

        searchNews.postValue(handleSearchNewsResponse(response))
    }

    // in this function we decide whether we want to emit the success state in breakingNews LivaData, or the error state
    // if a response is successful - return Success state, if not - return Error state
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            // if the body of response is not equal to null
            response.body()?.let { resultResponse ->
                // when we get that successful response, the first thing we want to do is...
                // ...increase current page number to be able to load the next page after that
                breakingNewsPage++

                // since breakingNewsResponse = null initially, we want to set it when we get the first response
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    // if that's not the first page == if breakingNewsResponse already has been set then...
                    // ...we instead want to take new response (resultResponse), take its "articles", and...
                    // ...add all of them to our articles of "breakingNewsResponse" so to the response that is already saved in viewModel
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                searchNewsPage++
                
                if(searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.saveArticle(article)
    }

    fun getSavedArticles() = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}