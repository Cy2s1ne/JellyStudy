package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.QA_table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QAtableRepository extends MongoRepository<QA_table, String> {
    // 基本的CRUD操作通过MongoRepository已经提供
} 