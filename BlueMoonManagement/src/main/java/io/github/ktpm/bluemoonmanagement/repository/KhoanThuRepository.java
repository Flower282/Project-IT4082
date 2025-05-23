package io.github.ktpm.bluemoonmanagement.repository;

import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.repository.custom.KhoanThuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhoanThuRepository extends JpaRepository<KhoanThu, String>, KhoanThuRepositoryCustom {

}
