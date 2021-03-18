package com.tourcool.bean.citizen_card;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @author :JenkinsZhou
 * @description : 交易记录
 * @company :途酷科技
 * @date 2021年03月01日11:16
 * @Email: 971613168@qq.com
 */
public class CostRecordInfo implements Parcelable {


    /**
     * 交易时间|行业|线路|车辆编号|基本票价|优惠金额|卡类型|订单号|订单状态,
     * <p>
     * 20210224160603|0001|K0227|01987F|300|0|41|21022416060700865236|1
     */

    private String date;
    private String trade;

    private String line;

    private String carNumber;

    /**
     * 基本票价
     */
    private String basePrice;


    private String discountAmount;

    private String cardType;


    private String orderNumber;


    private String orderStatus;

    public static CostRecordInfo createCostInfo(String data) {
        CostRecordInfo info = new CostRecordInfo();
        if (TextUtils.isEmpty(data)) {
            return info;
        }
        String[] records = data.split("\\|");
        if (records.length < 9) {
            return info;
        }
        info.date =records[0];
        info.trade =records[1];
        info.line =records[2];
        info.carNumber =records[3];
        info.basePrice =records[4];
        info.discountAmount =records[5];
        info.cardType =records[6];
        info.orderNumber =records[7];
        info.orderStatus =records[8];
        return info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.trade);
        dest.writeString(this.line);
        dest.writeString(this.carNumber);
        dest.writeString(this.basePrice);
        dest.writeString(this.discountAmount);
        dest.writeString(this.cardType);
        dest.writeString(this.orderNumber);
        dest.writeString(this.orderStatus);
    }

    public CostRecordInfo() {
    }

    protected CostRecordInfo(Parcel in) {
        this.date = in.readString();
        this.trade = in.readString();
        this.line = in.readString();
        this.carNumber = in.readString();
        this.basePrice = in.readString();
        this.discountAmount = in.readString();
        this.cardType = in.readString();
        this.orderNumber = in.readString();
        this.orderStatus = in.readString();
    }

    public static final Parcelable.Creator<CostRecordInfo> CREATOR = new Parcelable.Creator<CostRecordInfo>() {
        @Override
        public CostRecordInfo createFromParcel(Parcel source) {
            return new CostRecordInfo(source);
        }

        @Override
        public CostRecordInfo[] newArray(int size) {
            return new CostRecordInfo[size];
        }
    };
}
