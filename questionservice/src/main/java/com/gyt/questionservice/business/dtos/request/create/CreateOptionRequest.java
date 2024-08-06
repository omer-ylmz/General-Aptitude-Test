package com.gyt.questionservice.business.dtos.request.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOptionRequest {
    @Length(max=500,message = "Bir secenek en icerigi en fazla 500 karakterden olusabilir")
    private String text;
    private Boolean isCorrect;
    private String imageUrl;

}
