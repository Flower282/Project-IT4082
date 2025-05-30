package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto;
import io.github.ktpm.bluemoonmanagement.model.entity.PhiGuiXe;

@Mapper(componentModel = "spring")
public interface PhiGuiXeMapper {
    @Mapping(target = "maKhoanThu", source = "khoanThu.maKhoanThu")
    PhiGuiXeDto toPhiGuiXeDto(PhiGuiXe phiGuiXe);

    @Mapping(target = "khoanThu.maKhoanThu", source = "maKhoanThu")
    PhiGuiXe fromPhiGuiXeDto(PhiGuiXeDto phiGuiXeDto);
}
