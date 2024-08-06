package com.gyt.questionservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateQuestionRequest {
    private Long id;
    @Length(max=2000, message = "Bir soru metni en fazla 2000 karakterden olusabilir")
    private String text;
    private String imageUrl;
}
