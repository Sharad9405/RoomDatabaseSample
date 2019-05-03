package com.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.roomdatabase.entity.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("SELECT * FROM word_table ORDER BY id ASC")
    LiveData<List<Word>> getAllWords();


    @Query("DELETE FROM word_table")
    void deleteAll();
}
