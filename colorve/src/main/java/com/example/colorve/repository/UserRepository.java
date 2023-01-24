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

import com.example.colorve.model.db.UserDTO;

@Repository
public class UserRepository implements InitializingBean{
 
    @Autowired
    private MongoTemplate mongoTemplate;
 
    public List<UserDTO> findAll() {
        return mongoTemplate.findAll(UserDTO.class);
    }
    
    public List<UserDTO> findAllByStatus(String status) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        return mongoTemplate.find(query, UserDTO.class);
    }
    
    public UserDTO findByEmail(String email) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, UserDTO.class);
    }
    
    public UserDTO findById(String id) {
        return mongoTemplate.findById(id, UserDTO.class);
    }
    
    public UserDTO updateUser(UserDTO user){
        return mongoTemplate.save(user);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		mongoTemplate.indexOps(UserDTO.class).ensureIndex(new Index().on("email", Direction.ASC).unique());
	}
}
