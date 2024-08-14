package com.gyt.examservice.business.concretes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.examservice.api.clients.ManagementServiceClient;
import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.business.dtos.response.get.OptionDTO;
import com.gyt.examservice.business.rules.ExamBusinessRules;
import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.concretes.Rule;
import com.gyt.examservice.mapper.ExamMapper;
import com.gyt.examservice.mapper.RuleMapper;
import com.gyt.questionservice.GrpcGetQuestionRequest;
import com.gyt.questionservice.GrpcGetQuestionResponse;
import com.gyt.questionservice.GrpcOptionDTO;
import com.gyt.questionservice.QuestionServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;


class ExamManagerTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ManagementServiceClient managementServiceClient;

    @Mock
    private QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub;

    @Mock
    private ExamMapper examMapper;

    @Mock
    private RuleMapper ruleMapper;

    @Mock
    private ExamBusinessRules examBusinessRules;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ExamManager examManager; // Use the concrete class

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExamSuccess() {
        // Arrange
        CreateExamRequest createExamRequest = new CreateExamRequest();
        createExamRequest.setQuestionIds(List.of(1L, 2L, 3L));
        createExamRequest.setRules(List.of(new RuleDTO("Rule 1"), new RuleDTO("Rule 2")));
        createExamRequest.setStartDate(LocalDateTime.now().plusDays(1));
        createExamRequest.setEndDate(LocalDateTime.now().plusDays(2));

        GetUserResponse getUserResponse = new GetUserResponse(1L, "user@example.com", List.of("ROLE_USER"));

        Exam exam = new Exam();
        exam.setId(1L);

        List<RuleDTO> ruleDTOs = createExamRequest.getRules();
        List<Rule> rules = new ArrayList<>();
        for (RuleDTO ruleDTO : ruleDTOs) {
            Rule rule = new Rule();
            rule.setDescription(ruleDTO.getDescription());
            rules.add(rule);
        }

        // Mocking the business rules
        doNothing().when(examBusinessRules).validateExamDates(any(), any());
        doNothing().when(examBusinessRules).validateUniqueQuestions(any());

        // Mocking the management service client
        when(managementServiceClient.getAuthenticatedUser()).thenReturn(getUserResponse);

        // Mocking the exam mapper
        when(examMapper.createRequestToExam(createExamRequest)).thenReturn(exam);
        when(examMapper.createExamToResponse(exam)).thenReturn(new CreateExamResponse());

        // Mocking the rule mapper
        for (RuleDTO ruleDTO : ruleDTOs) {
            Rule rule = new Rule();
            rule.setDescription(ruleDTO.getDescription());
            when(ruleMapper.requestToRule(ruleDTO)).thenReturn(rule);
            when(ruleMapper.ruleToRuleDTO(rule)).thenReturn(ruleDTO);
        }

        // Mocking the exam repository
        when(examRepository.save(exam)).thenReturn(exam);

        // Mocking GrpcGetQuestionResponse for each question ID
        GrpcGetQuestionResponse grpcGetQuestionResponse1 = GrpcGetQuestionResponse.newBuilder()
                .addOptions(GrpcOptionDTO.newBuilder().setId(1L).setText("Option 1").setImageUrl("Image1").build())
                .build();

        GrpcGetQuestionResponse grpcGetQuestionResponse2 = GrpcGetQuestionResponse.newBuilder()
                .addOptions(GrpcOptionDTO.newBuilder().setId(2L).setText("Option 2").setImageUrl("Image2").build())
                .build();

        // Mocking the question service stub to return the mocked GrpcGetQuestionResponse
        when(questionServiceBlockingStub.getQuestionByID(GrpcGetQuestionRequest.newBuilder().setId(1L).build()))
                .thenReturn(grpcGetQuestionResponse1);
        when(questionServiceBlockingStub.getQuestionByID(GrpcGetQuestionRequest.newBuilder().setId(2L).build()))
                .thenReturn(grpcGetQuestionResponse2);
        when(questionServiceBlockingStub.getQuestionByID(GrpcGetQuestionRequest.newBuilder().setId(3L).build()))
                .thenReturn(grpcGetQuestionResponse1); // Mocking the third response similarly

        // Mocking the examMapper to convert GrpcGetQuestionResponse to GetQuestionResponse
        GetQuestionResponse getQuestionResponse1 = new GetQuestionResponse();
        getQuestionResponse1.setOptions(List.of(new OptionDTO(1L, "Option 1", "Image1")));

        GetQuestionResponse getQuestionResponse2 = new GetQuestionResponse();
        getQuestionResponse2.setOptions(List.of(new OptionDTO(2L, "Option 2", "Image2")));

        when(examMapper.grpcGetQuestionResponseToResponse(grpcGetQuestionResponse1)).thenReturn(getQuestionResponse1);
        when(examMapper.grpcGetQuestionResponseToResponse(grpcGetQuestionResponse2)).thenReturn(getQuestionResponse2);

        List<GetQuestionResponse> questionResponses = List.of(getQuestionResponse1, getQuestionResponse2, getQuestionResponse1);
        when(examManager.fetchAndMapQuestions(createExamRequest.getQuestionIds())).thenReturn(questionResponses);

        // Act
        CreateExamResponse createExamResponse = examManager.createExam(createExamRequest);

        // Assert
        assertNotNull(createExamResponse);
        assertEquals(exam.getId(), createExamResponse.getId());
        assertEquals(createExamRequest.getRules().size(), createExamResponse.getRules().size());
        assertEquals(questionResponses, createExamResponse.getQuestions());

        verify(examBusinessRules).validateExamDates(any(), any());
        verify(examBusinessRules).validateUniqueQuestions(createExamRequest.getQuestionIds());
        verify(examMapper).createRequestToExam(createExamRequest);
        verify(examRepository).save(exam);
        verify(examMapper).createExamToResponse(exam);
        verify(examManager).fetchAndMapQuestions(createExamRequest.getQuestionIds());
    }
}