package com.example.roombasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var catDb : CatDB? = null
    private var catList = listOf<Cat>()
    lateinit var mAdapter : CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        val mAddBtn = findViewById<Button>(R.id.mAddBtn)


        catDb = CatDB.getInstance(this)
        mAdapter = CatAdapter(this, catList)

        // 데이터 읽고 쓸 때 쓰레드 사용
        val r = Runnable {
            try {
                catList = catDb?.catDao()?.getALL()!!
                mAdapter = CatAdapter(this, catList)
                mAdapter.notifyDataSetChanged()

                mRecyclerView.adapter = mAdapter
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        mAddBtn.setOnClickListener {
            val i = Intent(this, AddActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onDestroy() {
        CatDB.destroyInstance()
        catDb = null
        super.onDestroy()
    }
}