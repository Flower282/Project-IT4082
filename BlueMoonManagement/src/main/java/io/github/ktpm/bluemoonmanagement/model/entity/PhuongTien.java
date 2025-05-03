package io.github.ktpm.bluemoonmanagement.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table(name = "phuong_tien")
@Entity
public class PhuongTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int soThuTu;

    @Column(name = "loai_phuong_tien")
    private String loaiPhuongTien;

    @Column(name = "bien_so")
    private String bienSo;

    @ManyToOne
    @JoinColumn(name = "ma_can_ho", referencedColumnName = "ma_can_ho")
    private CanHo canHo;
}
