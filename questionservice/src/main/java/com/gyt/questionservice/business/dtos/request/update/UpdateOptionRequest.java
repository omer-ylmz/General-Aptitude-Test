package com.gyt.questionservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOptionRequest {
    private Long id;
    @Length(max=500,message = "Bir secenek en icerigi en fazla 500 karakterden oluşabilir")
    private String text;
    private Boolean isCorrect;
    private String imageUrl;
}
