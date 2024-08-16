package com.gyt.questionservice.business.dtos.request.create;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQuestionRequest {
    @Length(max=2000, message = "Bir soru metni en fazla 2000 karakterden olusabilir")
    private String text;
    private String imageUrl;
    @Size(min = 2,max = 5,message = "Bir soru en az 2 en fazla 5 secenege sahip olabilir.")
    private List<CreateOptionRequest> optionRequestList;

}
