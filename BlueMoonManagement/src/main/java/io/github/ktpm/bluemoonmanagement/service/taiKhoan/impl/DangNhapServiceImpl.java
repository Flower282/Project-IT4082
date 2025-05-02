package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangNhapDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangNhapServive;

@Service
public class DangNhapServiceImpl implements DangNhapServive {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    public DangNhapServiceImpl(TaiKhoanRepository taiKhoanRepository){
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public ThongTinTaiKhoanDto dangNhap(DangNhapDto dangNhapDto) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dangNhap'");
    }
    

}
    

