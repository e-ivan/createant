package com.qnyy.re.base.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.8.0008.
 */
@Getter@Setter
public class UserFansQueryObject extends QueryObject {
    private Long byFanUid = -1L;//被关注人id，查粉丝
    private Long fanUid = -1L;//关注人id，查关注
}
