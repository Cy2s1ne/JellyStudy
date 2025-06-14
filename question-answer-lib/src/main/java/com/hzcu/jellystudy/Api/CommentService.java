package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.Comment;
import java.util.List;

public interface CommentService {

    // 创建评论
    Comment createComment(Comment comment);

    // 更新评论
    Comment updateComment(String id, Comment commentUpdate);

    // 删除评论
    void deleteComment(String id);

    // 点赞评论
    Comment voteComment(String id, boolean isUpvote);

    // 获取回答的所有评论（包括子评论）
    List<Comment> getCommentsByAnswerId(String answerId);
}