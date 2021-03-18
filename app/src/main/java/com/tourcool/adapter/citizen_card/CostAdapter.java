package com.tourcool.adapter.citizen_card;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.util.DoubleUtil;
import com.frame.library.core.util.StringUtil;
import com.tourcool.bean.citizen_card.CostRecordInfo;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 消费记录适配器
 * @company :途酷科技
 * @date 2021年02月20日16:57
 * @Email: 971613168@qq.com
 */
public class CostAdapter extends BaseQuickAdapter<CostRecordInfo, BaseViewHolder> {

    public CostAdapter() {
        super(R.layout.item_citizen_card_cost_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, CostRecordInfo item) {
        helper.setText(R.id.tvDate, StringUtil.getNotNullValueLine(item.getDate()));
        if ("1".equals(StringUtil.getNotNullValueLine(item.getOrderStatus()))) {
            helper.setText(R.id.tvStatus, "已完成");
        } else {
            helper.setText(R.id.tvStatus, "已取消");
        }
        String amount;
        try {
            amount = StringUtil.getNotNullValueLine(item.getBasePrice());
            amount = DoubleUtil.doubleFormatString(Double.parseDouble(amount) / 100);
        } catch (NumberFormatException e) {
            amount = "0.00";
        }
        helper.setText(R.id.tvAmount, "¥ " + amount);


    }

}
