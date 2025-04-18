package DAO;

import DTO.ThuocTinhSanPham.DungLuongRamDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit Test cho lớp DungLuongRamDAO
 */
public class DungLuongRamDAOTest {

    private static Connection connection;
    private DungLuongRamDAO dungLuongRamDAO;
    private static final int DL_TEST = 1024;
    private static int maDLRamTest;

    @Before
    public void setUp() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        connection.setAutoCommit(false);
        dungLuongRamDAO = DungLuongRamDAO.getInstance();

        String sql = "INSERT INTO dungluongram (kichthuocram, trangthai) VALUES (?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, DL_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            maDLRamTest = rs.getInt(1);
        }
        rs.close();
        pst.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void globalTearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * TC01 - Insert thành công
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        DungLuongRamDTO dto = new DungLuongRamDTO(0, 2048);
        int result = dungLuongRamDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM dungluongram WHERE kichthuocram = ?");
        pst.setInt(1, 2048);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(2048, rs.getInt("kichthuocram"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Insert trùng ID (nếu cứng ID)
     */
    @Test
    public void testInsertTrungID() throws Exception {
        DungLuongRamDTO dto = new DungLuongRamDTO(maDLRamTest, 4096);
        int result = dungLuongRamDAO.insert(dto);
        assertEquals(0, result); // vì ID đã tồn tại
    }

    /**
     * TC03 - Update thành công
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        DungLuongRamDTO dto = new DungLuongRamDTO(maDLRamTest, 8192);
        int result = dungLuongRamDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM dungluongram WHERE madlram = ?");
        pst.setInt(1, maDLRamTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(8192, rs.getInt("kichthuocram"));
        rs.close();
        pst.close();
    }

    /**
     * TC04 - Update không tồn tại
     */
    @Test
    public void testUpdateKhongTonTai() throws Exception {
        DungLuongRamDTO dto = new DungLuongRamDTO(999999, 1024);
        int result = dungLuongRamDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC05 - Delete thành công
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = dungLuongRamDAO.delete(String.valueOf(maDLRamTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM dungluongram WHERE madlram = ?");
        pst.setInt(1, maDLRamTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC06 - Delete không tồn tại
     */
    @Test
    public void testDeleteKhongTonTai() throws Exception {
        int result = dungLuongRamDAO.delete("999999");
        assertEquals(0, result);
    }

    /**
     * TC07 - SelectAll có dữ liệu
     */
    @Test
    public void testSelectAll() throws Exception {
        ArrayList<DungLuongRamDTO> ds = dungLuongRamDAO.selectAll();
        assertNotNull(ds);

        boolean found = false;
        for (DungLuongRamDTO dl : ds) {
            if (dl.getMadlram() == maDLRamTest) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * TC08 - SelectById thành công
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        DungLuongRamDTO dto = dungLuongRamDAO.selectById(String.valueOf(maDLRamTest));
        assertNotNull(dto);
        assertEquals(maDLRamTest, dto.getMadlram());
    }

    /**
     * TC09 - SelectById không tồn tại
     */
    @Test
    public void testSelectByIdKhongTonTai() throws Exception {
        DungLuongRamDTO dto = dungLuongRamDAO.selectById("999999");
        assertNull(dto);
    }

    /**
     * TC10 - GetAutoIncrement chính xác
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = dungLuongRamDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'dungluongram'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }

    /**
     * TC11 - SelectById null (xử lý an toàn)
     */
    @Test
    public void testSelectByIdNull() {
        DungLuongRamDTO dto = dungLuongRamDAO.selectById(null);
        assertNull(dto);
    }
}
