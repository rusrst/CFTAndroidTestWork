package com.example.cftandroidtestwork.data.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentCurrency(val Date: String? = null,
                           val PreviousDate: String? = null,
                           val PreviousURL: String? = null,
                           val Timestamp: String? = null,
                           @SerialName("Valute") val valutes: Map<String, Valute>? = null)

@Serializable
data class Valute(@SerialName("ID") val id: String? = null,
                  @SerialName("NumCode") val numCode: String? = null,
                  @SerialName("CharCode") val charCode: String? = null,
                  @SerialName("Nominal") val nominal: Int? = null,
                  @SerialName("Name") val name: String? = null,
                  @SerialName("Value") val value: Double? = null,
                  @SerialName("Previous") val previous: Double? = null)

data class CurrentCurrencyWithListValuteAndName(
    var Date: String? = null,
    var PreviousDate: String? = null,
    var PreviousURL: String? = null,
    var Timestamp: String? = null,
    var valutes: List<ValuteAndName>? = null)

data class ValuteAndName(var name: String, var valute: Valute)
