package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangNhapDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;

public interface DangNhapServive {

    ThongTinTaiKhoanDto dangNhap(DangNhapDto dangNhapDto) throws Exception;
}
