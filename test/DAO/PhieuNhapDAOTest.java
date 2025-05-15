package DAO;

import DTO.PhieuNhapDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PhieuNhapDAOTest {
    private Connection testCon;
    private PhieuNhapDAO dao;
    private static int maphieu = 1001;
    @Before
    public void setUp() throws SQLException {
        testCon = JDBCUtil.getConnection();
        testCon.setAutoCommit(false);
        dao = PhieuNhapDAO.getInstance();
        dao.setConnection(testCon);
    }

    @After
    public void tearDown() throws SQLException {
        try {
            testCon.rollback();
        } finally {
            testCon.setAutoCommit(true);
//            JDBCUtil.closeConnection(testCon);
        }
    }
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        PhieuNhapDAO dao = PhieuNhapDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("PhieuNhapDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của PhieuNhapDAO
        Assert.assertTrue("Object should be an instance of PhieuNhapDAO", dao instanceof PhieuNhapDAO);
    }
    // ✅ Case: update với dữ liệu hợp lệ
    @Test
    public void updatePhieuNhap_ValidData() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(500000);
        phieu.setTrangthai(1);

        int result = dao.update(phieu);
        assertEquals("Cập nhật thành công nên phải trả về 1", 1, result);
    }

    // ❌ Case: maphieunhap không tồn tại
    @Test
    public void updatePhieuNhap_NonExistentId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(9999); // giả sử không tồn tại
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(1000000);
        phieu.setTrangthai(1);

        int result = dao.update(phieu);
        assertEquals("Không có phiếu nhập với ID này nên update trả về 0", 0, result);
    }

    // ❌ Case: maphieunhap âm
    @Test
    public void updatePhieuNhap_NegativeId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(-1);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(100000);
        phieu.setTrangthai(1);

        int result = dao.update(phieu);
        assertEquals("maphieunhap âm → update trả về 0", 0, result);
    }

    // ❌ Case: thoigiantao null
    @Test
    public void updatePhieuNhap_NullTimestamp() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(null);
        phieu.setManhacungcap(1001);
        phieu.setTongTien(100000);
        phieu.setTrangthai(1);

        try {
            dao.update(phieu);
            fail("Expected SQLException due to null timestamp");
        } catch (NullPointerException e) {
            assertTrue(true); // expected
        }
    }

    // ❌ Case: manhacungcap âm
    @Test
    public void updatePhieuNhap_NegativeSupplierId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(-5);
        phieu.setTongTien(100000);
        phieu.setTrangthai(1);

        int result = dao.update(phieu);
        assertEquals("manhacungcap âm nên không update được", 0, result);
    }

    // ❌ Case: tongtien âm
    @Test
    public void updatePhieuNhap_NegativeTotalAmount() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(-100000);
        phieu.setTrangthai(1);

        int result = dao.update(phieu);
        assertEquals("tongtien âm nên update không thành công", 0, result);
    }

    // ❌ Case: trangthai không hợp lệ (>1)
    @Test
    public void updatePhieuNhap_InvalidTrangThaiHigh() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(100000);
        phieu.setTrangthai(5);

        int result = dao.update(phieu);
        assertEquals("trangthai không hợp lệ nên update trả về 0", 0, result);
    }

    // ❌ Case: trangthai không hợp lệ (<0)
    @Test
    public void updatePhieuNhap_InvalidTrangThaiNegative() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(100000);
        phieu.setTrangthai(-1);

        int result = dao.update(phieu);
        assertEquals("trangthai âm nên update không thành công", 0, result);
    }

    // ✅ Case: trangthai = 0 (valid)
    @Test
    public void updatePhieuNhap_TrangThaiZero() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(1001);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(150000);
        phieu.setTrangthai(0);

        int result = dao.update(phieu);
        assertEquals("trangthai = 0 vẫn hợp lệ", 1, result);
    }
    // ✅ Case: Chèn với dữ liệu hợp lệ
    @Test
    public void insertPhieuNhap_ValidData() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(500000);

        int result = dao.insert(phieu);
        assertEquals("Chèn thành công nên phải trả về 1", 1, result);
    }

    // ❌ Case: maphieunhap trùng
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_DuplicateId() throws SQLException {
        PhieuNhapDTO phieu1 = new PhieuNhapDTO();
        phieu1.setMaphieu(maphieu);
        phieu1.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu1.setManhacungcap(1001);
        phieu1.setManguoitao(1);
        phieu1.setTongTien(500000);
        dao.insert(phieu1); // Chèn lần 1

        PhieuNhapDTO phieu2 = new PhieuNhapDTO();
        phieu2.setMaphieu(maphieu++); // Trùng maphieu
        phieu2.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu2.setManhacungcap(1001);
        phieu2.setManguoitao(1);
        phieu2.setTongTien(500000);

        dao.insert(phieu2); // Phải ném SQLException (vi phạm khóa chính)
    }

    // ❌ Case: maphieunhap âm
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(-1);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (maphieu âm)
    }

    // ❌ Case: thoigiantao null
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NullTimestamp() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(null);
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (thoigian NOT NULL)
    }

    // ❌ Case: manhacungcap không tồn tại
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NonExistentSupplierId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(9999); // Không tồn tại
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (vi phạm khóa ngoại)
    }

    // ❌ Case: manhacungcap âm
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeSupplierId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(-5);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (manhacungcap âm)
    }

    // ❌ Case: nguoitao không tồn tại
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NonExistentCreatorId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(999); // Không tồn tại
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (vi phạm khóa ngoại)
    }

    // ❌ Case: nguoitao âm
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeCreatorId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(-1);
        phieu.setTongTien(100000);

        dao.insert(phieu); // Phải ném SQLException (nguoitao âm)
    }

    // ❌ Case: tongtien âm
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeTotalAmount() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(-100000);

        dao.insert(phieu); // Phải ném SQLException (tongtien âm)
    }

    // ✅ Case: tongtien = 0 (hợp lệ nếu không có ràng buộc)
    @Test
    public void insertPhieuNhap_ZeroTotalAmount() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(0);

        int result = dao.insert(phieu);
        assertEquals("tongtien = 0 vẫn hợp lệ nếu không có ràng buộc", 1, result);
    }

    // ❌ Case: DTO null
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NullDTO() throws SQLException {
        dao.insert(null); // Phải ném SQLException
    }
    // Helper method to insert a test record
    private void insertTestRecord() throws SQLException {
        String sql = "INSERT INTO phieunhap (maphieunhap, thoigiantao, manhacungcap, tongtien, trangthai) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = testCon.prepareStatement(sql);
        pst.setString(1, String.valueOf(maphieu));
        pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        pst.setInt(3, 1001);
        pst.setDouble(4, 500000);
        pst.setInt(5, 1);
        pst.executeUpdate();
        pst.close();
    }


    // Test cases for delete
    @Test
    public void deletePhieuNhap_ValidId() throws SQLException {
        int result = dao.delete(String.valueOf(maphieu));
        assertEquals("Xóa thành công (trangthai = 0) nên trả về 1", 1, result);

        // Verify the record was "deleted" (trangthai = 0)
        String sql = "SELECT trangthai FROM phieunhap WHERE maphieunhap = ?";
        PreparedStatement pst = testCon.prepareStatement(sql);
        pst.setString(1, String.valueOf(maphieu));
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            assertEquals("trangthai phải là 0 sau khi xóa", 0, rs.getInt("trangthai"));
        }
        rs.close();
        pst.close();
    }

    @Test
    public void deletePhieuNhap_NonExistentId() throws SQLException {
        int result = dao.delete("9999");
        assertEquals("maphieunhap không tồn tại nên trả về 0", 0, result);
    }

    @Test
    public void deletePhieuNhap_NullId() throws SQLException {
        int result = dao.delete(null);
        assertEquals("maphieunhap null nên trả về 0 hoặc ném lỗi", 0, result);
    }

    @Test
    public void deletePhieuNhap_EmptyId() throws SQLException {
        int result = dao.delete("");
        assertEquals("maphieunhap rỗng nên trả về 0", 0, result);
    }

    @Test
    public void deletePhieuNhap_InvalidFormat() throws SQLException {
        int result = dao.delete("abc");
        assertEquals("maphieunhap không phải số nên trả về 0 hoặc ném lỗi", 0, result);
    }
    @Test
    public void selectAll_ReturnsListWithInsertedData() throws SQLException {
        // Tạo phiếu nhập test
        PhieuNhapDTO dto = new PhieuNhapDTO();
        dto.setMaphieu(3001);
        dto.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        dto.setManhacungcap(1);
        dto.setManguoitao(1);
        dto.setTongTien(99999);
        dto.setTrangthai(1);
        dao.insert(dto);

        ArrayList<PhieuNhapDTO> list = dao.selectAll();

        // Danh sách không được null và phải có ít nhất 1 phần tử vừa thêm
        assertNotNull("Danh sách không được null", list);
        assertTrue("Phải có ít nhất 1 phiếu nhập", list.size() > 0);

        // Kiểm tra có chứa phần tử vừa thêm vào không
        boolean containsInserted = list.stream().anyMatch(p ->
                p.getMaphieu() == 3001 &&
                        p.getManhacungcap() == 1 &&
                        p.getManguoitao() == 1 &&
                        p.getTongTien() == 99999 &&
                        p.getTrangthai() == 1
        );
        assertTrue("Danh sách phải chứa phiếu nhập vừa thêm", containsInserted);
    }

    @Test
    public void selectAll_ReturnsEmptyListWhenNoRecords() throws SQLException {
        // Xoá sạch dữ liệu test nếu cần (phụ thuộc vào setup môi trường test)
        Statement stmt = testCon.createStatement();
        stmt.execute("DELETE FROM phieunhap");

        ArrayList<PhieuNhapDTO> list = dao.selectAll();

        assertNotNull("Danh sách không được null", list);
        assertEquals("Không có bản ghi nên danh sách phải rỗng", 0, list.size());
    }

    @Test
    public void selectAll_ListIsSortedDescendingByMaphieu() throws SQLException {
        // Chèn 3 bản ghi có maphieu khác nhau
        PhieuNhapDTO p1 = new PhieuNhapDTO(1, 1001, 1, new Timestamp(System.currentTimeMillis()), 50000, 1);
        PhieuNhapDTO p2 = new PhieuNhapDTO(1, 1002, 1, new Timestamp(System.currentTimeMillis()), 60000, 1);
        PhieuNhapDTO p3 = new PhieuNhapDTO(1, 1003, 1, new Timestamp(System.currentTimeMillis()), 70000, 1);
        dao.insert(p1);
        dao.insert(p2);
        dao.insert(p3);

        ArrayList<PhieuNhapDTO> list = dao.selectAll();

        // Lọc 3 maphieu vừa test ra khỏi danh sách
        List<Integer> maphieus = list.stream()
                .map(PhieuNhapDTO::getMaphieu)
                .filter(id -> id >= 1001 && id <= 1003)
                .collect(Collectors.toList());

        // Kiểm tra xem có sắp xếp giảm dần không
        assertEquals(Arrays.asList(1003, 1002, 1001), maphieus);
    }
    @Test
    public void selectById_ExistingId() throws SQLException {
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 5001, 1, new Timestamp(System.currentTimeMillis()), 123456L, 1);
        dao.insert(dto);

        PhieuNhapDTO result = dao.selectById("5001");

        assertNotNull("Phải trả về DTO nếu ID tồn tại", result);
        assertEquals(5001, result.getMaphieu());
        assertEquals(1, result.getManhacungcap());
        assertEquals(1, result.getManguoitao());
        assertEquals(123456L, result.getTongTien());
        assertEquals(1, result.getTrangthai());
    }
    @Test
    public void selectById_NonExistentId() {
        PhieuNhapDTO result = dao.selectById("999999");
        assertNull("Phải trả về null nếu ID không tồn tại", result);
    }
    @Test
    public void selectById_InvalidStringId() {
        PhieuNhapDTO result = dao.selectById("abc"); // sẽ không có bản ghi nào với ID "abc"
        assertNull("Phải trả về null nếu ID là chuỗi không hợp lệ", result);
    }
    @Test
    public void selectById_EmptyString() {
        PhieuNhapDTO result = dao.selectById("");
        assertNull("ID rỗng → không tìm thấy → trả về null", result);
    }
    @Test
    public void selectById_NullInput() {
        PhieuNhapDTO result = dao.selectById(null);
        assertNull("ID null → không tìm thấy → trả về null", result);
    }
    @Test
    public void selectById_SpecialCharacters() {
        PhieuNhapDTO result = dao.selectById("!@#$%^");
        assertNull("Ký tự đặc biệt không match ID → trả về null", result);
    }
    @Test
    public void selectById_NegativeId() {
        PhieuNhapDTO result = dao.selectById("-1");
        assertNull("ID âm không tồn tại → trả về null", result);
    }
    private void insertPhieuNhapTestData(int maphieu, long tongTien) throws SQLException {
        PhieuNhapDTO dto = new PhieuNhapDTO();
        dto.setMaphieu(maphieu);
        dto.setManhacungcap(1);
        dto.setManguoitao(1);
        dto.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        dto.setTongTien(tongTien);
        dto.setTrangthai(1);
    }
    // ✅ Dữ liệu hợp lệ, có kết quả
    @Test
    public void statistical_WithMatchingRecords() throws SQLException {
        insertPhieuNhapTestData(1001, 500000);
        insertPhieuNhapTestData(1002, 800000);

        ArrayList<PhieuNhapDTO> results = dao.statistical(400000, 900000);
        assertEquals("Phải có 2 bản ghi trong khoảng [400000, 900000]", 2, results.size());
    }

    // ❌ Không có bản ghi nào trong khoảng
    @Test
    public void statistical_NoRecordsInRange() throws SQLException {
        insertPhieuNhapTestData(1003, 1000000);

        ArrayList<PhieuNhapDTO> results = dao.statistical(1, 1000);
        assertEquals("Không có bản ghi nào trong khoảng [1, 1000]", 0, results.size());
    }

    // ❌ min > max
    @Test
    public void statistical_MinGreaterThanMax() throws SQLException {
        insertPhieuNhapTestData(1004, 300000);

        ArrayList<PhieuNhapDTO> results = dao.statistical(1000000, 100000);
        assertEquals("min > max → không có kết quả", 0, results.size());
    }

    // ✅ min = max và có đúng một bản ghi khớp
    @Test
    public void statistical_MinEqualsMax_OneMatch() throws SQLException {
        insertPhieuNhapTestData(1005, 777777);

        ArrayList<PhieuNhapDTO> results = dao.statistical(777777, 777777);
        assertEquals("Phải tìm được đúng 1 bản ghi khi min = max và khớp", 1, results.size());
    }

    // ❌ min = max nhưng không có bản ghi
    @Test
    public void statistical_MinEqualsMax_NoMatch() throws SQLException {
        ArrayList<PhieuNhapDTO> results = dao.statistical(555555, 555555);
        assertEquals("Không có bản ghi nào có tổng tiền = 555555", 0, results.size());
    }

    // ✅ Trường hợp khoảng rộng [0, Long.MAX_VALUE]
    @Test
    public void statistical_ZeroToMax() throws SQLException {
        insertPhieuNhapTestData(1006, 10000000);

        ArrayList<PhieuNhapDTO> results = dao.statistical(0, Long.MAX_VALUE);
        assertTrue("Khoảng lớn → phải có ít nhất 1 bản ghi", results.size() >= 1);
    }

    // ❌ min và max âm → không hợp lệ về ngữ nghĩa
    @Test
    public void statistical_NegativeMinMax() throws SQLException {
        ArrayList<PhieuNhapDTO> results = dao.statistical(-100000, -1);
        assertEquals("Tổng tiền không thể âm nên không có bản ghi", 0, results.size());
    }

    // ✅ Test toàn khoảng Long
    @Test
    public void statistical_MinToMaxRange() throws SQLException {
        insertPhieuNhapTestData(1007, 999999999);

        ArrayList<PhieuNhapDTO> results = dao.statistical(Long.MIN_VALUE, Long.MAX_VALUE);
        assertTrue("Toàn bộ range của Long → phải chứa bản ghi", results.size() >= 1);
    }
    private void insertChiTietSanPham(String imei, int maphienban, int maphieunhap, int maphieuxuat, int tinhtrang) throws SQLException {
        String sql = "INSERT INTO ctsanpham (maimei, maphienbansp, maphieunhap, maphieuxuat, tinhtrang) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = testCon.prepareStatement(sql);
        pst.setString(1, imei);
        pst.setInt(2, maphienban);
        pst.setInt(3, maphieunhap);
        pst.setInt(4, maphieuxuat);
        pst.setInt(5, tinhtrang);
        pst.executeUpdate();
        pst.close();
    }

    // ✅ Test Case 1: Không có sản phẩm nào thuộc phiếu nhập
    @Test
    public void checkCancelPn_NoProductFound() {
        int maphieu = 9999; // Giả sử không tồn tại
        boolean result = dao.checkCancelPn(maphieu);
        assertTrue("Không có sản phẩm nào → có thể hủy", result);
    }

    // ✅ Test Case 2: Tất cả sản phẩm chưa xuất
    @Test
    public void checkCancelPn_AllUnexported() throws SQLException {
        int maphieu = 1001;
        insertChiTietSanPham("IMEI0001", 1, maphieu, 0, 1);
        insertChiTietSanPham("IMEI0002", 1, maphieu, 0, 1);

        boolean result = dao.checkCancelPn(maphieu);
        assertTrue("Tất cả sản phẩm chưa xuất → có thể hủy", result);
    }

    // ❌ Test Case 3: Một sản phẩm đã xuất
    @Test
    public void checkCancelPn_OneExported() throws SQLException {
        int maphieu = 1002;
        insertChiTietSanPham("IMEI0003", 1, maphieu, 0, 1);
        insertChiTietSanPham("IMEI0004", 1, maphieu, 1234, 1); // Đã xuất

        boolean result = dao.checkCancelPn(maphieu);
        assertFalse("Có 1 sản phẩm đã xuất → không thể hủy", result);
    }

    // ❌ Test Case 4: Tất cả sản phẩm đã xuất
    @Test
    public void checkCancelPn_AllExported() throws SQLException {
        int maphieu = 1003;
        insertChiTietSanPham("IMEI0005", 1, maphieu, 1111, 1);
        insertChiTietSanPham("IMEI0006", 1, maphieu, 2222, 1);

        boolean result = dao.checkCancelPn(maphieu);
        assertFalse("Tất cả sản phẩm đã xuất → không thể hủy", result);
    }

    // Helper method to check soluongton in phienbansanpham
    private int getSoLuongTon(int maphienban) throws SQLException {
        String sql = "SELECT soluongton FROM phienbansanpham WHERE maphienban = ?";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphienban);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("soluongton");
            }
            return 0;
        }
    }

    // Helper method to insert a phienbansanpham record
    private void insertPhienBanSanPham(int maphienban, int soluongton) throws SQLException {
        String sql = "INSERT INTO phienbansanpham (maphienban, masanpham, soluongton) VALUES (?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphienban);
            pst.setInt(2, 1); // Dummy masanpham
            pst.setInt(3, soluongton);
            pst.executeUpdate();
        }
    }
    private void insertDummyPhieuNhap(int maphieu) throws SQLException {
        String sql = "INSERT INTO phieunhap (maphieunhap, thoigian, manhacungcap, nguoitao, tongtien, trangthai) " +
                "VALUES (?, CURRENT_TIMESTAMP, 1, 1, 1000000, 1)";
        PreparedStatement pst = testCon.prepareStatement(sql);
        pst.setInt(1, maphieu);
        pst.executeUpdate();
        pst.close();
    }

    // ✅ TC4: Cancel with multiple chitietphieunhap and verify stock update
    @Test
    public void testCancelPhieuNhap_MultipleChiTiet_StockUpdate() throws SQLException {
        int maphieu = 5003;
        int maphienban1 = 1;
        int maphienban2 = 2;

        // Insert phienbansanpham with initial stock
        insertPhienBanSanPham(maphienban1, 100);
        insertPhienBanSanPham(maphienban2, 200);

        // Insert phieunhap
        insertDummyPhieuNhap(maphieu);

        // Insert two chitietphieunhap
        String sql = "INSERT INTO chitietphieunhap (maphieunhap, maphienbansp, soluong, dongia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            // First chitiet: 10 units
            pst.setInt(1, maphieu);
            pst.setInt(2, maphienban1);
            pst.setInt(3, 10);
            pst.setDouble(4, 500000);
            pst.executeUpdate();

            // Second chitiet: 20 units
            pst.setInt(1, maphieu);
            pst.setInt(2, maphienban2);
            pst.setInt(3, 20);
            pst.setDouble(4, 600000);
            pst.executeUpdate();
        }

        // Verify initial stock
        assertEquals(100, getSoLuongTon(maphienban1));
        assertEquals(200, getSoLuongTon(maphienban2));

        // Cancel phieunhap
        int result = dao.cancelPhieuNhap(maphieu);
        assertEquals("Cancel thành công nên trả về 1", 1, result);

        // Verify stock reduction
        assertEquals("Soluongton should decrease by 10", 90, getSoLuongTon(maphienban1));
        assertEquals("Soluongton should decrease by 20", 180, getSoLuongTon(maphienban2));

        // Verify phieunhap is deleted
        String checkSql = "SELECT COUNT(*) FROM phieunhap WHERE maphieunhap = ?";
        try (PreparedStatement pst = testCon.prepareStatement(checkSql)) {
            pst.setInt(1, maphieu);
            ResultSet rs = pst.executeQuery();
            rs.next();
            assertEquals("Phieunhap should be deleted", 0, rs.getInt(1));
        }
    }

    // ❌ TC5: Negative maphieu
    @Test
    public void testCancelPhieuNhap_NegativeId() throws SQLException {
        int result = dao.cancelPhieuNhap(-1);
        assertEquals("maphieu âm nên trả về 0", 0, result);
    }

    // ❌ TC6: Zero maphieu
    @Test
    public void testCancelPhieuNhap_ZeroId() throws SQLException {
        int result = dao.cancelPhieuNhap(0);
        assertEquals("maphieu = 0 nên trả về 0", 0, result);
    }
    // Helper method to get AUTO_INCREMENT directly for verification
    private int getAutoIncrementDirectly() throws SQLException {
        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'phieunhap'";
        try (PreparedStatement pst = testCon.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("AUTO_INCREMENT");
            }
            return -1;
        }
    }
    // ✅ TC1: Valid case - phieunhap table exists with AUTO_INCREMENT
    @Test
    public void testGetAutoIncrement_ValidTable() throws SQLException {
        // Ensure table is empty and AUTO_INCREMENT is at least 1
        testCon.createStatement().executeUpdate("TRUNCATE TABLE phieunhap");

        int result = dao.getAutoIncrement();
        int expected = getAutoIncrementDirectly();

        assertTrue("AUTO_INCREMENT should be positive", result > 0);
        assertEquals("Should return correct AUTO_INCREMENT value", expected, result);
    }



}
