package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;

@Mapper(componentModel = "spring", uses = { CuDanMapper.class, HoaDonMapper.class, PhuongTienMapper.class }) 
public interface CanHoMapper {
    @Mapping(target = "chuSoHuu", source = "canHo.chuHo.hoVaTen") // Chuyển đổi chuSoHuu từ CanHo sang CanHoDto
    CanHoDto toCanHoDto(CanHo canHo); // Chuyển đổi từ CanHo sang CanHoDto

    CanHoChiTietDto toCanHoChiTietDto(CanHo canHo);

    //CanHo fromDto(CanHoDto canHoDto);
}
