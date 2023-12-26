package com.example.lab16_2

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>

    private val uri = Uri.parse("com.example.lab16")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listview).adapter = adapter

        setListener()
    }

    private fun setListener() {
        val inputbook = findViewById<EditText>(R.id.inputbook)
        val inputprice = findViewById<EditText>(R.id.inputprice)
        findViewById<Button>(R.id.btn_insert).setOnClickListener {
            val name = inputbook.text.toString()
            val price = inputprice.text.toString()

            if (name.isEmpty() || price.isEmpty())
                showToast("欄位請勿留空")
            else {
                val values = ContentValues()
                values.put("book", name)
                values.put("price", price)

                val contentUri = contentResolver.insert(uri, values)

                if (contentUri != null) {
                    showToast("新增:$name,價格:$price")
                    cleanEditText()
                } else
                    showToast("新增失敗")
            }
        }
        findViewById<Button>(R.id.btn_update).setOnClickListener {
            val name = inputbook.text.toString()
            val price = inputprice.text.toString()

            if (name.isEmpty() || price.isEmpty())
                showToast("欄位請勿留空")
            else {
                val values = ContentValues()
                values.put("price", price)

                val count = contentResolver.update(uri, values, name, null)

                if (count > 0) {
                    showToast("更新:$name,價格:$price")
                    cleanEditText()
                } else
                    showToast("更新失敗")
            }
        }
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            val name = inputbook.text.toString()

            if (name.isEmpty())
                showToast("書名請勿留空")
            else {

                val count = contentResolver.delete(uri, name, null)

                if (count > 0) {
                    showToast("刪除:${name}")
                    cleanEditText()
                } else
                    showToast("刪除失敗")
            }
        }
        findViewById<Button>(R.id.btn_query).setOnClickListener {
            val name = inputbook.text.toString()

            val selection = if (name.isEmpty()) null else name

            val c = contentResolver.query(uri, null, selection, null, null)
            c ?: return@setOnClickListener
            c.moveToFirst()
            items.clear()
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                //加入新資料
                items.add("書名:${c.getString(0)}\t\t\t\t 價格:${c.getInt(1)}")
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()

    private fun cleanEditText() {
        findViewById<EditText>(R.id.inputbook).setText("")
        findViewById<EditText>(R.id.inputprice).setText("")
    }
}