package io.github.ktpm.bluemoonmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanHoDto {
    private String maCanHo;
    private String toaNha;
    private int tang;
    private String soNha;
    private String chuSoHuu;
    private boolean daBanChua;
    private String trangThaiKiThuat;
    private String trangThaiSuDung; 
}
