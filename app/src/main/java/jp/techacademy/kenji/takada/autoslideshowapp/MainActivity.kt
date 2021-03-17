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
import android.os.Handler
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    //    Permission用　Field変数
    private val PERMISSIONS_REQUEST_CODE = 100

    //   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
    var cursor: Cursor? = null

    // Timer用の時間のための変数
    private var mTimer: Timer? = null
    //    import要

    // Timer用の時間のための変数
    private var mTimerSec = 0.0

    private var mHandler = Handler()
    //    import要


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
                // Android 5系以下の場合
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE )
            }
        }
        // Permission設定　END＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

//      戻る　Button　Click　Listener
        back_button.setOnClickListener {
            //戻る実行 Method　使用
            getPreviousInfo()
        }

//      次へ　Button　Click　Listener
        next_button.setOnClickListener {
            //次へ実行 Method　使用
            getNextInfo()
        }

//      再生　停止　Button　Click　Listener
        play_stop_button.setOnClickListener {

            if (play_stop_button.text == "再生") {
                play_stop_button.text = "停止"
                timerPlay()
                back_button.setEnabled(false);
                next_button.setEnabled(false);
            } else {
                play_stop_button.text = "再生"
                timerPlayStop()
                back_button.setEnabled(true);
                next_button.setEnabled(true);
            }

        }


    }
    //    onCreate END


    //    Permission関数
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                    getPreviousInfo()
                    getNextInfo()
                } else {
                    getContentsInfo()
                    getPreviousInfo()
                    getNextInfo()
                }
        }
    }
    //    onRequestPermissionsResul END



    // Timerの始動　Method
    private fun timerPlay() {
        //        Slude Timerの作成
//        変数mTimer　に　Timer()　Method　を指定
        mTimer = Timer()

        // Timerの始動
        mTimer!!.schedule(object : TimerTask() {

//            val resolver = contentResolver
//
//            //   cursor は　Cursor型で宣言 ContentProviderのデータを参照するClass
//            var cursor = resolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, null
//            )

            override fun run() {
                mTimerSec += 0.1
                mHandler.post {

                    if (!cursor!!.moveToNext()) {
                        cursor!!.moveToFirst()
                    }

                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id)

                    //  val imageVIew = findViewById(R.id.imageView)
                    imageView.setImageURI(imageUri)

                }

            }
        }, 2000, 2000) // 最初に始動させるまで 100ミリ秒、ループの間隔を 100ミリ秒 に設定

    }

    //    Timerを止める　Method定義
    private fun timerPlayStop() {
        mTimer!!.cancel()
    }


    //    画像情報取得　Method
    private fun getContentsInfo() {


           //    画像取得用　 変数
            val resolver = contentResolver

            cursor= resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null
            )

            //    実験用
            Log.d("kotlintest", cursor.toString())


        //       検索結果の最初のData を指定している
        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }
    }
    //    getContentsInfo() END



    // 次の画像へ
  private fun getNextInfo() {

//            画像取得用　Field変数
        val resolver = contentResolver

//        cursor= resolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            null, null, null, null
//        )


        if (!cursor!!.moveToNext()) {
            cursor!!.moveToFirst();
        }

        //    実験用
        Log.d("kotlintest", cursor!!.toString())


//        cursor.moveToNext()
        // indexからIDを取得し、そのIDから画像のURIを取得する
        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        val id = cursor!!.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

        imageView.setImageURI(imageUri)
    }


    // 前の画像へ
    private fun getPreviousInfo() {

////            画像取得用　Field変数
//        val resolver = contentResolver
//
//        cursor= resolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            null, null, null, null
//        )

        //    実験用
        Log.d("kotlintest", cursor.toString())


        if (!cursor!!.moveToPrevious()) {
            cursor!!.moveToLast();
        }

        // indexからIDを取得し、そのIDから画像のURIを取得する
        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        val id = cursor!!.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

        imageView.setImageURI(imageUri)

    }

//    cursorを閉じるMethod 終了時自動実行
    override fun onStop() {
        super.onStop()
        cursor!!.close()
    }


}
//Class END


