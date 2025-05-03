package io.github.ktpm.bluemoonmanagement.repository;

import io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhuongTienRepository extends JpaRepository<PhuongTien, Integer> {
    boolean existsByBienSo(String bienSo);
}
