package io.github.ktpm.bluemoonmanagement.service.hoaDon.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoHoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonTuNguyenDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuTuNguyenDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDichVuDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.entity.HoaDon;
import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.HoaDonMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.HoaDonRepository;
import io.github.ktpm.bluemoonmanagement.repository.KhoanThuRepository;
import io.github.ktpm.bluemoonmanagement.service.hoaDon.HoaDonService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Function;

@Service
public class HoaDonServiceImpl implements HoaDonService {
    
    private final HoaDonRepository hoaDonRepository;
    private final CanHoRepository canHoRepository;
    private final KhoanThuRepository khoanThuRepository;
    private final HoaDonMapper hoaDonMapper;
    private final CanHoMapper canHoMapper;
    
    public HoaDonServiceImpl(HoaDonRepository hoaDonRepository, HoaDonMapper hoaDonMapper, CanHoRepository canHoRepository, CanHoMapper canHoMapper, KhoanThuRepository khoanThuRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.canHoRepository = canHoRepository;
        this.hoaDonMapper = hoaDonMapper;
        this.canHoMapper = canHoMapper;
        this.khoanThuRepository = khoanThuRepository;
    }
    

    private List<CanHoHoaDonDto> getCanHoHoaDonDtoList() {
        // Use eager fetching to avoid LazyInitializationException
        List<CanHo> canHoList = canHoRepository.findAllWithPhuongTien();
        return canHoList.stream()
                .map(canHoMapper::toCanHoHoaDonDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto generateHoaDon(KhoanThuDto khoanThuDto) {
        System.out.println("🎯 === STARTING generateHoaDon() ===");
        System.out.println("📋 Fee details: " + khoanThuDto.getTenKhoanThu() + " (ID: " + khoanThuDto.getMaKhoanThu() + ")");
        
        // Check user permissions first
        if (Session.getCurrentUser() == null || !Session.getCurrentUser().getVaiTro().equals("Kế toán")) {
            System.out.println("❌ Permission denied: User is not accountant");
            return new ResponseDto(false, "Bạn không có quyền tạo hóa đơn. Chỉ Kế toán mới được phép thực hiện thao tác này.");
        }

        // Validate fee exists and is in correct state
        if (khoanThuDto == null || khoanThuDto.isTaoHoaDon()) {
            System.out.println("❌ Fee validation failed: already has invoice or is null");
            return new ResponseDto(false, "Khoản thu đã tạo hóa đơn hoặc không hợp lệ!");
        }
        
        // Early validation for field lengths to prevent database errors
        if (khoanThuDto.getMaKhoanThu() != null && khoanThuDto.getMaKhoanThu().length() > 15) {
            System.err.println("❌ Fee code too long: '" + khoanThuDto.getMaKhoanThu() + "' (" + 
                             khoanThuDto.getMaKhoanThu().length() + " chars > 15 limit)");
            return new ResponseDto(false, "Mã khoản thu quá dài (" + khoanThuDto.getMaKhoanThu().length() + 
                                 " ký tự), vượt quá giới hạn 15 ký tự của database!");
        }

        System.out.println("✅ Permissions and validation passed");

        try {
            // Load fee entity
            KhoanThu khoanThuEntity = khoanThuRepository.findById(khoanThuDto.getMaKhoanThu()).orElse(null);
            if (khoanThuEntity == null) {
                return new ResponseDto(false, "Không tìm thấy khoản thu trong hệ thống!");
            }

            // Load apartments with simple query
            List<CanHo> danhSachCanHo;
            try {
                danhSachCanHo = canHoRepository.findAll();
                System.out.println("📊 Loaded " + danhSachCanHo.size() + " apartments from database");
            } catch (Exception e) {
                System.err.println("❌ Failed to load apartments: " + e.getMessage());
                return new ResponseDto(false, "Không thể tải danh sách căn hộ!");
            }

            // ===== BƯỚC LỌC NHANH BẰNG STREAM API =====
            List<CanHo> eligibleApartments;
            
            if ("Phương tiện".equals(khoanThuDto.getDonViTinh())) {
                System.out.println("🔍 Lọc căn hộ đủ điều kiện cho phí phương tiện...");
                
                // Lấy danh sách căn hộ có phương tiện
                List<CanHo> apartmentsWithVehicles = canHoRepository.findAllWithPhuongTien();
                System.out.println("📋 Found " + apartmentsWithVehicles.size() + " apartments with vehicle data");
                
                // Lọc căn hộ: ĐANG SỬ DỤNG + CÓ PHƯƠNG TIỆN
                eligibleApartments = apartmentsWithVehicles.stream()
                        .filter(canHo -> {
                            // Điều kiện 1: Căn hộ đang sử dụng
                            boolean isOccupied = "Đang sử dụng".equals(canHo.getTrangThaiSuDung());
                            
                            // Điều kiện 2: Có phương tiện
                            boolean hasVehicles = canHo.getPhuongTienList() != null && 
                                                !canHo.getPhuongTienList().isEmpty();
                            
                            if (isOccupied && hasVehicles) {
                                System.out.println("   ✅ " + canHo.getMaCanHo() + " - Đang sử dụng + Có " + 
                                                 canHo.getPhuongTienList().size() + " phương tiện");
                                return true;
                            } else {
                                System.out.println("   ❌ " + canHo.getMaCanHo() + " - Bỏ qua (Sử dụng: " + 
                                                 isOccupied + ", Có xe: " + hasVehicles + ")");
                                return false;
                            }
                        })
                        .collect(java.util.stream.Collectors.toList());
                        
                System.out.println("🎯 Kết quả lọc: " + eligibleApartments.size() + "/" + 
                                 apartmentsWithVehicles.size() + " căn hộ đủ điều kiện");
                
            } else if ("Diện tích".equals(khoanThuDto.getDonViTinh()) && 
                      "Căn hộ đang sử dụng".equals(khoanThuDto.getPhamVi())) {
                System.out.println("🔍 Lọc căn hộ đang sử dụng cho phí diện tích...");
                
                // Lọc chỉ căn hộ đang sử dụng
                eligibleApartments = danhSachCanHo.stream()
                        .filter(canHo -> "Đang sử dụng".equals(canHo.getTrangThaiSuDung()))
                        .collect(java.util.stream.Collectors.toList());
                        
                System.out.println("🎯 Kết quả lọc: " + eligibleApartments.size() + "/" + 
                                 danhSachCanHo.size() + " căn hộ đang sử dụng");
                                 
            } else {
                System.out.println("🔍 Sử dụng tất cả căn hộ (không cần lọc đặc biệt)");
                eligibleApartments = danhSachCanHo;
            }

            int totalInvoicesCreated = 0;
            int totalErrors = 0;

            // Create invoices for filtered apartments only
            System.out.println("🏭 Bắt đầu tạo hóa đơn cho " + eligibleApartments.size() + " căn hộ đã lọc...");
            
            for (CanHo canHo : eligibleApartments) {
                try {
                    // Validate apartment code length to prevent database errors
                    if (canHo.getMaCanHo() != null && canHo.getMaCanHo().length() > 15) {
                        System.err.println("❌ Skipping apartment " + canHo.getMaCanHo() + " - Code too long (" + 
                                         canHo.getMaCanHo().length() + " chars > 15 limit)");
                        totalErrors++;
                        continue;
                    }
                    
                    System.out.println("🏠 Processing apartment: " + canHo.getMaCanHo() + " (Length: " + 
                                     (canHo.getMaCanHo() != null ? canHo.getMaCanHo().length() : "null") + " chars)");
                    
                    // Calculate amount - now simplified since we pre-filtered
                    Integer soTien = calculateOptimizedInvoiceAmount(khoanThuDto, canHo);
                    if (soTien == null || soTien <= 0) {
                        System.out.println("⏭️ Skipping apartment " + canHo.getMaCanHo() + " (amount = " + soTien + ")");
                        continue;
                    }

                    // Validate fee code length  
                    if (khoanThuEntity.getMaKhoanThu() != null && khoanThuEntity.getMaKhoanThu().length() > 15) {
                        System.err.println("❌ Skipping apartment " + canHo.getMaCanHo() + " - Fee code too long (" + 
                                         khoanThuEntity.getMaKhoanThu().length() + " chars > 15 limit)");
                        totalErrors++;
                        continue;
                    }

                    // Create invoice entity
                    HoaDon hoaDon = new HoaDon();
                    // Don't set maHoaDon - let database auto-generate
                    hoaDon.setKhoanThu(khoanThuEntity);
                    hoaDon.setCanHo(canHo);
                    hoaDon.setSoTien(soTien);
                    hoaDon.setDaNop(false);

                    System.out.println("💾 Saving invoice: Apartment=" + canHo.getMaCanHo() + 
                                     ", Fee=" + khoanThuEntity.getMaKhoanThu() + 
                                     ", Amount=" + String.format("%,d", soTien));

                    // Save invoice
                    HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);
                    System.out.println("✅ Created invoice " + savedHoaDon.getMaHoaDon() + " for apartment " + canHo.getMaCanHo() + " (Amount: " + String.format("%,d", soTien) + " VND)");
                    totalInvoicesCreated++;

                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    System.err.println("❌ Database constraint violation for apartment " + canHo.getMaCanHo() + ": " + e.getMessage());
                    if (e.getMessage().contains("value too long")) {
                        System.err.println("   💡 Suggestion: Check if apartment code '" + canHo.getMaCanHo() + 
                                         "' (length: " + canHo.getMaCanHo().length() + ") exceeds database field limit");
                    }
                    totalErrors++;
                } catch (Exception e) {
                    System.err.println("❌ Error creating invoice for apartment " + canHo.getMaCanHo() + ": " + e.getMessage());
                    totalErrors++;
                }
            }

            // Update fee status if any invoices were created
            if (totalInvoicesCreated > 0) {
                try {
                    khoanThuEntity.setTaoHoaDon(true);
                    khoanThuRepository.save(khoanThuEntity);
                    System.out.println("✅ Updated fee status to 'invoice created'");
                } catch (Exception e) {
                    System.err.println("❌ Failed to update fee status: " + e.getMessage());
                }
            }

            System.out.println("🎯 === INVOICE GENERATION COMPLETED ===");
            System.out.println("📈 Total invoices created: " + totalInvoicesCreated);
            System.out.println("❌ Total errors: " + totalErrors);

            if (totalInvoicesCreated > 0) {
                return new ResponseDto(true, 
                    "✅ Tạo hóa đơn thành công!\n\n" +
                    "📊 Thống kê:\n" +
                    "• Khoản thu: " + khoanThuDto.getTenKhoanThu() + "\n" +
                    "• Số hóa đơn tạo: " + totalInvoicesCreated + "\n" +
                    "• Số lỗi: " + totalErrors + "\n\n" +
                    "🎯 Trạng thái khoản thu đã được cập nhật thành 'Đã tạo'\n" +
                    "📋 Hóa đơn đã được lưu vào database và sẽ hiển thị trong bảng 'Lịch sử thu'\n\n" +
                    "💡 Vui lòng kiểm tra tab 'Lịch sử thu' để xem danh sách hóa đơn mới được tạo!");
            } else {
                return new ResponseDto(false, "Không tạo được hóa đơn nào. Vui lòng kiểm tra dữ liệu!");
            }

        } catch (Exception e) {
            System.err.println("❌ MAJOR ERROR in generateHoaDon: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Lỗi hệ thống khi tạo hóa đơn: " + e.getMessage());
        }
    }
    
    /**
     * Optimized invoice amount calculation for pre-filtered apartments
     */
    private Integer calculateOptimizedInvoiceAmount(KhoanThuDto khoanThuDto, CanHo canHo) {
        try {
            String donViTinh = khoanThuDto.getDonViTinh();
            System.out.println("💰 Tính phí tối ưu cho căn hộ " + canHo.getMaCanHo() + " (Đơn vị: " + donViTinh + ")");
            
            switch (donViTinh) {
                case "Diện tích":
                    // Tính phí theo diện tích - đã lọc trước nên không cần kiểm tra điều kiện
                    int areaFee = (int) Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                    System.out.println("   📐 Phí diện tích: " + String.format("%,d", khoanThuDto.getSoTien()) + 
                                     " x " + canHo.getDienTich() + "m² = " + String.format("%,d", areaFee) + " VND");
                    return areaFee;
                    
                case "Phương tiện":
                    // Tính phí phương tiện - đã lọc trước nên chắc chắn có xe
                    return calculateVehicleFeeOptimized(khoanThuDto, canHo);
                    
                default:
                    // Các loại phí khác
                    int baseFee = khoanThuDto.getSoTien();
                    System.out.println("   💰 Phí cơ bản: " + String.format("%,d", baseFee) + " VND");
                    return baseFee;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi tính phí tối ưu cho căn hộ " + canHo.getMaCanHo() + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Optimized vehicle fee calculation - assumes apartment already has vehicles
     */
    private Integer calculateVehicleFeeOptimized(KhoanThuDto khoanThuDto, CanHo canHo) {
        try {
            System.out.println("   🚗 Tính phí phương tiện tối ưu cho căn hộ: " + canHo.getMaCanHo());
            
            // Căn hộ đã được lọc trước, chắc chắn có phương tiện
            List<io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien> phuongTienList = canHo.getPhuongTienList();
            
            // Đếm nhanh từng loại xe bằng Stream API
            long countXeDap = phuongTienList.stream()
                    .filter(pt -> "Xe đạp".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            long countXeMay = phuongTienList.stream()
                    .filter(pt -> "Xe máy".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            long countOto = phuongTienList.stream()
                    .filter(pt -> "Ô tô".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            System.out.println("   📊 Phương tiện: Xe đạp=" + countXeDap + ", Xe máy=" + countXeMay + ", Ô tô=" + countOto);
            
            // Tính phí nhanh bằng Stream API
            int totalVehicleFee = 0;
            
            if (khoanThuDto.getPhiGuiXeList() != null) {
                // Map loại xe -> phí, tính tổng
                totalVehicleFee = khoanThuDto.getPhiGuiXeList().stream()
                        .mapToInt(phi -> {
                            String loaiXe = phi.getLoaiXe();
                            int soLuong = 0;
                            
                            switch (loaiXe) {
                                case "Xe đạp": soLuong = (int) countXeDap; break;
                                case "Xe máy": soLuong = (int) countXeMay; break;
                                case "Ô tô": soLuong = (int) countOto; break;
                            }
                            
                            int phiLoaiXe = soLuong * phi.getSoTien();
                            if (soLuong > 0) {
                                System.out.println("     • " + loaiXe + ": " + soLuong + " x " + 
                                                 String.format("%,d", phi.getSoTien()) + " = " + 
                                                 String.format("%,d", phiLoaiXe) + " VND");
                            }
                            return phiLoaiXe;
                        })
                        .sum();
                        
                System.out.println("   🎯 TỔNG PHÍ PHƯƠNG TIỆN: " + String.format("%,d", totalVehicleFee) + " VND");
            } else {
                totalVehicleFee = khoanThuDto.getSoTien();
                System.out.println("   ⚠️ Không có bảng giá -> Dùng phí cơ bản: " + String.format("%,d", totalVehicleFee) + " VND");
            }
            
            return totalVehicleFee;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi tính phí phương tiện tối ưu: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Simple invoice amount calculation to avoid complex logic that might cause exceptions
     */
    private Integer calculateSimpleInvoiceAmount(KhoanThuDto khoanThuDto, CanHo canHo) {
        try {
            String donViTinh = khoanThuDto.getDonViTinh();
            System.out.println("💰 Calculating fee for apartment " + canHo.getMaCanHo() + " with unit: " + donViTinh);
            
            switch (donViTinh) {
                case "Diện tích":
                    // Tính phí theo diện tích
                    if (khoanThuDto.getPhamVi().equals("Căn hộ đang sử dụng") && 
                        !canHo.getTrangThaiSuDung().equals("Đang sử dụng")) {
                        System.out.println("   ⏭️ Bỏ qua căn hộ không sử dụng: " + canHo.getMaCanHo());
                        return 0;
                    }
                    int areaFee = (int) Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                    System.out.println("   📐 Phí theo diện tích: " + khoanThuDto.getSoTien() + " x " + canHo.getDienTich() + "m² = " + areaFee + " VND");
                    return areaFee;
                    
                case "Phương tiện":
                    // Tính phí theo phương tiện thực tế
                    return calculateVehicleFeeFromDatabase(khoanThuDto, canHo);
                    
                default:
                    // Các loại phí khác dùng số tiền cơ bản
                    int baseFee = khoanThuDto.getSoTien();
                    System.out.println("   💰 Phí cơ bản: " + baseFee + " VND");
                    return baseFee;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi tính toán phí cho căn hộ " + canHo.getMaCanHo() + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tính phí phương tiện dựa trên dữ liệu thực tế từ database
     */
    private Integer calculateVehicleFeeFromDatabase(KhoanThuDto khoanThuDto, CanHo canHo) {
        try {
            System.out.println("   🚗 Kiểm tra điều kiện phí phương tiện cho căn hộ: " + canHo.getMaCanHo());
            
            // ĐIỀU KIỆN 1: Căn hộ phải đang có người ở
            if (!"Đang sử dụng".equals(canHo.getTrangThaiSuDung())) {
                System.out.println("   ❌ Căn hộ " + canHo.getMaCanHo() + " không đang sử dụng (Trạng thái: " + canHo.getTrangThaiSuDung() + ") -> Bỏ qua");
                return 0;
            }
            
            // ĐIỀU KIỆN 2: Lấy danh sách phương tiện từ database
            CanHo apartmentWithVehicles = canHoRepository.findAllWithPhuongTien().stream()
                    .filter(c -> c.getMaCanHo().equals(canHo.getMaCanHo()))
                    .findFirst()
                    .orElse(canHo);
            
            List<io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien> phuongTienList = 
                apartmentWithVehicles.getPhuongTienList();
            
            if (phuongTienList == null || phuongTienList.isEmpty()) {
                System.out.println("   ❌ Căn hộ " + canHo.getMaCanHo() + " không có phương tiện nào -> Bỏ qua (Phí = 0)");
                return 0;
            }
            
            System.out.println("   ✅ Căn hộ " + canHo.getMaCanHo() + " đủ điều kiện:");
            System.out.println("     • Trạng thái: " + canHo.getTrangThaiSuDung());
            System.out.println("     • Số phương tiện: " + phuongTienList.size() + " chiếc");
            
            // Đếm từng loại xe
            long countXeDap = phuongTienList.stream()
                    .filter(pt -> "Xe đạp".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            long countXeMay = phuongTienList.stream()
                    .filter(pt -> "Xe máy".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            long countOto = phuongTienList.stream()
                    .filter(pt -> "Ô tô".equals(pt.getLoaiPhuongTien()))
                    .count();
            
            System.out.println("   📊 Chi tiết phương tiện:");
            System.out.println("     • Xe đạp: " + countXeDap + " chiếc");
            System.out.println("     • Xe máy: " + countXeMay + " chiếc"); 
            System.out.println("     • Ô tô: " + countOto + " chiếc");
            
            // Kiểm tra có ít nhất 1 phương tiện
            if (countXeDap == 0 && countXeMay == 0 && countOto == 0) {
                System.out.println("   ❌ Không có phương tiện hợp lệ -> Bỏ qua (Phí = 0)");
                return 0;
            }
            
            // Lấy bảng giá từ khoản thu và tính phí
            int totalVehicleFee = 0;
            
            if (khoanThuDto.getPhiGuiXeList() != null && !khoanThuDto.getPhiGuiXeList().isEmpty()) {
                // Tính phí xe đạp
                int phiXeDap = khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phi -> "Xe đạp".equals(phi.getLoaiXe()))
                        .findFirst()
                        .map(phi -> phi.getSoTien())
                        .orElse(0);
                int tienXeDap = (int)(countXeDap * phiXeDap);
                
                // Tính phí xe máy
                int phiXeMay = khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phi -> "Xe máy".equals(phi.getLoaiXe()))
                        .findFirst()
                        .map(phi -> phi.getSoTien())
                        .orElse(0);
                int tienXeMay = (int)(countXeMay * phiXeMay);
                
                // Tính phí ô tô
                int phiOto = khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phi -> "Ô tô".equals(phi.getLoaiXe()))
                        .findFirst()
                        .map(phi -> phi.getSoTien())
                        .orElse(0);
                int tienOto = (int)(countOto * phiOto);
                
                // Tổng phí
                totalVehicleFee = tienXeDap + tienXeMay + tienOto;
                
                System.out.println("   💰 Chi tiết tính phí:");
                System.out.println("     • Xe đạp: " + countXeDap + " x " + String.format("%,d", phiXeDap) + " = " + String.format("%,d", tienXeDap) + " VND");
                System.out.println("     • Xe máy: " + countXeMay + " x " + String.format("%,d", phiXeMay) + " = " + String.format("%,d", tienXeMay) + " VND");
                System.out.println("     • Ô tô: " + countOto + " x " + String.format("%,d", phiOto) + " = " + String.format("%,d", tienOto) + " VND");
                System.out.println("     • 🎯 TỔNG PHÍ: " + String.format("%,d", totalVehicleFee) + " VND");
                
            } else {
                System.out.println("   ⚠️ Không tìm thấy bảng giá phương tiện -> Sử dụng phí cơ bản");
                totalVehicleFee = khoanThuDto.getSoTien();
            }
            
            return totalVehicleFee;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi tính phí phương tiện cho căn hộ " + canHo.getMaCanHo() + ": " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: Trả về 0 nếu có lỗi để bỏ qua căn hộ này
            System.out.println("   🔄 Fallback: Bỏ qua căn hộ do lỗi (Phí = 0)");
            return 0;
        }
    }
    
    /**
     * Calculate invoice amount based on fee type and apartment details
     */
    private Integer calculateInvoiceAmount(KhoanThuDto khoanThuDto, CanHo canHo) {
        try {
            String donViTinh = khoanThuDto.getDonViTinh();
            System.out.println("💰 Calculating fee with unit: " + donViTinh + ", scope: " + khoanThuDto.getPhamVi());
            
            switch (donViTinh) {
                case "Diện tích":
                    if (khoanThuDto.getPhamVi().equals("Tất cả")) {
                        int soTien = (int) Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                        System.out.println("   📐 Area-based calculation (All): " + khoanThuDto.getSoTien() + " x " + canHo.getDienTich() + " = " + soTien);
                        return soTien;
                    } else if (khoanThuDto.getPhamVi().equals("Căn hộ đang sử dụng")) {
                        if (canHo.getTrangThaiSuDung().equals("Đang sử dụng")) {
                            int soTien = (int) Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                            System.out.println("   📐 Area-based calculation (In-use): " + khoanThuDto.getSoTien() + " x " + canHo.getDienTich() + " = " + soTien);
                            return soTien;
                        } else {
                            System.out.println("   ⏭️ Skipping apartment " + canHo.getMaCanHo() + " (not in use: " + canHo.getTrangThaiSuDung() + ")");
                            return 0;
                        }
                    }
                    break;
                    
                case "Phương tiện":
                    try {
                        // Load vehicles for this apartment with explicit handling
                        List<io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien> phuongTienList = canHo.getPhuongTienList();
                        if (phuongTienList == null) {
                            System.out.println("   🚗 No vehicles found for apartment " + canHo.getMaCanHo() + " (list is null)");
                            return 0;
                        }
                        
                        System.out.println("   🚗 Found " + phuongTienList.size() + " vehicles for apartment " + canHo.getMaCanHo());
                        
                        // Calculate fees for each vehicle type
                        int totalVehicleFee = 0;
                        
                        // Count and calculate for Xe đạp
                        int countXeDap = (int) phuongTienList.stream()
                                .filter(phuongTien -> "Xe đạp".equals(phuongTien.getLoaiPhuongTien()))
                                        .count();
                        int soTienXeDap = khoanThuDto.getPhiGuiXeList() != null ? 
                                khoanThuDto.getPhiGuiXeList().stream()
                                    .filter(phiGuiXe -> "Xe đạp".equals(phiGuiXe.getLoaiXe()))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                    .orElse(0) : 0;
                    int tienGuiXeDap = countXeDap * soTienXeDap;

                        // Count and calculate for Xe máy  
                        int countXeMay = (int) phuongTienList.stream()
                                .filter(phuongTien -> "Xe máy".equals(phuongTien.getLoaiPhuongTien()))
                                        .count();
                        int soTienXeMay = khoanThuDto.getPhiGuiXeList() != null ?
                                khoanThuDto.getPhiGuiXeList().stream()
                                    .filter(phiGuiXe -> "Xe máy".equals(phiGuiXe.getLoaiXe()))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                    .orElse(0) : 0;
                    int tienGuiXeMay = countXeMay * soTienXeMay;

                        // Count and calculate for Ô tô
                        int countOto = (int) phuongTienList.stream()
                                .filter(phuongTien -> "Ô tô".equals(phuongTien.getLoaiPhuongTien()))
                                        .count();
                        int soTienOto = khoanThuDto.getPhiGuiXeList() != null ?
                                khoanThuDto.getPhiGuiXeList().stream()
                                    .filter(phiGuiXe -> "Ô tô".equals(phiGuiXe.getLoaiXe()))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                    .orElse(0) : 0;
                    int tienGuiOto = countOto * soTienOto;
                        
                        totalVehicleFee = tienGuiXeDap + tienGuiXeMay + tienGuiOto;
                        
                        System.out.println("   🚗 Vehicle calculation details:");
                        System.out.println("     - Xe đạp: " + countXeDap + " x " + soTienXeDap + " = " + tienGuiXeDap);
                        System.out.println("     - Xe máy: " + countXeMay + " x " + soTienXeMay + " = " + tienGuiXeMay);
                        System.out.println("     - Ô tô: " + countOto + " x " + soTienOto + " = " + tienGuiOto);
                        System.out.println("     - Total: " + totalVehicleFee);
                        
                        return totalVehicleFee;
                        
                    } catch (org.hibernate.LazyInitializationException e) {
                        System.err.println("   ❌ LazyInitializationException when accessing vehicles for " + canHo.getMaCanHo() + ": " + e.getMessage());
                        // Fallback: try to get vehicles through repository
                        try {
                            CanHo reloadedCanHo = canHoRepository.findAllWithPhuongTien().stream()
                                    .filter(c -> c.getMaCanHo().equals(canHo.getMaCanHo()))
                                    .findFirst()
                                    .orElse(null);
                            if (reloadedCanHo != null && reloadedCanHo.getPhuongTienList() != null) {
                                System.out.println("   🔄 Reloaded apartment with vehicles, trying calculation again...");
                                return calculateVehicleFeeFromList(khoanThuDto, reloadedCanHo.getPhuongTienList());
                            }
                        } catch (Exception ex) {
                            System.err.println("   ❌ Failed to reload apartment with vehicles: " + ex.getMessage());
                        }
                        
                        // Final fallback - return base amount if vehicle calculation fails
                        System.out.println("   ⚠️ Using base vehicle fee due to loading issues: " + khoanThuDto.getSoTien());
                        return khoanThuDto.getSoTien();
                    }
                    
                default:
                    System.out.println("   ⚠️ Unknown fee unit: " + donViTinh + ", using base amount");
                    return khoanThuDto.getSoTien();
            }
            
            return khoanThuDto.getSoTien(); // Default fallback
            
        } catch (Exception e) {
            System.err.println("❌ Error calculating invoice amount for apartment " + canHo.getMaCanHo() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Helper method to calculate vehicle fees from a given vehicle list
     */
    private Integer calculateVehicleFeeFromList(KhoanThuDto khoanThuDto, List<io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien> phuongTienList) {
        try {
            if (phuongTienList == null || phuongTienList.isEmpty()) {
                return 0;
            }
            
            int totalVehicleFee = 0;
            
            // Count and calculate for each vehicle type
            int countXeDap = (int) phuongTienList.stream()
                    .filter(phuongTien -> "Xe đạp".equals(phuongTien.getLoaiPhuongTien()))
                    .count();
            int soTienXeDap = khoanThuDto.getPhiGuiXeList() != null ? 
                    khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phiGuiXe -> "Xe đạp".equals(phiGuiXe.getLoaiXe()))
                        .findFirst()
                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                        .orElse(0) : 0;
            
            int countXeMay = (int) phuongTienList.stream()
                    .filter(phuongTien -> "Xe máy".equals(phuongTien.getLoaiPhuongTien()))
                    .count();
            int soTienXeMay = khoanThuDto.getPhiGuiXeList() != null ?
                    khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phiGuiXe -> "Xe máy".equals(phiGuiXe.getLoaiXe()))
                        .findFirst()
                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                        .orElse(0) : 0;
            
            int countOto = (int) phuongTienList.stream()
                    .filter(phuongTien -> "Ô tô".equals(phuongTien.getLoaiPhuongTien()))
                    .count();
            int soTienOto = khoanThuDto.getPhiGuiXeList() != null ?
                    khoanThuDto.getPhiGuiXeList().stream()
                        .filter(phiGuiXe -> "Ô tô".equals(phiGuiXe.getLoaiXe()))
                        .findFirst()
                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                        .orElse(0) : 0;
            
            totalVehicleFee = (countXeDap * soTienXeDap) + (countXeMay * soTienXeMay) + (countOto * soTienOto);
            
            System.out.println("   🚗 Vehicle fee calculation from list: " + totalVehicleFee);
            return totalVehicleFee;
            
        } catch (Exception e) {
            System.err.println("❌ Error in calculateVehicleFeeFromList: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public List<HoaDonDto> getAllHoaDon() {
        // Get all bills from repository
        List<HoaDon> hoaDonList = hoaDonRepository.findAll();
        // Convert entities to DTOs and return
        return hoaDonList.stream()
                .map(hoaDonMapper::toHoaDonDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto addHoaDonTuNguyen(HoaDonTuNguyenDto hoaDonTuNguyenDto, KhoanThuTuNguyenDto khoanThuTuNguyenDto) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm hóa đơn tự nguyện. Chỉ Kế toán mới được phép.");
        }
        hoaDonTuNguyenDto.setTenKhoanThu(khoanThuTuNguyenDto.getTenKhoanThu());
        HoaDon hoaDon = hoaDonMapper.fromHoaDonTuNguyenDto(hoaDonTuNguyenDto);
        hoaDon.setNgayNop(LocalDateTime.now());
        hoaDon.setDaNop(true);
        hoaDonRepository.save(hoaDon);
        KhoanThu khoanThu = khoanThuRepository.findById(khoanThuTuNguyenDto.getMaKhoanThu()).orElse(null);
        khoanThu.setTaoHoaDon(true);
        khoanThuRepository.save(khoanThu);
        return new ResponseDto(true, "Hóa đơn đã được thêm thành công");
    }

    @Override
    public ResponseDto importFromExcel(MultipartFile file) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền import hóa đơn. Chỉ Kế toán mới được phép.");
        }
        try {
            System.out.println("=== DEBUG: Starting HoaDon Excel import ===");
            File tempFile = File.createTempFile("hoadondichvu_temp", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            
            Function<Row, HoaDonDichVuDto> rowMapper = row -> {
                try {
                    // Skip header row
                    if (row.getRowNum() == 0) {
                        System.out.println("DEBUG: Skipping header row");
                        return null;
                    }
                    
                    System.out.println("DEBUG: Processing row " + row.getRowNum());
                    
                    HoaDonDichVuDto dto = new HoaDonDichVuDto();
                    
                    // Tên khoản thu (column 0)
                    String tenKhoanThu = "";
                    if (row.getCell(0) != null) {
                        try {
                            tenKhoanThu = row.getCell(0).getStringCellValue();
                        } catch (Exception e) {
                            tenKhoanThu = String.valueOf(row.getCell(0).getNumericCellValue());
                        }
                    }
                    dto.setTenKhoanThu(tenKhoanThu);
                    System.out.println("DEBUG: Tên khoản thu: " + tenKhoanThu);
                    
                    // Mã căn hộ (column 1)
                    String maCanHo = "";
                    if (row.getCell(1) != null) {
                        try {
                            maCanHo = row.getCell(1).getStringCellValue();
                        } catch (Exception e) {
                            maCanHo = String.valueOf((long) row.getCell(1).getNumericCellValue());
                        }
                    }
                    dto.setMaCanHo(maCanHo);
                    System.out.println("DEBUG: Mã căn hộ: " + maCanHo);
                    
                    // Số tiền (column 2)
                    int soTien = 0;
                    if (row.getCell(2) != null) {
                        try {
                            soTien = (int) row.getCell(2).getNumericCellValue();
                        } catch (Exception e) {
                            try {
                                String soTienStr = row.getCell(2).getStringCellValue().replaceAll("[^0-9]", "");
                                soTien = Integer.parseInt(soTienStr);
                            } catch (Exception e2) {
                                System.err.println("DEBUG: Không thể đọc số tiền ở row " + row.getRowNum());
                                soTien = 0;
                            }
                        }
                    }
                    dto.setSoTien(soTien);
                    System.out.println("DEBUG: Số tiền: " + soTien);
                    
                    // Validation cơ bản
                    if (tenKhoanThu.trim().isEmpty() || maCanHo.trim().isEmpty() || soTien <= 0) {
                        System.err.println("DEBUG: Invalid data at row " + row.getRowNum() + 
                            " - tenKhoanThu: '" + tenKhoanThu + "', maCanHo: '" + maCanHo + "', soTien: " + soTien);
                        return null;
                    }
                    
                    System.out.println("DEBUG: Successfully parsed row " + row.getRowNum());
                    return dto;
                } catch (Exception e) {
                    System.err.println("ERROR: Lỗi khi đọc dòng Excel hóa đơn row " + row.getRowNum() + ": " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            };
            
            List<HoaDonDichVuDto> hoaDonDichVuDtoList = XlxsFileUtil.importFromExcel(tempFile.getAbsolutePath(), rowMapper);
            System.out.println("DEBUG: Parsed " + hoaDonDichVuDtoList.size() + " valid rows from Excel");
            
            // Filter out null values
            hoaDonDichVuDtoList = hoaDonDichVuDtoList.stream()
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
            
            if (hoaDonDichVuDtoList.isEmpty()) {
                tempFile.delete();
                return new ResponseDto(false, "Không có dữ liệu hợp lệ trong file Excel. Vui lòng kiểm tra lại format file.");
            }
            
            // Validate and map to entities
            List<HoaDon> hoaDonList = new ArrayList<>();
            for (HoaDonDichVuDto dto : hoaDonDichVuDtoList) {
                try {
                    // Check if apartment exists
                    CanHo canHo = canHoRepository.findById(dto.getMaCanHo()).orElse(null);
                    if (canHo == null) {
                        System.err.println("WARNING: Căn hộ không tồn tại: " + dto.getMaCanHo() + " - bỏ qua hóa đơn này");
                        continue;
                    }
                    
                    // Create HoaDon entity
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setCanHo(canHo);
                    hoaDon.setSoTien(dto.getSoTien());
                    hoaDon.setDaNop(false); // Mặc định chưa nộp
                    hoaDon.setNgayNop(null);
                    
                    // Create temporary KhoanThu for the invoice
                    KhoanThu khoanThu = new KhoanThu();
                    khoanThu.setTenKhoanThu(dto.getTenKhoanThu());
                    khoanThu.setBatBuoc(false); // Default to optional
                    khoanThu.setDonViTinh("Theo hóa đơn");
                    khoanThu.setSoTien(0);
                    khoanThu.setPhamVi("Theo căn hộ");
                    khoanThu.setNgayTao(java.time.LocalDate.now());
                    khoanThu.setThoiHan(java.time.LocalDate.now().plusMonths(1));
                    khoanThu.setGhiChu("Bên thứ 3");
                    khoanThu.setTaoHoaDon(true);
                    
                    // Save KhoanThu first if it doesn't exist
                    KhoanThu existingKhoanThu = khoanThuRepository.findAll().stream()
                        .filter(kt -> kt.getTenKhoanThu().equals(dto.getTenKhoanThu()))
                        .findFirst()
                        .orElse(null);
                    
                    if (existingKhoanThu == null) {
                        khoanThu = khoanThuRepository.save(khoanThu);
                        System.out.println("DEBUG: Created new KhoanThu: " + dto.getTenKhoanThu());
                    } else {
                        khoanThu = existingKhoanThu;
                        System.out.println("DEBUG: Using existing KhoanThu: " + dto.getTenKhoanThu());
                    }
                    
                    hoaDon.setKhoanThu(khoanThu);
                    hoaDonList.add(hoaDon);
                    
                } catch (Exception e) {
                    System.err.println("ERROR: Lỗi khi tạo hóa đơn từ DTO: " + e.getMessage());
                    continue;
                }
            }
            
            if (hoaDonList.isEmpty()) {
                tempFile.delete();
                return new ResponseDto(false, "Không thể tạo hóa đơn nào từ dữ liệu Excel. Vui lòng kiểm tra lại mã căn hộ.");
            }
            
            hoaDonRepository.saveAll(hoaDonList);
            tempFile.delete();
            
            System.out.println("DEBUG: Successfully imported " + hoaDonList.size() + " invoices");
            return new ResponseDto(true, "Đã import thành công " + hoaDonList.size() + " hóa đơn từ file Excel.");
        } catch (Exception e) {
            System.err.println("ERROR: Import hóa đơn từ Excel thất bại: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Import hóa đơn từ Excel thất bại: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto updateTrangThaiThanhToan(Integer maHoaDon, boolean daNop) {
        try {
            System.out.println("💰 Updating payment status for invoice: " + maHoaDon + " to " + (daNop ? "PAID" : "UNPAID"));
            
            // Tìm hóa đơn trong database
            HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
            if (hoaDon == null) {
                System.err.println("❌ Invoice not found: " + maHoaDon);
                return new ResponseDto(false, "Không tìm thấy hóa đơn với mã: " + maHoaDon);
            }
            
            // Cập nhật trạng thái thanh toán
            hoaDon.setDaNop(daNop);
            
            // Cập nhật ngày nộp nếu đã thanh toán
            if (daNop) {
                hoaDon.setNgayNop(LocalDateTime.now());
                System.out.println("✅ Set payment date to: " + hoaDon.getNgayNop());
            } else {
                hoaDon.setNgayNop(null);
                System.out.println("🔄 Cleared payment date");
            }
            
            // Lưu vào database
            hoaDonRepository.save(hoaDon);
            
            String statusMessage = daNop ? "đã thanh toán" : "chưa thanh toán";
            System.out.println("✅ Successfully updated invoice " + maHoaDon + " to " + statusMessage);
            
            return new ResponseDto(true, "Cập nhật trạng thái hóa đơn thành công: " + statusMessage);
            
        } catch (Exception e) {
            System.err.println("❌ Error updating payment status: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Lỗi khi cập nhật trạng thái thanh toán: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto thuToanBoPhiCanHo(String maCanHo, List<HoaDonDto> hoaDonList) {
        try {
            System.out.println("💰 Starting bulk payment for apartment: " + maCanHo);
            System.out.println("   📋 Processing " + hoaDonList.size() + " invoices");
            
            // Kiểm tra quyền
            if (Session.getCurrentUser() == null) {
                return new ResponseDto(false, "Vui lòng đăng nhập để thực hiện thao tác này");
            }
            
            String userRole = Session.getCurrentUser().getVaiTro();
            if (!"Kế toán".equals(userRole)) {
                return new ResponseDto(false, "Bạn không có quyền thu phí. Chỉ Kế toán mới có thể thực hiện");
            }
            
            if (hoaDonList == null || hoaDonList.isEmpty()) {
                return new ResponseDto(false, "Không có hóa đơn nào để thu");
            }
            
            int successCount = 0;
            int totalAmount = 0;
            List<String> errors = new ArrayList<>();
            
            // Xử lý từng hóa đơn
            for (HoaDonDto hoaDonDto : hoaDonList) {
                try {
                    // Chỉ xử lý hóa đơn chưa thanh toán
                    if (!hoaDonDto.isDaNop()) {
                        ResponseDto result = updateTrangThaiThanhToan(hoaDonDto.getMaHoaDon(), true);
                        if (result.isSuccess()) {
                            successCount++;
                            totalAmount += hoaDonDto.getSoTien() != null ? hoaDonDto.getSoTien() : 0;
                            System.out.println("   ✅ Processed invoice: " + hoaDonDto.getMaHoaDon() + " - " + hoaDonDto.getTenKhoanThu());
                        } else {
                            errors.add("Hóa đơn " + hoaDonDto.getMaHoaDon() + ": " + result.getMessage());
                            System.err.println("   ❌ Failed to process invoice: " + hoaDonDto.getMaHoaDon());
                        }
                    } else {
                        System.out.println("   ⏭️ Skipping already paid invoice: " + hoaDonDto.getMaHoaDon());
                    }
                } catch (Exception e) {
                    errors.add("Hóa đơn " + hoaDonDto.getMaHoaDon() + ": " + e.getMessage());
                    System.err.println("   ❌ Exception processing invoice " + hoaDonDto.getMaHoaDon() + ": " + e.getMessage());
                }
            }
            
            // Tạo thông báo kết quả
            StringBuilder message = new StringBuilder();
            message.append("Thu phí thành công cho căn hộ ").append(maCanHo).append("\n");
            message.append("📊 Tổng kết:\n");
            message.append("  • Đã thu: ").append(successCount).append(" hóa đơn\n");
            message.append("  • Tổng tiền: ").append(String.format("%,d", totalAmount)).append(" VNĐ\n");
            
            if (!errors.isEmpty()) {
                message.append("  • Lỗi: ").append(errors.size()).append(" hóa đơn\n");
                message.append("\n❌ Chi tiết lỗi:\n");
                for (String error : errors) {
                    message.append("  - ").append(error).append("\n");
                }
            }
            
            boolean isSuccess = successCount > 0;
            System.out.println("🏁 Bulk payment completed: " + successCount + "/" + hoaDonList.size() + " success");
            
            return new ResponseDto(isSuccess, message.toString().trim());
            
        } catch (Exception e) {
            System.err.println("❌ Error in bulk payment: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Lỗi khi thu toàn bộ phí: " + e.getMessage());
        }
    }
}
