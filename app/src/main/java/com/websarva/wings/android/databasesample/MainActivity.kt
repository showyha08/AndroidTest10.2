package com.websarva.wings.android.databasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var _cocktailId = -1
    private var _cocktailName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //カクテルリスト用ListViewを取得
        val lvCocktail = findViewById<ListView>(R.id.lvCocktail)
        //カクテルリスト用ListViewにリスナを設定
        lvCocktail.onItemClickListener = ListItemClickListner()
    }

    // 保存ボタンがタップされたときの処理メソッド
    fun onSaveButtonClick(view: View){
        //感想欄を取得
        val etNote = findViewById<EditText>(R.id.etNote)
        //感想欄の入力値を消去
        etNote.setText("")
        //カクテル名を表示するTextViewを取得
        val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
        //カクテル名を表示するTextViewに「未選択」を設定
        tvCocktailName.text = getString(R.string.tv_name)
        //保存ボタンを取得
        val btnSave = findViewById<View>(R.id.btnSave)
        //保存ボタンをタップできないように設定
        btnSave.isEnabled = false
    }

    // リストがタップされた時の処理が記述されたメンバクラス
    private inner class ListItemClickListner : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view:View,position:Int,id:Long) {
            //タップされた行番号をプロパティの主キーIDに代入
            _cocktailId = position
            //タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入
            _cocktailName = parent.getItemAtPosition(position) as String
            //カクテル名を表示するTextViewを取得
            val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
            tvCocktailName.text = _cocktailName
            //保存ボタンを取得
            val btnSave = findViewById<View>(R.id.btnSave)
            //保存ボタンをタップできるように設定
            btnSave.isEnabled = true
        }
    }
}