package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by E_Iva on 2017.12.6.0006.
 */
@Getter@Setter
public class UpdateUserInfoVO extends BaseParamVO {
    private String nickname;

    private String headUrl;

    private Integer sex = -1;

    private String address;

    private String intro;

    private MultipartFile file;

    private String phone;
}
