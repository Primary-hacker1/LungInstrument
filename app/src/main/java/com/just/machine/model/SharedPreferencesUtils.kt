package com.just.machine.model

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.just.machine.App
import com.just.machine.dao.PatientBean


/**
 *create by 2022/6/30
 *@author zt
 */
class SharedPreferencesUtils private constructor() {

    companion object {
        val instance = SharedPreferencesUtils()
        private const val PER_USERNAME = "per_username"
        private const val USER = "per_user_user"
        private const val PER_IS_CLICK_BTN = "per_user_is_click_btn"
        private const val PASS = "per_user_pass"
        private const val SIX_MIN_SYS_SETTING = "per_six_min_sys_setting"
        private const val SIX_MIN_TEST_GUIDE = "per_six_min_test_guide"
        private const val PATIENTBEAN = "per_patient_bean"
        private const val INGREDIENT_CALIBRATE_STANDARDONE_O2 = "per_ingredient_calibrate_standardone_o2"
        private const val INGREDIENT_CALIBRATE_STANDARDONE_CO2 = "per_ingredient_calibrate_standardone_co2"
        private const val INGREDIENT_CALIBRATE_STANDARDTWO_O2 = "per_ingredient_calibrate_standardtwo_o2"
        private const val INGREDIENT_CALIBRATE_STANDARDTWO_CO2 = "per_ingredient_calibrate_standardtwo_co2"
    }

    var user: String? = null
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(USER, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(USER, serialNo)
        }

    var pass: String? = null
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(PASS, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(PASS, serialNo)
        }

    var isClickBtn: String? = null //第一次点击的时候创建患者
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(PER_IS_CLICK_BTN, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(PER_IS_CLICK_BTN, serialNo)
        }

    var sixMinSysSetting: String? = null //6分钟系统设置
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(SIX_MIN_SYS_SETTING, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(SIX_MIN_SYS_SETTING, serialNo)
        }

    var sixMinGuide: String? = null //6分钟引导
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(SIX_MIN_TEST_GUIDE, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(SIX_MIN_TEST_GUIDE, serialNo)
        }

    var patientBean: PatientBean? = PatientBean()
        get() {
            if (field == null) {
                // 从 SharedPreferences 中获取 PatientBean 对象
                field =
                    CommonSharedPreferences.getBean(PATIENTBEAN, PatientBean())
            }
            return field
        }
        set(serialNo) {
            if (serialNo != null) {
                // 将 PatientBean 对象保存到 SharedPreferences 中
                CommonSharedPreferences.setBean(PATIENTBEAN, serialNo)
            }
            field = serialNo
        }

    var ingredientCaliStanderOneO2: String? = null //成分定标标气1氧气
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(INGREDIENT_CALIBRATE_STANDARDONE_O2, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(INGREDIENT_CALIBRATE_STANDARDONE_O2, serialNo)
        }

    var ingredientCaliStanderOneCO2: String? = null //成分定标标气1二氧化碳
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(INGREDIENT_CALIBRATE_STANDARDONE_CO2, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(INGREDIENT_CALIBRATE_STANDARDONE_CO2, serialNo)
        }

    var ingredientCaliStanderTwoO2: String? = null //成分定标标气2氧气
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(INGREDIENT_CALIBRATE_STANDARDTWO_O2, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(INGREDIENT_CALIBRATE_STANDARDTWO_O2, serialNo)
        }

    var ingredientCaliStanderTwoCO2: String? = null //成分定标标气2二氧化碳
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(INGREDIENT_CALIBRATE_STANDARDTWO_CO2, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(INGREDIENT_CALIBRATE_STANDARDTWO_CO2, serialNo)
        }

    /**
     * 登录信息销毁
     */
    fun logout() {
        user = ""
//        pass = ""
    }

}

object CommonSharedPreferences {

    /**
     * Created by lollipop on 2017/12/10.
     * Update by lollipop on 2020/11/09
     * @author Lollipop
     * 持久化储存的工具类
     */

    private const val USER = "Settings"

    private val gson = Gson()

    /**
     * 将一个 PatientBean 对象保存到指定的 SharedPreferences 中
     * @param key 名称
     * @param value PatientBean 对象
     */
    fun setBean(key: String, value: PatientBean) {
        val gson = Gson()
        val json = gson.toJson(value)
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        put(mShareConfig, key, json)
    }

    /**
     * 从指定的 SharedPreferences 中获取一个 PatientBean 对象
     * @param key 名称
     * @param defValue 默认值，如果 SharedPreferences 中找不到对应的值，将会返回默认值
     * @return PatientBean 对象，如果找不到对应的值，返回默认值
     */
    fun getBean(key: String, defValue: PatientBean): PatientBean? {
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        val json = get(mShareConfig, key, "") // 从 SharedPreferences 中获取保存的 JSON 字符串
        if (json.isNotEmpty()) {
            val gson = Gson()
            return gson.fromJson(json, PatientBean::class.java) // 将 JSON 字符串转换为 PatientBean 对象
        }
        return defValue
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    operator fun <T> Context.set(key: String, value: T) {
        val mShareConfig = applicationContext.getSharedPreferences(USER, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> setSPValue(key: String, value: T) {
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Activity中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> Activity.privateSet(key: String, value: T) {
        val name = this.javaClass.simpleName
        val mShareConfig = getSharedPreferences(name, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Fragment中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> Fragment.privateSet(key: String, value: T) {
        val name = this.javaClass.simpleName
        val mShareConfig = context!!.getSharedPreferences(name, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存数据到一个指定的SharedPreferences中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    private fun <T> put(mShareConfig: SharedPreferences, key: String, value: T) {
        val conEdit = mShareConfig.edit()
        when (value) {
            is String -> conEdit.putString(key, (value as String).trim { it <= ' ' })
            is Long -> conEdit.putLong(key, value as Long)
            is Boolean -> conEdit.putBoolean(key, value as Boolean)
            is Int -> conEdit.putInt(key, value as Int)
            is Float -> conEdit.putFloat(key, value as Float)
        }
        conEdit.apply()
    }

    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    operator fun <T> Context.get(key: String, defValue: T): T {
        val mShareConfig = applicationContext.getSharedPreferences(USER, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> getSPValue(key: String, defValue: T): T {
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Activity中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> Activity.privateGet(key: String, defValue: T): T {
        val name = this.javaClass.simpleName
        val mShareConfig = getSharedPreferences(name, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Fragment中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> Fragment.privateGet(key: String, defValue: T): T {
        val name = this.javaClass.simpleName
        val mShareConfig = context!!.getSharedPreferences(name, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从指定的SharedPreferences中获取数据
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(mShareConfig: SharedPreferences, key: String, defValue: T): T {
        var value: T = defValue
        when (defValue) {
            is String -> value = mShareConfig.getString(key, defValue as String) as T
            is Long -> value =
                java.lang.Long.valueOf(mShareConfig.getLong(key, defValue as Long)) as T

            is Boolean -> value =
                java.lang.Boolean.valueOf(mShareConfig.getBoolean(key, defValue as Boolean)) as T

            is Int -> value = Integer.valueOf(mShareConfig.getInt(key, defValue as Int)) as T
            is Float -> value =
                java.lang.Float.valueOf(mShareConfig.getFloat(key, defValue as Float)) as T
        }
        return value
    }
}