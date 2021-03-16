package jp.techacademy.kenji.takada.autoslideshowapp

import android.Manifest
import android.content.ContentUris;
import android.content.pm.PackageManager;

//Cursor型で定義
import android.database.Cursor
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kotlinx.android.synthetic.main.activity_main.*
//import android.content.ContentResolver
//import android.support.v4.app.SupportActivity
//import android.support.v4.app.SupportActivity.ExtraData
//import android.support.v4.content.ContextCompat.getSystemService
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import android.net.nsd.NsdManager


class MainActivity : AppCompatActivity() {

//    Permission用　Field変数
    private val PERMISSIONS_REQUEST_CODE = 100


//    画像取得用　Field変数
//   contentResolver は　 ContentProviderのデータを参照するClass
    val resolver = contentResolver

//   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
//    abstract val cursor : Cursor

    var cursor : Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Permission設定＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()

        }

        back_button.setOnClickListener {
            //戻る実行 Method　使用
            getPreviousInfo()
        }

        next_button.setOnClickListener {
            //次へ実行 Method　使用
            getNextInfo()
        }


    }
    //    onCreate END

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()

                }
        }
    }
    //    onRequestPermissionsResul END



    //    画像情報取得　Method
    private fun getContentsInfo() {

       //   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
        var cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )

        //       検索結果の最初のData を指定している
        if (cursor!!.moveToFirst()) {

    //          画像のURIを順番に取得
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } while (cursor.moveToNext())
        }
    //        cursor.close()
    }
    //    getContentsInfo() END



    // 次の画像へ
    private fun getNextInfo() {

        //   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
        var cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )

        if (!cursor.moveToNext()) {
            cursor.moveToFirst()
        }

        // indexからIDを取得し、そのIDから画像のURIを取得する
        val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val id = cursor.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id!!)

//        val imageVIew = findViewById(R.id.imageView)
        imageView.setImageURI(imageUri)
    }



    // 前の画像へ
    private fun getPreviousInfo() {

        //   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
        var cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )

        if (!cursor.moveToPrevious()) {
            cursor.moveToLast()
        }

        // indexからIDを取得し、そのIDから画像のURIを取得する
        val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val id = cursor.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id!!)

//        val imageVIew = findViewById(R.id.imageView)
        imageView.setImageURI(imageUri)
    }




}
//Class END