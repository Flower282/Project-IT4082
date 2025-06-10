# 📋 **Hướng Dẫn Import Hóa Đơn Excel**

## 🎯 **Format File Excel**

File Excel phải có **chính xác 3 cột** với thứ tự sau:

| Cột | Tên cột       | Kiểu dữ liệu | Bắt buộc | Ví dụ      |
|-----|---------------|--------------|----------|------------|
| A   | Tên khoản thu | Text         | ✅       | Tiền điện  |
| B   | Mã căn hộ     | Text/Number  | ✅       | CH001      |
| C   | Số tiền       | Number       | ✅       | 150000     |

⚠️ **LƯU Ý QUAN TRỌNG**: 
- **KHÔNG** cần cột "Mã hóa đơn" - hệ thống sẽ tự sinh
- Chỉ cần 3 cột như bảng trên
- **Tự động cập nhật trạng thái** khoản thu từ "Chưa tạo" → "Đã tạo"

## 📄 **Ví dụ file Excel:**

```
|   A           |   B    |   C     |
|---------------|--------|---------|
| Tên khoản thu | Mã căn | Số tiền | ← HEADER (dòng 1)
| Tiền điện     | CH001  | 150000  | ← DATA (dòng 2)
| Tiền nước     | CH001  | 80000   | ← DATA (dòng 3)
| Internet      | CH002  | 200000  | ← DATA (dòng 4)
```

## ✅ **Checklist trước khi import:**

### **1. Kiểm tra cấu trúc file:**
- [ ] File có đúng 3 cột (A, B, C)
- [ ] Dòng 1 là header, dữ liệu bắt đầu từ dòng 2
- [ ] Không có cột trống giữa các cột dữ liệu
- [ ] File định dạng `.xlsx` hoặc `.xls`

### **2. Kiểm tra dữ liệu:**
- [ ] **Tên khoản thu**: Không được để trống
- [ ] **Mã căn hộ**: Phải tồn tại trong hệ thống
- [ ] **Số tiền**: Phải là số > 0

### **3. Kiểm tra format cell:**
- [ ] Cột "Mã căn hộ": Format `Text` (không phải Number)
- [ ] Cột "Số tiền": Format `Number` (không có dấu phẩy, không có text)

## 🚀 **Các bước thực hiện:**

### **Bước 1: Chuẩn bị file Excel**
1. Tạo file Excel mới
2. Điền header ở dòng 1: `Tên khoản thu | Mã căn hộ | Số tiền`
3. Điền dữ liệu từ dòng 2 trở đi
4. Lưu file định dạng `.xlsx`

### **Bước 2: Import trong hệ thống**
1. Vào **form tạo/sửa khoản thu**
2. Chọn **"Bên thứ 3"** trong "Bộ phận quản lý"
3. Click nút **"Nhập excel hóa đơn"**
4. Chọn file Excel vừa tạo
5. Chờ hệ thống xử lý

### **Bước 3: Kiểm tra kết quả**
1. Xem thông báo kết quả import
2. Kiểm tra tab **"Lịch sử thu"** để xem hóa đơn đã tạo
3. Kiểm tra tab **"Khoản thu"** → trạng thái chuyển từ "Chưa tạo" → "Đã tạo"
4. Nếu có lỗi, mở Console (F12) để xem log chi tiết

## 🐛 **Troubleshooting**

### **Lỗi: "Không có dữ liệu hợp lệ trong file Excel"**

**Nguyên nhân có thể:**
- File không đúng format 3 cột
- Tất cả dòng dữ liệu đều có lỗi
- Header không đúng vị trí

**Cách fix:**
1. Mở Console (F12) → Tab Console
2. Tìm dòng log bắt đầu với `DEBUG:`
3. Kiểm tra thông tin chi tiết:
   ```
   DEBUG: Processing row 1 - Total cells: 3
   DEBUG: Tên khoản thu: 'Tiền điện'
   DEBUG: Mã căn hộ: 'CH001'
   DEBUG: Số tiền: 150000
   DEBUG: ✅ Successfully parsed row 1
   ```

### **Lỗi: "Mã căn hộ không tồn tại"**

**Cách kiểm tra mã căn hộ hợp lệ:**
1. Vào tab **"Căn hộ"** trong ứng dụng
2. Xem danh sách mã căn hộ có sẵn
3. So sánh với file Excel của bạn

**Các lỗi thường gặp:**
- ❌ Gõ sai: `CH01` thay vì `CH001`  
- ❌ Format số: `1` thay vì `CH001`
- ❌ Có khoảng trắng: `CH001 ` (có space)
- ❌ Case sensitive: `ch001` thay vì `CH001`

**Giải pháp:**
- Copy chính xác mã căn hộ từ tab "Căn hộ"
- Đảm bảo format cell là "Text" (không phải "Number")
- Kiểm tra không có khoảng trắng thừa

### **Lỗi: "Cannot read soTien"**
- Cột C phải là số, không phải text
- Không được có dấu phẩy (,) trong số tiền
- Ví dụ đúng: `150000`, sai: `150,000` hoặc `150 nghìn`

## 🔄 **Tự động cập nhật trạng thái**

Sau khi import hóa đơn thành công, hệ thống sẽ **tự động**:

✅ **Tạo hóa đơn mới** với trạng thái "Chưa thanh toán"  
✅ **Cập nhật khoản thu** từ "Chưa tạo hóa đơn" → "Đã tạo hóa đơn"  
✅ **Link hóa đơn với căn hộ** để hiển thị trong chi tiết căn hộ  
✅ **Tạo khoản thu mới** nếu "Tên khoản thu" chưa tồn tại  

**Kết quả:**
- Tab "Khoản thu" → cột "Trạng thái" chuyển thành "Đã tạo"  
- Tab "Lịch sử thu" → hiển thị hóa đơn mới
- Chi tiết căn hộ → thêm hóa đơn cần thanh toán

## 💡 **Tips để import thành công:**

### **1. Tạo file mẫu nhanh:**
```excel
A1: Tên khoản thu    B1: Mã căn hộ    C1: Số tiền
A2: Test            B2: CH001        C2: 1000
```

🚫 **KHÔNG làm như này:**
```excel
A1: Mã hóa đơn    B1: Tên khoản thu    C1: Mã căn hộ    D1: Số tiền  ← SAI!
A2: 1             B2: Test            C2: CH001        D2: 1000     ← SAI!
```

### **2. Format cells đúng cách:**
- Chọn cột B → Right click → Format Cells → Text
- Chọn cột C → Right click → Format Cells → Number

### **3. Test với dữ liệu nhỏ:**
- Tạo file có 1-2 dòng dữ liệu trước
- Import thành công rồi mới import file lớn

### **4. Backup trước khi import:**
- Export dữ liệu hiện tại trước khi import
- Có thể rollback nếu cần

## 📞 **Hỗ trợ**

Nếu vẫn gặp lỗi sau khi làm theo hướng dẫn:
1. Chụp màn hình thông báo lỗi
2. Mở Console (F12) và chụp log DEBUG
3. Gửi file Excel mẫu để kiểm tra
4. Liên hệ để được hỗ trợ

---
*Cập nhật: Tháng 12/2024* 