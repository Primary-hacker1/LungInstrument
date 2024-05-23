package com.just.machine.model.lungdata

/*
* 预计值计算
* zt 2024/4/29
* */

import com.common.base.log
import com.common.network.LogUtils
import net.objecthunter.exp4j.ExpressionBuilder

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

    private val tag = LungFormula::class.java.name

    fun calculateLungValue(
        formula: LungFormula,
        heightCm: Double,
        age: Double,
        weightKg: Double,
        isMale: Boolean
    ): Double {
        val ageAdjust = if (age < 18) age else 18.0

        val expression = when {
            isMale && age < 18 -> formula.maleUnder18
            isMale && age >= 18 -> formula.maleOver18
            !isMale && age < 18 -> formula.femaleUnder18
            else -> formula.femaleOver18
        }

        if (expression == null) {
            // 添加详细日志
            LogUtils.e(tag + "LungFormulaNo valid formula found for gender: $isMale, age: $age")
            return 0.0
        }

        val substitutedExpression = expression.replace("HeightCm", heightCm.toString())
            .replace("AgeAdjust", ageAdjust.toString())
            .replace("WeightKg", weightKg.toString())

        return ExpressionBuilder(substitutedExpression).build().evaluate()
    }


    companion object {
        @JvmStatic
        fun main(
            parameter: String? = "SVC",
            heightCm: Double? = 170.0,
            age: Double? = 25.0,
            weightKg: Double? = 100.0,
            isMale: Boolean? = true
        ): Double? {
            val formulas = listOf(
                LungFormula(
                    2,
                    "Eccs",
                    "SVC",
                    "SVC",
                    "(-0.12509*HeightCm)+((0.000605)*HeightCm*HeightCm)+7.9942",
                    "((0.061*HeightCm)-(0.028*AgeAdjust)-4.65)",
                    "(-0.01217*HeightCm)+((0.000189)*HeightCm*HeightCm)+0.1694",
                    "((0.0466*HeightCm)-(0.026*AgeAdjust)-3.28)",
                    "Eccs83"
                ),
                LungFormula(
                    4,
                    "Cherniak",
                    "MVV",
                    "MVV",
                    "1.193*HeightCm-0.816*AgeAdjust-37.9",
                    "1.193*HeightCm-0.816*AgeAdjust-37.9",
                    "0.843*HeightCm-0.685*AgeAdjust-4.87",
                    "0.843*HeightCm-0.685*AgeAdjust-4.87",
                    "Cherniak"
                ),
                LungFormula(
                    1,
                    "Zapletal",
                    "FVC",
                    "FVC",
                    "(-0.125*HeightCm)+((0.000605)*HeightCm*HeightCm)+7.9942",
                    "(-0.026*AgeAdjust)+(0.0576*HeightCm)-4.34",
                    "((0.000189)*HeightCm*HeightCm)-(0.0122*HeightCm)+0.1694",
                    "(-0.026*AgeAdjust)+(0.0443*HeightCm)-2.89",
                    "Zapletal"
                ),
                LungFormula(
                    5,
                    "Jaeger",
                    "SVC",
                    "IC",
                    "(-0.2584 - 0.20415*AgeAdjust + 0.010133*AgeAdjust *AgeAdjust  + 0.00018642*HeightCm*HeightCm) *0.66",
                    "0.061*HeightCm-0.028*AgeAdjust-4.65-(0.0234*HeightCm+0.009*AgeAdjust-1.09)+(0.0131*HeightCm+0.022*AgeAdjust-1.23)",
                    "(-1.2082 + 0.05916*AgeAdjust  + 0.00014815*HeightCm*HeightCm) *0.66",
                    "0.0466*HeightCm-0.024*AgeAdjust-3.28-(14.0/625.0*HeightCm+0.001*AgeAdjust-1.0)+(0.0181*HeightCm+0.016*AgeAdjust-2.0)",
                    "Wasserman"
                ),
                LungFormula(
                    23,
                    "Eccs",
                    "FVC",
                    "FRC",
                    null,
                    "((0.056*HeightCm)-(0.024*AgeAdjust)-3.12)",
                    null,
                    "((0.035*HeightCm)-(0.022*AgeAdjust)-1.9)",
                    "Eccs"
                ),
                LungFormula(
                    24,
                    "Jaeger",
                    "SVC",
                    "ERV",
                    "(-0.2584 - 0.20415*AgeAdjust + 0.010133*AgeAdjust *AgeAdjust  + 0.00018642*HeightCm*HeightCm) *0.33",
                    "14.0/625.0*HeightCm+0.001*AgeAdjust-1.0-(0.0181*HeightCm+0.016*AgeAdjust-2.0)",
                    "(-1.2082 + 0.05916*AgeAdjust  + 0.00014815*HeightCm*HeightCm) *0.33",
                    "0.0234*HeightCm+0.009*AgeAdjust-1.09-(0.0131*HeightCm+0.022*AgeAdjust-1.23)",
                    "Jaeger"
                ),
                LungFormula(
                    8,
                    "Zapletal",
                    "FVC",
                    "FEV1",
                    "(-0.10261*HeightCm)+((0.000499)*HeightCm*HeightCm)+6.6314",
                    null,
                    "(0.0364*HeightCm)-3.038",
                    null,
                    "Zapletal"
                ),
                LungFormula(
                    9,
                    "Zapletal",
                    "FVC",
                    "FEF25",
                    "(0.07811*HeightCm)-6.822",
                    "((0.0546*HeightCm)-(0.029*AgeAdjust)-0.47)",
                    "(0.0637*HeightCm)-5.1934",
                    "((0.0322*HeightCm)-(0.025*AgeAdjust)+1.6)",
                    "Zapletal"
                ),
                LungFormula(
                    10,
                    "Zapletal",
                    "FVC",
                    "FEF50",
                    "(0.0543*HeightCm)-4.5848",
                    "((0.0379*HeightCm)-(0.031*AgeAdjust)-0.35)",
                    "(0.04477*HeightCm)-3.3655",
                    "((0.0245*HeightCm)-(0.025*AgeAdjust)+1.16)",
                    "Zapletal"
                ),
                LungFormula(
                    11,
                    "Zapletal",
                    "FVC",
                    "FEF75",
                    "(0.02817*HeightCm)-2.3069",
                    "((0.0261*HeightCm)-(0.026*AgeAdjust)-1.34)",
                    "(0.02483*HeightCm)-1.8576",
                    "((0.0105*HeightCm)-(0.025*AgeAdjust)+1.11)",
                    "Zapletal"
                ),
                LungFormula(
                    12,
                    "Zapletal",
                    "FVC",
                    "PEF",
                    "(0.0806*HeightCm)-6.9865",
                    "((0.0614*HeightCm)-(0.043*AgeAdjust)+0.15)",
                    "(0.06594*HeightCm)-5.3794",
                    "((0.055*HeightCm)-(0.03*AgeAdjust)+2.61)",
                    "Zapletal"
                ),
                LungFormula(
                    18,
                    "Eccs",
                    "FVC",
                    "IVC",
                    null,
                    "((0.067*HeightCm)-(0.026*AgeAdjust)-4.12)",
                    null,
                    "((0.046*HeightCm)-(0.025*AgeAdjust)-2.71)",
                    "Eccs"
                ),

                LungFormula(
                    19,
                    "Eccs",
                    "FVC",
                    "FEV1/FVC",
                    "(-0.18*AgeAdjust)+87.21",
                    "(-0.19*AgeAdjust)+89.1",
                    "(-0.18*AgeAdjust)+87.21",
                    "(-0.19*AgeAdjust)+89.1",
                    "Eccs"
                ),
                LungFormula(
                    25,
                    "Zapletal",
                    "FVC",
                    "FEV1/FVC",
                    "90.6043-0.04104*HeightCm",
                    "90.6043-0.04104*HeightCm",
                    "90.6043-0.04104*HeightCm",
                    "90.6043-0.04104*HeightCm",
                    "Zapletal"
                ),
                LungFormula(
                    3,
                    "Wasserman",
                    "CPET",
                    "HR",
                    "220.0-AgeAdjust",
                    "220.0-AgeAdjust",
                    "220.0-AgeAdjust",
                    "220.0-AgeAdjust",
                    "Jaeger"
                ),
                LungFormula(
                    20,
                    "Eccs",
                    "FVC",
                    "TLC",
                    null,
                    "((0.087*HeightCm)-(0.028*AgeAdjust)-7.08)",
                    null,
                    "((0.065*HeightCm)-(0.027*AgeAdjust)-5.01)",
                    "Eccs"
                ),
                LungFormula(
                    21,
                    "Wasserman",
                    "CPET",
                    "VO2",
                    "(WeightKg*(50.72-0.372*AgeAdjust))",
                    "(WeightKg*(50.72-0.372*AgeAdjust))",
                    "(WeightKg*(50.72-0.372*AgeAdjust))",
                    "((43+WeightKg)*(22.78-0.17*AgeAdjust))",
                    "Wasserman"
                ),
                LungFormula(
                    22,
                    "Eccs",
                    "FVC",
                    "RV",
                    null,
                    "((0.027*HeightCm)-(0.01*AgeAdjust)-2.09)",
                    null,
                    "((0.025*HeightCm)-(0.015*AgeAdjust)-1.19)",
                    "Eccs"
                ),
            )

            val lungFormula = LungFormula()
            var value: Double? = 0.0
            for (formula in formulas) {
                if (formula.parameter != parameter) continue // 如果不匹配，继续检查下一个
                value =
                    if (heightCm != null && age != null && weightKg != null && isMale != null) {
                        lungFormula.calculateLungValue(formula, heightCm, age, weightKg, isMale)
                    } else {
                        0.0 // 或者其他你认为合适的默认值
                    }
                LogUtils.d("${formula.preName} ${formula.type}: $value")
            }

            // 保证格式化发生在有效值更新之后
            value = "%.2f".format(value ?: 0.0).toDouble()

            return value

        }
    }
}




