package mx.ine.demo.RoomData.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mx.ine.demo.RoomData.entities.UserInfoIdBack;

@Dao
public interface UserInfoIdBackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfoIdBack userInfoIdBack);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserInfoIdBack> userInfoIdBack);

    @Update
    void update(List<UserInfoIdBack> userInfoIdBack);

    @Delete
    void delete(UserInfoIdBack userInfoIdBack);

    @Delete
    void delete(List<UserInfoIdBack> userInfoIdBack);

    @Query("SELECT * FROM userInfoIdBack")
    List<UserInfoIdBack> getAll();
}
