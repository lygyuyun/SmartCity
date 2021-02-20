
import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.util.FrameUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.citizen_card.card.CitizenCardTransactionRecordActivity
import kotlinx.android.synthetic.main.fragment_citizen_card_deal_record_enter.*

/**
 *@description : 交易记录
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日16:29
 * @Email: 971613168@qq.com
 */
class CitizenCardDealRecordFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_deal_record_enter
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun loadData() {
        llRecordCost.setOnClickListener {
            FrameUtil.startActivity(mContext, CitizenCardTransactionRecordActivity::class.java)
        }
    }
    companion object {


        fun newInstance(): CitizenCardDealRecordFragment? {
            return CitizenCardDealRecordFragment()
        }
    }
}