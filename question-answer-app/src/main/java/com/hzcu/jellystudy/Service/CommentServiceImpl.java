package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.CommentService;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Comment;
import com.hzcu.jellystudy.Repository.AnswerRepository;
import com.hzcu.jellystudy.Repository.CommentRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@DubboService(version = "1.0.0")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // 创建评论
    @Override
    public Comment createComment(Comment comment) {
        Date now = new Date();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);
        comment.setVoteCount(0);

        // 设置评论深度
        if (comment.getParentId().startsWith("A-")) {
            // 一级评论，父级是回答
            comment.setDepth(1);
        } else {
            // 子评论，父级是评论
            Comment parentComment = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            comment.setDepth(parentComment.getDepth() + 1);
        }

        Comment savedComment = commentRepository.save(comment);

        // 设置路径
        if (comment.getDepth() == 1) {
            // 一级评论
            Answer answer = answerRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new RuntimeException("回答不存在"));
            savedComment.setPath(answer.getPath() + ".C-" + savedComment.getId());

            // 更新回答的评论数
            answer.setCommentCount(answer.getCommentCount() + 1);
            answerRepository.save(answer);
        } else {
            // 子评论
            Comment parentComment = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            savedComment.setPath(parentComment.getPath() + ".C-" + savedComment.getId());
        }

        return commentRepository.save(savedComment);
    }

    // 更新评论
    @Override
    public Comment updateComment(String id, Comment commentUpdate) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 只允许更新内容
        existingComment.setContent(commentUpdate.getContent());
        existingComment.setUpdatedAt(new Date());

        return commentRepository.save(existingComment);
    }

    // 删除评论
    @Override
    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 删除所有子评论
        List<Comment> childComments = commentRepository.findByPathStartingWith(
                comment.getPath(), Sort.by("path"));
        commentRepository.deleteAll(childComments);

        // 删除评论本身
        commentRepository.delete(comment);

        // 如果是一级评论，更新回答的评论数
        if (comment.getDepth() == 1) {
            Answer answer = answerRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new RuntimeException("回答不存在"));
            answer.setCommentCount(answer.getCommentCount() - 1 - childComments.size());
            answerRepository.save(answer);
        }
    }

    // 点赞评论
    @Override
    public Comment voteComment(String id, boolean isUpvote) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        if (isUpvote) {
            comment.setVoteCount(comment.getVoteCount() + 1);
        } else {
            comment.setVoteCount(comment.getVoteCount() - 1);
        }

        return commentRepository.save(comment);
    }

    // 获取回答的所有评论（包括子评论）
    @Override
    public List<Comment> getCommentsByAnswerId(String answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("回答不存在"));

        return commentRepository.findByPathStartingWith(
                answer.getPath() + ".C-", Sort.by("path"));
    }
}