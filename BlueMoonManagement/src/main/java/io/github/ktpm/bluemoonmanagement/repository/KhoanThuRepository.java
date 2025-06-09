package io.github.ktpm.bluemoonmanagement.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.repository.custom.KhoanThuRepositoryCustom;

public interface KhoanThuRepository extends JpaRepository<KhoanThu, String>, KhoanThuRepositoryCustom {

    @Query("SELECT k FROM KhoanThu k LEFT JOIN FETCH k.phiGuiXeList")
    List<KhoanThu> findAllWithPhiGuiXe();

}
