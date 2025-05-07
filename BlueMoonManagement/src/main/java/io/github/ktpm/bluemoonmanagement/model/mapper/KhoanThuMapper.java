package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;

@Mapper(componentModel = "spring")
public interface KhoanThuMapper {
    KhoanThuDto toKhoanThuDto(KhoanThu khoanThu);

    @Mapping(target = "hoaDonList", ignore = true)
    KhoanThu fromKhoanThuDto(KhoanThuDto khoanThuDto);
}
