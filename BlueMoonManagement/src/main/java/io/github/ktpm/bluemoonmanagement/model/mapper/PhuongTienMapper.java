package io.github.ktpm.bluemoonmanagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien;

@Mapper(componentModel = "spring")
public interface PhuongTienMapper {
    PhuongTienDto toPhuongTienDto(PhuongTien phuongTien);

    @Mapping(target = "ngayHuyDangKy", ignore = true)
    @Mapping(target = "canHo", ignore = true)
    PhuongTien fromPhuongTienDto(PhuongTienDto phuongTienDto);
}
