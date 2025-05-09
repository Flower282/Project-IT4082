package io.github.ktpm.bluemoonmanagement.service.canHo;

import java.util.List;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;

public interface CanHoService {
    List<CanHoDto> getAllCanHo();

    ResponseDto addCanHo(CanHoDto canHoDto);

    CanHoChiTietDto getCanHoChiTiet(CanHoDto canHoDto);
}
