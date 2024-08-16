package com.gyt.questionservice.business.concretes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.mapper.OptionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class OptionManagerTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OptionBusinessRules optionBusinessRules;

    @Mock
    private QuestionBusinessRules questionBusinessRules;

    @Mock
    private MessageService messageService;

    @Mock
    private OptionMapper optionMapper;

    @InjectMocks
    private OptionManager optionManager;

    @Test
    void updateOption_success() {
        Long optionId = 1L;
        UpdateOptionRequest request = new UpdateOptionRequest(optionId, "Updated Option Text", true, "http://example.com/image.jpg");

        Option existingOption = new Option();
        existingOption.setId(optionId);
        existingOption.setText("Old Option Text");
        existingOption.setQuestion(new Question());

        UpdateOptionResponse expectedResponse = new UpdateOptionResponse();
        expectedResponse.setId(optionId);
        expectedResponse.setText("Updated Option Text");
        expectedResponse.setQuestionId(existingOption.getQuestion().getId());

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        when(optionMapper.updateRequestToOption(request)).thenReturn(existingOption);
        when(optionMapper.updateOptionToResponse(existingOption)).thenReturn(expectedResponse);

        UpdateOptionResponse response = optionManager.updateOption(request);

        assertNotNull(response);
        assertEquals("Updated Option Text", response.getText());
        assertEquals(existingOption.getQuestion().getId(), response.getQuestionId());
        verify(optionRepository).findById(optionId);
        verify(optionBusinessRules).userAuthorizationCheck(existingOption.getQuestion().getCreatorId());
        verify(questionBusinessRules).checkIfQuestionIsEditable(existingOption.getQuestion().getIsEditable());
        verify(optionBusinessRules).textAndImageValidationRule(request.getText(), request.getImageUrl());
        verify(optionBusinessRules).validateCorrectOption(existingOption);
        verify(optionMapper).updateRequestToOption(request);
        verify(optionRepository).save(existingOption);
        verify(optionMapper).updateOptionToResponse(existingOption);
    }

    @Test
    void updateOption_optionNotFound() {
        Long optionId = 1L;
        UpdateOptionRequest request = new UpdateOptionRequest(optionId, "Updated Option Text", true, "http://example.com/image.jpg");

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist)).thenReturn("Option does not exist");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.updateOption(request);
        });
        assertEquals("Option does not exist", exception.getMessage());
    }

    @Test
    void updateOption_userAuthorizationFails() {
        Long optionId = 1L;
        UpdateOptionRequest request = new UpdateOptionRequest(optionId, "Updated Option Text", true, "http://example.com/image.jpg");

        Option existingOption = new Option();
        existingOption.setId(optionId);
        existingOption.setQuestion(new Question());

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        doThrow(new BusinessException("User is not authorized")).when(optionBusinessRules).userAuthorizationCheck(existingOption.getQuestion().getCreatorId());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.updateOption(request);
        });
        assertEquals("User is not authorized", exception.getMessage());
    }

    @Test
    void updateOption_questionNotEditable() {
        Long optionId = 1L;
        UpdateOptionRequest request = new UpdateOptionRequest(optionId, "Updated Option Text", true, "http://example.com/image.jpg");

        Option existingOption = new Option();
        existingOption.setId(optionId);
        existingOption.setQuestion(new Question());
        existingOption.getQuestion().setIsEditable(false); // Set question as not editable

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        doThrow(new BusinessException("Question is not editable")).when(questionBusinessRules).checkIfQuestionIsEditable(existingOption.getQuestion().getIsEditable());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.updateOption(request);
        });
        assertEquals("Question is not editable", exception.getMessage());
    }

    @Test
    void updateOption_validationFails() {
        Long optionId = 1L;
        UpdateOptionRequest request = new UpdateOptionRequest(optionId, "", true, "");

        Option existingOption = new Option();
        existingOption.setId(optionId);
        existingOption.setQuestion(new Question());

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        doThrow(new BusinessException("Validation failed")).when(optionBusinessRules).textAndImageValidationRule(request.getText(), request.getImageUrl());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.updateOption(request);
        });
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    void getOptionByID_success() {
        // Arrange
        Long optionId = 1L;
        Option option = new Option();
        option.setId(optionId);

        GetOptionResponse expectedResponse = new GetOptionResponse();
        expectedResponse.setId(optionId);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(optionMapper.getOptionToResponse(option)).thenReturn(expectedResponse);

        // Act
        GetOptionResponse response = optionManager.getOptionByID(optionId);

        // Assert
        assertNotNull(response);
        assertEquals(optionId, response.getId());
        verify(optionRepository).findById(optionId);
        verify(optionMapper).getOptionToResponse(option);
    }

    @Test
    void getOptionByID_optionNotFound() {
        // Arrange
        Long optionId = 1L;
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist)).thenReturn("Option does not exist");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.getOptionByID(optionId);
        });
        assertEquals("Option does not exist", exception.getMessage());
        verify(optionRepository).findById(optionId);
        verify(optionMapper, never()).getOptionToResponse(any(Option.class)); // Ensure that the mapper method is not called
    }

    @Test
    void getAllOptions_success() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        Option option = new Option(); // You can populate it with test data if needed
        Page<Option> optionsPage = new PageImpl<>(Collections.singletonList(option), pageable, 1);

        GetAllOptionResponse response = new GetAllOptionResponse(); // Populate this if needed
        when(optionRepository.findAll(pageable)).thenReturn(optionsPage);
        when(optionMapper.getAllOptionToResponse(option)).thenReturn(response);

        Page<GetAllOptionResponse> result = optionManager.getAllOptions(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(response, result.getContent().get(0));
        verify(optionRepository).findAll(pageable);
        verify(optionMapper).getAllOptionToResponse(option);
    }

    @Test
    void deleteOption_success() {

        // Option ve Question nesnelerini initialize ediyoruz
        Question question = new Question();
        question.setId(1L);
        question.setCreatorId(2L);
        question.setIsEditable(true);

        Option option = new Option();
        option.setId(1L);
        option.setQuestion(question);

        question.setOptions(List.of(option));

        // Option id'si ile ilgili option'ı repository'den döndürüyoruz
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));

        // Option'ı silme işlemi
        optionManager.deleteOption(option.getId());

        // Metot içinde yapılan kontrollerin her birinin çağrıldığını doğruluyoruz
        verify(optionRepository).findById(option.getId());
        verify(optionBusinessRules).userAuthorizationCheck(question.getCreatorId());
        verify(questionBusinessRules).checkIfQuestionIsEditable(question.getIsEditable());
        verify(optionBusinessRules).atLeastTwoAnswerChecks(question.getOptions());
        verify(optionBusinessRules).ensureAtLeastOneCorrectOption(question.getId(), option.getId());

        // Option'ın silindiğini doğruluyoruz
        verify(optionRepository).deleteById(option.getId());

        // options listesinden option'ın kaldırıldığını doğruluyoruz
        assertFalse(question.getOptions().stream().anyMatch(opt -> opt.getId().equals(option.getId())));

        // Loglama yapıldığına dair kontrol edebiliriz
    }


    @Test
    void deleteOption_optionNotFound() {
        // Arrange
        Long optionId = 1L;
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist))
                .thenReturn("Option should exist");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.deleteOption(optionId);
        });
        assertEquals("Option should exist", exception.getMessage());
    }

    @Test
    void deleteOption_userAuthorizationFail() {
        // Arrange
        Long optionId = 1L;
        Option option = new Option();
        option.setId(optionId);
        Question question = new Question();
        question.setCreatorId(2L); // Simulate authorization failure
        question.setIsEditable(true);
        option.setQuestion(question);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        doThrow(new BusinessException("Unauthorized"))
                .when(optionBusinessRules).userAuthorizationCheck(anyLong());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.deleteOption(optionId);
        });
        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    void deleteOption_questionNotEditable() {
        // Arrange
        Long optionId = 1L;
        Option option = new Option();
        option.setId(optionId);
        Question question = new Question();
        question.setCreatorId(1L);
        question.setIsEditable(false);
        option.setQuestion(question);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        doNothing().when(optionBusinessRules).userAuthorizationCheck(anyLong());
        doThrow(new BusinessException("Question not editable"))
                .when(questionBusinessRules).checkIfQuestionIsEditable(question.getIsEditable());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.deleteOption(optionId);
        });
        assertEquals("Question not editable", exception.getMessage());
    }

    @Test
    void deleteOption_notEnoughAnswers() {
        Long optionId = 1L;
        Option option = new Option();
        option.setId(optionId);
        Question question = new Question();
        question.setCreatorId(1L);
        question.setIsEditable(true);
        question.setOptions(Collections.singletonList(option)); // Only one option
        option.setQuestion(question);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        doNothing().when(optionBusinessRules).userAuthorizationCheck(anyLong());
        doNothing().when(questionBusinessRules).checkIfQuestionIsEditable(anyBoolean());
        doThrow(new BusinessException("At least two answers required"))
                .when(optionBusinessRules).atLeastTwoAnswerChecks(question.getOptions());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.deleteOption(optionId);
        });
        assertEquals("At least two answers required", exception.getMessage());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void deleteOption_noCorrectOption() {
        // Arrange
        Long optionId = 1L;
        Option option = new Option();
        option.setId(optionId);
        Question question = new Question();
        question.setCreatorId(1L);
        question.setIsEditable(true);
        Option correctOption = new Option(); // This should be a correct option
        correctOption.setId(2L);
        question.setOptions(Arrays.asList(option, correctOption));
        option.setQuestion(question);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        doNothing().when(optionBusinessRules).userAuthorizationCheck(anyLong());
        doNothing().when(questionBusinessRules).checkIfQuestionIsEditable(anyBoolean());
        doNothing().when(optionBusinessRules).atLeastTwoAnswerChecks(anyList());
        doThrow(new BusinessException("At least one correct option required"))
                .when(optionBusinessRules).ensureAtLeastOneCorrectOption(anyLong(), anyLong());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            optionManager.deleteOption(optionId);
        });
        assertEquals("At least one correct option required", exception.getMessage());
    }
}