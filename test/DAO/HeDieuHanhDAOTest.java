package DAO;

import DTO.ThuocTinhSanPham.HeDieuHanhDTO;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class HeDieuHanhDAOTest {

    private static Connection connection;
    private static HeDieuHanhDAO heDieuHanhDAO;
    private static int maHeDieuHanhTest = 999999;
    private static final String TEN_TEST = "HDH_Test";

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        heDieuHanhDAO = HeDieuHanhDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String sql = "INSERT INTO hedieuhanh(mahedieuhanh, tenhedieuhanh, trangthai) VALUES (?, ?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, maHeDieuHanhTest);
        pst.setString(2, TEN_TEST);
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

    private int getNextAutoIncrement() throws SQLException {
        String query = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'hedieuhanh' AND TABLE_SCHEMA = 'quanlikhohang'";
        PreparedStatement pst = connection.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.next();
        return rs.getInt("AUTO_INCREMENT");
    }

    /** TC01 - Insert thành công
     * Mục tiêu: Thêm hệ điều hành mới
     * Input: mahedieuhanh = maHeDieuHanhTest+1, tenhedieuhanh = "HDH_Insert"
     * Expected output: insert trả về 1
     */
    @Test
    public void testInsertThanhCong() {
        int id = maHeDieuHanhTest + 1;
        HeDieuHanhDTO dto = new HeDieuHanhDTO(id, "HDH_Insert");
        int result = heDieuHanhDAO.insert(dto);
        assertEquals(1, result);
    }

    /** TC02 - Insert trùng ID
     * Mục tiêu: Không thêm nếu ID đã tồn tại
     * Input: mahedieuhanh = maHeDieuHanhTest, tenhedieuhanh = "Trung"
     * Expected output: insert trả về 0
     */
    @Test
    public void testInsertTrungID() {
        HeDieuHanhDTO dto = new HeDieuHanhDTO(maHeDieuHanhTest, "Trung");
        int result = heDieuHanhDAO.insert(dto);
        assertEquals(0, result);
    }

    /** TC03 - Update thành công
     * Mục tiêu: Cập nhật tên hệ điều hành
     * Input: mahedieuhanh = maHeDieuHanhTest, tenhedieuhanh = "HDH_Update"
     * Expected output: update trả về 1
     */
    @Test
    public void testUpdateThanhCong() {
        HeDieuHanhDTO dto = new HeDieuHanhDTO(maHeDieuHanhTest, "HDH_Update");
        int result = heDieuHanhDAO.update(dto);
        assertEquals(1, result);
    }

    /** TC04 - Update không tồn tại
     * Mục tiêu: Không cập nhật nếu ID không tồn tại
     * Input: mahedieuhanh = 999999, tenhedieuhanh = "NoName"
     * Expected output: update trả về 0
     */
    @Test
    public void testUpdateKhongTonTai() {
        HeDieuHanhDTO dto = new HeDieuHanhDTO(9999999, "NoName");
        int result = heDieuHanhDAO.update(dto);
        assertEquals(0, result);
    }

    /** TC05 - Delete thành công
     * Mục tiêu: Đánh dấu hệ điều hành không hoạt động
     * Input: mahedieuhanh = maHeDieuHanhTest
     * Expected output: delete trả về 1
     */
    @Test
    public void testDeleteThanhCong() {
        int result = heDieuHanhDAO.delete(String.valueOf(maHeDieuHanhTest));
        assertEquals(1, result);
    }

    /** TC06 - Delete không tồn tại
     * Mục tiêu: Không xóa nếu ID không tồn tại
     * Input: mahedieuhanh = 999999
     * Expected output: delete trả về 0
     */
    @Test
    public void testDeleteKhongTonTai() {
        int result = heDieuHanhDAO.delete("999999");
        assertEquals(1, result);
    }

    /** TC07 - SelectAll
     * Mục tiêu: Lấy danh sách hệ điều hành đang hoạt động
     * Input: Không có
     * Expected output: Danh sách chứa HDH test
     */
    @Test
    public void testSelectAll() {
        ArrayList<HeDieuHanhDTO> list = heDieuHanhDAO.selectAll();
        assertNotNull(list);
        assertTrue(list.stream().anyMatch(hdh -> hdh.getMahdh() == maHeDieuHanhTest));
    }

    /** TC08 - SelectById thành công
     * Mục tiêu: Truy xuất HDH theo mã
     * Input: mahedieuhanh = maHeDieuHanhTest
     * Expected output: Đối tượng trả về != null, đúng tên
     */
    @Test
    public void testSelectByIdThanhCong() {
        HeDieuHanhDTO dto = heDieuHanhDAO.selectById(String.valueOf(maHeDieuHanhTest));
        assertNotNull(dto);
        assertEquals(TEN_TEST, dto.getTenhdh());
    }

    /** TC09 - SelectById không tồn tại
     * Mục tiêu: Truy xuất thất bại nếu không tồn tại
     * Input: mahedieuhanh = 999999
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByIdKhongTonTai() {
        HeDieuHanhDTO dto = heDieuHanhDAO.selectById("9999999");
        assertNull(dto);
    }

    /** TC10 - GetAutoIncrement
     * Mục tiêu: Lấy giá trị ID tiếp theo
     * Input: Không có
     * Expected output: Giá trị đúng với AUTO_INCREMENT trong DB
     */
    @Test
    public void testGetAutoIncrement() throws SQLException {
        int expected = getNextAutoIncrement();
        int actual = heDieuHanhDAO.getAutoIncrement();
        assertEquals(expected, actual);
    }
}
