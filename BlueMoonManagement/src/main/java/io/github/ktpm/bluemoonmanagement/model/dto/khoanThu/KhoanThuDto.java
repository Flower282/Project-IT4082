package io.github.ktpm.bluemoonmanagement.model.dto.khoanThu;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhoanThuDto {
    private String maKhoanThu;
    private String tenKhoanThu;
    private boolean batBuoc;
    private String donViTinh;
    private int soTien;
    private LocalDate ngayTao;
    private LocalDate thoiHan;
    private String ghiChu;
}
