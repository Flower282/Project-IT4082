package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;

public interface QuanLyTaiKhoanService {

    ResponseDto thayDoiThongTinTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto);
    ResponseDto xoaTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto);
}
