package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangNhapDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;

public interface DangNhapServive {

    /**
     * Kiểm tra email có tồn tại trong hệ thống không (bước 1)
     */
    boolean kiemTraEmailTonTai(String email);

    /**
     * Kiểm tra mật khẩu và trả về thông tin tài khoản nếu đúng (bước 2)
     */
    ThongTinTaiKhoanDto dangNhap(DangNhapDto dangNhapDto);
}
