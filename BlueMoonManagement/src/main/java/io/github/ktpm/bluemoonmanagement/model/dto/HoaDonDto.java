package io.github.ktpm.bluemoonmanagement.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDto {
    private String maHoaDon;
    private String tenKhoanThu;
    private String soTien;
    private LocalDateTime ngayNop;
    private boolean daNop;
}
