package com.tourcool.adapter.citizen_card;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 消费记录适配器
 * @company :途酷科技
 * @date 2021年02月20日16:57
 * @Email: 971613168@qq.com
 */
public class CostAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CostAdapter() {
        super(R.layout.item_citizen_card_cost_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }

}
