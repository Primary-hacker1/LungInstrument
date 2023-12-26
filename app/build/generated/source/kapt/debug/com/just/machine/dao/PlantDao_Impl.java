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

  private final EntityInsertionAdapter<Plant> __insertionAdapterOfPlant;

  public PlantDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlant = new EntityInsertionAdapter<Plant>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `plants` (`id`,`name`,`description`,`growZoneNumber`,`wateringInterval`,`imageUrl`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Plant value) {
        if (value.getPlantId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPlantId());
        }
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        stmt.bindLong(4, value.getGrowZoneNumber());
        stmt.bindLong(5, value.getWateringInterval());
        if (value.getImageUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageUrl());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<Plant> plants, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlant.insert(plants);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Plant>> getPlants() {
    final String _sql = "SELECT * FROM plants ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"plants"}, new Callable<List<Plant>>() {
      @Override
      public List<Plant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlantId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGrowZoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "growZoneNumber");
          final int _cursorIndexOfWateringInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringInterval");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final List<Plant> _result = new ArrayList<Plant>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Plant _item;
            final String _tmpPlantId;
            if (_cursor.isNull(_cursorIndexOfPlantId)) {
              _tmpPlantId = null;
            } else {
              _tmpPlantId = _cursor.getString(_cursorIndexOfPlantId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpGrowZoneNumber;
            _tmpGrowZoneNumber = _cursor.getInt(_cursorIndexOfGrowZoneNumber);
            final int _tmpWateringInterval;
            _tmpWateringInterval = _cursor.getInt(_cursorIndexOfWateringInterval);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            _item = new Plant(_tmpPlantId,_tmpName,_tmpDescription,_tmpGrowZoneNumber,_tmpWateringInterval,_tmpImageUrl);
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
  public Flow<List<Plant>> getPlantsWithGrowZoneNumber(final int growZoneNumber) {
    final String _sql = "SELECT * FROM plants WHERE growZoneNumber = ? ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, growZoneNumber);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"plants"}, new Callable<List<Plant>>() {
      @Override
      public List<Plant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlantId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGrowZoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "growZoneNumber");
          final int _cursorIndexOfWateringInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringInterval");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final List<Plant> _result = new ArrayList<Plant>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Plant _item;
            final String _tmpPlantId;
            if (_cursor.isNull(_cursorIndexOfPlantId)) {
              _tmpPlantId = null;
            } else {
              _tmpPlantId = _cursor.getString(_cursorIndexOfPlantId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpGrowZoneNumber;
            _tmpGrowZoneNumber = _cursor.getInt(_cursorIndexOfGrowZoneNumber);
            final int _tmpWateringInterval;
            _tmpWateringInterval = _cursor.getInt(_cursorIndexOfWateringInterval);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            _item = new Plant(_tmpPlantId,_tmpName,_tmpDescription,_tmpGrowZoneNumber,_tmpWateringInterval,_tmpImageUrl);
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
  public Flow<Plant> getPlant(final String plantId) {
    final String _sql = "SELECT * FROM plants WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (plantId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plantId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"plants"}, new Callable<Plant>() {
      @Override
      public Plant call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlantId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGrowZoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "growZoneNumber");
          final int _cursorIndexOfWateringInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringInterval");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final Plant _result;
          if(_cursor.moveToFirst()) {
            final String _tmpPlantId;
            if (_cursor.isNull(_cursorIndexOfPlantId)) {
              _tmpPlantId = null;
            } else {
              _tmpPlantId = _cursor.getString(_cursorIndexOfPlantId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpGrowZoneNumber;
            _tmpGrowZoneNumber = _cursor.getInt(_cursorIndexOfGrowZoneNumber);
            final int _tmpWateringInterval;
            _tmpWateringInterval = _cursor.getInt(_cursorIndexOfWateringInterval);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            _result = new Plant(_tmpPlantId,_tmpName,_tmpDescription,_tmpGrowZoneNumber,_tmpWateringInterval,_tmpImageUrl);
          } else {
            _result = null;
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
