package com.tourcool.bean.citizen_card;

/**
 * @author :JenkinsZhou
 * @description : 市名卡账户信息
 * @company :途酷科技
 * @date 2021年02月19日17:43
 * @Email: 971613168@qq.com
 */
public class CitizenAccountInfo {
    /**
     * funcode : 1102
     * key : funcode,cardno,balance,qrcode,date,time,retcode
     * sign : FD417A298BB4047140EF8696842FBAEC7F1E0921
     * cardno : 21420000000208811108
     * balance : 7060
     * qrcode : 80015424010100000212034730231025900001040400210370E2223143B2C1C390194DC448DE7EB7408BD73990E7DD14B6AE01D44B184F0F98583936003F9FBA5986B94786DCF9DD0E7B93169AAE65885D73F181F2E5EAC717A94419182F7990320DDF480E635F4DEBD6C83E045AF84BF8E4F93983B834F93231303530303030303030303034342021420000000208811108034730230347302341001B9403C9FE16DC83261CC1A230AAC8BB62D8129D5F34669DB294628D80C34E44D01AE5602F847100780B049175569880055813FF0015B18AF728C285AC2B12CAF5265A6BAD12FF95A56725950C3A5618B3EC582B62FB7D8C939DF075E7A78A2E99962B16B97459CBF20200D9AA315DE6D799297D5BCB602F843515C783B5D3F8E1AB9D0B0D4DB6CAFA0CAA354A70C4AD19DEBB43C70C9BC3CDD2AB7826278CFBE1EEEBCD461C4C756E3EEDBAF37619F8C4A06104592FDEC221F497
     * date : 210219
     * time : 172613
     * retcode : 00
     * retmsg :
     */

    private String funcode;
    private String key;
    private String sign;
    private String cardno;
    private String balance;
    private String qrcode;
    private String date;
    private String time;
    private String retcode;
    private String retmsg;

    public String getFuncode() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
