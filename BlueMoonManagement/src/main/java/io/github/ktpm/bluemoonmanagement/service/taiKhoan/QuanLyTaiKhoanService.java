package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;

public interface QuanLyTaiKhoanService {

    void thayDoiThongTinTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto);
    void xoaTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto);
}
