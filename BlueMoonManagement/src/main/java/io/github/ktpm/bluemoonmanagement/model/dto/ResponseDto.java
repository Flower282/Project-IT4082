package io.github.ktpm.bluemoonmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {
    private boolean success;
    private String message;
    
}
