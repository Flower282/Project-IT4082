package io.github.ktpm.bluemoonmanagement.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.cache.DataCache;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService;
import io.github.ktpm.bluemoonmanagement.service.hoaDon.HoaDonService;
import io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService;
import io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService;

/**
 * Component để load tất cả dữ liệu từ database vào cache khi khởi động ứng dụng
 * Giúp tăng tốc độ truy cập dữ liệu trong suốt quá trình chạy ứng dụng
 */
@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private DataCache dataCache;
    
    @Autowired
    private CanHoService canHoService;
    
    @Autowired
    private CuDanService cuDanService;
    
    @Autowired(required = false) 
    private PhuongTienService phuongTienService;
    
    @Autowired(required = false)
    private HoaDonService hoaDonService;
    
    @Autowired(required = false)
    private KhoanThuService khoanThuService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== BẮT ĐẦU LOAD DỮ LIỆU VÀO CACHE ===");
        long startTime = System.currentTimeMillis();
        
        try {
            // Load căn hộ
            loadCanHoData();
            
            // Load cư dân
            loadCuDanData();
            
            // Load khoản thu
            loadKhoanThuData();
            
            // Mark cache as loaded
            dataCache.setLoaded(true);
            
            long endTime = System.currentTimeMillis();
            System.out.println("=== LOAD DỮ LIỆU HOÀN TẤT ===");
            System.out.println("Tổng thời gian: " + (endTime - startTime) + "ms");
            System.out.println("Tổng số items đã cache: " + dataCache.getTotalCachedItems());
            
        } catch (Exception e) {
            System.err.println("LỖI KHI LOAD DỮ LIỆU: " + e.getMessage());
            e.printStackTrace();
            // Không throw exception để ứng dụng vẫn có thể khởi động
        }
    }
    
    private void loadCanHoData() {
        try {
            if (canHoService != null) {
                System.out.print("Loading căn hộ... ");
                var canHoList = canHoService.getAllCanHo();
                dataCache.setCanHoList(canHoList);
                System.out.println("✓ " + (canHoList != null ? canHoList.size() : 0) + " căn hộ");
            } else {
                System.out.println("⚠ CanHoService không khả dụng");
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi load căn hộ: " + e.getMessage());
        }
    }
    
    private void loadCuDanData() {
        try {
            if (cuDanService != null) {
                System.out.print("Loading cư dân... ");
                var cuDanList = cuDanService.getAllCuDan();
                dataCache.setCuDanList(cuDanList);
                System.out.println("✓ " + (cuDanList != null ? cuDanList.size() : 0) + " cư dân");
            } else {
                System.out.println("⚠ CuDanService không khả dụng");
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi load cư dân: " + e.getMessage());
        }
    }
    
    private void loadKhoanThuData() {
        try {
            if (khoanThuService != null) {
                System.out.print("Loading khoản thu... ");
                var khoanThuList = khoanThuService.getAllKhoanThu();
                dataCache.setKhoanThuList(khoanThuList);
                System.out.println("✓ " + (khoanThuList != null ? khoanThuList.size() : 0) + " khoản thu");
            } else {
                System.out.println("⚠ KhoanThuService không khả dụng");
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi load khoản thu: " + e.getMessage());
        }
    }
} 