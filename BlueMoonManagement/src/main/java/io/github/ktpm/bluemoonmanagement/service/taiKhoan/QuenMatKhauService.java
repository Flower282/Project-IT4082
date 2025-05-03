package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DatLaiMatKhauDto;

public interface QuenMatKhauService {

    ResponseDto guiMaOtp(String email);

    ResponseDto xacThucOtp(String otp);

    ResponseDto datLaiMatKhau(DatLaiMatKhauDto datLaiMatKhauDto);
    
}


