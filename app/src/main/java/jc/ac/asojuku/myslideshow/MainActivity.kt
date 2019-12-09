package jc.ac.asojuku.myslideshow

import android.media.AsyncPlayer
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //MediaPlayer用の変数(プロパティ)
    private lateinit var player: MediaPlayer;

    //pagerのを操作するために必要なアダプタークラスを内部クラスとして用意する
    class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        //アダプターに画像の情報を保持しておく
        //画像のリソースIDのリストを作る
        /**ここでは固定で定義しているが、データベースなどから読み込むのが普通**/
        private val resources = listOf(
            R.drawable.slide00,R.drawable.slide01,
            R.drawable.slide02,R.drawable.slide03,
            R.drawable.slide04,R.drawable.slide05,
            R.drawable.slide06,R.drawable.slide07,
            R.drawable.slide08,R.drawable.slide09
        );

        //指定されたページ番号(position)に相当するページのインスタンスを返す
        //ページが、今回はページのフラグメントのインスタンス
        override fun getItem(position:Int): Fragment {
            //ページ番号からリソースIDを指定して引き渡してImageFragmentのインスタンスを生成、リターン
            return ImageFragment.newInstance(resources[position]);
        }

        //アダプターが保持している全データ数(要素数)を返す
        override fun getCount():Int{
            return resources.size; //resourcesリストの要素数
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //画像クラスに配置されたPagerViewのadapterプロパティに
        //このクラスの内部クラスで定義したMyAdaperインスタンスを設定する
        this.pager.adapter = MyAdapter(this.supportFragmentManager);
        /** 画面のインスタンスが生成されると、タイマーのスレッドも起動させる **/
        //Handlerのインスタンスを取得
        val handler = Handler();
        //timer処理のスレッドを起こす
        // (5000ミリ秒間隔でタイマーマスクと呼ばれる処理をする)
        kotlin.concurrent.timer(period = 5000){
            //定期的に実行したい処理(TimerTask)
            //handlerでページャーを進める処理を実行させる
            handler.post(
                object:Runnable{
                    //実行するrunメソッド
                    override fun run() {
                        //メインスレッドの中で実行する処理
                        //ページャーのcurrentItem(ページ番号を１つ進める。10を超えたら0に戻る)
                        pager.currentItem = (pager.currentItem + 1) % 10;
                    }
                }
            )
        }
        //MediaPlayerの変数にインスタンスを設定する
        //第一引数:自画面のインスタンス、第二引数：サウンドファイル
        this.player = MediaPlayer.create(this,R.raw.getdown);
        //BGMとしてループ再生をオンにする
        this.player.isLooping = true;
    }

    override fun onResume() {
        super.onResume()
        //playerのサウンドファイル再生をスタート
        this.player.start();
    }
    //画面が非表示になるときのイベントコールバックメソッド
    override fun onPause() {
        super.onPause()
        //playerのサウンドファイル再生をストップ
        this.player.pause() //一時停止
    }
}
