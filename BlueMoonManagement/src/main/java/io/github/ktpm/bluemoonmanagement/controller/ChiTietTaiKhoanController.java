package io.github.ktpm.bluemoonmanagement.controller;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuanLyTaiKhoanService;
import org.springframework.context.ApplicationContext;

public class ChiTietTaiKhoanController {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("DEBUG: Setting ApplicationContext in ChiTietCanHoController");
        this.applicationContext = applicationContext;

        // Sau khi có ApplicationContext, thử lấy các service nếu chưa có
        ensureServicesAvailable();
    }

    private void ensureServicesAvailable() {
    }


    public void setTaiKhoanService(QuanLyTaiKhoanService taiKhoanService) {
    }

    public void setTaiKhoanData(ThongTinTaiKhoanDto taiKhoanDto) {
    }
}
