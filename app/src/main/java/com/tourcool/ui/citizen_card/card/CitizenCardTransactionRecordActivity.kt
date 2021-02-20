package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcool.adapter.citizen_card.CostAdapter
import com.tourcool.bean.citizen_card.TransactionRecord
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.DateUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.fragment_citizen_card_deal_record_list.*
import java.util.*


/**
 *@description : 市名卡交易列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2021年02月20日15:45
 * @Email: 971613168@qq.com
 */
class CitizenCardTransactionRecordActivity : BaseCommonTitleActivity(), View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private var timePickerStart: TimePickerView? = null
    private var endPickerStart: TimePickerView? = null
    private var startDate: String? = null
    private var endDate: String? = null
    private var adapter: CostAdapter? = null
    private var page: Int= 1
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_deal_record_list
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("消费记录")

    }

    override fun initView(savedInstanceState: Bundle?) {
        llStartDate.setOnClickListener(this)
        llEndDate.setOnClickListener(this)
        smartRefreshCommon.setOnRefreshListener(this)
        timePickerStart = TimePickerBuilder(mContext) { date, v ->
            startDate = DateUtil.formatDateTime(date)
            tvDateStart.text = DateUtil.formatDateTimeChina(date)
        }.setContentTextSize(23).build()
        recyclerViewCommon.layoutManager = LinearLayoutManager(mContext)

        endPickerStart = TimePickerBuilder(mContext) { date, v ->
            endDate = DateUtil.formatDateTime(date)
            tvDateEnd.text = DateUtil.formatDateTimeChina(date)
        }.setContentTextSize(23).build()
        smartRefreshCommon.setRefreshHeader(ClassicsHeader(mContext))
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

    override fun loadData() {
        super.loadData()
        loadAdapterData(null)
    }

    private fun loadAdapterData(data: TransactionRecord?) {
        if (recyclerViewCommon.adapter == null || adapter == null) {
            adapter = CostAdapter()
            val emptyView = View.inflate(mContext, R.layout.common_status_layout_empty, null)
            adapter!!.emptyView = emptyView
            adapter!!.bindToRecyclerView(recyclerViewCommon)
        }
        if (data == null) {
            val stringList: List<String> = ArrayList()
            adapter!!.setNewData(stringList)
            return
        }
        adapter!!.setEnableLoadMore(data.totalcount < 10)
        if (data.pageindex <= 1) {
            //说明当前是第一页
            adapter!!.setNewData(data.queryresult.split("|"))
        } else {
            //说明加载更多
            if (data.totalcount < 10) {
                adapter!!.loadMoreEnd()
                adapter!!.setEnableLoadMore(false)
            }
            adapter!!.addData(data.queryresult.split("|"))
        }

    }


    private fun requestRecord(page: Int) {
        val params: MutableMap<String, Any> = HashMap(5)
        params["acctype"] = "C"
        params["pageindex"] = page
        params["pagesize"] = 10
        if (!TextUtils.isEmpty(startDate)) {
            params["startdate"] = startDate!!
        }
        if (!TextUtils.isEmpty(endDate)) {
            params["enddate"] = endDate!!
        }
        ApiRepository.getInstance().requestQueryTransactionRecord(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TransactionRecord>>() {
            override fun onRequestNext(entity: BaseResult<TransactionRecord>?) {
                smartRefreshCommon.finishRefresh()
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    loadAdapterData(entity.data)

                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                smartRefreshCommon.finishRefresh()
            }
        })
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        requestRecord(page++)
    }
}