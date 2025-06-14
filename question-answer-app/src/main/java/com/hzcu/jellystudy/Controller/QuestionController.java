package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.AnswerService;
import com.hzcu.jellystudy.Api.CommentService;
import com.hzcu.jellystudy.Api.QuestionService;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Comment;
import com.hzcu.jellystudy.Entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CommentService commentService;

    // 创建问题
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    // 获取问题详情
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable String id) {
        Question question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    // 更新问题
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable String id, @RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestion(id, question);
        return ResponseEntity.ok(updatedQuestion);
    }

    // 删除问题
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    // 获取高人气问题
    @GetMapping("/high-popularity")
    public ResponseEntity<List<Question>> getHighPopularityQuestions(@RequestParam("limit") int limit) {
        List<Question> questions = questionService.getHighPopularityQuestions(limit);
        return ResponseEntity.ok(questions);
    }

    // 获取热门问题
    @GetMapping("/hot")
    public ResponseEntity<List<Question>> getHotQuestions(
            @RequestParam(defaultValue = "10") int limit) {
        List<Question> questions = questionService.getHotQuestions(limit);
        return ResponseEntity.ok(questions);
    }

    // 获取推荐问题列表
    @GetMapping("/recommended")
    public ResponseEntity<List<Question>> getRecommendedQuestions(
            @RequestParam(defaultValue = "10") int limit) {
        List<Question> questions = questionService.getRecommendedQuestions(limit);
        return ResponseEntity.ok(questions);
    }

    // 获取知识点相关问题列表
    @GetMapping("/by-knowledge/{knowledgePointId}")
    public ResponseEntity<List<Question>> getQuestionsByKnowledgePoint(
            @PathVariable String knowledgePointId) {
        List<Question> questions = questionService.getQuestionsByKnowledgePoint(knowledgePointId);
        return ResponseEntity.ok(questions);
    }

    // 搜索问题
    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(@RequestParam String query) {
        List<Question> questions = questionService.searchQuestions(query);
        return ResponseEntity.ok(questions);
    }

    // 获取问题完整数据（包括回答、评论等）
    @GetMapping("/{id}/full")
    public ResponseEntity<Map<String, Object>> getQuestionFullData(@PathVariable String id) {
        Question question = questionService.getQuestionById(id);
        List<Answer> answers = answerService.getAnswersByQuestionId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("question", question);

        Map<String, Object> answersWithComments = new HashMap<>();
        for (Answer answer : answers) {
            List<Comment> comments = commentService.getCommentsByAnswerId(answer.getId());
            Map<String, Object> answerData = new HashMap<>();
            answerData.put("answer", answer);
            answerData.put("comments", comments);
            answersWithComments.put(answer.getId(), answerData);
        }

        result.put("answersWithComments", answersWithComments);
        return ResponseEntity.ok(result);
    }
    
    // 设置问题悬赏金额
    @PostMapping("/{id}/bounty")
    public ResponseEntity<Question> setQuestionBounty(
            @PathVariable String id, @RequestParam int money) {
        Question question = questionService.setQuestionBounty(id, money);
        return ResponseEntity.ok(question);
    }
    
    // 接受回答并发放悬赏
    @PostMapping("/{questionId}/accept-answer/{answerId}")
    public ResponseEntity<Question> acceptAnswerWithBounty(
            @PathVariable String questionId, @PathVariable String answerId) {
        Question question = questionService.acceptAnswerWithBounty(questionId, answerId);
        return ResponseEntity.ok(question);
    }
}