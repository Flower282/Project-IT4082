package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.entity.HoaDon;

@Mapper(componentModel = "spring")
public interface HoaDonMapper {
    @Mapping(target = "tenKhoanThu", source = "hoaDon.khoanThu.tenKhoanThu")
    HoaDonDto toHoaDonDto(HoaDon hoaDon);

}
