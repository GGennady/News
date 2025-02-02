package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.databinding.FragmentBreakingNewsBinding

class BreakingNewsFragment: Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentBreakingNews must be not null.")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentBreakingNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}