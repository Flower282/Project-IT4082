package io.github.ktpm.bluemoonmanagement.repository;

import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.repository.custom.KhoanThuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KhoanThuRepository extends JpaRepository<KhoanThu, String>, KhoanThuRepositoryCustom {

    @Query("SELECT k FROM KhoanThu k LEFT JOIN FETCH k.phiGuiXeList")
    List<KhoanThu> findAllWithPhiGuiXe();

}
