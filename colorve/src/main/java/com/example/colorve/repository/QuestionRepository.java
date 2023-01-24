package com.example.colorve.repository;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.colorve.model.db.QuestionDTO;
import com.example.colorve.util.StringUtils;

@Repository
public class QuestionRepository implements InitializingBean{
 
    @Autowired
    private MongoTemplate mongoTemplate;
 
    public List<QuestionDTO> findAll() {
        return mongoTemplate.findAll(QuestionDTO.class);
    }
    
    public List<QuestionDTO> findAllByCriteria(String status, Integer groupId, Integer digit) {
    	Query query = new Query();
    	
    	if(!StringUtils.isNullOrEmpty(status))
    		query.addCriteria(Criteria.where("status").is(status));
    	
    	if(groupId != null && groupId.intValue() > 0)
    		query.addCriteria(Criteria.where("groupId").is(groupId));
    	
    	if(digit != null && digit.intValue() > 0)
    		query.addCriteria(Criteria.where("digit").is(digit));
    	
        return mongoTemplate.find(query, QuestionDTO.class);
    }
    
    public List<QuestionDTO> findQuestionsByGroupIdAndLimit(int groupId, int limit) {
    	Query query = new Query().limit(limit);
        query.addCriteria(Criteria.where("groupId").is(groupId))
        	.addCriteria(Criteria.where("status").is("ACTIVE"));
        return mongoTemplate.find(query, QuestionDTO.class);
    }
 
    public QuestionDTO findById(String id) {
        return mongoTemplate.findById(id, QuestionDTO.class);
    }
    
    public QuestionDTO updateQuestion(QuestionDTO question){
        return mongoTemplate.save(question);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		mongoTemplate.indexOps(QuestionDTO.class).ensureIndex(new Index().on("question", Direction.ASC).unique());
	}
}
