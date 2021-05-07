package mx.ine.demo.RoomData;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import mx.ine.demo.RoomData.Convert.Converter;
import mx.ine.demo.RoomData.dao.UserInfoDao;
import mx.ine.demo.RoomData.dao.UserInfoIdBackDao;
import mx.ine.demo.RoomData.entities.UserInfo;
import mx.ine.demo.RoomData.entities.UserInfoIdBack;

@TypeConverters(Converter.class)
@Database(version = 1, exportSchema = false, entities = {UserInfo.class, UserInfoIdBack.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "INETUM_DB";

    private static AppDatabase INSTANCE = null;

    abstract public UserInfoDao userInfoDao();
    abstract public UserInfoIdBackDao userInfoIdBackDao();

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).fallbackToDestructiveMigration().build();
        return INSTANCE;
    }
}
