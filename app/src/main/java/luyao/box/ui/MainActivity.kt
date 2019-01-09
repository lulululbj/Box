package luyao.box.ui

import android.Manifest
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.title_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luyao.box.APK_PATH
import luyao.box.BASE_PATH
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.ui.activity.CurrentActivity
import luyao.box.ui.appManager.AppListActivity
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    EasyPermissions.PermissionCallbacks {

    private val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)


    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {
        setSupportActionBar(mToolbar)

        fab.setOnClickListener {
            startActivity(AppListActivity::class.java)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, mToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        currentActivity.setOnClickListener { startActivity(CurrentActivity::class.java) }

        checkPermissions()
    }

    override fun initData() {
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun checkPermissions() {
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(this, "权限申请", 1001, *perms)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            createBaseFile()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    private fun createBaseFile() {
        CoroutineScope(Dispatchers.IO).launch {
            val baseFolder = File(BASE_PATH)
            if (!baseFolder.exists()) baseFolder.mkdirs()

            val apkFolder = File(APK_PATH)
            if (!apkFolder.exists()) apkFolder.mkdirs()
        }
    }
}
