package com.gyt.questionservice.business.concretes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.kafka.producer.QuestionProducer;
import com.gyt.questionservice.mapper.OptionMapper;
import com.gyt.questionservice.mapper.QuestionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class QuestionManagerTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionBusinessRules questionBusinessRules;

    @Mock
    private OptionBusinessRules optionBusinessRules;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private OptionMapper optionMapper;

    @Mock
    private OptionService optionService;

    @Mock
    private ManagementServiceClient managementServiceClient;

    @Mock
    private QuestionProducer questionProducer;

    @InjectMocks
    private QuestionManager questionManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createQuestion_success() {
        // Arrange
        CreateOptionRequest optionRequest1 = new CreateOptionRequest("Option 1", true, null);
        CreateOptionRequest optionRequest2 = new CreateOptionRequest("Option 2", false, null);
        List<CreateOptionRequest> optionRequests = Arrays.asList(optionRequest1, optionRequest2);
        CreateQuestionRequest createQuestionRequest = new CreateQuestionRequest("Question text", null, optionRequests);

        CreateOptionResponse optionResponse1 = new CreateOptionResponse(1L, "Option 1", true, null);
        CreateOptionResponse optionResponse2 = new CreateOptionResponse(2L, "Option 2", false, null);
        List<CreateOptionResponse> optionResponses = Arrays.asList(optionResponse1, optionResponse2);

        GetUserResponse mockUserResponse = new GetUserResponse();
        mockUserResponse.setRoles(List.of("ROLE_USER"));
        mockUserResponse.setId(1L);

        Question mockQuestion = new Question();
        CreateQuestionResponse mockCreateQuestionResponse = new CreateQuestionResponse();
        mockCreateQuestionResponse.setText("Question text");
        mockCreateQuestionResponse.setOptionList(optionResponses);

        when(managementServiceClient.getAuthenticatedUser()).thenReturn(mockUserResponse);
        when(questionMapper.createRequestToQuestion(createQuestionRequest)).thenReturn(mockQuestion);
        when(questionRepository.save(any(Question.class))).thenReturn(mockQuestion);
        when(questionMapper.createQuestionToResponse(any(Question.class))).thenReturn(mockCreateQuestionResponse);

        Option mockOption1 = new Option();
        mockOption1.setId(1L);
        Option mockOption2 = new Option();
        mockOption2.setId(2L);

        when(optionMapper.createRequestToOption(optionRequest1)).thenReturn(mockOption1);
        when(optionMapper.createRequestToOption(optionRequest2)).thenReturn(mockOption2);
        doNothing().when(optionService).saveOption(any(Option.class));

        // Act
        CreateQuestionResponse response = questionManager.createQuestion(createQuestionRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Question text", response.getText());
//        assertEquals(optionResponses, response.getOptionList());
        verify(questionBusinessRules).textAndImageValidationRule(createQuestionRequest.getText(), createQuestionRequest.getImageUrl());
        verify(optionBusinessRules, times(2)).correctOptionCheck(optionRequests); // Make sure this is called twice
        verify(questionRepository).save(any(Question.class));
        verify(optionService, times(2)).saveOption(any(Option.class));
        verify(optionMapper, times(2)).createOptionToResponse(any(Option.class));
        verify(questionProducer).sendQuestionForCreate(any()); // Verify that the event is sent to Kafka
    }

    @Test
    void createQuestion_shouldThrowException_whenValidationFails() {
        // Arrange
        CreateOptionRequest optionRequest1 = new CreateOptionRequest("Option 1", true, null);
        CreateOptionRequest optionRequest2 = new CreateOptionRequest("Option 2", false, null);
        List<CreateOptionRequest> optionRequests = Arrays.asList(optionRequest1, optionRequest2);
        CreateQuestionRequest createQuestionRequest = new CreateQuestionRequest("Question text", null, optionRequests);

        doThrow(new RuntimeException("Validation error")).when(questionBusinessRules)
                .textAndImageValidationRule(anyString(), anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionManager.createQuestion(createQuestionRequest);
        });

        assertEquals("Validation error", exception.getMessage());
        verify(questionBusinessRules).textAndImageValidationRule(createQuestionRequest.getText(), createQuestionRequest.getImageUrl());
        verify(optionBusinessRules, never()).correctOptionCheck(anyList());
        verify(questionRepository, never()).save(any(Question.class));
    }
}