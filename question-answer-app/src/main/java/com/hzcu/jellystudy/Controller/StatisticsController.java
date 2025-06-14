package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.StatisticsService;
import com.hzcu.jellystudy.Entity.Answer;
import com.hzcu.jellystudy.Entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // 获取问题总数
    @GetMapping("/questions/count")
    public ResponseEntity<Long> getTotalQuestionCount() {
        long count = statisticsService.getTotalQuestionCount();
        return ResponseEntity.ok(count);
    }

    // 获取最热问题
    @GetMapping("/questions/hot")
    public ResponseEntity<List<Question>> getHotQuestions(
            @RequestParam(defaultValue = "10") int limit) {
        List<Question> questions = statisticsService.getHotQuestions(limit);
        return ResponseEntity.ok(questions);
    }

    // 获取高赞回答
    @GetMapping("/answers/top-voted")
    public ResponseEntity<List<Answer>> getTopVotedAnswers(
            @RequestParam(defaultValue = "10") int limit) {
        List<Answer> answers = statisticsService.getTopVotedAnswers(limit);
        return ResponseEntity.ok(answers);
    }

    // 获取总体统计信息
    @GetMapping("/overall")
    public ResponseEntity<Map<String, Object>> getOverallStatistics() {
        Map<String, Object> statistics = statisticsService.getOverallStatistics();
        return ResponseEntity.ok(statistics);
    }
}
