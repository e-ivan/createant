package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import com.qnyy.re.business.enums.ReMomentScopeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by E_Iva on 2017.11.29 0029.
 */
@Setter@Getter
public class CreateReMomentVO extends BaseParamVO {
    private String content;
    @VerifyParam
    private BigDecimal reAmount;
    @VerifyParam
    private Integer reNum;
    @VerifyParam
    private String userLng;//用户经度
    @VerifyParam
    private String userLat;//用户纬度
    @VerifyParam
    private String reLng;//红包经度
    @VerifyParam
    private String reLat;//红包纬度
    @VerifyParam
    private Integer scope;//范围
    @VerifyParam
    private Integer crowd;//面向人群
    private String linkTo;//外链地址
    private Long storeId;//门店Id
    private Long shopId;   //店铺Id

    private List<Long> fileId;

    private MultipartFile[] file;

    public void setScope(Integer scope){
        if (ReMomentScopeEnum.getByCode(scope) == null) {
            scope = null;
        }
        this.scope = scope;
    }
}
