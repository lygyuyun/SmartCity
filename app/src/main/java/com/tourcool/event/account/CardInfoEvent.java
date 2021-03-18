package com.tourcool.event.account;

import com.tourcool.bean.citizen_card.CardInfo;

/**
 * @author :JenkinsZhou
 * @description : JenkinsZhou
 * @company :途酷科技
 * @date 2021年03月05日16:45
 * @Email: 971613168@qq.com
 */
public class CardInfoEvent {
    public CardInfo cardInfo;

    public CardInfoEvent(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    public CardInfoEvent() {
    }
}
