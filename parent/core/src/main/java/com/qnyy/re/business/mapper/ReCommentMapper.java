package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.ReComment;
import com.qnyy.re.business.query.ReCommentQueryObject;

import java.util.List;

public interface ReCommentMapper {

    int insert(ReComment record);

    ReComment selectByPrimaryKey(Long id);

    ReComment selectWithSomeReply(Long id);

    List<ReComment> query(ReCommentQueryObject qo);
    List<ReComment> querySomeReply(Long parentId);

    int queryCount(ReCommentQueryObject qo);

    int updateByPrimaryKey(ReComment record);

    int replyCountAddOne(Long id);
}