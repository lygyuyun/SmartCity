package com.tourcool.ui.citizen_card.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.FrameLoadMoreView
import com.frame.library.core.widget.titlebar.TitleBarView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcool.adapter.citizen_card.RechargeAdapter
import com.tourcool.bean.citizen_card.RechargeRecord
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.DateUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.fragment_citizen_card_deal_record_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 *@description : 市名卡充值记录
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2021年03月03日9:17
 * @Email: 971613168@qq.com
 */
class CitizenCardRecordRechargeListActivity : BaseCommonTitleActivity(), View.OnClickListener, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var timePickerStart: TimePickerView? = null
    private var endPickerStart: TimePickerView? = null
    private var startDate: String? = null
    private var endDate: String? = null
    private var adapter: RechargeAdapter? = null
    private var page: Int = 1
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_deal_record_list
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("充值记录")

    }

    override fun initView(savedInstanceState: Bundle?) {
        llStartDate.setOnClickListener(this)
        llEndDate.setOnClickListener(this)
        smartRefreshCommon.setOnRefreshListener(this)
        timePickerStart = TimePickerBuilder(mContext) { date, v ->
            startDate = DateUtil.formatDateTime(date)
            tvDateStart.text = DateUtil.formatDateTimeChina(date)
            doQueryRecord()
        }.setContentTextSize(23).build()
        recyclerViewCommon.layoutManager = LinearLayoutManager(mContext)

        endPickerStart = TimePickerBuilder(mContext) { date, v ->
            endDate = DateUtil.formatDateTime(date)
            tvDateEnd.text = DateUtil.formatDateTimeChina(date)
            doQueryRecord()
        }.setContentTextSize(23).build()
        smartRefreshCommon.setRefreshHeader(ClassicsHeader(mContext))
        loadAdapterData(null)
        adapter?.setOnItemClickListener { adapter, view, position ->
            skipRechargeDetail(adapter!!.data[position] as RechargeRecord)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llStartDate -> {
                timePickerStart?.show()
            }
            R.id.llEndDate -> {
                endPickerStart?.show()
            }
            else -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        requestRecord(page)
    }


    private fun loadAdapterData(dataList: MutableList<RechargeRecord>?) {
        smartRefreshCommon?.finishRefresh()
        if (recyclerViewCommon.adapter == null || adapter == null) {
            adapter = RechargeAdapter()
            val emptyView = View.inflate(mContext, R.layout.common_status_layout_tips, null)
            adapter!!.emptyView = emptyView
            adapter!!.bindToRecyclerView(recyclerViewCommon)
            adapter!!.setOnLoadMoreListener(this, recyclerViewCommon)
            //先判断是否Activity/Fragment设置过;再判断是否有全局设置;最后设置默认
            adapter!!.setLoadMoreView(FrameLoadMoreView(mContext).builder.build())
            return
        }
        val emptyView = View.inflate(mContext, R.layout.common_status_layout_empty, null)
        adapter!!.emptyView = emptyView
        adapter!!.loadMoreComplete()
        if (dataList == null) {
            val empty: List<RechargeRecord> = ArrayList()
            adapter!!.setNewData(empty)
            return
        }

        if (dataList.isEmpty()) {
            //第一页没有
            if (page <= 1) {
                adapter!!.setNewData(ArrayList<RechargeRecord?>())
            } else {
                adapter!!.loadMoreEnd()
            }
            return
        }
        if (page <= 1) {
            adapter!!.setNewData(ArrayList<RechargeRecord?>())
        }
        adapter!!.addData(dataList)
        if (dataList.size < 10) {
            adapter!!.loadMoreEnd()
        }
    }


    private fun requestRecord(page: Int) {
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            smartRefreshCommon.finishRefresh()
            return
        }
        val params: MutableMap<String, Any> = HashMap(5)
//        params["acctype"] = "C"
        params["pageIndex"] = page
        params["pageSize"] = 10
        if (!TextUtils.isEmpty(startDate)) {
            params["begindate"] = startDate!!
        }
        if (!TextUtils.isEmpty(endDate)) {
            params["enddate"] = endDate!!
        }
        ApiRepository.getInstance().requestRechargeRecord(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<RechargeRecord>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<RechargeRecord>>?) {
                smartRefreshCommon.finishRefresh()
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    loadAdapterData(entity.data)
                } else {
                    loadAdapterData(null)
                    ToastUtil.show(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                smartRefreshCommon.finishRefresh()
            }
        })
    }


    private fun doQueryRecord() {
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            return
        }
        page = 1
        requestRecord(page)
    }

    private fun skipRechargeDetail(info: RechargeRecord?) {
        val intent = Intent()
        intent.setClass(mContext, CitizenCardRecordRechargeDetailActivity::class.java)
        intent.putExtra("recharge_detail", info)
        startActivityForResult(intent, 1002)
    }

    override fun onLoadMoreRequested() {
        requestRecord(++page)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1002 -> {
                if (Activity.RESULT_OK == resultCode) {
                    smartRefreshCommon?.autoRefresh()
                }
            }
            else -> {
            }
        }
    }
}