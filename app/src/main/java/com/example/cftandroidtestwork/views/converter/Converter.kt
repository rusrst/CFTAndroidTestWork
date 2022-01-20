package com.example.cftandroidtestwork.views.converter

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cftandroidtestwork.data.contract.HasCustomTitle
import com.example.cftandroidtestwork.data.contract.Navigator
import com.example.cftandroidtestwork.databinding.ConverterBinding
import com.example.cftandroidtestwork.views.currencylist.SharedViewModel


val charArray = listOf('0', '1', '2', '3', '4', '5', '6','7', '8', '9', ',', '.' )
class Converter: Fragment(), HasCustomTitle{

    var adapter: ArrayAdapter<String>? = null
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: ConverterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinnerVal.onItemSelectedListener = onItemSpinnerItemSelectedListener
        binding.numRub.addTextChangedListener(twRub)
        adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, viewModel.listNamed)
        binding.spinnerVal.adapter = adapter
        binding.spinnerVal.setSelection(viewModel.position)
        viewModel.data.observe(viewLifecycleOwner){ currentcurrencywithlistvalueandname ->
            if (currentcurrencywithlistvalueandname == null || currentcurrencywithlistvalueandname.valutes == null){
                (requireActivity() as Navigator).updateUi(0)
                viewModel.listNamed.clear()
                adapter?.notifyDataSetChanged()
                binding.spinnerVal.adapter = adapter
                binding.errorConverter.visibility = View.VISIBLE
                binding.numRub.visibility = View.GONE
                binding.textViewRub.visibility = View.GONE
                binding.spinnerVal.visibility = View.GONE
                binding.result.visibility = View.GONE
                binding.textViewReload.visibility = View.GONE
                binding.multiplier.visibility = View.GONE
            }
            else{
                (requireActivity() as Navigator).updateUi(0)
                binding.errorConverter.visibility = View.GONE
                binding.numRub.visibility = View.VISIBLE
                binding.textViewRub.visibility = View.VISIBLE
                binding.spinnerVal.visibility = View.VISIBLE
                binding.result.visibility = View.VISIBLE
                viewModel.listNamed.clear()
                currentcurrencywithlistvalueandname.valutes!!.forEach {
                    viewModel.listNamed.add(it.name)
                }
                adapter?.notifyDataSetChanged()
                binding.textViewReload.visibility = View.VISIBLE
                val str = "Данные от: " +  viewModel.data.value?.Date
                binding.textViewReload.text = str
                binding.multiplier.visibility = View.VISIBLE
            }
        }

    }
    val twRub = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0.toString().isEmpty()){
                binding.numRub.text = SpannableStringBuilder("0")
                binding.numRub.setSelection(1)
                return
            }
            else{
                var count = 0
                p0?.forEach {
                    if(it !in charArray){
                        binding.numRub.text = SpannableStringBuilder("0")
                        binding.numRub.setSelection(1)
                        return
                    }
                    else if (it == '.' || it == ','){
                        count++
                    }
                }
                if (count > 1){
                        binding.numRub.text = SpannableStringBuilder("0")
                        binding.numRub.setSelection(1)
                        return
                    }
                val multiplier: Double
                if (viewModel.currentVal == null || viewModel.currentVal?.value == null || viewModel.currentVal?.nominal == null) multiplier = 1.0
                else multiplier = viewModel.currentVal?.value!! /viewModel.currentVal?.nominal!!
                binding.multiplier.text = multiplier.toString()
                val data = p0.toString().replace(',', '.').toDouble() * multiplier
                val dataStart = (data.toString().substringBefore('.')).plus('.')
                val dataEnd = try{
                        var temp = (data.toString().substringAfter('.'))
                    if (temp.length >= 4) {
                        temp = temp.substring(0, 4)
                        temp                    }
                    else {
                        val tempSize = temp.length
                        temp = temp.substring(0, tempSize)
                        temp
                    }
                }
                catch (e: Exception) {
                    "0000"
                }
                binding.result.text = dataStart+dataEnd
            }
        }
    }

    val onItemSpinnerItemSelectedListener:AdapterView.OnItemSelectedListener? = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            viewModel.position = p2
            viewModel.currentVal = viewModel.data.value!!.valutes!![p2].valute
            binding.numRub.text = binding.numRub.text
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
    override fun onResume() {
        (requireActivity() as Navigator).updateUi(1)
        super.onResume()
    }

    override fun getTitle(): String {
        if (viewModel.data.value?.Date != null)  return viewModel.data.value?.Date!!
        return "Данные пока не загружены"
    }
}