package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.CommentService;
import com.hzcu.jellystudy.Entity.Comment;
import com.hzcu.jellystudy.Service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 创建评论
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 更新评论
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable String id, @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(id, comment);
        return ResponseEntity.ok(updatedComment);
    }

    // 删除评论
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // 点赞评论
    @PostMapping("/{id}/vote")
    public ResponseEntity<Comment> voteComment(
            @PathVariable String id, @RequestParam boolean upvote) {
        Comment votedComment = commentService.voteComment(id, upvote);
        return ResponseEntity.ok(votedComment);
    }

    // 获取回答的所有评论
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<List<Comment>> getCommentsByAnswerId(
            @PathVariable String answerId) {
        List<Comment> comments = commentService.getCommentsByAnswerId(answerId);
        return ResponseEntity.ok(comments);
    }
}