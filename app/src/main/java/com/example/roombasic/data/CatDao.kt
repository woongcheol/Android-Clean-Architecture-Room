package com.example.roombasic.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.roombasic.domain.entity.Cat

@Dao
interface CatDao {
    @Query("SELECT * FROM cat")
    fun getALL(): List<Cat>

    @Insert(onConflict = REPLACE) // PrimaryKey 중복 시, 덮어쓰기
    fun insert(cat: Cat)

    @Query("DELETE from cat")
    fun deleteALL()
}