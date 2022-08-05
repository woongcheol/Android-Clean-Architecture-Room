package com.example.roombasic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.roombasic.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit private var binding: ActivityAddBinding
    private var catDb: CatDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        catDb = CatDB.getInstance(this)

        binding = ActivityAddBinding.inflate(layoutInflater)

        /* 새로운 cat 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
        val addRunnable = Runnable {
            val newCat = Cat()
            newCat.catName = binding.addName.text.toString()
            newCat.lifeSpan = binding.addLifeSpan.text.toString().toInt()
            newCat.origin = binding.addOrigin.text.toString()
            catDb?.catDao()?.insert(newCat)
        }

        binding.addBtn.setOnClickListener {
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