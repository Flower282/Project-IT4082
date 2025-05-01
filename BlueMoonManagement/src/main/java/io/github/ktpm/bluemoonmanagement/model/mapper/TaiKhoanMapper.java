package io.github.ktpm.bluemoonmanagement.model.mapper;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.*;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    CapNhatTaiKhoanDto toCapNhatTaiKhoanDto(TaiKhoan taiKhoan);

    DangKiDto toDangKiDto(TaiKhoan taiKhoan);

    DatLaiMatKhauDto toDatLaiMatKhauDto(TaiKhoan taiKhoan);

    DoiMatKhauDto toDoiMatKhauDto(TaiKhoan taiKhoan);

    GuiOtpDto toGuiOtpDto(TaiKhoan taiKhoan);

    XacMinhOtpDto toXacMinhOtpDto(TaiKhoan taiKhoan);

    TaiKhoan fromCapNhatTaiKhoanDto(CapNhatTaiKhoanDto capNhatTaiKhoanDto);

    TaiKhoan fromDangKiDto(DangKiDto dangKiDto);

    TaiKhoan fromDatLaiMatKhauDto(DatLaiMatKhauDto datLaiMatKhauDto);

    TaiKhoan fromDoiMatKhauDto(DoiMatKhauDto doiMatKhauDto);

    TaiKhoan fromGuiOtpDto(GuiOtpDto guiOtpDto);

    TaiKhoan fromXacMinhOtpDto(XacMinhOtpDto xacMinhOtpDto);
}
