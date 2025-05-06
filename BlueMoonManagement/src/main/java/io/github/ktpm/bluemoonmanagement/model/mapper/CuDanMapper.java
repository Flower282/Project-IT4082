package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;

import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;

@Mapper(componentModel = "spring")
public interface CuDanMapper {
    CuDanDto toCuDanDto(CuDan cuDan);
}
