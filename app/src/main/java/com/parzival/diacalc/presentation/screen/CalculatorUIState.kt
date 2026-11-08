package com.parzival.diacalc.presentation.screen

import com.parzival.diacalc.domain.CalculatorResult

data class CalculatorUIState (
    val carbsInput: String = "",
    val weightInput: String = "",
    val gramsToXE: String = "12",
    val factorInput: String = "",
    val result: CalculatorResult = CalculatorResult()
)