package DAO;

import junit.framework.TestCase;
import DTO.ThuocTinhSanPham.MauSacDTO;
import java.sql.*;
import java.util.ArrayList;

public class MauSacDAOTest extends TestCase {

    private static Connection connection;
    private MauSacDAO mauSacDAO;
    private static final String TENMAU_TEST = "Màu test";  // Tên màu test
    private static int mamauTest;

    /**
     * Thiết lập kết nối DB và tắt auto commit để rollback được
     */
    protected void setUp() throws Exception {
        // Lấy kết nối từ ConnectionCustom hoặc các lớp kết nối của bạn
        connection = ConnectionCustom.getInstance().getConnect();
        connection.setAutoCommit(false);  // Tắt auto commit để có thể rollback

        // Khởi tạo MauSacDAO
        mauSacDAO = MauSacDAO.getInstance();

        // Chèn dữ liệu test vào DB
        String sql = "INSERT INTO mausac (tenmau, trangthai) VALUES (?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TENMAU_TEST);
        pst.executeUpdate();

        // Lấy giá trị generated key (mamau) sau khi insert
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            mamauTest = rs.getInt(1);
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
     * Mục tiêu: Thêm mới màu sắc thành công khi dữ liệu hợp lệ
     */
    public void testInsertThanhCong() throws Exception {
        MauSacDTO dto = new MauSacDTO(0, "MauSac Test");
        int result = mauSacDAO.insert(dto);
        assertEquals(1, result);

        // Kiểm tra trong cơ sở dữ liệu
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE mamau = ?");
        pst.setInt(1, 0);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("MauSac Test", rs.getString("tenmau"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Test update thành công
     * Mục tiêu: Cập nhật tên màu thành công theo mã màu
     */
    public void testUpdateThanhCong() throws Exception {
        MauSacDTO dto = new MauSacDTO(mamauTest, "Blue");
        int result = mauSacDAO.update(dto);
        assertEquals(1, result);

        // Kiểm tra trong cơ sở dữ liệu
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE mamau = ?");
        pst.setInt(1, mamauTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("Blue", rs.getString("tenmau"));
        rs.close();
        pst.close();
    }

    /**
     * TC03 - Test delete thành công
     * Mục tiêu: Xóa mềm màu sắc (chuyển trạng thái về 0)
     */
    public void testDeleteThanhCong() throws Exception {
        int result = mauSacDAO.delete(String.valueOf(mamauTest));
        assertEquals(1, result);

        // Kiểm tra trong cơ sở dữ liệu
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE mamau = ?");
        pst.setInt(1, mamauTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC04 - Test selectById thành công
     * Mục tiêu: Trả về đúng bản ghi khi tìm theo mã màu tồn tại
     */
    public void testSelectByIdThanhCong() throws Exception {
        MauSacDTO result = mauSacDAO.selectById("2");
        assertNotNull(result);
        assertEquals(2, result.getMamau());
    }

    /**
     * TC05 - Test selectAll thành công
     * Mục tiêu: Lấy danh sách màu có trạng thái = 1
     */
    public void testSelectAllThanhCong() throws Exception {
        ArrayList<MauSacDTO> list = mauSacDAO.selectAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC05 - Test getAll thành công
     * Mục tiêu: Lấy toàn bộ danh sách màu (kể cả đã bị ẩn)
     */
    public void testGetAllThanhCong() throws Exception {
        ArrayList<MauSacDTO> list = mauSacDAO.getAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC06 - Test insert với đối tượng null
     * Mục tiêu: Không cho phép insert khi đối tượng null
     */
    public void testInsertNull() {
        try {
            mauSacDAO.insert(null);
            fail("Expected SQLException");
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * TC07 - Test update với id không tồn tại
     * Mục tiêu: Không cập nhật khi mã màu không tồn tại
     */
    public void testUpdateIdNotFound() {
        MauSacDTO dto = new MauSacDTO(9999, "Green");
        try {
            int result = mauSacDAO.update(dto);
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception không mong đợi");
        }
    }

    /**
     * TC08 - Test selectById với id không tồn tại
     * Mục tiêu: Không trả về kết quả khi mã màu không tồn tại
     */
    public void testSelectByIdNotFound() {
        MauSacDTO result = mauSacDAO.selectById("9999");
        assertNull(result);
    }

    /**
     * TC10 - Test getAutoIncrement
     * Mục tiêu: Lấy đúng giá trị AUTO_INCREMENT từ bảng mausac
     */
    public void testGetAutoIncrement() throws Exception {
        int value = mauSacDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'mausac'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }
}
