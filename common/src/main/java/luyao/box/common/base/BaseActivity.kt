package luyao.box.common.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.common.R

/**
 * Created by luyao
 * on 2018/12/29 10:05
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        initView()
        initData()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(this, z))
    }

    protected fun startActivity(z: Class<*>,name:String,extra:String) {
        startActivity(Intent(this, z).putExtra(name,extra))
    }
}