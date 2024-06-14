package com.just.machine.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.just.machine.dao.calibration.CalibrationDao
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.lung.LungDao
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.dao.setting.DynamicSettingBean
import com.just.machine.dao.setting.SettingDao
import com.just.machine.dao.setting.StaticSettingBean
import com.just.machine.dao.sixmin.SixMinReportInfoDao
import com.just.machine.helper.UriConfig.DATABASE_NAME
import com.just.machine.helper.UriConfig.PLANT_DATA_FILENAME
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import com.just.machine.workers.SeedDatabaseWorker
import com.just.machine.workers.SeedDatabaseWorker.Companion.KEY_FILENAME

/**
 *create by 2021/9/18
 *@author zt
 * 此应用程序的房间数据库
 */
@Database(
    entities = [PatientBean::class, EnvironmentalCalibrationBean::class, AllSettingBean::class, StaticSettingBean::class,
        CPXBreathInOutData::class, FlowBean::class, IngredientBean::class, DynamicSettingBean::class, SixMinReportWalk::class,
        SixMinBloodOxygen::class, SixMinHeartEcg::class, SixMinReportBreathing::class,
        SixMinReportEvaluation::class, SixMinReportHeartBeat::class, SixMinReportInfo::class,
        SixMinReportOther::class, SixMinReportPrescription::class, SixMinReportStride::class],
    version = 2,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao
    abstract fun environmentalCalibrationDao(): CalibrationDao
    abstract fun settingDao(): SettingDao
    abstract fun lungDao(): LungDao
    abstract fun sixMinReportInfoDao(): SixMinReportInfoDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // 创建并预填充数据库。 有关更多详细信息，请参阅这篇文章：
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().setInputData(
                            workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME)
                        ).build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                }).build()
        }
    }
}
