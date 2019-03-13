package luyao.box.ui.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.common.util.DeviceUtils

/**
 * Created by luyao
 * on 2019/1/25 16:17
 */
class DeviceInfoActivity : BaseActivity() {

    private val permission = Manifest.permission.READ_PHONE_STATE


    override fun getLayoutResId() = R.layout.activity_device_info

    override fun initView() {
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
//        os.text = DeviceUtils.getKernelVersion()
        macAddress.text = DeviceUtils.getMacAddress()

        screen.text=String.format("%s * %s", DeviceUtils.getDeviceWidth(this), DeviceUtils.getDeviceHeight(this))
        ram.text=DeviceUtils.getRamInfo(this)
        rom.text=DeviceUtils.getRomInfo(this)


    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1001)
        } else {
            imei.text = DeviceUtils.getIMEI(this)
            meid.text = DeviceUtils.getMEID(this)
            sn.text = DeviceUtils.getSN()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            imei.text = DeviceUtils.getIMEI(this)
            meid.text = DeviceUtils.getMEID(this)
            sn.text = DeviceUtils.getSN()
        }
    }
}