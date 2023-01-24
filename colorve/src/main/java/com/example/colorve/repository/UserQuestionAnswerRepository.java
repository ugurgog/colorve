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

import com.example.colorve.model.db.UserQuestionAnswersDTO;

@Repository
public class UserQuestionAnswerRepository implements InitializingBean{
 
    @Autowired
    private MongoTemplate mongoTemplate;
 
    public List<UserQuestionAnswersDTO> findUserQuestionAnswersByUserId(String userId) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, UserQuestionAnswersDTO.class);
    }
 
    public UserQuestionAnswersDTO findById(String id) {
        return mongoTemplate.findById(id, UserQuestionAnswersDTO.class);
    }
    
    public UserQuestionAnswersDTO addOrupdateUserQuestionAnswer(UserQuestionAnswersDTO question){
        return mongoTemplate.save(question);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		mongoTemplate.indexOps(UserQuestionAnswersDTO.class)
			.ensureIndex(new Index().on("questionId", Direction.ASC).on("userId", Direction.ASC));
	}
}
