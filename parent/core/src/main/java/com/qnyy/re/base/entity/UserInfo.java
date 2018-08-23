package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

@Setter@Getter
public class UserInfo extends BaseUserEntity {
    public static final int MALE = 1;
    public static final int FEMALE = 0;
    private String nickname;

    private String headUrl;

    private Integer sex = -1;

    private String address;

    private String promoCode;

    @JsonIgnore
    private Long bitState = 0L;

    @JsonIgnore
    private Integer version;

    private Date updated;

    private String intro;
    @JsonIgnore
    private String lat;
    @JsonIgnore
    private String lng;

    private Long createantUid;

    public boolean getBindPhone() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.OP_BIND_PHONE);
    }

    public boolean getBindCashAccount() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.OP_BIND_CASH_ACCOUNT);
    }
    public boolean getHasCashProcess() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.OP_HAS_CASH_PROCESS);
    }
    public boolean getCreateantMerchant() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.TYPE_CREATEANT_MERCHANT);
    }
    public boolean getCreateantMember() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.TYPE_CREATEANT_MEMBER);
    }
    public boolean getCreateantAgent() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.TYPE_CREATEANT_AGENT);
    }
    public boolean getBindWechat() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.OP_BIND_WECHAT);
    }

    public boolean getHasBindDevice() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.OP_HAS_BIND_DEVICE);
    }

    public boolean admin() {
        return BitStateUtil.hasState(this.bitState, BitStateUtil.TYPE_ADMIN);
    }

    public void addState(long newState) {
        this.bitState = BitStateUtil.addState(this.bitState, newState);
    }

    public void removeState(long newState) {
        this.bitState = BitStateUtil.removeState(this.bitState, newState);
    }



}