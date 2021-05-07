package mx.ine.demo.RoomData.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mx.ine.demo.RoomData.entities.UserInfo;

@Dao
public interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfo userInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserInfo> userInfo);

    @Update
    void update(List<UserInfo> userInfo);

    @Delete
    void delete(UserInfo userInfo);

    @Delete
    void delete(List<UserInfo> userInfo);

    @Query("SELECT * FROM userInfo")
    List<UserInfo> getAll();

}
