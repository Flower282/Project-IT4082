package io.github.ktpm.bluemoonmanagement.repository;

import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.repository.custom.CanHoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CanHoRepository extends JpaRepository<CanHo, String>, CanHoRepositoryCustom {
}
