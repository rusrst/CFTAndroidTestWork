package com.example.cftandroidtestwork.views

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
            itemId.text = "ID = " + currentItem.valute.id
            numCode.text = "Числовой код = " + currentItem.valute.numCode
            charCode.text ="Буквенный код = " +  currentItem.valute.charCode
            nominal.text = "Номинал = " + currentItem.valute.nominal.toString()
            name.text = "Название валюты " + currentItem.valute.name
            value.text = "Стоимость на сейчас = " + currentItem.valute.value.toString()
            previous.text = "Предыдущая стоимость = " + currentItem.valute.previous.toString()
        }
    }

    override fun getItemCount(): Int = items.size
}