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
    private val _helper = DatabaseHelper(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //カクテルリスト用ListViewを取得
        val lvCocktail = findViewById<ListView>(R.id.lvCocktail)
        //カクテルリスト用ListViewにリスナを設定
        lvCocktail.onItemClickListener = ListItemClickListner()
    }

    // 保存ボタンがタップされたときの処理メソッド
    fun onSaveButtonClick(view: View) {
        //感想欄を取得
        val etNote = findViewById<EditText>(R.id.etNote)

        //入力された感想を取得
        val note = etNote.text.toString()

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        // まず、リストで選択されたカクテルのメモデータを削除する
        // 削除用SQL文字列を用意
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"

        // SQL文字列を元にプリペアドステートメントを取得
        var stmt = db.compileStatement(sqlDelete)

        // 変数のバインド
        stmt.bindLong(1, _cocktailId.toLong())

        // 削除SQLの実行
        stmt.executeUpdateDelete()

        // インサート用SQL文字列の用意
        val sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)"

        // SQL文字列を元にプリペアドステートメントを取得
        stmt = db.compileStatement(sqlInsert)

        // 変数のバインド
        stmt.bindLong(1, _cocktailId.toLong())
        stmt.bindString(2, _cocktailName)
        stmt.bindString(3, note)

        // インサートSQLの実行
        stmt.executeInsert()

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
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
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

            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            val db = _helper.writableDatabase

            //主キーによる検索カクテルのメモデータを取得するSQL文字列を用意
            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"

            //SQLの実行
            val cursor = db.rawQuery(sql, null)

            //データベースから取得した値を格納する変数を用意。なかった場合のため初期値は空文字列
            var note = ""

            //SQL実行の戻り値であるカーソルオブジェクトをループさせてDB内のデータベースを取得
            while (cursor.moveToNext()) {
                //カラムのインデックス値を取得
                val idxNote = cursor.getColumnIndex("note")
                //カラムのインデックス値を元に実際のデータを取得
                note = cursor.getString(idxNote)
            }

            //感想のEditTextの各画面部品を取得しデータベースの値を反映
            val etNote = findViewById<EditText>(R.id.etNote)
            etNote.setText(note)

        }
    }

    override fun onDestroy() {
        // ヘルパーオブジェクトの解放
        _helper.close()
        super.onDestroy()
    }
}