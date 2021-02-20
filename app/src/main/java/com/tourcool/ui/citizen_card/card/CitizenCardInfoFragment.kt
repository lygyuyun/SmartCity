package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseObserver
import com.frame.library.core.util.DoubleUtil
import com.frame.library.core.util.StringUtil
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.citizen_card.CardInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_citizen_card_info.*

/**
 *@description : 市名卡信息Fragment
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日16:42
 * @Email: 971613168@qq.com
 */
class CitizenCardInfoFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun loadData() {
        super.loadData()
        requestAccount()
    }
    companion object {


        fun newInstance(): CitizenCardInfoFragment? {
            return CitizenCardInfoFragment()
        }
    }

    private fun requestAccount(){
        ApiRepository.getInstance().requestQueryCitizenCardAccount().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseObserver<BaseResult<CardInfo>?>() {
            override fun onRequestNext(entity: BaseResult<CardInfo>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showAccountInfo(entity.data)
                }
            }

            override fun onRequestError(e: Throwable) {
                TourCooLogUtil.e(TAG, e.toString())
            }
        })

    }


    private fun showAccountInfo(info: CardInfo?) {
        val money: Double = try {
            (StringUtil.getNotNullValue(info?.amt).toDouble()) / 100.00
        } catch (e: NumberFormatException) {
            0.0
        }
        val accountMoney = "" + DoubleUtil.doubleFormatString(money)  + "元"
        tvAccountMoney.text = accountMoney
        tvName.text =info?.name
        tvPhone.text =AccountHelper.getInstance().userInfo.phoneNumber
    }
}