package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;

public interface ThayDoiThongTinService {

    ResponseDto thayDoiThongTinTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto) throws Exception;
}
