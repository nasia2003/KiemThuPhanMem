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
    // PN_0: Kiểm tra phương thức getInstance trả về đối tượng PhieuNhapDAO không null và là thể hiện của PhieuNhapDAO
    @Test
    public void testGetInstance() {
        PhieuNhapDAO dao = PhieuNhapDAO.getInstance();
        Assert.assertNotNull("PhieuNhapDAO instance should not be null", dao);
        Assert.assertTrue("Object should be an instance of PhieuNhapDAO", dao instanceof PhieuNhapDAO);
    }

    // PN_1: Kiểm tra cập nhật phiếu nhập với dữ liệu hợp lệ, mong đợi trả về 1 (thành công)
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

    // PN_2: Kiểm tra cập nhật phiếu nhập với maphieu không tồn tại, mong đợi trả về 0 (thất bại)
    @Test
    public void updatePhieuNhap_NonExistentId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(9999);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setTongTien(1000000);
        phieu.setTrangthai(1);
        int result = dao.update(phieu);
        assertEquals("Không có phiếu nhập với ID này nên update trả về 0", 0, result);
    }

    // PN_3: Kiểm tra cập nhật phiếu nhập với maphieu âm, mong đợi trả về 0 (thất bại)
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

    // PN_4: Kiểm tra cập nhật phiếu nhập với thoigiantao null, mong đợi ném SQLException
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
            assertTrue(true);
        }
    }

    // PN_5: Kiểm tra cập nhật phiếu nhập với manhacungcap âm, mong đợi trả về 0 (thất bại)
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

    // PN_6: Kiểm tra cập nhật phiếu nhập với tongtien âm, mong đợi trả về 0 (thất bại)
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

    // PN_7: Kiểm tra cập nhật phiếu nhập với trangthai lớn hơn 1, mong đợi trả về 0 (thất bại)
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

    // PN_8: Kiểm tra cập nhật phiếu nhập với trangthai âm, mong đợi trả về 0 (thất bại)
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

    // PN_9: Kiểm tra cập nhật phiếu nhập với trangthai = 0, mong đợi trả về 1 (thành công)
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

    // PN_10: Kiểm tra chèn phiếu nhập với dữ liệu hợp lệ, mong đợi trả về 1 (thành công)
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

    // PN_11: Kiểm tra chèn phiếu nhập với maphieu trùng, mong đợi ném SQLException (vi phạm khóa chính)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_DuplicateId() throws SQLException {
        PhieuNhapDTO phieu1 = new PhieuNhapDTO();
        phieu1.setMaphieu(maphieu);
        phieu1.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu1.setManhacungcap(1001);
        phieu1.setManguoitao(1);
        phieu1.setTongTien(500000);
        dao.insert(phieu1);
        PhieuNhapDTO phieu2 = new PhieuNhapDTO();
        phieu2.setMaphieu(maphieu++);
        phieu2.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu2.setManhacungcap(1001);
        phieu2.setManguoitao(1);
        phieu2.setTongTien(500000);
        dao.insert(phieu2);
    }

    // PN_12: Kiểm tra chèn phiếu nhập với maphieu âm, mong đợi ném SQLException (maphieu không hợp lệ)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(-1);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_13: Kiểm tra chèn phiếu nhập với thoigiantao null, mong đợi ném SQLException (thoigiantao NOT NULL)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NullTimestamp() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(null);
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_14: Kiểm tra chèn phiếu nhập với manhacungcap không tồn tại, mong đợi ném SQLException (vi phạm khóa ngoại)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NonExistentSupplierId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(9999);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_15: Kiểm tra chèn phiếu nhập với manhacungcap âm, mong đợi ném SQLException (manhacungcap không hợp lệ)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeSupplierId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(-5);
        phieu.setManguoitao(1);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_16: Kiểm tra chèn phiếu nhập với nguoitao không tồn tại, mong đợi ném SQLException (vi phạm khóa ngoại)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NonExistentCreatorId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(999);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_17: Kiểm tra chèn phiếu nhập với nguoitao âm, mong đợi ném SQLException (nguoitao không hợp lệ)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeCreatorId() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(-1);
        phieu.setTongTien(100000);
        dao.insert(phieu);
    }

    // PN_18: Kiểm tra chèn phiếu nhập với tongtien âm, mong đợi ném SQLException (tongtien không hợp lệ)
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NegativeTotalAmount() throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu++);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001);
        phieu.setManguoitao(1);
        phieu.setTongTien(-100000);
        dao.insert(phieu);
    }

    // PN_19: Kiểm tra chèn phiếu nhập với tongtien = 0, mong đợi trả về 1 (hợp lệ nếu không có ràng buộc)
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

    // PN_20: Kiểm tra chèn phiếu nhập với DTO null, mong đợi ném SQLException
    @Test(expected = SQLException.class)
    public void insertPhieuNhap_NullDTO() throws SQLException {
        dao.insert(null);
    }

    // PN_21: Kiểm tra xóa phiếu nhập với ID hợp lệ, mong đợi trả về 1 và trạng thái chuyển về 0
    @Test
    public void deletePhieuNhap_ValidId() throws SQLException {
        int result = dao.delete(String.valueOf(maphieu));
        assertEquals("Xóa thành công (trangthai = 0) nên trả về 1", 1, result);
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

    // PN_22: Kiểm tra xóa phiếu nhập với ID không tồn tại, mong đợi trả về 0
    @Test
    public void deletePhieuNhap_NonExistentId() throws SQLException {
        int result = dao.delete("9999");
        assertEquals("maphieunhap không tồn tại nên trả về 0", 0, result);
    }

    // PN_23: Kiểm tra xóa phiếu nhập với ID null, mong đợi trả về 0
    @Test
    public void deletePhieuNhap_NullId() throws SQLException {
        int result = dao.delete(null);
        assertEquals("maphieunhap null nên trả về 0 hoặc ném lỗi", 0, result);
    }

    // PN_24: Kiểm tra xóa phiếu nhập với ID rỗng, mong đợi trả về 0
    @Test
    public void deletePhieuNhap_EmptyId() throws SQLException {
        int result = dao.delete("");
        assertEquals("maphieunhap rỗng nên trả về 0", 0, result);
    }

    // PN_25: Kiểm tra xóa phiếu nhập với ID không hợp lệ (chuỗi không phải số), mong đợi trả về 0
    @Test
    public void deletePhieuNhap_InvalidFormat() throws SQLException {
        int result = dao.delete("abc");
        assertEquals("maphieunhap không phải số nên trả về 0 hoặc ném lỗi", 0, result);
    }

    // PN_26: Kiểm tra selectAll trả về danh sách chứa dữ liệu vừa chèn
    @Test
    public void selectAll_ReturnsListWithInsertedData() throws SQLException {
        PhieuNhapDTO dto = new PhieuNhapDTO();
        dto.setMaphieu(3001);
        dto.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        dto.setManhacungcap(1);
        dto.setManguoitao(1);
        dto.setTongTien(99999);
        dto.setTrangthai(1);
        dao.insert(dto);
        ArrayList<PhieuNhapDTO> list = dao.selectAll();
        assertNotNull("Danh sách không được null", list);
        assertTrue("Phải có ít nhất 1 phiếu nhập", list.size() > 0);
        boolean containsInserted = list.stream().anyMatch(p ->
                p.getMaphieu() == 3001 &&
                        p.getManhacungcap() == 1 &&
                        p.getManguoitao() == 1 &&
                        p.getTongTien() == 99999 &&
                        p.getTrangthai() == 1
        );
        assertTrue("Danh sách phải chứa phiếu nhập vừa thêm", containsInserted);
    }

    // PN_27: Kiểm tra selectAll trả về danh sách rỗng khi không có bản ghi
    @Test
    public void selectAll_ReturnsEmptyListWhenNoRecords() throws SQLException {
        Statement stmt = testCon.createStatement();
        stmt.execute("DELETE FROM phieunhap");
        ArrayList<PhieuNhapDTO> list = dao.selectAll();
        assertNotNull("Danh sách không được null", list);
        assertEquals("Không có bản ghi nên danh sách phải rỗng", 0, list.size());
    }

    // PN_28: Kiểm tra selectAll trả về danh sách được sắp xếp giảm dần theo maphieu
    @Test
    public void selectAll_ListIsSortedDescendingByMaphieu() throws SQLException {
        PhieuNhapDTO p1 = new PhieuNhapDTO(1, 1001, 1, new Timestamp(System.currentTimeMillis()), 50000, 1);
        PhieuNhapDTO p2 = new PhieuNhapDTO(1, 1002, 1, new Timestamp(System.currentTimeMillis()), 60000, 1);
        PhieuNhapDTO p3 = new PhieuNhapDTO(1, 1003, 1, new Timestamp(System.currentTimeMillis()), 70000, 1);
        dao.insert(p1);
        dao.insert(p2);
        dao.insert(p3);
        ArrayList<PhieuNhapDTO> list = dao.selectAll();
        List<Integer> maphieus = list.stream()
                .map(PhieuNhapDTO::getMaphieu)
                .filter(id -> id >= 1001 && id <= 1003)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1003, 1002, 1001), maphieus);
    }

    // PN_29: Kiểm tra selectById với ID tồn tại, mong đợi trả về DTO đúng
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

    // PN_30: Kiểm tra selectById với ID không tồn tại, mong đợi trả về null
    @Test
    public void selectById_NonExistentId() {
        PhieuNhapDTO result = dao.selectById("999999");
        assertNull("Phải trả về null nếu ID không tồn tại", result);
    }

    // PN_31: Kiểm tra selectById với ID là chuỗi không hợp lệ, mong đợi trả về null
    @Test
    public void selectById_InvalidStringId() {
        PhieuNhapDTO result = dao.selectById("abc");
        assertNull("Phải trả về null nếu ID là chuỗi không hợp lệ", result);
    }

    // PN_32: Kiểm tra selectById với ID rỗng, mong đợi trả về null
    @Test
    public void selectById_EmptyString() {
        PhieuNhapDTO result = dao.selectById("");
        assertNull("ID rỗng → không tìm thấy → trả về null", result);
    }

    // PN_33: Kiểm tra selectById với ID null, mong đợi trả về null
    @Test
    public void selectById_NullInput() {
        PhieuNhapDTO result = dao.selectById(null);
        assertNull("ID null → không tìm thấy → trả về null", result);
    }

    // PN_34: Kiểm tra selectById với ID chứa ký tự đặc biệt, mong đợi trả về null
    @Test
    public void selectById_SpecialCharacters() {
        PhieuNhapDTO result = dao.selectById("!@#$%^");
        assertNull("Ký tự đặc biệt không match ID → trả về null", result);
    }

    // PN_35: Kiểm tra selectById với ID âm, mong đợi trả về null
    @Test
    public void selectById_NegativeId() {
        PhieuNhapDTO result = dao.selectById("-1");
        assertNull("ID âm không tồn tại → trả về null", result);
    }

    // PN_36: Kiểm tra statistical với bản ghi trong khoảng tổng tiền hợp lệ, mong đợi trả về 2 bản ghi
    @Test
    public void statistical_WithMatchingRecords() throws SQLException {
        insertPhieuNhapTestData(1001, 500000);
        insertPhieuNhapTestData(1002, 800000);
        ArrayList<PhieuNhapDTO> results = dao.statistical(400000, 900000);
        assertEquals("Phải có 2 bản ghi trong khoảng [400000, 900000]", 2, results.size());
    }

    // PN_37: Kiểm tra statistical với khoảng không có bản ghi, mong đợi trả về danh sách rỗng
    @Test
    public void statistical_NoRecordsInRange() throws SQLException {
        insertPhieuNhapTestData(1003, 1000000);
        ArrayList<PhieuNhapDTO> results = dao.statistical(1, 1000);
        assertEquals("Không có bản ghi nào trong khoảng [1, 1000]", 0, results.size());
    }

    // PN_38: Kiểm tra statistical với min lớn hơn max, mong đợi trả về danh sách rỗng
    @Test
    public void statistical_MinGreaterThanMax() throws SQLException {
        insertPhieuNhapTestData(1004, 300000);
        ArrayList<PhieuNhapDTO> results = dao.statistical(1000000, 100000);
        assertEquals("min > max → không có kết quả", 0, results.size());
    }

    // PN_39: Kiểm tra statistical với min bằng max và có một bản ghi khớp, mong đợi trả về 1 bản ghi
    @Test
    public void statistical_MinEqualsMax_OneMatch() throws SQLException {
        insertPhieuNhapTestData(1005, 777777);
        ArrayList<PhieuNhapDTO> results = dao.statistical(777777, 777777);
        assertEquals("Phải tìm được đúng 1 bản ghi khi min = max và khớp", 1, results.size());
    }

    // PN_40: Kiểm tra statistical với min bằng max nhưng không có bản ghi, mong đợi trả về danh sách rỗng
    @Test
    public void statistical_MinEqualsMax_NoMatch() throws SQLException {
        ArrayList<PhieuNhapDTO> results = dao.statistical(555555, 555555);
        assertEquals("Không có bản ghi nào có tổng tiền = 555555", 0, results.size());
    }

    // PN_41: Kiểm tra statistical với khoảng từ 0 đến Long.MAX_VALUE, mong đợi trả về ít nhất 1 bản ghi
    @Test
    public void statistical_ZeroToMax() throws SQLException {
        insertPhieuNhapTestData(1006, 10000000);
        ArrayList<PhieuNhapDTO> results = dao.statistical(0, Long.MAX_VALUE);
        assertTrue("Khoảng lớn → phải có ít nhất 1 bản ghi", results.size() >= 1);
    }

    // PN_42: Kiểm tra statistical với min và max âm, mong đợi trả về danh sách rỗng
    @Test
    public void statistical_NegativeMinMax() throws SQLException {
        ArrayList<PhieuNhapDTO> results = dao.statistical(-100000, -1);
        assertEquals("Tổng tiền không thể âm nên không có bản ghi", 0, results.size());
    }

    // PN_43: Kiểm tra statistical với khoảng toàn bộ Long, mong đợi trả về ít nhất 1 bản ghi
    @Test
    public void statistical_MinToMaxRange() throws SQLException {
        insertPhieuNhapTestData(1007, 999999999);
        ArrayList<PhieuNhapDTO> results = dao.statistical(Long.MIN_VALUE, Long.MAX_VALUE);
        assertTrue("Toàn bộ range của Long → phải chứa bản ghi", results.size() >= 1);
    }

    // PN_44: Kiểm tra checkCancelPn khi không có sản phẩm thuộc phiếu nhập, mong đợi trả về true (có thể hủy)
    @Test
    public void checkCancelPn_NoProductFound() {
        int maphieu = 9999;
        boolean result = dao.checkCancelPn(maphieu);
        assertTrue("Không có sản phẩm nào → có thể hủy", result);
    }

    // PN_45: Kiểm tra checkCancelPn khi tất cả sản phẩm chưa xuất, mong đợi trả về true (có thể hủy)
    @Test
    public void checkCancelPn_AllUnexported() throws SQLException {
        int maphieu = 1001;
        insertChiTietSanPham("IMEI0001", 1, maphieu, 0, 1);
        insertChiTietSanPham("IMEI0002", 1, maphieu, 0, 1);
        boolean result = dao.checkCancelPn(maphieu);
        assertTrue("Tất cả sản phẩm chưa xuất → có thể hủy", result);
    }

    // PN_46: Kiểm tra checkCancelPn khi có một sản phẩm đã xuất, mong đợi trả về false (không thể hủy)
    @Test
    public void checkCancelPn_OneExported() throws SQLException {
        int maphieu = 1002;
        insertChiTietSanPham("IMEI0003", 1, maphieu, 0, 1);
        insertChiTietSanPham("IMEI0004", 1, maphieu, 1234, 1);
        boolean result = dao.checkCancelPn(maphieu);
        assertFalse("Có 1 sản phẩm đã xuất → không thể hủy", result);
    }

    // PN_47: Kiểm tra checkCancelPn khi tất cả sản phẩm đã xuất, mong đợi trả về false (không thể hủy)
    @Test
    public void checkCancelPn_AllExported() throws SQLException {
        int maphieu = 1003;
        insertChiTietSanPham("IMEI0005", 1, maphieu, 1111, 1);
        insertChiTietSanPham("IMEI0006", 1, maphieu, 2222, 1);
        boolean result = dao.checkCancelPn(maphieu);
        assertFalse("Tất cả sản phẩm đã xuất → không thể hủy", result);
    }

    // PN_48: Kiểm tra cancelPhieuNhap với nhiều chi tiết phiếu nhập, xác minh cập nhật số lượng tồn và xóa phiếu nhập
    @Test
    public void testCancelPhieuNhap_MultipleChiTiet_StockUpdate() throws SQLException {
        int maphieu = 5003;
        int maphienban1 = 1;
        int maphienban2 = 2;
        insertPhienBanSanPham(maphienban1, 100);
        insertPhienBanSanPham(maphienban2, 200);
        insertDummyPhieuNhap(maphieu);
        String sql = "INSERT INTO chitietphieunhap (maphieunhap, maphienbansp, soluong, dongia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphieu);
            pst.setInt(2, maphienban1);
            pst.setInt(3, 10);
            pst.setDouble(4, 500000);
            pst.executeUpdate();
            pst.setInt(1, maphieu);
            pst.setInt(2, maphienban2);
            pst.setInt(3, 20);
            pst.setDouble(4, 600000);
            pst.executeUpdate();
        }
        assertEquals(100, getSoLuongTon(maphienban1));
        assertEquals(200, getSoLuongTon(maphienban2));
        int result = dao.cancelPhieuNhap(maphieu);
        assertEquals("Cancel thành công nên trả về 1", 1, result);
        assertEquals("Soluongton should decrease by 10", 90, getSoLuongTon(maphienban1));
        assertEquals("Soluongton should decrease by 20", 180, getSoLuongTon(maphienban2));
        String checkSql = "SELECT COUNT(*) FROM phieunhap WHERE maphieunhap = ?";
        try (PreparedStatement pst = testCon.prepareStatement(checkSql)) {
            pst.setInt(1, maphieu);
            ResultSet rs = pst.executeQuery();
            rs.next();
            assertEquals("Phieunhap should be deleted", 0, rs.getInt(1));
        }
    }

    // PN_49: Kiểm tra cancelPhieuNhap với maphieu âm, mong đợi trả về 0 (thất bại)
    @Test
    public void testCancelPhieuNhap_NegativeId() throws SQLException {
        int result = dao.cancelPhieuNhap(-1);
        assertEquals("maphieu âm nên trả về 0", 0, result);
    }

    // PN_50: Kiểm tra cancelPhieuNhap với maphieu bằng 0, mong đợi trả về 0 (thất bại)
    @Test
    public void testCancelPhieuNhap_ZeroId() throws SQLException {
        int result = dao.cancelPhieuNhap(0);
        assertEquals("maphieu = 0 nên trả về 0", 0, result);
    }

    // PN_51: Kiểm tra getAutoIncrement với bảng phieunhap hợp lệ, mong đợi trả về giá trị AUTO_INCREMENT đúng
    @Test
    public void testGetAutoIncrement_ValidTable() throws SQLException {
        testCon.createStatement().executeUpdate("TRUNCATE TABLE phieunhap");
        int result = dao.getAutoIncrement();
        int expected = getAutoIncrementDirectly();
        assertTrue("AUTO_INCREMENT should be positive", result > 0);
        assertEquals("Should return correct AUTO_INCREMENT value", expected, result);
    }
    private void insertPhieuNhapTestData(int maphieu, long tongtien) throws SQLException {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        phieu.setMaphieu(maphieu);
        phieu.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        phieu.setManhacungcap(1001); // Giả sử mã nhà cung cấp 1001 tồn tại
        phieu.setManguoitao(1);      // Giả sử người tạo 1 tồn tại
        phieu.setTongTien(tongtien);
        phieu.setTrangthai(1);       // Trạng thái mặc định là 1 (hợp lệ)
        dao.insert(phieu);
    }
    private void insertChiTietSanPham(String maimei, int maphienbansp, int maphieunhap, int maphieuxuat, int tinhtrang) throws SQLException {
        String sql = "INSERT INTO ctsanpham (maimei, maphienbansp, maphieunhap, maphieuxuat, tinhtrang) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setString(1, maimei);
            pst.setInt(2, maphienbansp);
            pst.setInt(3, maphieunhap);
            pst.setInt(4, maphieuxuat);
            pst.setInt(5, tinhtrang);
            pst.executeUpdate();
        }
    }
    private void insertPhienBanSanPham(int maphienbansp, int soluongton) throws SQLException {
        String sql = "INSERT INTO phienbansanpham (maphienbansp, soluongton, masanpham) VALUES (?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphienbansp);
            pst.setInt(2, soluongton);
            pst.setInt(3, 1); // Giả sử mã sản phẩm 1 tồn tại
            pst.executeUpdate();
        }
    }
    private int getSoLuongTon(int maphienbansp) throws SQLException {
        String sql = "SELECT soluongton FROM phienbansanpham WHERE maphienbansp = ?";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphienbansp);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("soluongton");
                }
            }
        }
        throw new SQLException("Không tìm thấy phiên bản sản phẩm với maphienbansp = " + maphienbansp);
    }
    private void insertDummyPhieuNhap(int maphieu) throws SQLException {
        String sql = "INSERT INTO phieunhap (maphieunhap, thoigian, manhacungcap, nguoitao, tongtien, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            pst.setInt(1, maphieu);
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setInt(3, 1001); // Giả sử mã nhà cung cấp 1001 tồn tại
            pst.setInt(4, 1);    // Giả sử người tạo 1 tồn tại
            pst.setLong(5, 0);   // Tổng tiền mặc định là 0
            pst.setInt(6, 1);    // Trạng thái mặc định là 1 (hợp lệ)
            pst.executeUpdate();
        }
    }
    private int getAutoIncrementDirectly() throws SQLException {
        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'phieunhap'";
        try (PreparedStatement pst = testCon.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AUTO_INCREMENT");
                }
            }
        }
        throw new SQLException("Không thể lấy giá trị AUTO_INCREMENT cho bảng phieunhap");
    }
}
