package com.example.lasttry.classes;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;



import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM PIcId")
    List<PIcId> getAll();

    @Query("SELECT * FROM PIcId WHERE pic_Id IN (:userIds)")
    List<PIcId> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM PIcId WHERE group_Id LIKE :groupId AND " +
            "pic_Id LIKE :picId LIMIT 1")
    PIcId findById(String groupId, String picId);

    @Insert
    void insertAll(PIcId... users);

    @Delete
    void delete(PIcId user);
}