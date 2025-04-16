package DAO;

import DTO.KhuVucKhoDTO;
import java.sql.*;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 * Unit Test cho lớp KhuVucKhoDAO
 * Sử dụng JUnit 3
 */
public class KhuVucKhoDAOTest extends TestCase {

    private static Connection connection;
    private KhuVucKhoDAO khuVucKhoDAO;
    private static final String TEN_TEST = "KVK_Test";
    private static final String GHI_CHU_TEST = "GhiChu_Test";
    private static int maKhuVucTest;

    /**
     * Thiết lập kết nối DB và tắt auto commit để rollback được
     */
    protected void setUp() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        connection.setAutoCommit(false);
        khuVucKhoDAO = KhuVucKhoDAO.getInstance();

        // Chèn dữ liệu test vào DB
        String sql = "INSERT INTO khuvuckho (tenkhuvuc, ghichu, trangthai) VALUES (?, ?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TEN_TEST);
        pst.setString(2, GHI_CHU_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            maKhuVucTest = rs.getInt(1);
        }
        rs.close();
        pst.close();
    }

    /**
     * Rollback sau mỗi test để không ảnh hưởng dữ liệu thật
     */
    protected void tearDown() throws Exception {
        connection.rollback();
    }

    /**
     * Đóng kết nối sau khi chạy xong tất cả test
     */
    public static void globalTearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * TC01 - Test insert thành công
     * Mục tiêu: Insert khu vực mới vào DB
     */
    public void testInsertThanhCong() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(0, "KVK Insert", "Insert Test");
        int result = khuVucKhoDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM khuvuckho WHERE tenkhuvuc = ?");
        pst.setString(1, "KVK Insert");
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("Insert Test", rs.getString("ghichu"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Test insert trùng ID
     * Mục tiêu: Không cho phép insert khi trùng ID
     */
    public void testInsertTrungID() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(maKhuVucTest, "KVK Trùng", "Note");
        int result = khuVucKhoDAO.insert(dto);
        assertEquals(0, result);
    }

    /**
     * TC03 - Test update thành công
     * Mục tiêu: Cập nhật thành công khu vực theo ID
     */
    public void testUpdateThanhCong() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(maKhuVucTest, "KVK Updated", "Note Updated");
        int result = khuVucKhoDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM khuvuckho WHERE makhuvuc = ?");
        pst.setInt(1, maKhuVucTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("KVK Updated", rs.getString("tenkhuvuc"));
        rs.close();
        pst.close();
    }

    /**
     * TC04 - Test update không tồn tại
     * Mục tiêu: Không update nếu ID không có
     */
    public void testUpdateKhongTonTai() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(999999, "XXX", "YYY");
        int result = khuVucKhoDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC05 - Test delete thành công
     * Mục tiêu: Xóa mềm khu vực (chuyển trạng thái về 0)
     */
    public void testDeleteThanhCong() throws Exception {
        int result = khuVucKhoDAO.delete(String.valueOf(maKhuVucTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM khuvuckho WHERE makhuvuc = ?");
        pst.setInt(1, maKhuVucTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC06 - Test delete không tồn tại
     * Mục tiêu: Không được xóa khi ID không tồn tại
     */
    public void testDeleteKhongTonTai() throws Exception {
        int result = khuVucKhoDAO.delete("999999");
        assertEquals(0, result);
    }

    /**
     * TC07 - Test selectAll
     * Mục tiêu: Trả về tất cả khu vực đang hoạt động
     */
    public void testSelectAll() throws Exception {
        ArrayList<KhuVucKhoDTO> ds = khuVucKhoDAO.selectAll();
        assertNotNull(ds);

        boolean found = false;
        for (KhuVucKhoDTO kvk : ds) {
            if (kvk.getMakhuvuc() == maKhuVucTest) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * TC08 - Test selectById thành công
     */
    public void testSelectByIdThanhCong() throws Exception {
        KhuVucKhoDTO dto = khuVucKhoDAO.selectById(String.valueOf(maKhuVucTest));
        assertNotNull(dto);
        assertEquals(maKhuVucTest, dto.getMakhuvuc());
    }

    /**
     * TC09 - Test selectById không tồn tại
     */
    public void testSelectByIdKhongTonTai() throws Exception {
        KhuVucKhoDTO dto = khuVucKhoDAO.selectById("999999");
        assertNull(dto);
    }

    /**
     * TC10 - Test getAutoIncrement
     */
    public void testGetAutoIncrement() throws Exception {
        int value = khuVucKhoDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'khuvuckho'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }
}