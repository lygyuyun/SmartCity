package com.tourcool.bean.citizen_card;

/**
 * @author :JenkinsZhou
 * @description : 实体卡卡号
 * @company :途酷科技
 * @date 2021年02月19日16:19
 * @Email: 971613168@qq.com
 */
public class CardMaterialInfo {
    private String name;
    private String idCardNum;
    private String materialCardNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getMaterialCardNum() {
        return materialCardNum;
    }

    public void setMaterialCardNum(String materialCardNum) {
        this.materialCardNum = materialCardNum;
    }
}
