package com.example.colorve.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.colorve.config.RestHttpClient;
import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.color.ColorDetailModel;
import com.example.colorve.model.db.QuestionDTO;
import com.example.colorve.model.db.UserDTO;
import com.example.colorve.model.db.UserQuestionAnswersDTO;
import com.example.colorve.model.question.UserQuestionAnswerResponseModel;
import com.example.colorve.repository.QuestionRepository;
import com.example.colorve.repository.UserQuestionAnswerRepository;
import com.example.colorve.repository.UserRepository;
import com.example.colorve.service.IUserQuestionAnswerService;
import com.example.colorve.util.StringUtils;
import com.google.gson.Gson;

@Service
public class UserQuestionAnswerService implements IUserQuestionAnswerService{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserQuestionAnswerRepository userQuestionAnswerRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private RestHttpClient restHttpClient;
	
	@Override
	public BaseResponseModel<List<UserQuestionAnswersDTO>> findUserQuestionAnswersByUserId(String userId) {
		BaseResponseModel<List<UserQuestionAnswersDTO>> response = new BaseResponseModel<>();
		List<UserQuestionAnswersDTO> answers = userQuestionAnswerRepository.findUserQuestionAnswersByUserId(userId);
		
		if(CollectionUtils.isEmpty(answers))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "No user answers founded!");
		
		response.setResult(answers);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<UserQuestionAnswersDTO> getUserQuestionAnswerById(String id) {
		BaseResponseModel<UserQuestionAnswersDTO> response = new BaseResponseModel<>();
		UserQuestionAnswersDTO answer = userQuestionAnswerRepository.findById(id);
		
		if(answer == null)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "User Question answer not founded bu given id id:".concat(id));
		
		response.setResult(answer);
		response.setSuccess(true);
		return response;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponseModel<UserQuestionAnswerResponseModel> addOrUpdateUserQuestionAnswer(
			UserQuestionAnswersDTO request) {
		BaseResponseModel<UserQuestionAnswerResponseModel> response = new BaseResponseModel<>();
		UserQuestionAnswerResponseModel answerModel = new UserQuestionAnswerResponseModel();
		
		if(request == null || StringUtils.isNullOrEmpty(request.getQuestionId()) || StringUtils.isNullOrEmpty(request.getUserId()) ||
				request.getAnswer() == null)
			return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Request invalid - Check questionId, userId or answer data!");
		
		//Soruyu alıp group ve digit setleyelim ---------------------
		QuestionDTO question = questionRepository.findById(request.getQuestionId());
		
		if(question == null)
			return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "Question not founded by given id!");
		
		UserDTO userDto = userRepository.findById(request.getUserId());
		
		if(userDto == null)
			return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "User not founded by given id!");
		
		request.setQuestionGroupId(question.getGroupId());
		request.setQuestionGroupDigit(question.getDigit());
		
		//Aynı digit ve group id ile cevap verişmiş mi ---------------------
		List<UserQuestionAnswersDTO> userQuestions = userQuestionAnswerRepository.findUserQuestionAnswersByUserId(request.getUserId());
		
		if(!CollectionUtils.isEmpty(userQuestions)) {
			UserQuestionAnswersDTO isAnswered = userQuestions
					.stream().filter(u -> u.getQuestionId().equals(question.getId())).findFirst().orElse(null);
			
			if(isAnswered != null)
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "This question was answered before by user!");
			
			UserQuestionAnswersDTO foundedQuestion = userQuestions.stream()
				.filter(u -> (u.getQuestionGroupId() == question.getGroupId() && u.getQuestionGroupDigit() == question.getDigit()))
				.findFirst().orElse(null);
			
			if(foundedQuestion != null)
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Aynı grup ve digit için daha önce cevap verilmiş!");
		}
		
		//Herhangi bir sorun yoksa cevap kaydedilir -------------------------
		UserQuestionAnswersDTO savedData = userQuestionAnswerRepository.addOrupdateUserQuestionAnswer(request);
		
		if(savedData == null || StringUtils.isNullOrEmpty(savedData.getId()))
			return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "User answer cannot be saved!");
		
		answerModel.setAnswer(savedData);
		
		List<UserQuestionAnswersDTO> userAnswers = userQuestionAnswerRepository.findUserQuestionAnswersByUserId(request.getUserId());
		
		if(CollectionUtils.isEmpty(userAnswers))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "User answers cannot be founded!");
		
		if(userAnswers.size() > 24)
			return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "24 ten fazla soruya cevap verilmiş. Hatalı durum!");
		
		if(userAnswers.size() == 24) {
			BaseResponseModel resp = checkValidityOfAnswers(userAnswers);

			if(resp.isSuccess()) {
				answerModel.setCompleted(true);
				String colorCode = calculateColorHexCode(userAnswers);
				
				if(!StringUtils.isNullOrEmpty(colorCode)) {
					answerModel.setColorHexValue(colorCode);
					userDto.setColorHex(colorCode);
					ColorDetailModel colorDetail = getColorDetail(colorCode);
					
					if(colorDetail != null) {
						if(colorDetail.getName() != null) {
							answerModel.setColorName(colorDetail.getName().getValue());
							userDto.setColorName(colorDetail.getName().getValue());
						}
						if(colorDetail.getRgb() != null) {
							answerModel.setRgbValue(colorDetail.getRgb().getValue());
							userDto.setRgbValue(colorDetail.getRgb().getValue());
						}
						if(colorDetail.getImage() != null) {
							answerModel.setColorImageUrl(colorDetail.getImage().getBare());
							userDto.setColorImageUrl(colorDetail.getImage().getBare());
						}
					}	
				}
				userRepository.updateUser(userDto);
			}
		}
		
		response.setSuccess(true);
		response.setResult(answerModel);
		logger.info("::addOrUpdateUserQuestionAnswer response:{}", gson.toJson(response));

		return response;	
	}
	
	private ColorDetailModel getColorDetail(String colorCode) {
		
		String url = "https://www.thecolorapi.com/id?hex=" + colorCode ;
		
		try {
			ColorDetailModel responseApiModel = restHttpClient.send(url, ColorDetailModel.class, HttpMethod.GET, null);
			return responseApiModel;
		} catch (Exception e) {
			logger.error("::getColorDetail exception colorCode:{}", colorCode, e);
			e.printStackTrace();
		}
		return null;
	}
	
	private String calculateColorHexCode(List<UserQuestionAnswersDTO> answersData) {
		if(CollectionUtils.isEmpty(answersData))
			return null;
		
		try {
			String colorCode = "";
			colorCode = colorCode + getHexValue(answersData, 1);
			colorCode = colorCode + getHexValue(answersData, 2);
			colorCode = colorCode + getHexValue(answersData, 3);
			colorCode = colorCode + getHexValue(answersData, 4);
			colorCode = colorCode + getHexValue(answersData, 5);
			colorCode = colorCode + getHexValue(answersData, 6);
			
			return colorCode;
		}catch(Exception e) {
			logger.error("::calculateColorHexCode exception ", e);
			return null;
		}
	}
	
	private String getHexValue(List<UserQuestionAnswersDTO> answersData, int groupId) {
		String bitmap = "";
		List<UserQuestionAnswersDTO> foundedAnswers = answersData.stream().filter(u -> u.getQuestionGroupId() == groupId).collect(Collectors.toList());
		UserQuestionAnswersDTO foundedAnswer = foundedAnswers.stream().filter(u -> u.getQuestionGroupDigit() == 1).findFirst().orElse(null);
		bitmap = bitmap + String.valueOf(foundedAnswer.getAnswer().getValue());
		
		foundedAnswer = foundedAnswers.stream().filter(u -> u.getQuestionGroupDigit() == 2).findFirst().orElse(null);
		bitmap = bitmap + String.valueOf(foundedAnswer.getAnswer().getValue());
		
		foundedAnswer = foundedAnswers.stream().filter(u -> u.getQuestionGroupDigit() == 3).findFirst().orElse(null);
		bitmap = bitmap + String.valueOf(foundedAnswer.getAnswer().getValue());
		
		foundedAnswer = foundedAnswers.stream().filter(u -> u.getQuestionGroupDigit() == 4).findFirst().orElse(null);
		bitmap = bitmap + String.valueOf(foundedAnswer.getAnswer().getValue());
		
		logger.info("::fillBitmap bitmap:{}", bitmap);
		
		int decimal = Integer.parseInt(bitmap, 2);
		String hex = Integer.toHexString(decimal);
		
		logger.info("::fillBitmap hex:{}", hex);
		
		return hex;
	}

	@SuppressWarnings("rawtypes")
	private BaseResponseModel checkValidityOfAnswers(List<UserQuestionAnswersDTO> answers){
		if(CollectionUtils.isEmpty(answers))
			return new BaseResponseModel<Boolean>(false);
		
		List<UserQuestionAnswersDTO> foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 1).collect(Collectors.toList());
		
		if(!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(),"1.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");
		
		if(foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);
		
		//--------------
		foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 2).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel(ErrorTypeEnum.ERROR.name(),"2.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");

		if (foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);

		// --------------
		foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 3).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel(ErrorTypeEnum.ERROR.name(),"3.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");

		if (foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);

		// --------------
		foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 4).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel(ErrorTypeEnum.ERROR.name(),"4.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");

		if (foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);

		// --------------
		foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 5).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel(ErrorTypeEnum.ERROR.name(),"5.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");

		if (foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);

		// --------------
		foundedAnswers = answers.stream().filter(u -> u.getQuestionGroupId() == 6).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(foundedAnswers) && foundedAnswers.size() > 4)
			return new BaseResponseModel(ErrorTypeEnum.ERROR.name(),"6.grup - Grup bazında 4 ten fazla soruya cevap verilmiş. Renk hesaplanamaz!");

		if (foundedAnswers.size() != 4)
			return new BaseResponseModel<Boolean>(false);
		
		return new BaseResponseModel<Boolean>(true);
	}
}
