package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    // 根据父ID查询评论（可以是回答ID或评论ID）
    List<Comment> findByParentId(String parentId);

    // 根据路径前缀查询所有子评论
    List<Comment> findByPathStartingWith(String pathPrefix, Sort sort);

    // 查询用户的所有评论
    List<Comment> findByAuthorUserId(String userId);
}
