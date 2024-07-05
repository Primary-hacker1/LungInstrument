package com.just.machine.ui.fragment.setting

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.pm.PackageInfoCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.*
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.ALL_SETTING_SUCCESS
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.just.machine.dao.AppDatabase
import com.just.machine.dao.PatientBean
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.model.Constants.Companion.settingsAreSaved
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentAllSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

/**
 *create by 2024/5/19
 *@author zt
 * 心肺通用设置
 */
@AndroidEntryPoint
class AllSettingFragment : CommonBaseFragment<FragmentAllSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var allSettingBean: AllSettingBean = AllSettingBean()

    private val spScenarios by lazy {
        SpinnerHelper(
            requireContext(),
            binding.spScenarios,
            R.array.spScenarios_items
        )
    }

    private val spBreathing: SpinnerHelper by lazy {
        SpinnerHelper(requireContext(), binding.spBreathing, R.array.spBreathing_items)
    }

    private val spEcg: SpinnerHelper by lazy {
        SpinnerHelper(requireContext(), binding.spEcg, R.array.spinner_items)
    }

    override fun loadData() {//懒加载

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        viewModel.getAllSettingBeans()

        viewModel.getCPXBreathInOutDataAll()//获取存储的心肺数据

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                ALL_SETTING_SUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is AllSettingBean) {
                            return@observe
                        }
                        allSettingBean = settingBean
                    }
                    initData()
                }
            }
        }

        val packageInfo: PackageInfo =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val versionName: String = packageInfo.versionName
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
        binding.tvSystemInfo1.text = "$versionCode 完整版本：v$versionName"
    }

    private fun initData() {
        binding.editHospitalName.setText(allSettingBean.hospitalName)
        binding.editLoginPass.setText(allSettingBean.pass)

        val arrayResourceId = R.array.spScenarios_items
        val itemsArray: Array<String> = resources.getStringArray(arrayResourceId)
        for ((index, item) in itemsArray.withIndex()) {
            if (item == allSettingBean.projectedValueScenarios) {
                // 如果找到了特定字符串，则设置 Spinner 的选中项为该位置
                spScenarios.setSelection(index)
                break // 找到后立即退出循环
            }
        }

        val spBreathingItem = R.array.spBreathing_items
        val items: Array<String> = resources.getStringArray(spBreathingItem)
        for ((index, item) in items.withIndex()) {
            if (item == allSettingBean.breathingItem) {
                spBreathing.setSelection(index)
                break
            }
        }

        val spECGItem = R.array.spinner_items
        val itemsECG: Array<String> = resources.getStringArray(spECGItem)
        for ((index, item) in itemsECG.withIndex()) {
            if (item == allSettingBean.ecg) {
                spEcg.setSelection(index)
                break
            }
        }

        binding.editTestDeadSpace.setText(allSettingBean.testDeadSpace.toString())


    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListener() {
        spScenarios.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.projectedValueScenarios = selectedItem
            }

            override fun onNothingSelected() {

            }
        })


        spBreathing.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.breathingItem = selectedItem
            }

            override fun onNothingSelected() {

            }
        })

        spEcg.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.ecg = selectedItem
            }

            override fun onNothingSelected() {

            }
        })

        LiveDataBus.get().with(settingsAreSaved).observe(this) {
            if (binding.editUpdatePass.text.toString() != binding.editConfirmPass.text.toString()) {
                toast("登陆两次密码输入不相同！")
                return@observe//两次密码输入不正确
            }

            if (binding.editUpdatePass.text.toString() != binding.editConfirmPass.text.toString()) {
                toast("登陆两次密码输入不相同！")
                return@observe//两次密码输入不正确
            }

            val editPass = binding.editUpdatePass.text.toString()

            SharedPreferencesUtils.instance.pass = editPass

            allSettingBean.pass = editPass

            allSettingBean.hospitalName = binding.editHospitalName.text.toString()

            allSettingBean.testDeadSpace = binding.editTestDeadSpace.text.toString().toInt()

            viewModel.setAllSettingBean(allSettingBean)
        }

        binding.tvDataBackup.setNoRepeatListener {
            lifecycleScope.launch {
                try {
                    backupData()
                    toast("数据库备份成功！")
                } catch (e: Exception) {
                    LogUtils.e(TAG + "Error backing up data: ${e.message}" + e)
                    toast("数据库备份失败！")
                }
            }
        }

        binding.tvDataRestoration.setNoRepeatListener {
            lifecycleScope.launch {
                try {
                    restoreData()
                    toast("数据库还原成功！")
                } catch (e: Exception) {
                    LogUtils.e(TAG + "Error restoring data: ${e.message}" + e)
                    toast("数据库还原失败！")
                }
            }
        }

    }

    private suspend fun backupData() {
        val db = AppDatabase.getInstance(requireContext())
        val backupDir = File(requireContext().getExternalFilesDir(null), "backup")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }

        // 备份患者数据
        val patients = db.plantDao().getAllPatient()
        val patientsBackupFile = File(backupDir, "plants-backup.json")
        patientsBackupFile.writeText(Gson().toJson(patients))

        // 备份定标数据 - ingredients
        val ingredients = db.environmentalCalibrationDao().getIngredients()
        val ingredientsBackupFile = File(backupDir, "ingredients-backup.json")
        ingredientsBackupFile.writeText(Gson().toJson(ingredients))

        // 备份定标数据 - flows
        val flows = db.environmentalCalibrationDao().getFlows()
        val flowsBackupFile = File(backupDir, "flows-backup.json")
        flowsBackupFile.writeText(Gson().toJson(flows))

        // 备份定标数据 - environments
        val environments = db.environmentalCalibrationDao().getEnvironmentals()
        val environmentsBackupFile = File(backupDir, "environments-backup.json")
        environmentsBackupFile.writeText(Gson().toJson(environments))

        // 备份动态心肺数据 - lung
        val lung = db.lungDao().getCPSBreathInOutData()
        val lungBackupFile = File(backupDir, "lung-backup.json")
        lungBackupFile.writeText(Gson().toJson(lung))
    }


    private suspend fun restoreData() {
        val db = AppDatabase.getInstance(requireContext())
        val backupDir = File(requireContext().getExternalFilesDir(null), "backup")

        // 恢复患者数据
        val patientsBackupFile = File(backupDir, "plants-backup.json")
        if (patientsBackupFile.exists()) {
            val patientsJson = patientsBackupFile.readText()
            val patients: List<PatientBean> = try {
                Gson().fromJson(patientsJson, object : TypeToken<List<PatientBean>>() {}.type)
            } catch (e: JsonSyntaxException) {
                listOf(Gson().fromJson(patientsJson, PatientBean::class.java))
            }
            db.plantDao().insertAll(patients)
        }

        // 恢复定标数据 - ingredients
        val ingredientsBackupFile = File(backupDir, "ingredients-backup.json")
        if (ingredientsBackupFile.exists()) {
            val ingredientsJson = ingredientsBackupFile.readText()
            val ingredients: List<IngredientBean> = try {
                Gson().fromJson(ingredientsJson, object : TypeToken<List<IngredientBean>>() {}.type)
            } catch (e: JsonSyntaxException) {
                listOf(Gson().fromJson(ingredientsJson, IngredientBean::class.java))
            }
            db.environmentalCalibrationDao().insertAllIngredientBean(ingredients)
        }

        // 恢复定标数据 - flows
        val flowsBackupFile = File(backupDir, "flows-backup.json")
        if (flowsBackupFile.exists()) {
            val flowsJson = flowsBackupFile.readText()
            val flows: List<FlowBean> = try {
                Gson().fromJson(flowsJson, object : TypeToken<List<FlowBean>>() {}.type)
            } catch (e: JsonSyntaxException) {
                listOf(Gson().fromJson(flowsJson, FlowBean::class.java))
            }
            db.environmentalCalibrationDao().insertAllFlow(flows)
        }

        // 恢复定标数据 - environments
        val environmentsBackupFile = File(backupDir, "environments-backup.json")
        if (environmentsBackupFile.exists()) {
            val environmentsJson = environmentsBackupFile.readText()
            val environments: List<EnvironmentalCalibrationBean> = try {
                Gson().fromJson(
                    environmentsJson,
                    object : TypeToken<List<EnvironmentalCalibrationBean>>() {}.type
                )
            } catch (e: JsonSyntaxException) {
                listOf(Gson().fromJson(environmentsJson, EnvironmentalCalibrationBean::class.java))
            }
            db.environmentalCalibrationDao().insertAllEnvironmental(environments)
        }

        // 恢复动态心肺数据 - lung
        val lungBackupFile = File(backupDir, "lung-backup.json")
        if (lungBackupFile.exists()) {
            val lungJson = lungBackupFile.readText()
            val lung: List<CPXBreathInOutData> = try {
                Gson().fromJson(lungJson, object : TypeToken<List<CPXBreathInOutData>>() {}.type)
            } catch (e: JsonSyntaxException) {
                listOf(Gson().fromJson(lungJson, CPXBreathInOutData::class.java))
            }
            db.lungDao().insertAllCPXBreathInOutData(lung)
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAllSettingBinding.inflate(inflater, container, false)
}