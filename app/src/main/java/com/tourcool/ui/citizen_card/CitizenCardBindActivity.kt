package com.tourcool.ui.citizen_card

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.citizen_card.CardMaterialInfo
import com.tourcool.bean.citizen_card.OpenCardVirtual
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.widget.CommonRadiusDialog
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.certify.SelectCertifyActivity
import com.tourcool.ui.mvp.account.LoginActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_citizen_card_enter.*

/**
 *@description : 市名卡绑定申请页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2021年02月07日15:48
 * @Email: 971613168@qq.com
 */
class CitizenCardBindActivity : BaseCommonTitleActivity(), View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_card_enter
    }

    override fun initView(savedInstanceState: Bundle?) {
        llCardApply.setOnClickListener(this)
        llCardBind.setOnClickListener(this)
    }

    override fun loadData() {
        super.loadData()
        if (!AccountHelper.getInstance().isLogin) {
            ToastUtil.show("当前登录已失效，请重新登录")
            skipLogin()
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llCardApply -> {
                doApplyCard()
            }
            R.id.llCardBind -> {
                getCardMaterialInfo()
            }
            else -> {
            }
        }
    }


    private fun showApplyCardVirtualDialog(listener: View.OnClickListener) {
        val userInfo = AccountHelper.getInstance().userInfo
        val dialog = CommonRadiusDialog(mContext)
        val info = StringBuffer()
        info.append("姓    名    ").append(userInfo.name).append("\n").append("证件号    ")
                .append(userInfo.idCard).append("\n").append("手机号    ").append(userInfo.phoneNumber)
        dialog.setNegativeButton("确认申请", listener).setMsg(info.toString())
                .setTitle("确认身份信息").setCancelable(false).setCanceledOnTouchOutside(false).show()
    }


    private fun doApplyCard() {
        if (!AccountHelper.getInstance().userInfo.isVerified) {
            val dialog = CommonRadiusDialog(mContext)
            val info = StringBuffer()
            info.append("您还未进行实名认证\n" + "请先完成实名认证")
            dialog.setNegativeButton("前往认证") {
                skipCertify()
            }.setMsg(info.toString())
                    .setTitle("").setCancelable(false).setCanceledOnTouchOutside(false).show()
            return
        }
        showApplyCardVirtualDialog(View.OnClickListener {
            requestApplyVirtualCard()
        })
    }

    private fun skipLogin() {
        val intent = Intent()
        intent.setClass(mContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun skipCertify() {
        val intent = Intent()
        intent.setClass(mContext, SelectCertifyActivity::class.java)
        startActivity(intent)
    }

    private fun requestApplyVirtualCard() {
        ApiRepository.getInstance().requestApplyVirtualCard().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<OpenCardVirtual>>() {
            override fun onRequestNext(entity: BaseResult<OpenCardVirtual>?) {
                if (entity?.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    AccountHelper.getInstance().userInfo.citizenCardVirtualNo = entity.data.cardno
                    skipCardTabAndSaveCard()
                } else {
                    ToastUtil.show(entity?.errorMsg)
                }
            }

        })


    }


    private fun getCardMaterialInfo() {
        ApiRepository.getInstance().requestCitizenCardMaterialInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CardMaterialInfo>>() {
            override fun onRequestNext(entity: BaseResult<CardMaterialInfo>?) {
                if (entity?.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showCardMaterialInfoDialog(entity.data)
                } else {
                    ToastUtil.show(entity?.errorMsg)
                }
            }
        })
    }

    private fun requestBindCitizenCardMaterial() {
        ApiRepository.getInstance().requestBindCitizenCardMaterial().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CardMaterialInfo>>() {
            override fun onRequestNext(entity: BaseResult<CardMaterialInfo>?) {
                if (entity?.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    ToastUtil.showSuccess(entity.errorMsg)
                    AccountHelper.getInstance().userInfo.citizenCardMaterialNo = entity.data.materialCardNum
                    skipCardTabAndSaveCard()
                } else {
                    ToastUtil.show(entity?.errorMsg)
                }
            }
        })
    }


    private fun showCardMaterialInfoDialog(cardInfo: CardMaterialInfo) {
        val dialog = CommonRadiusDialog(mContext)
        val info = StringBuffer()
        info.append("姓    名    ").append(cardInfo.name).append("\n").append("证件号    ")
                .append(cardInfo.idCardNum).append("\n").append("卡    号    ").append(cardInfo.materialCardNum)
        dialog.setNegativeButton("确认绑定") {
            requestBindCitizenCardMaterial()
        }.setMsg(info.toString())
                .setTitle("确认市名卡信息").setCancelable(false).setCanceledOnTouchOutside(false).show()
    }


    private fun skipCardTabAndSaveCard() {
        AccountHelper.getInstance().saveUserInfoToDisk(AccountHelper.getInstance().userInfo)
        FrameUtil.startActivity(mContext, CitizenCardTabActivity::class.java)
        finish()
    }
}