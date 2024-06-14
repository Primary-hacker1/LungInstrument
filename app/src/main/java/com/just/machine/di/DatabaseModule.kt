package com.just.machine.di



import android.content.Context
import com.just.machine.dao.AppDatabase
import com.just.machine.dao.PlantDao
import com.just.machine.dao.calibration.CalibrationDao
import com.just.machine.dao.lung.LungDao
import com.just.machine.dao.setting.SettingDao
import com.just.machine.dao.sixmin.SixMinReportInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.plantDao()
    }


    @Singleton
    @Provides
    fun provideEnvironmentalDao(appDatabase: AppDatabase): CalibrationDao {
        return appDatabase.environmentalCalibrationDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportInfoDao(appDatabase: AppDatabase): SixMinReportInfoDao {
        return appDatabase.sixMinReportInfoDao()
    }

    @Singleton
    @Provides
    fun provideSettingDao(appDatabase: AppDatabase): SettingDao {
        return appDatabase.settingDao()
    }

    @Singleton
    @Provides
    fun provideLungDao(appDatabase: AppDatabase): LungDao {
        return appDatabase.lungDao()
    }
}
