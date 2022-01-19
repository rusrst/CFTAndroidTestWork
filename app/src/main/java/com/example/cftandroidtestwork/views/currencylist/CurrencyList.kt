package com.example.cftandroidtestwork.views.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cftandroidtestwork.GlobalState
import com.example.cftandroidtestwork.WorkerThread
import com.example.cftandroidtestwork.data.contract.HasCustomTitle
import com.example.cftandroidtestwork.data.contract.Navigator
import com.example.cftandroidtestwork.databinding.CurrencyListBinding


private const val MESSAGE_DOWNLOAD = 0
private const val MESSAGE_DOWNLOAD_FROM_INTERNET = 1
class CurrencyList: Fragment(), HasCustomTitle {
    private lateinit var adapter: CurrencyListAdapter
    private lateinit var binding: CurrencyListBinding
    private val viewModel: SharedViewModel by activityViewModels()
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
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js", MESSAGE_DOWNLOAD)
        }
        adapter = CurrencyListAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCurrencyList.layoutManager = layoutManager
        binding.recyclerViewCurrencyList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner){
            binding.swipeToRefresh.isRefreshing = false
            if (it?.Date != null) {
                (requireActivity() as Navigator).updateUi(0)
                binding.progressBarCurrencyList.visibility = View.GONE
                binding.recyclerViewCurrencyList.visibility = View.VISIBLE
                binding.errorCurrencyList.visibility = View.GONE
                adapter.items = it.valutes ?: emptyList()
            }
            else if(it != null){
                (requireActivity() as Navigator).updateUi(0)
                binding.progressBarCurrencyList.visibility = View.GONE
                binding.recyclerViewCurrencyList.visibility = View.GONE
                binding.errorCurrencyList.visibility = View.VISIBLE
            }
            else{
                (requireActivity() as Navigator).updateUi(0)
                binding.progressBarCurrencyList.visibility = View.VISIBLE
                binding.recyclerViewCurrencyList.visibility = View.GONE
                binding.errorCurrencyList.visibility = View.GONE
            }
        }
        binding.tryAgainCurrencyList.setOnClickListener{
            viewModel.data.value = null
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js", MESSAGE_DOWNLOAD)
        }
        binding.reloadButton.setOnClickListener{
            viewModel.data.value = null
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js", MESSAGE_DOWNLOAD_FROM_INTERNET)
        }
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js", MESSAGE_DOWNLOAD_FROM_INTERNET)
        }
        GlobalState.get().data.observe(viewLifecycleOwner){
            if (it){
                GlobalState.get().data.value = false
                viewModel.data.value = null
                viewModel.workerThread?.returnData("https://www.cbr-xml-daily.ru/daily_json.js", MESSAGE_DOWNLOAD)
            }
        }
    }

    override fun onResume() {
        (requireActivity() as Navigator).updateUi(0)
        super.onResume()
    }

    override fun getTitle(): String {
        if (viewModel.data.value?.Date != null)  return viewModel.data.value?.Date!!
        return "Данные пока не загружены"
    }
}