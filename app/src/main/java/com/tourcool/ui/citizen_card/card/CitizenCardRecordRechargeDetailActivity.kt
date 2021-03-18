package com.tourcool.ui.citizen_card.card

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.DoubleUtil
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.citizen_card.RechargeRecord
import com.tourcool.bean.citizen_card.RefundResult
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.DateUtil
import com.tourcool.core.widget.CommonRadiusDialog
import com.tourcool.event.account.CardInfoEvent
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.mvp.account.LoginActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_citizen_card_cost_detail.*
import kotlinx.android.synthetic.main.activity_citizen_card_cost_detail.tvOrderMoney
import kotlinx.android.synthetic.main.activity_citizen_card_cost_detail.tvOrderNum
import kotlinx.android.synthetic.main.activity_citizen_recharge_detail.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 *@description : 充值详情页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2021年03月03日17:48
 * @Email: 971613168@qq.com
 */
class CitizenCardRecordRechargeDetailActivity : BaseCommonTitleActivity() {
    private var detail: RechargeRecord? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_recharge_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        detail = intent.getParcelableExtra("recharge_detail")
        if (detail == null) {
            ToastUtil.show("未获取到消费记录")
            finish()
        }
        tvRefund.setOnClickListener {
            requestRechargeRefund(detail!!)
        }
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("消费详情")
    }

    override fun loadData() {
        super.loadData()
        if (!AccountHelper.getInstance().isLogin) {
            FrameUtil.startActivity(mContext, LoginActivity::class.java)
            finish()
            return
        }
        showDetail(detail!!)
    }


    private fun showDetail(detail: RechargeRecord) {
        if (StringUtil.getNotNullValue(detail.tradeNo).length >= 20) {
            detail.tradeNo = detail.tradeNo.substring(1)
        }
        var amount = ""
        try {
            amount = StringUtil.getNotNullValueLine(detail.amount)
            detail.amountFen = DoubleUtil.doubleFormatString1(amount.toDouble())
            amount = DoubleUtil.doubleFormatString(amount.toDouble() / 100)
        } catch (e: NumberFormatException) {
            amount = "0.00"
        }
        detail.amount = amount
        tvOrderNum?.text = StringUtil.getNotNullValueLine(detail.tradeNo)
        tvOrderMoney?.text = StringUtil.getNotNullValueLine(detail.amount + "元")
        val date = DateUtil.getDate(detail.date)
        val time = DateUtil.getDate(detail.time)
        val dateStr = DateUtil.formatDate1(date) + " " + DateUtil.formatTime(time)
        if (date != null) {
            etDate?.text = dateStr
        } else {
            etDate?.text = ""
        }

        tvTransContent?.text = StringUtil.getNotNullValueLine("市名卡充值")
        val userInfo = AccountHelper.getInstance().userInfo
        var cardNum = "-"
        if (!TextUtils.isEmpty(userInfo.citizenCardMaterialNo)) {
            cardNum = userInfo.citizenCardMaterialNo
        } else if (!TextUtils.isEmpty(userInfo.citizenCardVirtualNo)) {
            cardNum = userInfo.citizenCardVirtualNo
        }
        tvCitizenCardNum?.text = cardNum
//        etDate?.text = StringUtil.getNotNullValueLine(detail.date)
    }

    private fun requestRechargeRefund(info: RechargeRecord) {
        val params: MutableMap<String, Any> = HashMap(5)
        params["cardNo"] = info.cardNo
        if (TextUtils.isEmpty(info.amountFen)) {
            ToastUtil.show("当前金额无效")
            return
        }
        params["amount"] = info.amountFen
        params["actionNo"] = info.actionNo
        params["tradeNo"] = info.tradeNo
        params["tradeDate"] = info.date
        ApiRepository.getInstance().requestRechargeRefund(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<RefundResult>?>() {
            override fun onRequestNext(entity: BaseResult<RefundResult>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
//                    aliPay(entity.data.aliPayInfo.toString())
                    doCloseCardCallback(entity.errorMsg)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable) {
                TourCooLogUtil.e(TAG, e.toString())
            }
        })
    }


    private fun doCloseCardCallback(message: String) {
        val dialog = CommonRadiusDialog(mContext)
        dialog.setNegativeButton("确认") {
            ToastUtil.showSuccess(message)
            EventBus.getDefault().postSticky(CardInfoEvent())
            setResult(Activity.RESULT_OK)
            baseHandler.postDelayed(Runnable {
                finish()
            }, 500)
        }.setMsg("已发起退款，退款状态可去\n" +
                "充值记录中查看，退款成功后\n" +
                "金额会返还到原充值账户。", Gravity.CENTER)
                .setCancelable(false).setCanceledOnTouchOutside(false).show()
    }

}