package io.github.ktpm.bluemoonmanagement.model.mapper;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.*;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    ThongTinTaiKhoanDto toThongTinTaiKhoanDto(TaiKhoan taiKhoan);

    @Mapping(target = "matKhauMoi", ignore = true)
    @Mapping(target = "xacNhanMatKhauMoi", ignore = true)
    @Mapping(target = "matKhauCu", source = "taiKhoan.matKhau")
    DoiMatKhauDto toDoiMatKhauDto(TaiKhoan taiKhoan);

    GuiOtpDto toGuiOtpDto(TaiKhoan taiKhoan);

    XacMinhOtpDto toXacMinhOtpDto(TaiKhoan taiKhoan);

    @Mapping(target = "matKhau", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "thoiHanOtp", ignore = true)
    TaiKhoan fromThongTinTaiKhoanDto(ThongTinTaiKhoanDto thongTinTaiKhoanDto);

    @Mapping(target = "vaiTro", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "thoiHanOtp", ignore = true)
    TaiKhoan fromDangKiDto(DangKiDto dangKiDto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "matKhau", source = "datLaiMatKhauDto.matKhauMoi")
    @Mapping(target = "hoTen", ignore = true)
    @Mapping(target = "vaiTro", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "thoiHanOtp", ignore = true)
    TaiKhoan fromDatLaiMatKhauDto(DatLaiMatKhauDto datLaiMatKhauDto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "matKhau", source = "doiMatKhauDto.matKhauMoi")
    @Mapping(target = "hoTen", ignore = true)
    @Mapping(target = "vaiTro", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "thoiHanOtp", ignore = true)
    TaiKhoan fromDoiMatKhauDto(DoiMatKhauDto doiMatKhauDto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "matKhau", ignore = true)
    @Mapping(target = "hoTen", ignore = true)
    @Mapping(target = "vaiTro", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    @Mapping(target = "otp", source = "guiOtpDto.otp")
    @Mapping(target = "thoiHanOtp", ignore = true)
    TaiKhoan fromGuiOtpDto(GuiOtpDto guiOtpDto);
}
