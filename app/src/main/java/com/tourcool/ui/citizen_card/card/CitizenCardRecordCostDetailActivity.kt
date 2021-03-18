package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import android.text.TextUtils
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.citizen_card.CostRecordInfo
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.mvp.account.LoginActivity
import kotlinx.android.synthetic.main.activity_citizen_card_cost_detail.*

/**
 *@description : 消费记录
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2021年03月01日15:10
 * @Email: 971613168@qq.com
 */
class CitizenCardRecordCostDetailActivity : BaseCommonTitleActivity() {

    private var detail: CostRecordInfo? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_card_cost_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        detail = intent.getParcelableExtra("cost_detail")

        if (detail == null) {
            ToastUtil.show("未获取到消费记录")
            finish()
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


    private fun showDetail(detail: CostRecordInfo) {
        tvOrderNum?.text = StringUtil.getNotNullValueLine(detail.orderNumber)
        tvOrderMoney?.text = StringUtil.getNotNullValueLine(detail.basePrice + "元")
        etDate?.text = StringUtil.getNotNullValueLine(detail.date)
        tvTransContent?.text = StringUtil.getNotNullValueLine("公交乘车")
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
}