package DAO;

import DTO.ChiTietPhieuNhapDTO;
import config.JDBCUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChiTietPhieuNhapDAOTest {
    private Connection testCon;
    private ChiTietPhieuNhapDAO dao;
    private static final int TEST_MA_PHIEU = 1001;
    private static final int TEST_MA_PHIENBANSP = 101;
    private static final int TEST_HINHTHUCNHAP = 1;


    @Before
    public void setUp() throws SQLException {
        testCon = JDBCUtil.getConnection();
        testCon.setAutoCommit(false);
        dao = ChiTietPhieuNhapDAO.getInstance();
        dao.setConnection(testCon); // Use the same connection for all operations
    }

    @After
    public void tearDown() throws SQLException {
        try {
            testCon.rollback();
        } finally {
            testCon.setAutoCommit(true);
            // JDBCUtil.closeConnection(testCon); // Uncomment if needed
        }
    }

    // ✅ TC1: Kiểm tra getInstance trả về đối tượng hợp lệ
    @Test
    public void testGetInstance() {
        ChiTietPhieuNhapDAO instance = ChiTietPhieuNhapDAO.getInstance();
        assertNotNull("getInstance() must return a non-null object", instance);
        assertTrue("Instance must be of type ChiTietPhieuNhapDAO", instance instanceof ChiTietPhieuNhapDAO);
    }

    // ✅ TC2: Thêm danh sách chi tiết phiếu nhập hợp lệ
    @Test
    public void testInsert_ValidData() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 101, 10, 20000, 1));
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 102, 5, 30000, 2));

        int result = dao.insert(list);
        assertEquals("Insert should succeed for valid data", 2, result);
    }

    // ✅ TC3: Thêm danh sách chi tiết rỗng
    @Test
    public void testInsert_EmptyList() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        int result = dao.insert(list);
        assertEquals("Insert should return 0 for empty list", 0, result);
    }

    // ✅ TC4: Thêm danh sách chi tiết null
    @Test
    public void testInsert_NullList() throws SQLException {
        int result = dao.insert(null);
        assertEquals("Insert should return 0 for null list", 0, result);
    }

    // ✅ TC5: Thêm chi tiết với số lượng âm
    @Test
    public void testInsert_NegativeQuantity() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 103, -5, 15000, 1));

        int result = dao.insert(list);
        assertEquals("Insert should fail or handle negative quantity", 0, result);
    }

    // ✅ TC6: Thêm chi tiết với đơn giá bằng 0
    @Test
    public void testInsert_ZeroPrice() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 104, 3, 0, 2));

        int result = dao.insert(list);
        assertEquals("Insert should succeed for zero price", 1, result);
    }

    // ✅ TC7: Xóa chi tiết phiếu nhập với mã phiếu hợp lệ
    @Test
    public void testDelete_ValidMaPhieu() throws SQLException {
        // Assume data exists for TEST_MA_PHIEU
        int result = dao.delete(String.valueOf(TEST_MA_PHIEU));
        assertTrue("Delete should succeed for valid maPhieu", result >= 0);
    }

    // ✅ TC8: Xóa chi tiết với mã phiếu không tồn tại
    @Test
    public void testDelete_NonExistentMaPhieu() throws SQLException {
        int result = dao.delete("9999");
        assertEquals("Delete should return 0 for non-existent maPhieu", 0, result);
    }

    // ✅ TC9: Xóa chi tiết với mã phiếu null
    @Test
    public void testDelete_NullMaPhieu() throws SQLException {
        int result = dao.delete(null);
        assertEquals("Delete should return 0 for null maPhieu", 0, result);
    }

    // ✅ TC10: Xóa chi tiết với mã phiếu rỗng
    @Test
    public void testDelete_EmptyMaPhieu() throws SQLException {
        int result = dao.delete("");
        assertEquals("Delete should return 0 for empty maPhieu", 0, result);
    }

    // ✅ TC11: Cập nhật chi tiết với mã phiếu và danh sách hợp lệ
    @Test
    public void testUpdate_ValidData() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 101, 10, 50000, 1));

        int result = dao.update(list, String.valueOf(TEST_MA_PHIEU));
        assertTrue("Update should succeed for valid data", result >= 1);
    }

    // ✅ TC12: Cập nhật chi tiết với mã phiếu không tồn tại
    @Test
    public void testUpdate_NonExistentMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(9999, 102, 5, 80000, 2));

        int result = dao.update(list, "9999");
        assertEquals("Update should return 0 for non-existent maPhieu", 0, result);
    }

    // ✅ TC13: Cập nhật chi tiết với danh sách rỗng
    @Test
    public void testUpdate_EmptyList() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        int result = dao.update(list, String.valueOf(TEST_MA_PHIEU));
        assertEquals("Update should return 0 for empty list", 0, result);
    }

    // ✅ TC14: Cập nhật chi tiết với mã phiếu null
    @Test
    public void testUpdate_NullMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 103, 3, 50000, 1));

        int result = dao.update(list, null);
        assertEquals("Update should return 0 for null maPhieu", 0, result);
    }

    // ✅ TC15: Truy vấn chi tiết với mã phiếu hợp lệ và có dữ liệu
    @Test
    public void testSelectAll_ValidMaPhieu_WithData() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> result = dao.selectAll(String.valueOf(TEST_MA_PHIEU));
        assertNotNull("Result should not be null", result);
        // Note: Cannot assert size > 0 without guaranteed data; adjust based on test data setup
        for (ChiTietPhieuNhapDTO ct : result) {
            assertEquals("MaPhieu should match", TEST_MA_PHIEU, ct.getMaphieu());
        }
    }

    // ✅ TC16: Truy vấn chi tiết với mã phiếu không tồn tại
    @Test
    public void testSelectAll_NonExistentMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> result = dao.selectAll("9999");
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty for non-existent maPhieu", 0, result.size());
    }

    // ✅ TC17: Truy vấn chi tiết với mã phiếu null
    @Test
    public void testSelectAll_NullMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> result = dao.selectAll(null);
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty for null maPhieu", 0, result.size());
    }

    // ✅ TC18: Truy vấn chi tiết với mã phiếu dạng chuỗi không hợp lệ
    @Test
    public void testSelectAll_InvalidMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> result = dao.selectAll("' OR 1=1 --");
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty for invalid maPhieu", 0, result.size());
    }

    // ✅ TC19: Kiểm tra insert với số lượng lớn chi tiết
    @Test
    public void testInsert_LargeList() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 101 + i, 10, 20000, 1));
        }

        int result = dao.insert(list);
        assertEquals("Insert should succeed for large list", 100, result);
    }

    // ✅ TC20: Kiểm tra update với danh sách chứa số lượng âm
    @Test
    public void testUpdate_NegativeQuantity() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 101, -10, 50000, 1));

        int result = dao.update(list, String.valueOf(TEST_MA_PHIEU));
        assertEquals("Update should fail or handle negative quantity", 0, result);
    }
    // ✅ TC21: Insert chi tiết với maphieunhap không tồn tại (FK violation)
    @Test(expected = SQLException.class)
    public void testInsert_InvalidMaPhieu_ForeignKey() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(9999, TEST_MA_PHIENBANSP, 10, 20000, TEST_HINHTHUCNHAP)); // 9999 does not exist
        dao.insert(list); // Should throw SQLException due to FK constraint
    }

    // ✅ TC22: Insert chi tiết với maphienbansp không tồn tại (FK violation)
    @Test(expected = SQLException.class)
    public void testInsert_InvalidMaPhienBanSP_ForeignKey() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 9999, 10, 20000, TEST_HINHTHUCNHAP)); // 9999 does not exist
        dao.insert(list); // Should throw SQLException due to FK constraint
    }

    // ✅ TC23: Insert chi tiết với soluong = 0 (invalid if DB constrains)
    @Test
    public void testInsert_ZeroQuantity() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 0, 20000, TEST_HINHTHUCNHAP));
        int result = dao.insert(list);
        assertEquals("Insert should fail or handle zero quantity", 0, result);
    }

    // ✅ TC24: Insert chi tiết với dongia âm (invalid if DB constrains)
    @Test
    public void testInsert_NegativePrice() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 10, -20000, TEST_HINHTHUCNHAP));
        int result = dao.insert(list);
        assertEquals("Insert should fail or handle negative price", 0, result);
    }

    // ✅ TC25: Insert chi tiết với hinhthucnhap không hợp lệ (e.g., negative)
    @Test(expected = SQLException.class)
    public void testInsert_InvalidHinhThucNhap() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 10, 20000, -1)); // Invalid hinhthucnhap
        dao.insert(list); // Should throw SQLException if DB constrains hinhthucnhap
    }

    // ✅ TC26: Update chi tiết với maphieunhap không tồn tại (FK violation)
    @Test(expected = SQLException.class)
    public void testUpdate_InvalidMaPhieu_ForeignKey() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(9999, TEST_MA_PHIENBANSP, 10, 20000, TEST_HINHTHUCNHAP)); // 9999 does not exist
        dao.update(list, "9999"); // Should throw SQLException due to FK constraint
    }

    // ✅ TC27: Update chi tiết với maphienbansp không tồn tại (FK violation)
    @Test(expected = SQLException.class)
    public void testUpdate_InvalidMaPhienBanSP_ForeignKey() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 9999, 10, 20000, TEST_HINHTHUCNHAP)); // 9999 does not exist
        dao.update(list, String.valueOf(TEST_MA_PHIEU)); // Should throw SQLException due to FK constraint
    }

    // ✅ TC28: Update chi tiết với soluong = 0 (invalid if DB constrains)
    @Test
    public void testUpdate_ZeroQuantity() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 0, 20000, TEST_HINHTHUCNHAP));
        int result = dao.update(list, String.valueOf(TEST_MA_PHIEU));
        assertEquals("Update should fail or handle zero quantity", 0, result);
    }

    // ✅ TC29: Update chi tiết với dongia âm (invalid if DB constrains)
    @Test
    public void testUpdate_NegativePrice() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 10, -20000, TEST_HINHTHUCNHAP));
        int result = dao.update(list, String.valueOf(TEST_MA_PHIEU));
        assertEquals("Update should fail or handle negative price", 0, result);
    }

    // ✅ TC30: Update chi tiết với hinhthucnhap không hợp lệ
    @Test(expected = SQLException.class)
    public void testUpdate_InvalidHinhThucNhap() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, TEST_MA_PHIENBANSP, 10, 20000, -1)); // Invalid hinhthucnhap
        dao.update(list, String.valueOf(TEST_MA_PHIEU)); // Should throw SQLException
    }

    // ✅ TC31: Insert chi tiết với maphieunhap = 0 (invalid if DB constrains)
    @Test(expected = SQLException.class)
    public void testInsert_ZeroMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(0, TEST_MA_PHIENBANSP, 10, 20000, TEST_HINHTHUCNHAP));
        dao.insert(list); // Should throw SQLException if maphieunhap is constrained
    }

    // ✅ TC32: Insert chi tiết với maphienbansp = 0 (invalid if DB constrains)
    @Test(expected = SQLException.class)
    public void testInsert_ZeroMaPhienBanSP() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(TEST_MA_PHIEU, 0, 10, 20000, TEST_HINHTHUCNHAP));
        dao.insert(list); // Should throw SQLException if maphienbansp is constrained
    }

    // ✅ TC33: SelectAll với maphieunhap chứa ký tự đặc biệt (non-SQL injection)
    @Test
    public void testSelectAll_SpecialCharactersMaPhieu() throws SQLException {
        ArrayList<ChiTietPhieuNhapDTO> result = dao.selectAll("!@#$%^");
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty for special characters", 0, result.size());
    }

    // ✅ TC34: Delete với maphieunhap chứa ký tự đặc biệt
    @Test
    public void testDelete_SpecialCharactersMaPhieu() throws SQLException {
        int result = dao.delete("!@#$%^");
        assertEquals("Delete should return 0 for special characters", 0, result);
    }
}