package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.mapper.QuestionMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionManager implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ManagementServiceClient managementServiceClient;

    @Override
    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {

        Question question = QuestionMapper.INSTANCE.createRequestToQuestion(request);
        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        question.setCreatorId(authenticatedUser.getId());
        questionRepository.save(question);
        CreateQuestionResponse questionToResponse = QuestionMapper.INSTANCE.createQuestionToResponse(question);
        return questionToResponse;
    }
}
