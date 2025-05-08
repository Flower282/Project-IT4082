package io.github.ktpm.bluemoonmanagement.service.khoanThu;
import java.util.List;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;

public interface KhoanThuService {
    List<KhoanThuDto> getAllKhoanThu();
    ResponseDto addKhoanThu(KhoanThuDto khoanThuDto);
    ResponseDto updateKhoanThu(KhoanThuDto khoanThuDto);
    ResponseDto deleteKhoanThu(String maKhoanThu);
    
}
