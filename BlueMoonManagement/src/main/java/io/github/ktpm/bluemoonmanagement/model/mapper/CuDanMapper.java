package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;

import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;

@Mapper(componentModel = "spring")
public interface CuDanMapper {
    CuDanTrongCanHoDto toCuDanDto(CuDan cuDan);
}
