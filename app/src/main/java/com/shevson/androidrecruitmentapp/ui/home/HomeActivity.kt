package com.shevson.androidrecruitmentapp.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shevson.androidrecruitmentapp.R
import com.shevson.androidrecruitmentapp.data.remote.ApiService
import com.shevson.androidrecruitmentapp.util.ErrorHandler
import com.shevson.androidrecruitmentapp.util.LoadingState
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var service: ApiService

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            HomeViewModel::class.java
        )
    }

    private val errorHandler by lazy { ErrorHandler(this, snackBarContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()

        viewModel.data.observe(this, Observer {
            if (it.isNullOrEmpty()) return@Observer
            list.adapter = HomeAdapter(it)
            (list.adapter as HomeAdapter).notifyDataSetChanged()
        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                LoadingState.IN_PROGRESS -> {
                    loadingView.visibility = View.VISIBLE
                    noDataTxt.visibility = View.GONE
                }
                LoadingState.NO_RESULTS -> {
                    loadingView.visibility = View.GONE
                    noDataTxt.visibility = View.VISIBLE
                }
                else -> {
                    loadingView.visibility = View.GONE
                    noDataTxt.visibility = View.GONE
                }
            }
        })

        viewModel.errorData.observe(this, Observer {
            errorHandler.displayError(it)
        })
    }

    private fun initRecyclerView() {
        list.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu!!.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        (list.adapter as HomeAdapter).filter.filter(newText)
        return false
    }
}