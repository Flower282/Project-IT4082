package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;

@Mapper(componentModel = "spring")
public interface CanHoMapper {
    @Mapping(target = "chuSoHuu", source = "canHo.chuHo.hoVaTen") // Chuyển đổi chuSoHuu từ CanHo sang CanHoDto
    CanHoDto toCanHoDto(CanHo canHo); // Chuyển đổi từ CanHo sang CanHoDto

    CanHo fromDto(CanHoDto canHoDto); // Chuyển đổi từ CanHoDto sang CanHo
}
