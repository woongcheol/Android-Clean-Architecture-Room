package com.example.roombasic.presentaion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.roombasic.domain.entity.Cat
import com.example.roombasic.R
import com.example.roombasic.data.CatDB
import com.example.roombasic.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private var catDb: CatDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catDb = CatDB.getInstance(this)

        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 새로운 cat 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
        val addRunnable = Runnable {
            val newCat = Cat()
            newCat.catName = binding.addName.text.toString()
            newCat.lifeSpan = binding.addLifeSpan.text.toString().toInt()
            newCat.origin = binding.addOrigin.text.toString()
            catDb?.catDao()?.insert(newCat)
        }

        binding.addBtn.setOnClickListener {
            Log.d("testt", "end")

            val addThread = Thread(addRunnable)
            addThread.start()

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    override fun onDestroy() {
        CatDB.destroyInstance()
        super.onDestroy()
    }
}