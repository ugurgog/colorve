package com.example.colorve.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.colorve.model.db.TokenInfoDTO;

@Repository
public class TokenInfoRepository {
 
    @Autowired
    private MongoTemplate mongoTemplate;
 
    public List<TokenInfoDTO> findAll() {
        return mongoTemplate.findAll(TokenInfoDTO.class);
    }

    public TokenInfoDTO findById(String id) {
        return mongoTemplate.findById(id, TokenInfoDTO.class);
    }
    
    public TokenInfoDTO findBySite(String site) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("site").is(site));
        return mongoTemplate.findOne(query, TokenInfoDTO.class);
    }
}
