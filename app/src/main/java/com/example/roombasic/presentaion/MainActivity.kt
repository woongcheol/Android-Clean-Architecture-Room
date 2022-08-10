package com.example.roombasic.presentaion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roombasic.domain.entity.Cat
import com.example.roombasic.R
import com.example.roombasic.data.CatDB
import com.example.roombasic.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var catDb : CatDB? = null
    private var catList = listOf<Cat>()
    lateinit var mAdapter : CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        catDb = CatDB.getInstance(this)
        mAdapter = CatAdapter(this, catList)

        // 데이터 읽고 쓸 때 쓰레드 사용
        val r = Runnable {
            try {
                catList = catDb?.catDao()?.getALL()!!
                mAdapter = CatAdapter(this, catList)
                mAdapter.notifyDataSetChanged()

                binding.mRecyclerView.adapter = mAdapter
                binding.mRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.mRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        binding.mAddBtn.setOnClickListener {
            Log.d("testt", "start")
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