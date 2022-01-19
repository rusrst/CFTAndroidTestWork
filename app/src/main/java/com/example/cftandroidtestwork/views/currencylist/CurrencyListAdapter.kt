package com.example.cftandroidtestwork.views.currencylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cftandroidtestwork.data.json.ValuteAndName
import com.example.cftandroidtestwork.databinding.CurrencyListItemBinding

class CurrencyListAdapter: RecyclerView.Adapter<CurrencyListAdapter.CurrencyListHolder>() {
    class CurrencyListHolder(val binding: CurrencyListItemBinding): RecyclerView.ViewHolder(binding.root){
    }

     var items: List<ValuteAndName> = listOf()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CurrencyListItemBinding.inflate(inflater, parent, false)
        return CurrencyListHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyListHolder, position: Int) {
        val currentItem = items[position]

        holder.binding.run {
            var str = "ID = " + currentItem.valute.id
            itemId.text = str
            str = "Числовой код = " + currentItem.valute.numCode
            numCode.text = str
            str = "Буквенный код = " +  currentItem.valute.charCode
            charCode.text = str
            str = "Номинал = " + currentItem.valute.nominal.toString()
            nominal.text = str
            str = "Название валюты " + currentItem.valute.name
            name.text = str
            str = "Стоимость на сейчас = " + currentItem.valute.value.toString()
            value.text = str
            str = "Предыдущая стоимость = " + currentItem.valute.previous.toString()
            previous.text = str
        }
    }

    override fun getItemCount(): Int = items.size
}