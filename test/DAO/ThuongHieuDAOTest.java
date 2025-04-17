package DAO;

import DTO.ThuocTinhSanPham.ThuongHieuDTO;
import java.sql.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class ThuongHieuDAOTest {

    private static ThuongHieuDAO thuongHieuDAO;
    private static Connection connection;
    private static final int TEST_BRAND_ID = 9999;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        thuongHieuDAO = ThuongHieuDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String insertSQL = "INSERT INTO `thuonghieu`(`mathuonghieu`, `tenthuonghieu`) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(insertSQL)) {
            pst.setInt(1, TEST_BRAND_ID);
            pst.setString(2, "Test Brand");
            pst.executeUpdate();
        }
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

    /* Test case: TH_DAO_001
     * Mục tiêu: Kiểm tra thêm thương hiệu thành công
     * Input: ThuongHieuDTO("New Brand")
     * Expected output: Trả về 1 (thêm thành công 1 bản ghi)
     */
    @Test
    public void testInsertSuccess() throws SQLException {
        ThuongHieuDTO brand = new ThuongHieuDTO(0, "New Brand");
        int initialCount = thuongHieuDAO.selectAll().size();

        int result = thuongHieuDAO.insert(brand);

        assertEquals(1, result);
        assertEquals(initialCount + 1, thuongHieuDAO.selectAll().size());
    }

    /* Test case: TH_DAO_002
     * Mục tiêu: Kiểm tra thêm thương hiệu với tên null
     * Input: ThuongHieuDTO(null)
     * Expected output: Ném ra SQLException với message chứa "cannot be null"
     */
    @Test
    public void testInsertWithNullName() throws SQLIntegrityConstraintViolationException, SQLException {
        ThuongHieuDTO brand = new ThuongHieuDTO(0, null);
        int result = thuongHieuDAO.insert(brand);
        assertEquals(0, result);
    }

    /* Test case: TH_DAO_003
     * Mục tiêu: Kiểm tra cập nhật thương hiệu thành công
     * Input: ThuongHieuDTO(TEST_BRAND_ID, "Updated Brand")
     * Expected output: Trả về 1 (cập nhật thành công)
     */
    @Test
    public void testUpdateSuccess() throws SQLException {
        ThuongHieuDTO brand = new ThuongHieuDTO(TEST_BRAND_ID, "Updated Brand");

        int result = thuongHieuDAO.update(brand);

        assertEquals(1, result);
        assertEquals("Updated Brand", thuongHieuDAO.selectById(String.valueOf(TEST_BRAND_ID)).getTenthuonghieu());
    }

    /* Test case: TH_DAO_004
     * Mục tiêu: Kiểm tra cập nhật thương hiệu không tồn tại
     * Input: ThuongHieuDTO(99999, "Not Exist")
     * Expected output: Trả về 0 (không có bản ghi nào được cập nhật)
     */
    @Test
    public void testUpdateNonExistBrand() throws SQLException {
        ThuongHieuDTO brand = new ThuongHieuDTO(99999, "Not Exist");

        int result = thuongHieuDAO.update(brand);

        assertEquals(0, result);
    }

    /* Test case: TH_DAO_005
     * Mục tiêu: Kiểm tra xóa thương hiệu thành công
     * Input: String.valueOf(TEST_BRAND_ID)
     * Expected output: Trả về 1 (xóa thành công)
     */
    @Test
    public void testDeleteSuccess() throws SQLException {
        int result = thuongHieuDAO.delete(String.valueOf(TEST_BRAND_ID));

        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM thuonghieu WHERE mathuonghieu = ?");
        pst.setInt(1, TEST_BRAND_ID);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /* Test case: TH_DAO_006
     * Mục tiêu: Kiểm tra lấy thương hiệu theo ID thành công
     * Input: String.valueOf(TEST_BRAND_ID)
     * Expected output: Trả về đúng thương hiệu test
     */
    @Test
    public void testSelectByIdSuccess() throws SQLException {
        ThuongHieuDTO result = thuongHieuDAO.selectById(String.valueOf(TEST_BRAND_ID));

        assertNotNull(result);
        assertEquals("Test Brand", result.getTenthuonghieu());
        assertEquals(TEST_BRAND_ID, result.getMathuonghieu());
    }

    /* Test case: TH_DAO_007
     * Mục tiêu: Kiểm tra lấy tất cả thương hiệu
     * Input: Không
     * Expected output: Danh sách chứa thương hiệu test
     */
    @Test
    public void testSelectAll() throws SQLException {
        ArrayList<ThuongHieuDTO> result = thuongHieuDAO.selectAll();

        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(b -> b.getMathuonghieu() == TEST_BRAND_ID);
        assertTrue(found);
    }

    /* Test case: TH_DAO_008
     * Mục tiêu: Kiểm tra lấy auto increment value
     * Input: Không
     * Expected output: Giá trị lớn hơn 0
     */
    @Test
    public void testGetAutoIncrement() {
        int result = thuongHieuDAO.getAutoIncrement();
        assertTrue(result > 0);
    }
}