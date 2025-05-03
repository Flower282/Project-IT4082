package io.github.ktpm.bluemoonmanagement.model.mapper;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.*;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    ThongTinTaiKhoanDto toThongTinTaiKhoanDto(TaiKhoan taiKhoan);
}
