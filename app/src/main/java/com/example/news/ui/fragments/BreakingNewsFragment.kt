package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel

class BreakingNewsFragment: Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentBreakingNews must be not null.")

    lateinit var viewModel: NewsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentBreakingNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
    }
}