package com.qnyy.re.business.mapper;

import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.business.entity.ReMoment;
import com.qnyy.re.business.query.ReMomentQueryObject;
import com.qnyy.re.business.vo.RedEnvelopeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReMomentMapper {

    int insert(ReMoment record);

    ReMoment selectByPrimaryKey(Long id);

    ReMoment selectWithItem(Long id);

    int updateReMomentState(@Param("id") Long id, @Param("state") Integer state);

    int updateByPrimaryKey(ReMoment record);

    int queryCount(ReMomentQueryObject qo);

    List<RedEnvelopeVO> query(ReMomentQueryObject qo);

    UserStatistics selectUserStatisticsByReMoment(Long reId);

    RedEnvelopeVO getSingleRe(Long id);

    int querySingleReDrawCount(@Param("from") Long from,@Param("to") Long to);

}