package luyao.box.about

import android.os.Build
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.model.Notice
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.*
import luyao.util.ktx.base.BaseActivity
import luyao.util.ktx.ext.openBrowser

/**
 * Created by luyao
 * on 2019/1/18 13:30
 */
class AboutActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_about

    override fun initView() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = getString(R.string.about)
    }

    override fun initData() {
        license.setOnClickListener { showOwnLicense() }
        source.setOnClickListener { openBrowser(GITHUB_PAGE) }
        feedback.setOnClickListener { showFeedBackMenu() }
        thirdLib.setOnClickListener { showLicenseDialog() }
        developer.setOnClickListener { openBrowser(HOME_PAGE) }
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
                    openBrowser(ISSUE_URL)
                }
                R.id.menu_email -> {

                    sendEmail(
                        getString(R.string.sendto),
                        getString(R.string.mail_topic),
                        getString(R.string.device_model) + Build.MODEL + "\n"
                                + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                + getString(R.string.version)
                    )

                }
            }
            true
        }
        feedbackMenu.show()
    }
}