package com.savan.mytask

import android.graphics.Movie
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var paginationAdapter: PaginationAdapter? = null
    private var movieService: ApiService? = null
    private var progressBar: ProgressBar? = null
    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private val TOTAL_PAGES = 3
    private var currentPage = PAGE_START
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        progressBar = findViewById<ProgressBar>(R.id.progressbar)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        paginationAdapter = PaginationAdapter(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(paginationAdapter)
        movieService = RetrofitClient.retrofit.create(ApiService::class.java)

        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        loadFirstPage();

    }


    private fun loadNextPage() {
        movieService?.getPosts()?.enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                isLoading = false
                val results = response.body()
                if (results != null) {
                    paginationAdapter!!.addAll(results)
                }
                if (currentPage !== TOTAL_PAGES) paginationAdapter!!.addLoadingFooter() else
                    isLastPage = true
                    if(isLastPage) paginationAdapter?.removeLoadingFooter()
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun loadFirstPage() {
        movieService?.getPosts()?.enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                val results = response.body()
                progressBar!!.visibility = View.GONE
                if (results != null) {
                    paginationAdapter!!.addAll(results)
                }
                if (currentPage <= TOTAL_PAGES) paginationAdapter!!.addLoadingFooter() else isLastPage =
                    true
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {}
        })
    }
}