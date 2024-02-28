package com.just.machine.dao;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PlantDao _plantDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `patients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `sex` TEXT NOT NULL, `height` TEXT NOT NULL, `weight` TEXT NOT NULL, `identityCard` TEXT NOT NULL, `birthday` TEXT NOT NULL, `age` TEXT NOT NULL, `BMI` TEXT NOT NULL, `medicalRecordNumber` TEXT NOT NULL, `predictDistances` TEXT NOT NULL, `diseaseHistory` TEXT NOT NULL, `currentMedications` TEXT NOT NULL, `clinicalDiagnosis` TEXT NOT NULL, `remark` TEXT NOT NULL, `stride` TEXT NOT NULL, `numberOfTrialsParticipated` INTEGER NOT NULL, `hospitalNumber` TEXT NOT NULL, `floorNo` TEXT NOT NULL, `department` TEXT NOT NULL, `addTime` TEXT NOT NULL, `updatedTime` TEXT NOT NULL, `deleteTheTag` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'df9cede8a6db07f6b4b987085ea5df6d')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `patients`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsPatients = new HashMap<String, TableInfo.Column>(23);
        _columnsPatients.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("sex", new TableInfo.Column("sex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("height", new TableInfo.Column("height", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("weight", new TableInfo.Column("weight", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("identityCard", new TableInfo.Column("identityCard", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("birthday", new TableInfo.Column("birthday", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("age", new TableInfo.Column("age", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("BMI", new TableInfo.Column("BMI", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("medicalRecordNumber", new TableInfo.Column("medicalRecordNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("predictDistances", new TableInfo.Column("predictDistances", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("diseaseHistory", new TableInfo.Column("diseaseHistory", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("currentMedications", new TableInfo.Column("currentMedications", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("clinicalDiagnosis", new TableInfo.Column("clinicalDiagnosis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("remark", new TableInfo.Column("remark", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("stride", new TableInfo.Column("stride", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("numberOfTrialsParticipated", new TableInfo.Column("numberOfTrialsParticipated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("hospitalNumber", new TableInfo.Column("hospitalNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("floorNo", new TableInfo.Column("floorNo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("department", new TableInfo.Column("department", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("addTime", new TableInfo.Column("addTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("updatedTime", new TableInfo.Column("updatedTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("deleteTheTag", new TableInfo.Column("deleteTheTag", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPatients = new TableInfo("patients", _columnsPatients, _foreignKeysPatients, _indicesPatients);
        final TableInfo _existingPatients = TableInfo.read(_db, "patients");
        if (! _infoPatients.equals(_existingPatients)) {
          return new RoomOpenHelper.ValidationResult(false, "patients(com.just.machine.dao.PatientBean).\n"
                  + " Expected:\n" + _infoPatients + "\n"
                  + " Found:\n" + _existingPatients);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "df9cede8a6db07f6b4b987085ea5df6d", "b0b6cf23875492048d0b5f53fb85edb8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "patients");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `patients`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PlantDao.class, PlantDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public PlantDao plantDao() {
    if (_plantDao != null) {
      return _plantDao;
    } else {
      synchronized(this) {
        if(_plantDao == null) {
          _plantDao = new PlantDao_Impl(this);
        }
        return _plantDao;
      }
    }
  }
}
