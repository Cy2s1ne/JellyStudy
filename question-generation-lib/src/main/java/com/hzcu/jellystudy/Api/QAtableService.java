package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.QA_table;

public interface QAtableService {
    QA_table createQAtable(QA_table qa_table);
    QA_table updateQAtable(String id, QA_table qa_tableUpdate);
    void deleteQAtable(String id);
    QA_table getQAtableById(String id);
}
