package DAO;

import DTO.ThongKe.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ThongKeDAOTest {

    // getDoanhThuTheoTungNam
    @Test
    public void DoanhThuTheoTungNam_StartNegative() {
        Integer yearStart = -1;
        Integer yearEnd = 2023;
        ThongKeDAO dao = new ThongKeDAO();
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getDoanhThuTheoTungNam(yearStart, yearEnd)
        );
        Assert.assertEquals("Năm không thể âm", ex.getMessage());
    }

    @Test
    public void DoanhThuTheoTungNam_StartZero() {
        Integer yearStart = 0;
        Integer yearEnd = 2023;
        ThongKeDAO dao = new ThongKeDAO();
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getDoanhThuTheoTungNam(yearStart, yearEnd)
        );
        Assert.assertEquals("Năm không thể bằng không", ex.getMessage());
    }

    @Test
    public void DoanhThuTheoTungNam_EndNegative() {
        Integer yearStart = 2023;
        Integer yearEnd = -1;
        ThongKeDAO dao = ThongKeDAO.getInstance();
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getDoanhThuTheoTungNam(yearStart, yearEnd)
        );
        Assert.assertEquals("Năm không thể âm", ex.getMessage());
    }

    @Test
    public void DoanhThuTheoTungNam_EndZero() {
        Integer yearStart = 2023;
        Integer yearEnd = 0;
        ThongKeDAO dao = ThongKeDAO.getInstance();
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getDoanhThuTheoTungNam(yearStart, yearEnd)
        );
        Assert.assertEquals("Năm không thể bằng không", ex.getMessage());
    }

    @Test
    public void DoanhThuTheoTungNam_StartYearNhoHonEndYear() {
        Integer yearStart = 2021;
        Integer yearEnd = 2024;
        ThongKeDAO dao = ThongKeDAO.getInstance();
        ArrayList<ThongKeDoanhThuDTO> result = dao.getDoanhThuTheoTungNam(yearStart, yearEnd);
        Assert.assertEquals(4, result.size()); // empty thì fail

        Assert.assertEquals((long)yearStart, result.get(0).getThoigian());
        Assert.assertEquals((long)yearEnd, result.get(3).getThoigian());
    }

    @Test
    public void DoanhThuTheoTungNam_StartYearBangEndYear() {
        Integer yearStart = 2023;
        Integer yearEnd = 2023;
        ThongKeDAO dao = ThongKeDAO.getInstance();
        ArrayList<ThongKeDoanhThuDTO> result = dao.getDoanhThuTheoTungNam(yearStart, yearEnd);
        Assert.assertEquals(1, result.size()); // empty thì fail

        Assert.assertEquals((long)yearEnd, result.get(0).getThoigian());
    }

    @Test
    public void DoanhThuTheoTungNam_StartYearLonHonEndYear() {
        Integer yearStart = 2025;
        Integer yearEnd = 2023;
        ThongKeDAO dao = ThongKeDAO.getInstance();
        ArrayList<ThongKeDoanhThuDTO> result = dao.getDoanhThuTheoTungNam(yearStart, yearEnd);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getDoanhThuTheoTungNam(yearStart, yearEnd)
        );
        Assert.assertEquals("Năm bắt đầu không được lớn hơn năm kết thúc", ex.getMessage());
    }

    // getThongKeKhachHang
    @Test
    public void KhachHang_TonTaiKhachHangTrongThoiGianXet() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Hoàng Đức Anh";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeKhachHangDTO> result = dao.getThongKeKhachHang(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertFalse(result.isEmpty()); // empty thì fail
            for (ThongKeKhachHangDTO dto : result) {
                Assert.assertTrue(dto.getTenkh().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void KhachHang_TenRong() { // tên rỗng
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeKhachHangDTO> result = dao.getThongKeKhachHang(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertFalse(result.isEmpty()); // empty thì fail

            for (ThongKeKhachHangDTO dto : result) {
                Assert.assertTrue(dto.getTenkh().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void KhachHang_KhongTonTaiKhachHangTrongThoiGianXet() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Nguyễn Thị Minh Anh";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2024-01-01");
            Date EndDay = sdf.parse("2024-04-10");
            // Act
            ArrayList<ThongKeKhachHangDTO> result = dao.getThongKeKhachHang(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void KhachHang_KhongTonTaiKhachHang() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Nguyễn Văn AAAAAA";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeKhachHangDTO> result = dao.getThongKeKhachHang(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty()); // empty thi pass
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void KhachHang_StartLonHonEnd() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Nguyễn Văn A";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-12-01");
            Date EndDay = sdf.parse("2023-01-01");
            IllegalArgumentException ex =Assert.assertThrows(
                    IllegalArgumentException.class,
                    () -> dao.getThongKeKhachHang(NameExpected, StartDay, EndDay)
            );
            Assert.assertEquals("Ngày bắt đầu phải nhỏ hơn ngày kết thúc", ex.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void KhachHang_StartBangEnd() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Nguyễn Văn A";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-04-04");
            Date EndDay = sdf.parse("2023-04-04");
            // Act
            ArrayList<ThongKeKhachHangDTO> result = dao.getThongKeKhachHang(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertFalse(result.isEmpty()); // empty thi fail
            for (ThongKeKhachHangDTO dto : result) {
                Assert.assertTrue(dto.getTenkh().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // getThongKeNCC
    @Test
    public void NCC_TonTaiNCCTrongThoiGianXet() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Công Ty Samsung Việt Nam";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeNhaCungCapDTO> result = dao.getThongKeNCC(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertFalse(result.isEmpty()); // empty thì fail
            for (ThongKeNhaCungCapDTO dto : result) {
                Assert.assertTrue(dto.getTenncc().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void NCC_TenRong() { // tên rỗng
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeNhaCungCapDTO> result = dao.getThongKeNCC(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertFalse(result.isEmpty()); // empty thì fail

            for (ThongKeNhaCungCapDTO dto : result) {
                Assert.assertTrue(dto.getTenncc().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void NCC_KhongTonTaiNCCTrongThoiGianXet() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Công Ty Samsung Việt Nam";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2024-01-01");
            Date EndDay = sdf.parse("2024-05-10");
            // Act
            ArrayList<ThongKeNhaCungCapDTO> result = dao.getThongKeNCC(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void NCC_KhongTonTaiNCC() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Công ty Vovo Việt Nam";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-01-01");
            Date EndDay = sdf.parse("2023-05-10");
            // Act
            ArrayList<ThongKeNhaCungCapDTO> result = dao.getThongKeNCC(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty()); // empty thi pass
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void NCC_StartLonHonEnd() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Công ty Vivo Việt Nam";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-12-01");
            Date EndDay = sdf.parse("2023-01-01");
            IllegalArgumentException ex = Assert.assertThrows(
                    IllegalArgumentException.class,
                    () -> dao.getThongKeNCC(NameExpected, StartDay, EndDay)
            );
            Assert.assertEquals("Ngày bắt đầu phải nhỏ hơn ngày kết thúc", ex.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void NCC_StartBangEnd() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Công ty Vivo Việt Nam";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date StartDay = sdf.parse("2023-05-09");
            Date EndDay = sdf.parse("2023-05-09");
            // Act
            ArrayList<ThongKeNhaCungCapDTO> result = dao.getThongKeNCC(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertEquals(1, result.size());
            for (ThongKeNhaCungCapDTO dto : result) {
                Assert.assertTrue(dto.getTenncc().toLowerCase().contains(NameExpected.toLowerCase()));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // getThongKeTheoThang
    @Test
    public void TheoThang_YearNegative() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        Integer year = -1;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTheoThang(year)
        );
        Assert.assertEquals("Năm không thể âm", ex.getMessage());
    }

    @Test
    public void TheoThang_YearZero() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        Integer year = 0;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTheoThang(year)
        );
        Assert.assertEquals("Năm không thể bằng không", ex.getMessage());
    }

    @Test
    public void TheoThang_NamTonTai() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        Integer year = 2023;
        Boolean flag = false;
        ArrayList<ThongKeTheoThangDTO> result = dao.getThongKeTheoThang(year);
        Assert.assertFalse(result.isEmpty()); // empty thi fail
        for (ThongKeTheoThangDTO dto : result) {
            if (dto.getDoanhthu() > 0) {
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void TheoThang_NamKhongTonTai() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int year = 2033;
        ArrayList<ThongKeTheoThangDTO> result = dao.getThongKeTheoThang(year);
        for (ThongKeTheoThangDTO dto : result) {
            Assert.assertEquals(0, dto.getDoanhthu());
            Assert.assertEquals(0, dto.getChiphi());
        }
    }

    // getThongKeTungNgayTrongThang
    @Test
    public void TheoNgay_MonthNegative() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = -1;
        int year = 2023;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTungNgayTrongThang(month, year)
        );
        Assert.assertEquals("Tháng không thể âm", ex.getMessage());
    }

    @Test
    public void TheoNgay_MonthZero() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 0;
        int year = 2023;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTungNgayTrongThang(month, year)
        );
        Assert.assertEquals("Tháng không thể bằng không", ex.getMessage());
    }

    @Test
    public void TheoNgay_Month13() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 13;
        int year = 2023;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTungNgayTrongThang(month, year)
        );
        Assert.assertEquals("không có tháng lớn hơn 12", ex.getMessage());
    }

    @Test
    public void TheoNgay_YearNegative() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 11;
        int year = -1;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTungNgayTrongThang(month, year)
        );
        Assert.assertEquals("Năm không thể âm", ex.getMessage());
    }

    @Test
    public void TheoNgay_YearZero() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 11;
        int year = 0;
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.getThongKeTungNgayTrongThang(month, year)
        );
        Assert.assertEquals("Năm không thể bằng 0", ex.getMessage());
    }

    @Test
    public void TheoNgay_NgayVaNamTonTai() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 4;
        int year = 2023;
        Boolean flag = false;
        ArrayList<ThongKeTungNgayTrongThangDTO> result = dao.getThongKeTungNgayTrongThang(month, year);
        Assert.assertFalse(result.isEmpty()); // empty thi fail
        for (ThongKeTungNgayTrongThangDTO dto : result) {
            if (dto.getDoanhthu() > 0) {
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void TheoNgay_NgayVaNamKhongTonTai() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        int month = 4;
        int year = 2033;
        ArrayList<ThongKeTungNgayTrongThangDTO> result = dao.getThongKeTungNgayTrongThang(month, year);
        for (ThongKeTungNgayTrongThangDTO dto : result) {
            Assert.assertEquals(0, dto.getDoanhthu());
            Assert.assertEquals(0, dto.getChiphi());
        }
    }

    // getThongKe7NgayGanNhat
    @Test
    public void ThongKe7NgayGanNhat() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        ArrayList<ThongKeTungNgayTrongThangDTO> result = dao.getThongKe7NgayGanNhat();
        Assert.assertEquals(7, result.size());
    }

    // getThongKeTuNgayDenNgay
//    @Test
//    public void getThongKeTuNgayDenNgay_StartNhoHonEnd() {
//        String Start = "2023-01-01";
//        String End = "2023-10-10";
//        ThongKeDAO dao = ThongKeDAO.getInstance();
//
//    }
}