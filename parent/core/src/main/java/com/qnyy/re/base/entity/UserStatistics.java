package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 用户统计信息
 */
@Getter@Setter
public class UserStatistics extends BaseUserEntity {
    @Getter
    public enum CountField{
        RE_BY_DRAW_COUNT("reByDrawCount","红包被领取"),
        RE_DRAW_COUNT("reDrawCount","领取红包"),
        RE_PAY_COUNT("rePayCount","发红包"),
        VISITOR_COUNT("visitorCount","访客"),
        FEEDBACK_COUNT("feedbackCount","反馈"),
        INFORM_COUNT("informCount","举报"),
        BY_INFORM_COUNT("byInformCount","被举报"),
        FIRST_RELATION_COUNT("firstRelationCount","一级邀请人数"),
        SECOND_RELATION_COUNT("secondRelationCount","二级邀请人数"),
        DEVICE_COUNT("deviceCount","设备数"),
        ;

        CountField(String value, String fieldName) {
            this.value = value;
            this.fieldName = fieldName;
        }
        private String value;
        private String fieldName;
    }

    private BigDecimal reDrawAmount;

    private BigDecimal rePayAmount;

    private BigDecimal deviceRePayAmount;

    private BigDecimal reBestAmount;

    private Integer reByDrawCount;

    private Integer reDrawCount;

    private Integer rePayCount;

    private Integer visitorCount;

    private Integer feedbackCount;

    private Integer informCount;

    private Integer byInformCount;
    private Integer firstRelationCount;
    private Integer secondRelationCount;
    private Integer deviceCount;

    private BigDecimal totalRelationReap;

    private BigDecimal firstRelationReap;

}