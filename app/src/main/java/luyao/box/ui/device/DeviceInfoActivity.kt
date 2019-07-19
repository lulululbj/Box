package luyao.box.ui.device

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.util.DeviceUtils
import luyao.util.ktx.base.BaseActivity
import luyao.util.ktx.ext.permission.request

/**
 * Created by luyao
 * on 2019/1/25 16:17
 */
class DeviceInfoActivity : BaseActivity() {

    private val permission = Manifest.permission.READ_PHONE_STATE


    override fun getLayoutResId() = R.layout.activity_device_info

    override fun initView() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = getString(R.string.device_ifo)
    }

    override fun initData() {
        checkPermissions()
        setDeviceInfo()
    }

    private fun setDeviceInfo() {
        brand.text = DeviceUtils.getBrand()
        display.text = DeviceUtils.getDisplay()
        product.text = DeviceUtils.getProduct()
        device.text = DeviceUtils.getDevice()
        board.text = DeviceUtils.getBoard()
        model.text = DeviceUtils.getModel()
        manufacturer.text = DeviceUtils.getManufacturer()
        bootloader.text = DeviceUtils.getBootloader()
        hardware.text = DeviceUtils.getHardware()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis.text = DeviceUtils.getSupportedAbis()!!.contentToString()
        }
        radioVersion.text = Build.getRadioVersion()
        api.text = DeviceUtils.getSDK().toString()
        version.text = DeviceUtils.getVersion()
        os.text = DeviceUtils.getKernelVersion()
        macAddress.text = DeviceUtils.getMacAddress()

        screen.text = String.format("%s * %s", DeviceUtils.getDeviceWidth(this), DeviceUtils.getDeviceHeight(this))
        ram.text = DeviceUtils.getRamInfo(this)
        rom.text = DeviceUtils.getRomInfo(this)


    }

    @SuppressLint("MissingPermission")
    private fun checkPermissions() {

        request(permission) {
            onGranted {
                imei.text = DeviceUtils.getIMEI(this@DeviceInfoActivity)
                meid.text = DeviceUtils.getMEID(this@DeviceInfoActivity)
                sn.text = DeviceUtils.getSN()
            }
        }
    }
}