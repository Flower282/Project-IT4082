package io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XacMinhOtpDto {
    private String otp;
    private String thoiHanOtp;
}
