package com.tourcool.ui.citizen_card

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.amap.api.location.AMapLocation
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.retrofit.BaseObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.SizeUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.constellation.TabPagerAdapter
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.account.UserInfo
import com.tourcool.bean.citizen_card.CitizenAccountInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.certify.SelectCertifyActivity
import com.tourcool.ui.citizen_card.card.CitizenCardInfoFragment
import com.tourcool.ui.citizen_card.card.CitizenCardOperationContainerFragment
import com.tourcool.ui.citizen_card.card.LocateUtil
import com.tourcool.ui.mvp.account.LoginActivity
import com.tourcool.util.DeviceUtils
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_citizen_card_info_tab.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 *@description : 市名卡tab页
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日10:24
 * @Email: 971613168@qq.com
 */
class CitizenCardTabActivity : BaseTitleTransparentActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var commonNavigator: CommonNavigator
    private var mTabNameList = ArrayList<String>()
    private val mFragments = ArrayList<Fragment>()
    private var mChannelPagerAdapter: TabPagerAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_card_info_tab
    }

    override fun initView(savedInstanceState: Bundle?) {
        initFragment()
        initMagicIndicator()
        mChannelPagerAdapter = TabPagerAdapter(mFragments, supportFragmentManager)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mTabNameList.size + 1
        commonNavigator.notifyDataSetChanged()

        ivQrCode.setOnClickListener(View.OnClickListener {
           FrameUtil.startActivity(mContext,CitizenCardQrCodeActivity::class.java)
        })
    }

    private fun initFragment() {
        mFragments.clear()
        mFragments.add(CitizenCardInfoFragment.newInstance()!!)
        mTabNameList.add("市民卡信息")
        mFragments.add(CitizenCardDealRecordFragment.newInstance()!!)
        mTabNameList.add("市民卡交易")
        mFragments.add(CitizenCardOperationContainerFragment.newInstance()!!)
        mTabNameList.add("市民卡操作")
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("市民卡")
    }

    private fun initMagicIndicator() {
        commonNavigator = CommonNavigator(mContext)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTabNameList.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mTabNameList.get(index)
                clipPagerTitleView.textColor = FrameUtil.getColor(R.color.grayA2A2A2)
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.textSize = SizeUtil.sp2px(12f).toFloat()

                clipPagerTitleView.setOnClickListener {
                    mViewPager.currentItem = index
                }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight: Float = context.getResources().getDimension(R.dimen.common_navigator_height)
                val borderWidth: Float = SizeUtil.dp2px(1f).toFloat()
                val lineHeight = navigatorHeight - 2 * borderWidth
                indicator.lineHeight = lineHeight
                indicator.roundRadius = (lineHeight / 2)
                indicator.yOffset = borderWidth
                indicator.setColors(FrameUtil.getColor(R.color.colorPrimary))
                return indicator
            }
        }
        commonNavigator.isAdjustMode = true
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun requestApplyBusCode(params: HashMap<String, Any>) {
        val versionName = FrameUtil.getVersionName(mContext)
        params.put("phonever", DeviceUtils.getPhoneDetail())
        params.put("appver", StringUtil.getVersionCode(versionName))
        ApiRepository.getInstance().requestApplyBusCode(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CitizenAccountInfo>>() {
            override fun onRequestNext(entity: BaseResult<CitizenAccountInfo>) {
                TourCooLogUtil.i("CitizenCardTabActivity", entity)
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                TourCooLogUtil.i(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }


    override fun loadData() {
        super.loadData()
        if(!AccountHelper.getInstance().isLogin){
            skipLogin()
            return
        }
        if(!AccountHelper.getInstance().userInfo.isVerified){
            skipCertify()
            return
        }
        refreshUserInfo()
    }


    //Android 系统权限结果监听回调

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //调用easypermission结果监听返回
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        ToastUtil.showSuccess("成功获取了位置权限")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .setTitle("提示")
                    .setRationale("需要授予部分权限")
                    .setNegativeButton("拒绝")
                    .setPositiveButton("前往设置")
                    .setRequestCode(1001)
                    .build()
                    .show()
        }
    }

    private fun request() {
        val perms = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            //说明当前有位置权限
            showLoadingDialog("正在获取位置信息")
            LocateUtil.getInstance().startLocate {
                closeLoadingDialog()
                if (it != null) {
                    if (it.errorCode == 0) {
                        //定位成功回调信息，设置相关消息
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

    private fun showCardInfo() {
        val userInfo = AccountHelper.getInstance().userInfo
        tvName.text = StringUtil.getNotNullValue(userInfo.name)
        tvIdCard.text = StringUtil.getNotNullValue(userInfo.idCard)
        if (!TextUtils.isEmpty(userInfo.citizenCardMaterialNo)) {
            tvCardNum.text = StringUtil.getNotNullValue(userInfo.citizenCardMaterialNo)
        } else {
            tvCardNum.text = StringUtil.getNotNullValue(userInfo.citizenCardVirtualNo)
        }

    }

    private fun skipLogin() {
        val intent = Intent()
        intent.setClass(mContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun skipCertify() {
        val intent = Intent()
        intent.setClass(mContext, SelectCertifyActivity::class.java)
        startActivity(intent)
    }

    private fun refreshUserInfo() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseObserver<BaseResult<*>?>() {
            override fun onRequestNext(entity: BaseResult<*>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    val userInfo = parseJavaBean(entity.data, UserInfo::class.java) ?: return
                    AccountHelper.getInstance().saveUserInfoToDisk(userInfo)
                    showCardInfo()
                }
            }

            override fun onRequestError(e: Throwable) {
                TourCooLogUtil.e(TAG, e.toString())
            }
        })
    }
}