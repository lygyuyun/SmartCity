package com.tourcool.bean.citizen_card;


/**
 * @author :JenkinsZhou
 * @description :虚拟卡信息Vo
 * @company :途酷科技
 * @date 2021年02月03日11:26
 * @Email: 971613168@qq.com
 */
public class CardInfo {


    /**
     * funcode : 3007
     * appbizid : 90010005
     * channel : 000000
     * cardno : 21540000011995442751
     * key : funcode,appbizid,channel,cardno,cardid,flag,amt,time,date,cardstate,name,certno,spendinglimit,retcode
     * sign : 3C7C733551A587C28466B555346FA5D4B5C83DF0
     * cardid : 21540000011995442751
     * flag : 22
     * amt : 0
     * time : 112157
     * date : 210203
     * cardstate : 1
     * name : 张闯
     * certno : 412824199410201432
     * spendinglimit : 1000
     * retcode : 00
     * retmsg :
     */

    private String funcode;
    private String appbizid;
    private String channel;
    private String cardno;
    private String key;
    private String sign;
    private String cardid;
    private String flag;

    private String amt;
    private String time;
    private String date;

    /*@ApiModelProperty("账户状态 卡状态(0未启用1正常2挂失7作废8预挂失9注销)")*/
    private String cardstate;
    private String name;
    private String certno;
    private String spendinglimit;

    private String retcode;

    private String retmsg;

    public String getFuncode() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public String getAppbizid() {
        return appbizid;
    }

    public void setAppbizid(String appbizid) {
        this.appbizid = appbizid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
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

    public String getCardstate() {
        return cardstate;
    }

    public void setCardstate(String cardstate) {
        this.cardstate = cardstate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getSpendinglimit() {
        return spendinglimit;
    }

    public void setSpendinglimit(String spendinglimit) {
        this.spendinglimit = spendinglimit;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }
}
