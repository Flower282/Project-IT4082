package io.github.ktpm.bluemoonmanagement.model.dto.cuDan;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuDanDto {
    private String maDinhDanh;
    private String hoVaTen;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private LocalDate ngayChuyenDen;
    private LocalDate ngayChuyenDi;
    private String trangThaiCuTru;
}
