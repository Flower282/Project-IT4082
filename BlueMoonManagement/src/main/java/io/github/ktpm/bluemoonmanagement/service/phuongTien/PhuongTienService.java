package io.github.ktpm.bluemoonmanagement.service.phuongTien;

import java.util.List;


import io.github.ktpm.bluemoonmanagement.model.dto.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;

public interface PhuongTienService {
    ResponseDto themPhuongTien(PhuongTienDto phuongTienDto);
    ResponseDto capNhatPhuongTien(PhuongTienDto phuongTienDto);
    ResponseDto xoaPhuongTien(PhuongTienDto phuongTienDto);
    List<PhuongTienDto> layDanhSachPhuongTien();
}
