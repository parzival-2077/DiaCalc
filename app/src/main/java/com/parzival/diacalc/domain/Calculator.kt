package com.parzival.diacalc.domain

object Calculator {

    fun calculate(
        carbs100gr: Double,
        weight: Double,
        factor: Double,
        gramsToXE: Double
    ): CalculatorResult {

        val carbs = carbs100gr * weight / 100.0

        val xe = carbs / gramsToXE

        val insulin = xe * factor

        return CalculatorResult(
            carbohydrates = carbs,
            XE = xe,
            insulin = insulin
        )
    }
}