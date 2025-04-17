package DAO;

import DTO.ChiTietSanPhamDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

public class ChiTietSanPhamDAOTest {

    private static Connection connection;
    private static ChiTietSanPhamDAO chiTietSanPhamDAO;
    private static final String IMEI_TEST = "TEST_IMEI_123";
    private static final int MAPHIENBANSP_TEST = 1;
    private static final int MAPHIEUNHAP_TEST = 1;
    private static final int TINHTRANG_TEST = 1;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        chiTietSanPhamDAO = ChiTietSanPhamDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        // Thêm dữ liệu test
        String sql = "INSERT INTO ctsanpham (maimei, maphienbansp, maphieunhap, tinhtrang) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, IMEI_TEST);
        pst.setInt(2, MAPHIENBANSP_TEST);
        pst.setInt(3, MAPHIEUNHAP_TEST);
        pst.setInt(4, TINHTRANG_TEST);
        pst.executeUpdate();
        pst.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * TC01 - Insert thành công
     * Mục tiêu: Thêm mới chi tiết sản phẩm khi dữ liệu hợp lệ
     * Input: ChiTietSanPhamDTO hợp lệ
     * Expected Output: trả về 1, có bản ghi trong DB
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO("IMEI_NEW", 1, 1, 0, 1);
        int result = chiTietSanPhamDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maimei = ?");
        pst.setString(1, "IMEI_NEW");
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("IMEI_NEW", rs.getString("maimei"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Insert null
     * Mục tiêu: Không cho phép insert khi DTO là null
     * Input: dto = null
     * Expected Output: ném ra exception
     */
    @Test(expected = Exception.class)
    public void testInsertNull() throws Exception {
        chiTietSanPhamDAO.insert(null);
    }

    /**
     * TC03 - Insert trùng IMEI
     * Mục tiêu: Không cho phép thêm IMEI trùng
     * Input: imei = IMEI_TEST
     * Expected Output: ném ra exception hoặc result = 0
     */
    @Test
    public void testInsertTrungImei() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO(IMEI_TEST, 1, 1, 0, 1);
        try {
            int result = chiTietSanPhamDAO.insert(dto);
            assertEquals(0, result);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * TC04 - Update thành công
     * Mục tiêu: Cập nhật thông tin chi tiết sản phẩm thành công
     * Input: ChiTietSanPhamDTO hợp lệ
     * Expected Output: trả về 1, DB được cập nhật
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO(IMEI_TEST, 2, 2, 0, 2);
        int result = chiTietSanPhamDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maimei = ?");
        pst.setString(1, IMEI_TEST);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(2, rs.getInt("maphienbansp"));
        assertEquals(2, rs.getInt("tinhtrang"));
        rs.close();
        pst.close();
    }

    /**
     * TC05 - Update với IMEI không tồn tại
     * Mục tiêu: Không cập nhật khi IMEI không tồn tại
     * Input: imei không tồn tại
     * Expected Output: trả về 0
     */
    @Test
    public void testUpdateImeiNotFound() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO("IMEI_NOT_EXIST", 1, 1, 0, 1);
        int result = chiTietSanPhamDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC06 - UpdateXuat thành công
     * Mục tiêu: Cập nhật thông tin xuất thành công
     * Input: ChiTietSanPhamDTO hợp lệ
     * Expected Output: trả về 1, DB được cập nhật
     */
    @Test
    public void testUpdateXuatThanhCong() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO(IMEI_TEST, 0, 0, 1, 2);
        int result = chiTietSanPhamDAO.updateXuat(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maimei = ?");
        pst.setString(1, IMEI_TEST);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt("maphieuxuat"));
        assertEquals(2, rs.getInt("tinhtrang"));
        rs.close();
        pst.close();
    }

    /**
     * TC07 - Reset thành công
     * Mục tiêu: Reset thông tin xuất thành công
     * Input: IMEI hợp lệ
     * Expected Output: trả về 1, DB được cập nhật
     */
    @Test
    public void testResetThanhCong() throws Exception {
        // First update to have data to reset
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO(IMEI_TEST, 0, 0, 1, 2);
        chiTietSanPhamDAO.updateXuat(dto);

        // Test reset
        dto = new ChiTietSanPhamDTO(IMEI_TEST, 0, 0, 0, 0);
        int result = chiTietSanPhamDAO.reset(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maimei = ?");
        pst.setString(1, IMEI_TEST);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertNull(rs.getObject("maphieuxuat"));
        assertEquals(1, rs.getInt("tinhtrang"));
        rs.close();
        pst.close();
    }

    /**
     * TC08 - Delete thành công (update tinhtrang = 0)
     * Mục tiêu: Xóa mềm chi tiết sản phẩm thành công
     * Input: IMEI hợp lệ
     * Expected Output: trả về 1, tinhtrang = 0 trong DB
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = chiTietSanPhamDAO.delete(IMEI_TEST);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maimei = ?");
        pst.setString(1, IMEI_TEST);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("tinhtrang"));
        rs.close();
        pst.close();
    }

    /**
     * TC09 - DeletePn thành công
     * Mục tiêu: Xóa chi tiết sản phẩm theo phiếu nhập thành công
     * Input: maphieunhap hợp lệ
     * Expected Output: trả về số bản ghi bị xóa > 0
     */
    @Test
    public void testDeletePnThanhCong() throws Exception {
        int result = chiTietSanPhamDAO.deletePn(MAPHIEUNHAP_TEST);
        assertTrue(result > 0);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ctsanpham WHERE maphieunhap = ?");
        pst.setInt(1, MAPHIEUNHAP_TEST);
        ResultSet rs = pst.executeQuery();

        assertFalse(rs.next());
        rs.close();
        pst.close();
    }

    /**
     * TC10 - SelectAll thành công
     * Mục tiêu: Lấy tất cả chi tiết sản phẩm
     * Input: none
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllThanhCong() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectAll();
        System.out.println(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC11 - SelectAllbyPb thành công
     * Mục tiêu: Lấy chi tiết sản phẩm theo phiên bản SP và tinhtrang=1
     * Input: maphienbansp hợp lệ
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllbyPbThanhCong() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectAllbyPb(MAPHIENBANSP_TEST);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC12 - SelectbyPb thành công
     * Mục tiêu: Lấy chi tiết sản phẩm theo phiên bản SP
     * Input: maphienbansp hợp lệ
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectbyPbThanhCong() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectbyPb(MAPHIENBANSP_TEST);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC13 - SelectPBvaTT thành công
     * Mục tiêu: Lấy chi tiết sản phẩm theo phiên bản SP và tình trạng
     * Input: maphienbansp và tinhtrang hợp lệ
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectPBvaTTThanhCong() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectPBvaTT(MAPHIENBANSP_TEST, TINHTRANG_TEST);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC14 - GetMaPhienBanSPOfPhieu thành công
     * Mục tiêu: Lấy mã phiên bản SP theo phiếu nhập
     * Input: maphieunhap hợp lệ
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testGetMaPhienBanSPOfPhieuThanhCong() throws Exception {
        ArrayList<Integer> list = chiTietSanPhamDAO.getMaPhienBanSPOfPhieu(MAPHIEUNHAP_TEST);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC15 - SelectAllByMaPhieuNhap thành công
     * Mục tiêu: Lấy chi tiết sản phẩm theo phiếu nhập
     * Input: maphieunhap hợp lệ
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllByMaPhieuNhapThanhCong() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectAllByMaPhieuNhap(MAPHIEUNHAP_TEST);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC16 - SelectAllByMaPhieuXuat thành công
     * Mục tiêu: Lấy chi tiết sản phẩm theo phiếu xuất
     * Input: maphieuxuat hợp lệ (cần setup data trước)
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllByMaPhieuXuatThanhCong() throws Exception {
        // First create a phieuxuat record
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO(IMEI_TEST, 0, 0, 1, 2);
        chiTietSanPhamDAO.updateXuat(dto);

        ArrayList<ChiTietSanPhamDTO> list = chiTietSanPhamDAO.selectAllByMaPhieuXuat(1);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
}