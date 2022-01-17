package com.example.cftandroidtestwork.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cftandroidtestwork.WorkerThread
import com.example.cftandroidtestwork.databinding.CurrencyListBinding

class CurrencyList: Fragment() {
    private lateinit var adapter: CurrencyListAdapter
    private lateinit var binding: CurrencyListBinding
    private val viewModel: CurrencyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewModel.workerThread == null) viewModel.workerThread = WorkerThread(viewModel.data)
        binding = CurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.data.value == null){
            binding.progressBarCurrencyList.visibility = View.VISIBLE
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js")
        }
        adapter = CurrencyListAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCurrencyList.layoutManager = layoutManager
        binding.recyclerViewCurrencyList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner){
            if (it?.Date != null) {
                binding.progressBarCurrencyList.visibility = View.GONE
                binding.recyclerViewCurrencyList.visibility = View.VISIBLE
                binding.errorCurrencyList.visibility = View.GONE
                adapter.items = it.valutes ?: emptyList()
            }
            else if(it != null){
                binding.progressBarCurrencyList.visibility = View.GONE
                binding.recyclerViewCurrencyList.visibility = View.GONE
                binding.errorCurrencyList.visibility = View.VISIBLE
            }
            else{
                binding.progressBarCurrencyList.visibility = View.VISIBLE
                binding.recyclerViewCurrencyList.visibility = View.GONE
                binding.errorCurrencyList.visibility = View.GONE
            }
        }
        binding.tryAgainCurrencyList.setOnClickListener{
            viewModel.data.value = null
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js")
        }
    }
}