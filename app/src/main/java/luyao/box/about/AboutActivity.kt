package luyao.box.about

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.model.Notice
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.GITHUB_PAGE
import luyao.box.HOME_PAGE
import luyao.box.ISSUE_URL
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.common.util.AppUtils

/**
 * Created by luyao
 * on 2019/1/18 13:30
 */
class AboutActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_about

    override fun initView() {
        mToolbar.title = getString(R.string.about)
    }

    override fun initData() {
        license.setOnClickListener { showOwnLicense() }
        source.setOnClickListener { AppUtils.openBrowser(this, GITHUB_PAGE) }
        feedback.setOnClickListener { showFeedBackMenu() }
        thirdLib.setOnClickListener { showLicenseDialog() }
        developer.setOnClickListener { AppUtils.openBrowser(this, HOME_PAGE)}
    }

    private fun showOwnLicense() {
        val license = ApacheSoftwareLicense20()
        val notice = Notice(getString(R.string.app_name), GITHUB_PAGE, "", license)
        LicensesDialog.Builder(this)
            .setNotices(notice)
            .build()
            .show()
    }

    private fun showLicenseDialog() {
        LicensesDialog.Builder(this)
            .setNotices(R.raw.licenses)
            .build()
            .show()
    }

    private fun showFeedBackMenu() {
        val feedbackMenu = PopupMenu(this, feedback, Gravity.RIGHT)
        feedbackMenu.menuInflater.inflate(R.menu.menu_feedback, feedbackMenu.menu)
        feedbackMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_issue -> {
                    AppUtils.openBrowser(this, ISSUE_URL)
                    true
                }
                R.id.menu_email -> {
                    val uri = Uri.parse(getString(R.string.sendto))
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic))
                    intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.device_model) + Build.MODEL + "\n"
                                + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                + getString(R.string.version))
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }
        feedbackMenu.show()
    }
}