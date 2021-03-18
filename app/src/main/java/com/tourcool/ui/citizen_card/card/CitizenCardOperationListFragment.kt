package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.account.UserInfo
import com.tourcool.bean.citizen_card.CardMaterialInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.widget.CommonRadiusDialog
import com.tourcool.smartcity.R
import com.tourcool.ui.citizen_card.CitizenCardBindActivity
import com.tourcool.ui.citizen_card.CitizenCardTabActivity
import com.tourcool.ui.mvp.account.LoginActivity
import com.trello.rxlifecycle3.android.FragmentEvent


/**
 *@description : 市名卡操作
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日17:17
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationListFragment : BaseFragment(), View.OnClickListener {
    private var llCardRecharge: LinearLayout? = null
    private var llCardUnbind: LinearLayout? = null
    private var llCardBind: LinearLayout? = null
    private var llCardCancel: LinearLayout? = null

    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_operation_function_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        llCardRecharge = mContentView.findViewById(R.id.llCardRecharge)
        llCardUnbind = mContentView.findViewById(R.id.llCardUnbind)
        llCardBind = mContentView.findViewById(R.id.llCardBind)
        llCardCancel = mContentView.findViewById(R.id.llCardCancel)
        llCardRecharge!!.setOnClickListener(this)
        llCardUnbind!!.setOnClickListener(this)
        llCardBind!!.setOnClickListener(this)
        llCardCancel!!.setOnClickListener(this)
        showItemByCard()
    }

    override fun loadData() {
        super.loadData()
        showItemByCard()
    }

    companion object {


        fun newInstance(): CitizenCardOperationListFragment {
            return CitizenCardOperationListFragment()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llCardRecharge -> {
                doShowRecharge()
            }
            /*    R.id.llCardUnbind -> {
                    val dialog = CommonRadiusDialog(mContext)
                    dialog.setNegativeButton("好的") {

                    }.setMsg("请您先去线下市民卡网点办\n" +
                            "理实体卡，再进行绑定操作").setTitle("无实体卡可绑定").setCancelable(false).setCanceledOnTouchOutside(false).show()
                }*/
            R.id.llCardBind -> {
                doBindCardMaterial()
            }
            R.id.llCardUnbind -> {
                doUnBindCardMaterial()
            }
            R.id.llCardCancel -> {
                doCloseCard()
            }

            else -> {

            }
        }
    }

    private fun showItemByCard() {
        val info = AccountHelper.getInstance().userInfo
        if (info == null) {
            FrameUtil.startActivity(mContext, LoginActivity::class.java)
            mContext?.finish()
            return
        }
        if (TextUtils.isEmpty(info.citizenCardMaterialNo)) {
            //说明用户没有实体卡
            setViewGone(llCardUnbind, false)
            setViewGone(llCardCancel, true)
            setViewGone(llCardBind, true)
        } else {
            setViewGone(llCardUnbind, true)
            setViewGone(llCardCancel, false)
            setViewGone(llCardBind, false)
        }
    }

    /**
     * 绑定实体卡
     */
    private fun doBindCardMaterial() {
        val dialog = CommonRadiusDialog(mContext)
        dialog.setNegativeButton("确认") {
            requestBindCitizenCardMaterial()
        }.setMsg("绑定实体卡后，虚拟卡账户中的余额将会自动转入实体卡账户中进行使用。", Gravity.CENTER)
                .setTitle("绑定提示").setCancelable(false).setCanceledOnTouchOutside(false).show()
    }

    /**
     * 解绑实体卡
     */
    private fun doUnBindCardMaterial() {
        if (TextUtils.isEmpty(AccountHelper.getInstance().userInfo.citizenCardMaterialNo)) {
            ToastUtil.show("您还未办理实体卡")
            return
        }
        val dialog = CommonRadiusDialog(mContext)
        dialog.setNegativeButton("确认") {
            requestUnbindCardMaterial()
        }.setMsg("实体卡解绑后，将无法在线上继续消费使用，但不影响线下消费使用，是否确认解绑？", Gravity.CENTER)
                .setTitle("解绑提示").setPositiveButton("取消") {
                    dialog.dismiss()
                }.setCancelable(false).setCanceledOnTouchOutside(false).show()
    }

    private fun doCloseCard() {
        if (TextUtils.isEmpty(AccountHelper.getInstance().userInfo.citizenCardMaterialNo) && TextUtils.isEmpty(AccountHelper.getInstance().userInfo.citizenCardVirtualNo)) {
            ToastUtil.show("您还未办理市名卡")
            return
        }
        val dialog = CommonRadiusDialog(mContext)
        dialog.setNegativeButton("确定") {
            requestCloseCard()
        }.setMsg(" 电子市民卡在线上注销后，请到线下市民卡网点进行办理退款，若无余额直接注销。", Gravity.CENTER)
                .setPositiveButton("取消") {
                    dialog.dismiss()
                }.setCancelable(false).setCanceledOnTouchOutside(false).show()
    }


    /**
     * 解绑实体卡
     */
    private fun requestUnbindCardMaterial() {
        ApiRepository.getInstance().requestUnbindCardMaterial().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo>>() {
            override fun onRequestNext(entity: BaseResult<UserInfo>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    handleUnbindCallback(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }


        })
    }

    private fun requestCloseCard() {
        ApiRepository.getInstance().requestCloseCard().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo>?>() {
            override fun onRequestNext(entity: BaseResult<UserInfo>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS&&entity.data !=null) {
                    ToastUtil.showSuccess(entity.errorMsg)
                    handleCloseCardCallback(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }


        })
    }

    private fun requestBindCitizenCardMaterial() {
        ApiRepository.getInstance().requestBindCitizenCardMaterial().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CardMaterialInfo>>() {
            override fun onRequestNext(entity: BaseResult<CardMaterialInfo>?) {
                if (entity?.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    ToastUtil.showSuccess(entity.errorMsg)
                    handleBindCallback(entity.data)
                } else {
                    showBindFailed(entity?.errorMsg)
                }
            }
        })
    }


    private fun showBindFailed(message: String?) {
        val dialog = CommonRadiusDialog(mContext)
        dialog.setMsg(StringUtil.getNotNullValueLine(message), Gravity.CENTER)
                .setTitle("无实体卡可绑定").setNegativeButton("好的") {
                    dialog.dismiss()
                }.setCancelable(false).setCanceledOnTouchOutside(false).show()
    }

    private fun handleUnbindCallback(userInfo: UserInfo) {
        AccountHelper.getInstance().saveUserInfoToDisk(userInfo)
        showItemByCard()
        if (TextUtils.isEmpty(userInfo.citizenCardVirtualNo)) {
            //说明用户只有实体卡 没有虚拟卡 则直接 返回
            FrameUtil.startActivity(mContext, CitizenCardBindActivity::class.java)
            mContext.finish()
        } else {
            //说明用户还存在虚拟卡 则不能直接退出 此时需要直接显示虚拟卡
            (mContext as CitizenCardTabActivity).updateCardNum(userInfo.citizenCardVirtualNo)
        }

    }


    private fun handleBindCallback(data: CardMaterialInfo) {
        AccountHelper.getInstance().userInfo.citizenCardMaterialNo = data.materialCardNum
        AccountHelper.getInstance().saveUserInfoToDisk(AccountHelper.getInstance().userInfo)
        showItemByCard()
        (mContext as CitizenCardTabActivity).updateCardNum(data.materialCardNum)
    }


    private fun handleCloseCardCallback(userInfo: UserInfo) {
        Handler().postDelayed(Runnable {
            AccountHelper.getInstance().saveUserInfoToDisk(userInfo)
            mContext.finish()
        }, 500)
    }

    fun doShowRecharge(){
        val fragment = parentFragment as CitizenCardOperationContainerFragment
        fragment.showFragmentRecharge()
    }
}