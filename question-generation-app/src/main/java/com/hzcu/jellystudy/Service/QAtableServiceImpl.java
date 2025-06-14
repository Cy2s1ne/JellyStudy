package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.QAtableService;
import com.hzcu.jellystudy.Entity.QA_table;
import com.hzcu.jellystudy.Repository.QAtableRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0")
public class QAtableServiceImpl implements QAtableService {

    @Autowired
    private QAtableRepository qAtableRepository;

    @Override
    public QA_table createQAtable(QA_table qa_table) {
        return qAtableRepository.save(qa_table);
    }

    @Override
    public QA_table updateQAtable(String id, QA_table qa_tableUpdate) {
        QA_table existingQA = qAtableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("问答记录不存在"));
        
        // 更新字段
        existingQA.setQuestion(qa_tableUpdate.getQuestion());
        existingQA.setAnswer(qa_tableUpdate.getAnswer());
        existingQA.setExplanation(qa_tableUpdate.getExplanation());
        
        return qAtableRepository.save(existingQA);
    }

    @Override
    public void deleteQAtable(String id) {
        qAtableRepository.deleteById(id);
    }

    @Override
    public QA_table getQAtableById(String id) {
        return qAtableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("问答记录不存在"));
    }
} 