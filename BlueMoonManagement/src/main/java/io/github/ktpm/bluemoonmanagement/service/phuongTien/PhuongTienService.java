package io.github.ktpm.bluemoonmanagement.service.phuongTien;


import io.github.ktpm.bluemoonmanagement.model.dto.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;

public interface PhuongTienService {
    ResponseDto themPhuongTien(PhuongTienDto phuongTienDto);
    ResponseDto capNhatPhuongTien(PhuongTienDto phuongTienDto);
    ResponseDto xoaPhuongTien(PhuongTienDto phuongTienDto);
}
