package com.just.machine.dao;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PlantDao_Impl implements PlantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PatientBean> __insertionAdapterOfPatientBean;

  private final EntityInsertionAdapter<PatientBean> __insertionAdapterOfPatientBean_1;

  public PlantDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatientBean = new EntityInsertionAdapter<PatientBean>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `patients` (`id`,`name`,`sex`,`height`,`weight`,`identityCard`,`birthday`,`age`,`BMI`,`medicalRecordNumber`,`predictDistances`,`diseaseHistory`,`currentMedications`,`clinicalDiagnosis`,`remark`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PatientBean value) {
        stmt.bindLong(1, value.getPatientId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getSex() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSex());
        }
        if (value.getHeight() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getHeight());
        }
        if (value.getWeight() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWeight());
        }
        if (value.getIdentityCard() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIdentityCard());
        }
        if (value.getBirthday() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getBirthday());
        }
        if (value.getAge() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getAge());
        }
        if (value.getBMI() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getBMI());
        }
        if (value.getMedicalRecordNumber() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getMedicalRecordNumber());
        }
        if (value.getPredictDistances() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getPredictDistances());
        }
        if (value.getDiseaseHistory() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getDiseaseHistory());
        }
        if (value.getCurrentMedications() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getCurrentMedications());
        }
        if (value.getClinicalDiagnosis() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getClinicalDiagnosis());
        }
        if (value.getRemark() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getRemark());
        }
      }
    };
    this.__insertionAdapterOfPatientBean_1 = new EntityInsertionAdapter<PatientBean>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `patients` (`id`,`name`,`sex`,`height`,`weight`,`identityCard`,`birthday`,`age`,`BMI`,`medicalRecordNumber`,`predictDistances`,`diseaseHistory`,`currentMedications`,`clinicalDiagnosis`,`remark`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PatientBean value) {
        stmt.bindLong(1, value.getPatientId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getSex() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSex());
        }
        if (value.getHeight() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getHeight());
        }
        if (value.getWeight() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWeight());
        }
        if (value.getIdentityCard() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIdentityCard());
        }
        if (value.getBirthday() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getBirthday());
        }
        if (value.getAge() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getAge());
        }
        if (value.getBMI() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getBMI());
        }
        if (value.getMedicalRecordNumber() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getMedicalRecordNumber());
        }
        if (value.getPredictDistances() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getPredictDistances());
        }
        if (value.getDiseaseHistory() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getDiseaseHistory());
        }
        if (value.getCurrentMedications() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getCurrentMedications());
        }
        if (value.getClinicalDiagnosis() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getClinicalDiagnosis());
        }
        if (value.getRemark() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getRemark());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<PatientBean> plants,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPatientBean.insert(plants);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public long insertPatient(final PatientBean patients) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfPatientBean_1.insertAndReturnId(patients);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<PatientBean>> getPlants() {
    final String _sql = "SELECT * FROM patients ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"patients"}, new Callable<List<PatientBean>>() {
      @Override
      public List<PatientBean> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfIdentityCard = CursorUtil.getColumnIndexOrThrow(_cursor, "identityCard");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfBMI = CursorUtil.getColumnIndexOrThrow(_cursor, "BMI");
          final int _cursorIndexOfMedicalRecordNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "medicalRecordNumber");
          final int _cursorIndexOfPredictDistances = CursorUtil.getColumnIndexOrThrow(_cursor, "predictDistances");
          final int _cursorIndexOfDiseaseHistory = CursorUtil.getColumnIndexOrThrow(_cursor, "diseaseHistory");
          final int _cursorIndexOfCurrentMedications = CursorUtil.getColumnIndexOrThrow(_cursor, "currentMedications");
          final int _cursorIndexOfClinicalDiagnosis = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicalDiagnosis");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final List<PatientBean> _result = new ArrayList<PatientBean>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PatientBean _item;
            final long _tmpPatientId;
            _tmpPatientId = _cursor.getLong(_cursorIndexOfPatientId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpSex;
            if (_cursor.isNull(_cursorIndexOfSex)) {
              _tmpSex = null;
            } else {
              _tmpSex = _cursor.getString(_cursorIndexOfSex);
            }
            final String _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getString(_cursorIndexOfHeight);
            }
            final String _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getString(_cursorIndexOfWeight);
            }
            final String _tmpIdentityCard;
            if (_cursor.isNull(_cursorIndexOfIdentityCard)) {
              _tmpIdentityCard = null;
            } else {
              _tmpIdentityCard = _cursor.getString(_cursorIndexOfIdentityCard);
            }
            final String _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getString(_cursorIndexOfBirthday);
            }
            final String _tmpAge;
            if (_cursor.isNull(_cursorIndexOfAge)) {
              _tmpAge = null;
            } else {
              _tmpAge = _cursor.getString(_cursorIndexOfAge);
            }
            final String _tmpBMI;
            if (_cursor.isNull(_cursorIndexOfBMI)) {
              _tmpBMI = null;
            } else {
              _tmpBMI = _cursor.getString(_cursorIndexOfBMI);
            }
            final String _tmpMedicalRecordNumber;
            if (_cursor.isNull(_cursorIndexOfMedicalRecordNumber)) {
              _tmpMedicalRecordNumber = null;
            } else {
              _tmpMedicalRecordNumber = _cursor.getString(_cursorIndexOfMedicalRecordNumber);
            }
            final String _tmpPredictDistances;
            if (_cursor.isNull(_cursorIndexOfPredictDistances)) {
              _tmpPredictDistances = null;
            } else {
              _tmpPredictDistances = _cursor.getString(_cursorIndexOfPredictDistances);
            }
            final String _tmpDiseaseHistory;
            if (_cursor.isNull(_cursorIndexOfDiseaseHistory)) {
              _tmpDiseaseHistory = null;
            } else {
              _tmpDiseaseHistory = _cursor.getString(_cursorIndexOfDiseaseHistory);
            }
            final String _tmpCurrentMedications;
            if (_cursor.isNull(_cursorIndexOfCurrentMedications)) {
              _tmpCurrentMedications = null;
            } else {
              _tmpCurrentMedications = _cursor.getString(_cursorIndexOfCurrentMedications);
            }
            final String _tmpClinicalDiagnosis;
            if (_cursor.isNull(_cursorIndexOfClinicalDiagnosis)) {
              _tmpClinicalDiagnosis = null;
            } else {
              _tmpClinicalDiagnosis = _cursor.getString(_cursorIndexOfClinicalDiagnosis);
            }
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            _item = new PatientBean(_tmpPatientId,_tmpName,_tmpSex,_tmpHeight,_tmpWeight,_tmpIdentityCard,_tmpBirthday,_tmpAge,_tmpBMI,_tmpMedicalRecordNumber,_tmpPredictDistances,_tmpDiseaseHistory,_tmpCurrentMedications,_tmpClinicalDiagnosis,_tmpRemark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PatientBean>> getPlantsWithGrowZoneNumber(final int age) {
    final String _sql = "SELECT * FROM patients WHERE age = ? ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, age);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"patients"}, new Callable<List<PatientBean>>() {
      @Override
      public List<PatientBean> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfIdentityCard = CursorUtil.getColumnIndexOrThrow(_cursor, "identityCard");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfBMI = CursorUtil.getColumnIndexOrThrow(_cursor, "BMI");
          final int _cursorIndexOfMedicalRecordNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "medicalRecordNumber");
          final int _cursorIndexOfPredictDistances = CursorUtil.getColumnIndexOrThrow(_cursor, "predictDistances");
          final int _cursorIndexOfDiseaseHistory = CursorUtil.getColumnIndexOrThrow(_cursor, "diseaseHistory");
          final int _cursorIndexOfCurrentMedications = CursorUtil.getColumnIndexOrThrow(_cursor, "currentMedications");
          final int _cursorIndexOfClinicalDiagnosis = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicalDiagnosis");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final List<PatientBean> _result = new ArrayList<PatientBean>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PatientBean _item;
            final long _tmpPatientId;
            _tmpPatientId = _cursor.getLong(_cursorIndexOfPatientId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpSex;
            if (_cursor.isNull(_cursorIndexOfSex)) {
              _tmpSex = null;
            } else {
              _tmpSex = _cursor.getString(_cursorIndexOfSex);
            }
            final String _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getString(_cursorIndexOfHeight);
            }
            final String _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getString(_cursorIndexOfWeight);
            }
            final String _tmpIdentityCard;
            if (_cursor.isNull(_cursorIndexOfIdentityCard)) {
              _tmpIdentityCard = null;
            } else {
              _tmpIdentityCard = _cursor.getString(_cursorIndexOfIdentityCard);
            }
            final String _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getString(_cursorIndexOfBirthday);
            }
            final String _tmpAge;
            if (_cursor.isNull(_cursorIndexOfAge)) {
              _tmpAge = null;
            } else {
              _tmpAge = _cursor.getString(_cursorIndexOfAge);
            }
            final String _tmpBMI;
            if (_cursor.isNull(_cursorIndexOfBMI)) {
              _tmpBMI = null;
            } else {
              _tmpBMI = _cursor.getString(_cursorIndexOfBMI);
            }
            final String _tmpMedicalRecordNumber;
            if (_cursor.isNull(_cursorIndexOfMedicalRecordNumber)) {
              _tmpMedicalRecordNumber = null;
            } else {
              _tmpMedicalRecordNumber = _cursor.getString(_cursorIndexOfMedicalRecordNumber);
            }
            final String _tmpPredictDistances;
            if (_cursor.isNull(_cursorIndexOfPredictDistances)) {
              _tmpPredictDistances = null;
            } else {
              _tmpPredictDistances = _cursor.getString(_cursorIndexOfPredictDistances);
            }
            final String _tmpDiseaseHistory;
            if (_cursor.isNull(_cursorIndexOfDiseaseHistory)) {
              _tmpDiseaseHistory = null;
            } else {
              _tmpDiseaseHistory = _cursor.getString(_cursorIndexOfDiseaseHistory);
            }
            final String _tmpCurrentMedications;
            if (_cursor.isNull(_cursorIndexOfCurrentMedications)) {
              _tmpCurrentMedications = null;
            } else {
              _tmpCurrentMedications = _cursor.getString(_cursorIndexOfCurrentMedications);
            }
            final String _tmpClinicalDiagnosis;
            if (_cursor.isNull(_cursorIndexOfClinicalDiagnosis)) {
              _tmpClinicalDiagnosis = null;
            } else {
              _tmpClinicalDiagnosis = _cursor.getString(_cursorIndexOfClinicalDiagnosis);
            }
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            _item = new PatientBean(_tmpPatientId,_tmpName,_tmpSex,_tmpHeight,_tmpWeight,_tmpIdentityCard,_tmpBirthday,_tmpAge,_tmpBMI,_tmpMedicalRecordNumber,_tmpPredictDistances,_tmpDiseaseHistory,_tmpCurrentMedications,_tmpClinicalDiagnosis,_tmpRemark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PatientBean>> getPatients() {
    final String _sql = "SELECT * FROM patients";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"patients"}, new Callable<List<PatientBean>>() {
      @Override
      public List<PatientBean> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfIdentityCard = CursorUtil.getColumnIndexOrThrow(_cursor, "identityCard");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfBMI = CursorUtil.getColumnIndexOrThrow(_cursor, "BMI");
          final int _cursorIndexOfMedicalRecordNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "medicalRecordNumber");
          final int _cursorIndexOfPredictDistances = CursorUtil.getColumnIndexOrThrow(_cursor, "predictDistances");
          final int _cursorIndexOfDiseaseHistory = CursorUtil.getColumnIndexOrThrow(_cursor, "diseaseHistory");
          final int _cursorIndexOfCurrentMedications = CursorUtil.getColumnIndexOrThrow(_cursor, "currentMedications");
          final int _cursorIndexOfClinicalDiagnosis = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicalDiagnosis");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final List<PatientBean> _result = new ArrayList<PatientBean>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PatientBean _item;
            final long _tmpPatientId;
            _tmpPatientId = _cursor.getLong(_cursorIndexOfPatientId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpSex;
            if (_cursor.isNull(_cursorIndexOfSex)) {
              _tmpSex = null;
            } else {
              _tmpSex = _cursor.getString(_cursorIndexOfSex);
            }
            final String _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getString(_cursorIndexOfHeight);
            }
            final String _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getString(_cursorIndexOfWeight);
            }
            final String _tmpIdentityCard;
            if (_cursor.isNull(_cursorIndexOfIdentityCard)) {
              _tmpIdentityCard = null;
            } else {
              _tmpIdentityCard = _cursor.getString(_cursorIndexOfIdentityCard);
            }
            final String _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getString(_cursorIndexOfBirthday);
            }
            final String _tmpAge;
            if (_cursor.isNull(_cursorIndexOfAge)) {
              _tmpAge = null;
            } else {
              _tmpAge = _cursor.getString(_cursorIndexOfAge);
            }
            final String _tmpBMI;
            if (_cursor.isNull(_cursorIndexOfBMI)) {
              _tmpBMI = null;
            } else {
              _tmpBMI = _cursor.getString(_cursorIndexOfBMI);
            }
            final String _tmpMedicalRecordNumber;
            if (_cursor.isNull(_cursorIndexOfMedicalRecordNumber)) {
              _tmpMedicalRecordNumber = null;
            } else {
              _tmpMedicalRecordNumber = _cursor.getString(_cursorIndexOfMedicalRecordNumber);
            }
            final String _tmpPredictDistances;
            if (_cursor.isNull(_cursorIndexOfPredictDistances)) {
              _tmpPredictDistances = null;
            } else {
              _tmpPredictDistances = _cursor.getString(_cursorIndexOfPredictDistances);
            }
            final String _tmpDiseaseHistory;
            if (_cursor.isNull(_cursorIndexOfDiseaseHistory)) {
              _tmpDiseaseHistory = null;
            } else {
              _tmpDiseaseHistory = _cursor.getString(_cursorIndexOfDiseaseHistory);
            }
            final String _tmpCurrentMedications;
            if (_cursor.isNull(_cursorIndexOfCurrentMedications)) {
              _tmpCurrentMedications = null;
            } else {
              _tmpCurrentMedications = _cursor.getString(_cursorIndexOfCurrentMedications);
            }
            final String _tmpClinicalDiagnosis;
            if (_cursor.isNull(_cursorIndexOfClinicalDiagnosis)) {
              _tmpClinicalDiagnosis = null;
            } else {
              _tmpClinicalDiagnosis = _cursor.getString(_cursorIndexOfClinicalDiagnosis);
            }
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            _item = new PatientBean(_tmpPatientId,_tmpName,_tmpSex,_tmpHeight,_tmpWeight,_tmpIdentityCard,_tmpBirthday,_tmpAge,_tmpBMI,_tmpMedicalRecordNumber,_tmpPredictDistances,_tmpDiseaseHistory,_tmpCurrentMedications,_tmpClinicalDiagnosis,_tmpRemark);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
