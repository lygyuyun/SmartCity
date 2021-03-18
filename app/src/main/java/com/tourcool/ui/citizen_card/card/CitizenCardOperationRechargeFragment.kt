package com.tourcool.ui.citizen_card.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alipay.sdk.app.EnvUtils
import com.alipay.sdk.app.PayTask
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.threadpool.ThreadPoolManager
import com.frame.library.core.util.ToastUtil
import com.tourcool.adapter.citizen_card.CitizenCardRechargeAdapter
import com.tourcool.bean.PayInfo
import com.tourcool.bean.PayResult
import com.tourcool.bean.recharge.RechargeEntity
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.event.account.CardInfoEvent
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 *@description : JenkinsZhou
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月24日10:18
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationRechargeFragment : BaseFragment(), View.OnClickListener {
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    private val APPID = "2016101900725594"
    private val SDK_PAY_FLAG = 1

    /**
     * pkcs8 格式的商户私钥。
     *
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     *
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    private val RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQzSSwsGsHa21Q9iHGCgoyC2II3iXswaUmKhCMZtq1HaovRl7gEcVdVLfHdEzIJw2E8eRl61jmFe1ug0Nv40RFdtKNpcdb98EIJjHzoV+x5J5XdVeZACeQbk3hTfE/blNHZcCUtIalx39tUe2W+cttBNGvustO2T4jsoynKSOZpdl4S6LZRUl3E8jL0U75Po+BmNwzuJMN/Uzrm6So0O4c/NamCy045hPshMYfBhwrEjSmLE5CQsswQ+anaV0HboYa8Ktiyk2PtX+QOfE0E5IzSdc7lOxtb8RFqbGpHpIsjUavRuS0c7qkiyuq7spMJuXed5LuTSprkzew9rRggiLxAgMBAAECggEAcS33hKeQMK92NXb89Qc56t3c1aOVrxW42Ti90JBAWJqEQAlX9PnO82FZeGvd7AAiYAyWAlfPF6CCAgRVfKn93fG/J3oxdiAT4CPCnXRAERLp9OBCBNQLGKgu9XDvpS29qCvPOCxWplNltXP5Ki27JE5E+38clFfXax2PNMVCD0BLMAZVO3pDzcLiZreaSbnX/Rg6NkvlyhmYl23QdNBQKSvNXxbYelcGsUUW/2dK30qNPQrdodZP/yU1aWES3g6sjc1hlNB0OzHzPwg1vy1hhQp2LpP0cDE0HzaI/o5XKYn4sNN0qvBqX6Tobmq3ewrgcRBEcEf33PdYkuVgo89q8QKBgQD+Ja9WjhYnYGY+3eIzpakuGqGDmw2v2f24k7GN1loPNWJcghcyHqj5E3IUQp7c0mumXUKivHJUjeZ+bEkmMKrjnsRS/78ZqRw8NTQPVSsJFwmX16kGL0WOP8urJgXUK8VSV+BHyiKA/m4l0tlV0Zt9gtRnsDERDpzHWRfxiB1CFQKBgQCR22La0zXQQN+0F0D6C82LYxO3N7B4J8hLAlREk58FcD80NxKX1H1/jqpauJWX641AIUgXLTfE0FOPJiYOHPebNMscXsaSLnZyqZzBNvmGGD9962olzRJn9Now90Xzl609yVwFSZC7jNbcmPvffNYvB//qhsb5lDg5WVCQGBkAbQKBgQCxH7yKu24PNW0tBWEAE8XL/v1wBmFMnMXqnOyS6MBNdyw9QuE9qgajyPypyCCF0BT8lmS5nFKmQVtR4tA8UJDZIK69Fcau8KBgCchlozriE+84pzICwHFeiXG9rztCutrD38DM58CSXECSFdgoqnQOzl6QgUhBFgkIGjzItc+GXQKBgF2kmIcda0NVI0qgU1s2ELUY2yOaYXYDNJjT2f5dOI9pTWrcoNdR2XD3y2+Lap+q33j45SvcIB4AEmWpfG07M801y+FfB+X6ZpmfQ8DY5sKs9raFWhUC4HpVvXAKkaKfEt7EVIwgeweumJPwtGT75Rc0751HAqBiBObs7MhWcFbRAoGBAPAoCtbHB4yubouMfprXobjH4FxllCmVTfL8qgpwTyGvACGq1p/v2arA4Jr9BRQJIyFd5Ya5bwtUQJf6XPgq5o5hdcndpa+V4XxzdUOaixsUu9AWyFWyQwDfMnr+FzvQt5tWUB9OPKv2oeaN3vk1bQs8nRjYshfMRVeNzr3E1e1M"

    private val mRechargeEntityList: MutableList<RechargeEntity> = ArrayList()
    private var adapter: CitizenCardRechargeAdapter? = null
    private var mRechargeRecyclerView: RecyclerView? = null
    private var cbAli: CheckBox? = null
    private var cbWeChat: CheckBox? = null
    private var payType: Int = 0
    private var payAmount: Int = 0
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_operation_recharge
    }

    override fun initView(savedInstanceState: Bundle?) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        mRechargeRecyclerView = mContentView.findViewById(R.id.mRechargeRecyclerView)
        mContentView.findViewById<RelativeLayout>(R.id.rlBack).setOnClickListener(this)
        mContentView.findViewById<LinearLayout>(R.id.llPayTypeAli).setOnClickListener(this)
        mContentView.findViewById<LinearLayout>(R.id.llPayTypeWeChat).setOnClickListener(this)
        mContentView.findViewById<TextView>(R.id.tvConfirm).setOnClickListener(this)
        cbAli = mContentView.findViewById(R.id.cbAli)
        cbWeChat = mContentView.findViewById(R.id.cbWeChat)
        mRechargeRecyclerView!!.layoutManager = GridLayoutManager(mContext, 3)
        initData()
    }


    companion object {
        fun newInstance(): CitizenCardOperationRechargeFragment {
            return CitizenCardOperationRechargeFragment()
        }
    }

    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     *
     * @param savedInstanceState*/

    /*private fun stateCheck(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
          val  fm = childFragmentManager
            val fts: FragmentTransaction = fm.beginTransaction()
            val af = AnimationFragment()
            mContent = af
            fts.add(R.id.content_frame, af)
            fts.commit()
        } else {
            val af: AnimationFragment? = fragmentManager
                    .findFragmentByTag(tags.get(0)) as AnimationFragment?
            val pf: PlainFragment? = fragmentManager
                    .findFragmentByTag(tags.get(1)) as PlainFragment?
            val rf: RecordFragment? = fragmentManager
                    .findFragmentByTag(tags.get(2)) as RecordFragment?
            val inf: InformationFragment? = fragmentManager
                    .findFragmentByTag(tags.get(3)) as InformationFragment?
            val tf: TestingFragment? = fragmentManager
                    .findFragmentByTag(tags.get(4)) as TestingFragment?
            fragmentManager!!.beginTransaction().show(af).hide(pf).hide(rf)
                    .hide(inf).hide(tf).commit()
        }
    }*/

    private fun initData() {
        cbAli?.isChecked = true
        cbWeChat?.isChecked = false
        mRechargeEntityList.add(RechargeEntity(10.0, true))
        payAmount = 10
        mRechargeEntityList.add(RechargeEntity(30.0))
        mRechargeEntityList.add(RechargeEntity(50.0))
        mRechargeEntityList.add(RechargeEntity(80.0))
        mRechargeEntityList.add(RechargeEntity(100.0))
        mRechargeEntityList.add(RechargeEntity(200.0))
        adapter = CitizenCardRechargeAdapter(mRechargeEntityList)
        adapter!!.bindToRecyclerView(mRechargeRecyclerView)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            setSelect(position)
            adapter.notifyDataSetChanged()
            var current = adapter!!.data[position] as RechargeEntity
            payAmount = (current.rechargeMoney).toInt()
        }
    }

    /**
     * 设置选中属性
     *
     * @param position
     */
    private fun setSelect(position: Int) {
        if (position >= mRechargeEntityList.size) {
            return
        }
        var rechargeEntity: RechargeEntity
        for (i in mRechargeEntityList.indices) {
            rechargeEntity = mRechargeEntityList[i]
            rechargeEntity.selected = i == position
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlBack -> {
                val fragment = parentFragment as CitizenCardOperationContainerFragment
                fragment.showFragmentFunctionList()
            }
            R.id.llPayTypeAli -> {
                cbAli?.isChecked = true
                cbWeChat?.isChecked = false
            }
            R.id.llPayTypeWeChat -> {
                cbAli?.isChecked = false
                cbWeChat?.isChecked = true
            }
            R.id.tvConfirm -> {
                doPay()
            }


            else -> {
            }
        }
    }


    private fun doPay() {
        if (cbWeChat!!.isChecked) {
            ToastUtil.show("暂不支持微信支付")
            return
        }
        if (payAmount <= 0) {
            ToastUtil.show("支付金额必须大于0")
            return
        }
        if (cbAli!!.isChecked) {
            payType = 4
        }
        requestPayRecharge(payType, payAmount)
    }

    private fun requestPayRecharge(payType: Int, amount: Int) {
        if (payType <= 0) {
            ToastUtil.show("请选择支付类型")
            return
        }
        ApiRepository.getInstance().requestPayRecharge(payType, amount).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<PayInfo>?>() {
            override fun onRequestNext(entity: BaseResult<PayInfo>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    aliPay(entity.data.aliPayInfo.toString())
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable) {
                TourCooLogUtil.e(TAG, e.toString())
            }
        })


    }


    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = payResult.getResult() // 同步返回需要验证的信息
                    val resultStatus: String = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show("支付成功")
                        EventBus.getDefault().postSticky(CardInfoEvent())
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show("支付失败")
                    }
                }

                else -> {
                }
            }
        }
    }

    private fun aliPay(info: String) {
        val payRunnable = Runnable {
            val aliPay = PayTask(mContext)
            val result = aliPay.payV2(info, true)
            TourCooLogUtil.i("msp", result.toString())
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
     ThreadPoolManager.getThreadPoolProxy().execute(payRunnable)
    }
}