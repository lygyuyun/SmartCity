package com.tourcool.bean.account;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月31日14:24
 * @Email: 971613168@qq.com
 */
@Entity
public class UserInfo  implements Parcelable {


    /**
     * authenticationLevel : 0
     * iconUrl :
     * nickname :
     * phoneNumber :
     * "idCard": "",
     *                 "name": "",
     */

    private int authenticationLevel;
    private String iconUrl;
    private String nickname;
    private String phoneNumber;
    private String name;
    private String idCard;
    private boolean verified;
    private String citizenCardMaterialNo;
    private String citizenCardVirtualNo;
    private Integer citizenCardQrCodeState;

    public int getAuthenticationLevel() {
        return authenticationLevel;
    }

    public void setAuthenticationLevel(int authenticationLevel) {
        this.authenticationLevel = authenticationLevel;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getCitizenCardMaterialNo() {
        return citizenCardMaterialNo;
    }

    public void setCitizenCardMaterialNo(String citizenCardMaterialNo) {
        this.citizenCardMaterialNo = citizenCardMaterialNo;
    }

    public String getCitizenCardVirtualNo() {
        return citizenCardVirtualNo;
    }

    public void setCitizenCardVirtualNo(String citizenCardVirtualNo) {
        this.citizenCardVirtualNo = citizenCardVirtualNo;
    }

    public Integer getCitizenCardQrCodeState() {
        return citizenCardQrCodeState;
    }

    public void setCitizenCardQrCodeState(Integer citizenCardQrCodeState) {
        this.citizenCardQrCodeState = citizenCardQrCodeState;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "authenticationLevel=" + authenticationLevel +
                ", iconUrl='" + iconUrl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", verified=" + verified +
                ", citizenCardMaterialNo='" + citizenCardMaterialNo + '\'' +
                ", citizenCardVirtualNo='" + citizenCardVirtualNo + '\'' +
                ", citizenCardQrCodeState=" + citizenCardQrCodeState +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.authenticationLevel);
        dest.writeString(this.iconUrl);
        dest.writeString(this.nickname);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.name);
        dest.writeString(this.idCard);
        dest.writeByte(this.verified ? (byte) 1 : (byte) 0);
        dest.writeString(this.citizenCardMaterialNo);
        dest.writeString(this.citizenCardVirtualNo);
        dest.writeValue(this.citizenCardQrCodeState);
    }

    public boolean getVerified() {
        return this.verified;
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.authenticationLevel = in.readInt();
        this.iconUrl = in.readString();
        this.nickname = in.readString();
        this.phoneNumber = in.readString();
        this.name = in.readString();
        this.idCard = in.readString();
        this.verified = in.readByte() != 0;
        this.citizenCardMaterialNo = in.readString();
        this.citizenCardVirtualNo = in.readString();
        this.citizenCardQrCodeState = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    @Generated(hash = 1607468501)
    public UserInfo(int authenticationLevel, String iconUrl, String nickname,
            String phoneNumber, String name, String idCard, boolean verified,
            String citizenCardMaterialNo, String citizenCardVirtualNo,
            Integer citizenCardQrCodeState) {
        this.authenticationLevel = authenticationLevel;
        this.iconUrl = iconUrl;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.idCard = idCard;
        this.verified = verified;
        this.citizenCardMaterialNo = citizenCardMaterialNo;
        this.citizenCardVirtualNo = citizenCardVirtualNo;
        this.citizenCardQrCodeState = citizenCardQrCodeState;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
