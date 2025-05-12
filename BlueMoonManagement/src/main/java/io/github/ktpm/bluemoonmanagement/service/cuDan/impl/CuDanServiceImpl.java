package io.github.ktpm.bluemoonmanagement.service.cuDan.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;
import io.github.ktpm.bluemoonmanagement.model.mapper.CuDanMapper;
import io.github.ktpm.bluemoonmanagement.repository.CuDanRepository;
import io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService;
import io.github.ktpm.bluemoonmanagement.session.Session;

@Service
public class CuDanServiceImpl implements CuDanService {

    private final CuDanRepository cuDanRepository;
    private final CuDanMapper cuDanMapper;
    
    public CuDanServiceImpl(CuDanRepository cuDanRepository, CuDanMapper cuDanMapper) {
        this.cuDanRepository = cuDanRepository;
        this.cuDanMapper = cuDanMapper;
    }

    @Override
    public List<CudanDto> getAllCuDan() {
        return cuDanRepository.findAll()
                .stream()
                .map(cuDanMapper::toCudanDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto addCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Cư dân đã tồn tại");
        }
        
        CuDan cuDan = cuDanMapper.fromCudanDto(cudanDto);
        cuDanRepository.save(cuDan);
        return new ResponseDto(true, "Thêm cư dân thành công");
    }

    @Override
    public ResponseDto updateCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền cập nhật cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (!cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Không tìm thấy cư dân");
        }
        
        CuDan cuDan = cuDanMapper.fromCudanDto(cudanDto);
        cuDanRepository.save(cuDan);
        return new ResponseDto(true, "Cập nhật cư dân thành công");
    }

    @Override
    public ResponseDto deleteCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xóa cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (!cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Không tìm thấy cư dân");
        }
        
        cuDanRepository.deleteById(cudanDto.getMaDinhDanh());
        return new ResponseDto(true, "Xóa cư dân thành công");
    }
}