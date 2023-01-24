package com.example.colorve.config;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class RestHttpClient{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private RestTemplate restTemplate;
    
//    @Autowired
//    private ObjectMapper customDeserializer;

    private HttpHeaders httpHeaders = new HttpHeaders();
    
    private final ConcurrentHashMap<Class<?>, JAXBContext> JAXB_CONTEXTS = new ConcurrentHashMap<>();
    
//	@Override
//	public void afterPropertiesSet() throws Exception {
//
//		if (customDeserializer == null) {
//			customDeserializer = new ObjectMapper();
//			customDeserializer.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		}
//		
//		customDeserializer.registerModule(new JavaTimeModule());
//		
//		if(restTemplate == null) {
//			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//            factory.setConnectTimeout(60000);
//            factory.setReadTimeout(60000);
//        	restTemplate = new RestTemplate(factory);
//		}
//	}
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public <S> S send(String endpoint,  Class<S> serviceClass, HttpMethod httpMethod, Object data) throws Exception {
        try{
            HttpEntity entity = null;
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            if(data != null) entity = new HttpEntity(data,httpHeaders);
            else entity = new HttpEntity(httpHeaders);

            ResponseEntity response = restTemplate.exchange(endpoint, httpMethod, entity, serviceClass);

            return (S) response.getBody();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <S> S sendWithHeaders(String endpoint,  Class<S> serviceClass, HttpMethod httpMethod, Object data, Map<String, String> mapHeader) throws Exception {
        try{
            HttpEntity entity = null;
            
            if (mapHeader != null && !mapHeader.isEmpty()) {
    			Iterator<String> iter = mapHeader.keySet().iterator();
    			while (iter.hasNext()) {
    				String name = iter.next();
    				String value = mapHeader.get(name);
    				httpHeaders.add(name, value);
    			}
    		}

            if(data != null) entity = new HttpEntity(data,httpHeaders);
            else entity = new HttpEntity(httpHeaders);

            ResponseEntity response = restTemplate.exchange(endpoint, httpMethod, entity, serviceClass);

            return (S) response.getBody();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	private <T> T unmarshallString(String xml, Class<T> clazz){
		
		try {
			JAXBContext jaxbContext = getContext(clazz);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new StringReader(xml));
			
		} catch (Exception e) {
			logger.error("::unmarshallString xml:{} , clazz:{}", xml, clazz, e);
			return null;
		}
	}
    
    private JAXBContext getContext(Class<?> clazz) throws JAXBException {
		
		JAXBContext jaxbContext = this.JAXB_CONTEXTS.get(clazz);
		
		if(jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(clazz);
			JAXB_CONTEXTS.putIfAbsent(clazz, jaxbContext);
		}
		
		return jaxbContext;
	}
}