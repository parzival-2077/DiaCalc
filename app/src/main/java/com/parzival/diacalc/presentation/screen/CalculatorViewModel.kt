package com.parzival.diacalc.presentation.screen

import androidx.lifecycle.ViewModel
import com.parzival.diacalc.domain.Calculator
import com.parzival.diacalc.domain.CalculatorResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorUIState())

    val state = _state.asStateFlow()

    fun onCarbsChange(value: String) {
        updateInput(value) {
            copy(carbsInput = value)
        }
    }

    fun onGramsPerBreadUnitChange(value: String) {
        updateInput(value) {
            copy(gramsToXE = value)
        }
    }

    fun onWeightChange(value: String) {
        updateInput(value) {
            copy(weightInput = value)
        }
    }

    fun onMultiplierChange(value: String) {
        updateInput(value) {
            copy(factorInput = value)
        }
    }

    private fun updateInput(
        value: String,
        transform: CalculatorUIState.() -> CalculatorUIState
    ) {
        if (!isValidInput(value)) return

        _state.update { current ->
            val updated = current.transform()

            updated.copy(
                result = calculateResult(updated)
            )
        }
    }

    private fun calculateResult(
        state: CalculatorUIState
    ): CalculatorResult {

        val carbs = state.carbsInput.toNumber()
        val weight = state.weightInput.toNumber()
        val gramsPerXE = state.gramsToXE.toNumber()
        val multiplier = state.factorInput.toNumber()

        if (
            carbs == null ||
            weight == null ||
            gramsPerXE == null ||
            multiplier == null ||
            gramsPerXE <= 0.0
        ) {
            return CalculatorResult()
        }

        return Calculator.calculate(
            carbs100gr = carbs,
            weight = weight,
            factor = multiplier,
            gramsToXE = gramsPerXE
        )
    }

    private fun String.toNumber(): Double? {
        return replace(',', '.').toDoubleOrNull()
            ?.takeIf { it.isFinite() }
    }

    private fun isValidInput(value: String): Boolean {
        return value.matches(Regex("""\d*([.,]\d*)?"""))
    }
}
