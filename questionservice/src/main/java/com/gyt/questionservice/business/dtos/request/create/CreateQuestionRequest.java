package com.gyt.questionservice.business.dtos.request.create;

import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateQuestionRequest {
    private String text;
    private String imageUrl;
    @Size(min = 2,max = 5,message = "Bir soru en az 2 en fazla 5 secenege sahip olabilir.")
    private List<CreateOptionRequest> optionRequestList;

}
