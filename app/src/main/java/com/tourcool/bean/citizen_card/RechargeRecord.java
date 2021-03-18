package com.tourcool.bean.citizen_card;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2021年03月03日15:06
 * @Email: 971613168@qq.com
 */
public class RechargeRecord implements Parcelable {

    /**
     * 卡号20(后补空格)+交易金额12+账户余额12+账户类型2+交易流水号20+交易时间6+交易日期8+中心流水号12（后补空格）+终端号8+交易批次号10+交易类型1
     */
    private String cardNo;
//    @ApiModelProperty("交易金额")
    private String amount;
    private  String amountFen;
//    @ApiModelProperty("账户余额")
    private String account;
    private String accountType;
    private String tradeNo;
    private String time;
    private String date;

//    @ApiModelProperty("中心流水号")
    private String actionNo;

//    @ApiModelProperty("终端号")
    private String term;

//    @ApiModelProperty("交易批次号10")
    private String batchno;

//    @ApiModelProperty("交易状态")
    private String tradeStatus;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActionNo() {
        return actionNo;
    }

    public void setActionNo(String actionNo) {
        this.actionNo = actionNo;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardNo);
        dest.writeString(this.amount);
        dest.writeString(this.account);
        dest.writeString(this.accountType);
        dest.writeString(this.tradeNo);
        dest.writeString(this.time);
        dest.writeString(this.date);
        dest.writeString(this.actionNo);
        dest.writeString(this.term);
        dest.writeString(this.batchno);
        dest.writeString(this.tradeStatus);
    }

    public RechargeRecord() {
    }

    protected RechargeRecord(Parcel in) {
        this.cardNo = in.readString();
        this.amount = in.readString();
        this.account = in.readString();
        this.accountType = in.readString();
        this.tradeNo = in.readString();
        this.time = in.readString();
        this.date = in.readString();
        this.actionNo = in.readString();
        this.term = in.readString();
        this.batchno = in.readString();
        this.tradeStatus = in.readString();
    }

    public static final Parcelable.Creator<RechargeRecord> CREATOR = new Parcelable.Creator<RechargeRecord>() {
        @Override
        public RechargeRecord createFromParcel(Parcel source) {
            return new RechargeRecord(source);
        }

        @Override
        public RechargeRecord[] newArray(int size) {
            return new RechargeRecord[size];
        }
    };

    public String getAmountFen() {
        return amountFen;
    }

    public void setAmountFen(String amountFen) {
        this.amountFen = amountFen;
    }
}
