package com.tourcool.bean;

/**
 * @author :JenkinsZhou
 * @description : 支付信息
 * @company :途酷科技
 * @date 2021年02月25日16:03
 * @Email: 971613168@qq.com
 */
public class PayInfo {

    private Integer payType;
    private Double amount;
    private Object aliPayInfo;

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Object getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(Object aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }
}
