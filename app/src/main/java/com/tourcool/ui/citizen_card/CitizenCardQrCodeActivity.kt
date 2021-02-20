package com.tourcool.ui.citizen_card

import android.Manifest
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocation
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.*
import com.frame.library.core.widget.dialog.FrameLoadingDialog
import com.frame.library.core.widget.titlebar.TitleBarView
import com.king.zxing.util.CodeUtils
import com.tourcool.bean.citizen_card.CitizenAccountInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.citizen_card.card.LocateUtil
import com.tourcool.util.DeviceUtils
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_citizen_card_qrcode_info.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 *@description : 二维码
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日10:43
 * @Email: 971613168@qq.com
 */
class CitizenCardQrCodeActivity : BaseTitleTransparentActivity(),View.OnClickListener {
    private var dialog: FrameLoadingDialog? = null
    private val defaultCode = "宜兴消费码"
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_card_qrcode_info
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("消费码")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvRefresh.setOnClickListener(this)
        ivQrCode.setImageBitmap(CodeUtils.createQRCode(defaultCode, SizeUtil.dp2px(170f)))
        dialog = FrameLoadingDialog(mContext)
        requestCostCode()
    }

    private fun requestCostCode() {
        val perms = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            //说明当前有位置权限
            dialog?.setLoadingText("正在定位...")
            dialog?.show()
            LocateUtil.getInstance().startLocate {
                if (it != null) {
                    dialog?.dismiss()
                    if (it.errorCode == 0) {
                        //定位成功回调信息，设置相关消息
                        TourCooLogUtil.i("定位成功")
                        doRequestBusCode(it)
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        TourCooLogUtil.e("定位失败", "location Error, ErrCode:"
                                + it.getErrorCode() + ", errInfo:"
                                + it.getErrorInfo())
                    }
                }
                LocateUtil.getInstance().release()
            }
        } else {
            // 没有权限 需要申请权限
            EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, 1001, *perms)
                            .setRationale("乘车码需要获取您的位置信息")
                            .setNegativeButtonText("否")
                            .setPositiveButtonText("是")
                            .build()
            )
        }
    }

    override fun onDestroy() {
        LocateUtil.getInstance().release()
        super.onDestroy()
    }


    private fun doRequestBusCode(locationResult: AMapLocation) {
        val params: HashMap<String, Any> = HashMap()
        params["lat"] = "" + locationResult.latitude
        params["lng"] = "" + locationResult.longitude
        requestApplyBusCode(params)
    }


    private fun requestApplyBusCode(params: HashMap<String, Any>) {
        val versionName = FrameUtil.getVersionName(mContext)
        params.put("phonever", DeviceUtils.getPhoneDetail())
        params.put("appver", StringUtil.getVersionCode(versionName))
        ApiRepository.getInstance().requestApplyBusCode(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CitizenAccountInfo>>() {
            override fun onRequestNext(entity: BaseResult<CitizenAccountInfo>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showCostInfo(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }


    private fun showCostInfo(info: CitizenAccountInfo?) {
        var code = "智慧宜兴二维码消费"
        if (info != null) {
            code = StringUtil.getNotNullValueLine(info.qrcode)
        }
        val money: Double = try {
            (StringUtil.getNotNullValue(info?.balance).toDouble()) / 100.00
        } catch (e: NumberFormatException) {
            0.0
        }
        val accountMoney = "" + DoubleUtil.doubleFormatString(money)  + "元"
        tvCurrentMoney.text = accountMoney
        ivQrCode.setImageBitmap(CodeUtils.createQRCode(code, SizeUtil.dp2px(170f)))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvRefresh -> {
                requestCostCode()
            }
            else -> {
            }
        }
    }

}