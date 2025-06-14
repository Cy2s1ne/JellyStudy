package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.AnswerService;
import com.hzcu.jellystudy.Entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // 创建回答
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer) {
        Answer createdAnswer = answerService.createAnswer(answer);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    // 更新回答
    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(
            @PathVariable String id, @RequestBody Answer answer) {
        Answer updatedAnswer = answerService.updateAnswer(id, answer);
        return ResponseEntity.ok(updatedAnswer);
    }

    // 删除回答
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable String id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    // 接受回答
    @PutMapping("/{id}/accept")
    public ResponseEntity<Answer> acceptAnswer(@PathVariable String id) {
        Answer acceptedAnswer = answerService.acceptAnswer(id);
        return ResponseEntity.ok(acceptedAnswer);
    }

    // 点赞回答
    @PostMapping("/{id}/vote")
    public ResponseEntity<Answer> voteAnswer(
            @PathVariable String id, @RequestParam boolean upvote) {
        Answer votedAnswer = answerService.voteAnswer(id, upvote);
        return ResponseEntity.ok(votedAnswer);
    }

    // 获取问题的所有回答
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(
            @PathVariable String questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    // 获取高赞回答
    @GetMapping("/top-voted")
    public ResponseEntity<List<Answer>> getTopVotedAnswers(
            @RequestParam(defaultValue = "10") int limit) {
        List<Answer> answers = answerService.getTopVotedAnswers(limit);
        return ResponseEntity.ok(answers);
    }
}