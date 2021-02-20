package com.tourcool.bean.citizen_card;


/**
 * @author :JenkinsZhou
 * @description :交易记录Vo
 * @company :途酷科技
 * @date 2021年02月20日11:43
 * @Email: 971613168@qq.com
 */
public class TransactionRecord {


    /**
     * funcode : 1402
     * pageindex : 1
     * key : funcode,pageindex,cardno,totalcount,date,time,retcode
     * sign : DB5BF7C7ABFCC271E30E8996EE1F6714429910BC
     * cardno : 21420000000208811108
     * totalcount : 0
     * queryresult :
     * date : 210220
     * time : 113934
     * retcode : 00
     * retmsg :
     */

    private String funcode;
    private int pageindex;
    private String key;
    private String sign;
    private String cardno;
    private int totalcount;
    private String queryresult;
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

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
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

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public String getQueryresult() {
        return queryresult;
    }

    public void setQueryresult(String queryresult) {
        this.queryresult = queryresult;
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
