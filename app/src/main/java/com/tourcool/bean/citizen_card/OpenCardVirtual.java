package com.tourcool.bean.citizen_card;


/**
 * @author :JenkinsZhou
 * @description :虚拟卡开通成功返回的Vo
 * @company :途酷科技
 * @date 2021年02月02日17:19
 * @Email: 971613168@qq.com
 */
public class OpenCardVirtual {
    /**
     * funcode : 9004
     * certno : 342401199301012670
     * name : 周健健
     * phone : 18256070563
     * safecontrol : DES
     * appbizid : 90010005
     * channel : 000000
     * certtype : 01
     * key : funcode,certno,name,phone,safecontrol,appbizid,channel,certtype,cardno,clrdate,retcode
     * sign : 83F4753AA389457F0FA599B43D9ED87A6C4D60A0
     * cardno : 21540000011995423369
     * clrdate : 20210202
     * retcode : 00
     * retmsg :
     */

    private String funcode;
    private String certno;
    private String name;
    private String phone;
    private String safecontrol;
    private String appbizid;
    private String channel;
    private String certtype;
    private String key;
    private String sign;

    private String cardno;
    private String clrdate;
    private String retcode;
    private String retmsg;


    public String getFuncode() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSafecontrol() {
        return safecontrol;
    }

    public void setSafecontrol(String safecontrol) {
        this.safecontrol = safecontrol;
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

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype;
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

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getClrdate() {
        return clrdate;
    }

    public void setClrdate(String clrdate) {
        this.clrdate = clrdate;
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
