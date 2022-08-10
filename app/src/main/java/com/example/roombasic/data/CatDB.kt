package com.example.roombasic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roombasic.domain.entity.Cat

@Database(entities = [Cat::class], version = 1)
abstract class CatDB : RoomDatabase() {
    abstract fun catDao(): CatDao

    companion object {
        private var INSTANCE: CatDB? = null

        // 인스턴스를 생성한다
        fun getInstance(context: Context): CatDB? {
            if (INSTANCE == null) {

                // 싱글톤 중복 생성 방지
                synchronized(CatDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CatDB::class.java, "cat.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        // 인스턴스를 반환한다
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}