package com.tourcool.adapter.citizen_card;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.util.DoubleUtil;
import com.frame.library.core.util.StringUtil;
import com.tourcool.bean.citizen_card.RechargeRecord;
import com.tourcool.core.util.DateUtil;
import com.tourcool.smartcity.R;

import java.util.Date;

/**
 * @author :JenkinsZhou
 * @description : 市名卡充值适配器
 * @company :途酷科技
 * @date 2021年03月03日9:44
 * @Email: 971613168@qq.com
 */
public class RechargeAdapter extends BaseQuickAdapter<RechargeRecord, BaseViewHolder> {

    public RechargeAdapter() {
        super(R.layout.item_citizen_card_cost_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeRecord item) {
        Date date = DateUtil.getDate(item.getDate());
        Date time = DateUtil.getDate(item.getTime());
       if(date != null){
           helper.setText(R.id.tvDate,  DateUtil.formatDate1(date)+" "+DateUtil.formatTime(time));
       }
        helper.setText(R.id.tvTypeDesc, "市名卡充值");
        helper.setImageResource(R.id.ivRechargeType, R.mipmap.ic_citizen_card_recharge);
        if ("1".equals(StringUtil.getNotNullValueLine(item.getTradeStatus()))) {
            helper.setText(R.id.tvStatus, "已完成");
        } else {
            helper.setText(R.id.tvStatus, "已取消");
        }
        String amount;
        try {
            amount = StringUtil.getNotNullValueLine(item.getAmount());
            amount = DoubleUtil.doubleFormatString(Double.parseDouble(amount) / 100);
        } catch (NumberFormatException e) {
            amount = "0.00";
        }
        helper.setText(R.id.tvAmount, "¥ " + amount);


    }
}
