package com.just.machine.model.staticlung

import java.util.*


data class LungFormula(
    val id: Int? = 0,
    val preName: String? = "",
    val type: String? = "",
    val parameter: String? = "",
    val maleUnder18: String? = "",
    val maleOver18: String? = "",
    val femaleUnder18: String? = "",
    val femaleOver18: String? = "",
    val creater: String? = ""
) {

    fun calculateLungValue(
        formula: LungFormula,
        heightCm: Double,
        age: Double,
        weightKg: Double,
        isMale: Boolean
    ): Double {
        val ageAdjust = if (age < 18) age else 18.0

        return when {
            isMale && age < 18 && formula.maleUnder18 != null -> {
                formula.maleUnder18.replace("HeightCm", heightCm.toString())
                    .replace("AgeAdjust", ageAdjust.toString()).toDouble()
            }

            isMale && age >= 18 && formula.maleOver18 != null -> {
                formula.maleOver18.replace("HeightCm", heightCm.toString())
                    .replace("AgeAdjust", ageAdjust.toString()).toDouble()
            }

            !isMale && age < 18 && formula.femaleUnder18 != null -> {
                formula.femaleUnder18.replace("HeightCm", heightCm.toString())
                    .replace("AgeAdjust", ageAdjust.toString()).toDouble()
            }

            !isMale && age >= 18 && formula.femaleOver18 != null -> {
                formula.femaleOver18.replace("HeightCm", heightCm.toString())
                    .replace("AgeAdjust", ageAdjust.toString()).toDouble()
            }

            else -> {
                throw IllegalArgumentException("Invalid formula or parameters for given age and gender.")
            }
        }
    }

    fun main() {
        val formulas = listOf(
            LungFormula(
                2,
                "Eccs",
                "SVC",
                "SVC",
                null,
                "((0.061*HeightCm)-(0.028*AgeAdjust)-4.65)",
                null,
                "((0.0466*HeightCm)-(0.026*AgeAdjust)-3.28)",
                "Eccs83"
            ),
            // ... other formulas
        )

        // Example usage:
        val heightCm = 170.0
        val age = 25.0
        val weightKg = 70.0
        val isMale = true

        for (formula in formulas) {
            val value = calculateLungValue(formula, heightCm, age, weightKg, isMale)
            println("${formula.preName} ${formula.type}: $value")
        }
    }
}


