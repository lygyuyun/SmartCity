package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R
import com.tourcool.ui.citizen_card.CitizenCardTabActivity.Companion.EXTRA_KEY
import com.tourcool.ui.citizen_card.CitizenCardTabActivity.Companion.EXTRA_SKIP_RECHARGE


/**
 *@description : 市名卡操作
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月24日10:09
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationContainerFragment : BaseFragment() {
    private var functionFragment: CitizenCardOperationListFragment? = null
    private var skipType: String? = null
    private var rechargeFragment: CitizenCardOperationRechargeFragment? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_container
    }

    override fun initView(savedInstanceState: Bundle?) {
        skipType = arguments?.getString(EXTRA_KEY)
        init()
        if(EXTRA_SKIP_RECHARGE == skipType){
            showFragmentRecharge()
        }
    }

    companion object {
        fun newInstance(skipType: String?): CitizenCardOperationContainerFragment? {
            val args = Bundle()
            val fragment = CitizenCardOperationContainerFragment()
            args.putString(EXTRA_KEY, skipType)
            fragment.arguments = args
            return fragment
        }
    }

    private fun init() {
        functionFragment = CitizenCardOperationListFragment.newInstance()
        rechargeFragment = CitizenCardOperationRechargeFragment.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, functionFragment!!)
        transaction.add(R.id.fragmentContainer, rechargeFragment!!)
        transaction.hide(rechargeFragment!!)
        transaction.show(functionFragment!!)
        transaction.commit()
    }


    fun showFragmentFunctionList() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        if (functionFragment != null && rechargeFragment != null) {
            transaction.show(functionFragment!!)
            transaction.hide(rechargeFragment!!)
            transaction.commit()
        }
    }

    fun showFragmentRecharge() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        if (functionFragment != null && rechargeFragment != null) {
            transaction.show(rechargeFragment!!)
            transaction.hide(functionFragment!!)
            transaction.commit()
        }
    }
}