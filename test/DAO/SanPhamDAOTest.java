package DAO;

import DTO.SanPhamDTO;
import java.sql.*;
import junit.framework.TestCase;


public class SanPhamDAOTest extends TestCase{
    private static Connection connection;
    private SanPhamDAO sanPhamDAO;
    private static final String TEN_TEST = "SP_Test";
    private static final String HINH_ANH_TEST = "test.jpg";
    private static int maSanPhamTest;

    protected void setUp() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        connection.setAutoCommit(false);
        sanPhamDAO = SanPhamDAO.getInstance();

        String sql = "INSERT INTO sanpham (tensp, hinhanh, xuatxu, chipxuly, dungluongpin, kichthuocman, hedieuhanh, phienbanhdh, camerasau, cameratruoc, thoigianbaohanh, thuonghieu, khuvuckho, soluongton, trangthai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TEN_TEST);
        pst.setString(2, HINH_ANH_TEST);
        pst.setInt(3, 1);
        pst.setString(4, "Snapdragon Test");
        pst.setInt(5, 4000);
        pst.setDouble(6, 6.5);
        pst.setInt(7, 1);
        pst.setInt(8, 1);
        pst.setString(9, "12MP");
        pst.setString(10, "8MP");
        pst.setInt(11, 12);
        pst.setInt(12, 1);
        pst.setInt(13, 1);
        pst.setInt(14, 10);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            maSanPhamTest = rs.getInt(1);
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

    public void testInsertTrungID() throws Exception {
        SanPhamDTO dto = new SanPhamDTO(maSanPhamTest, "SP_Trung", "img.jpg", 1, "Chip", 4000, 6.0, 1, 1, "13MP", "10MP", 12, 1, 1, 5);
        int result = sanPhamDAO.insert(dto);
        assertEquals(0, result);
    }

    public void testUpdateThanhCong() throws Exception {
        SanPhamDTO dto = new SanPhamDTO(maSanPhamTest, "SP_Updated", "updated.jpg", 1, "Exynos", 4500, 6.7, 1, 1, "50MP", "20MP", 24, 2, 1, 15);
        int result = sanPhamDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM sanpham WHERE masp = ?");
        pst.setInt(1, maSanPhamTest);
        ResultSet rs = pst.executeQuery();
        assertTrue(rs.next());
        assertEquals("SP_Updated", rs.getString("tensp"));
        assertEquals("Exynos", rs.getString("chipxuly"));
        rs.close();
        pst.close();
    }

    public void testDeleteThanhCong() throws Exception {
        int result = sanPhamDAO.delete(String.valueOf(maSanPhamTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM sanpham WHERE masp = ?");
        pst.setInt(1, maSanPhamTest);
        ResultSet rs = pst.executeQuery();
        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    public void testSelectById() throws Exception {
        SanPhamDTO result = sanPhamDAO.selectById(String.valueOf(maSanPhamTest));
        assertNotNull(result);
        assertEquals(TEN_TEST, result.getTensp());
    }

    public void testUpdateSoLuongTon() throws Exception {
        int result = sanPhamDAO.updateSoLuongTon(maSanPhamTest, 5);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT soluongton FROM sanpham WHERE masp = ?");
        pst.setInt(1, maSanPhamTest);
        ResultSet rs = pst.executeQuery();
        assertTrue(rs.next());
        assertEquals(15, rs.getInt("soluongton"));
        rs.close();
        pst.close();
    }
}
