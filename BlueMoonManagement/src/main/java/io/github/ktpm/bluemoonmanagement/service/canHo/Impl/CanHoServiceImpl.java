package io.github.ktpm.bluemoonmanagement.service.canHo.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;

@Service
public class CanHoServiceImpl implements CanHoService {

    private final CanHoRepository canHoRepository;
    private final CanHoMapper canHoMapper;

    public CanHoServiceImpl(CanHoRepository canHoRepository, CanHoMapper canHoMapper) {
        this.canHoRepository = canHoRepository;
        this.canHoMapper = canHoMapper;
    }

    @Override
    public List<CanHoDto> getAllCanHo() {
        List<CanHo> canHoList = canHoRepository.findAll();
        return canHoList.stream()
                .map(canHoMapper::toCanHoDto)
                .collect(Collectors.toList());
    }

    @Override
    public CanHoChiTietDto getCanHoChiTiet(CanHoDto canHoDto) {
        CanHo canHo = canHoRepository.findById(canHoDto.getMaCanHo()).orElse(null);
        return canHoMapper.toCanHoChiTietDto(canHo);
    }

    
}
