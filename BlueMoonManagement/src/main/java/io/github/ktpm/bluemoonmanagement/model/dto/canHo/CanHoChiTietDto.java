package io.github.ktpm.bluemoonmanagement.model.dto.canHo;

import io.github.ktpm.bluemoonmanagement.model.dto.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.ChuHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanHoChiTietDto {
    private String maCanHo;
    private String toaNha;
    private int tang;
    private String soNha;
    private double dienTich;
    private boolean daBanChua;
    private String trangThaiKiThuat;
    private String trangThaiSuDung;
    private ChuHoDto chuHo;
    private List<PhuongTienDto> phuongTienList;
    private List<CuDanTrongCanHoDto> cuDanList;
    private List<HoaDonDto> hoaDonList;
}
