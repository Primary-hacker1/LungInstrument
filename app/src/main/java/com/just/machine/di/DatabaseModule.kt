package com.just.machine.di



import android.content.Context
import com.just.machine.dao.AppDatabase
import com.just.machine.dao.PlantDao
import com.just.machine.dao.calibration.CalibrationDao
import com.just.machine.dao.sixmin.SixMinReportBloodDao
import com.just.machine.dao.sixmin.SixMinReportBreathingDao
import com.just.machine.dao.sixmin.SixMinReportEvaluationDao
import com.just.machine.dao.sixmin.SixMinReportHeartDao
import com.just.machine.dao.sixmin.SixMinReportHeartEcgDao
import com.just.machine.dao.sixmin.SixMinReportInfoDao
import com.just.machine.dao.sixmin.SixMinReportOtherDao
import com.just.machine.dao.sixmin.SixMinReportPrescriptionDao
import com.just.machine.dao.sixmin.SixMinReportStrideDao
import com.just.machine.dao.sixmin.SixMinReportWalkDao
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
    fun provideSixMinReportWalkDao(appDatabase: AppDatabase): SixMinReportWalkDao {
        return appDatabase.sixMinReportWalkDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportBloodDao(appDatabase: AppDatabase): SixMinReportBloodDao {
        return appDatabase.sixMinReportBloodDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportHeartEcgDao(appDatabase: AppDatabase): SixMinReportHeartEcgDao {
        return appDatabase.sixMinReportHeartEcgDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportBreathingDao(appDatabase: AppDatabase): SixMinReportBreathingDao {
        return appDatabase.sixMinReportBreathingDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportEvaluationDao(appDatabase: AppDatabase): SixMinReportEvaluationDao {
        return appDatabase.sixMinReportEvaluationDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportHeartDao(appDatabase: AppDatabase): SixMinReportHeartDao {
        return appDatabase.sixMinReportHeartDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportInfoDao(appDatabase: AppDatabase): SixMinReportInfoDao {
        return appDatabase.sixMinReportInfoDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportOtherDao(appDatabase: AppDatabase): SixMinReportOtherDao {
        return appDatabase.sixMinReportOtherDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportPrescriptionDao(appDatabase: AppDatabase): SixMinReportPrescriptionDao {
        return appDatabase.sixMinReportPrescriptionDao()
    }

    @Singleton
    @Provides
    fun provideSixMinReportStrideDao(appDatabase: AppDatabase): SixMinReportStrideDao {
        return appDatabase.sixMinReportStrideDao()
    }
}
