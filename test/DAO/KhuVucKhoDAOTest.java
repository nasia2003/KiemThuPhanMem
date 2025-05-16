package DAO;

import DTO.KhuVucKhoDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit Test cho lớp KhuVucKhoDAO
 */
public class KhuVucKhoDAOTest {

    private static Connection connection;

    private static KhuVucKhoDAO dao;

    @BeforeClass
    public static void setUpClass() throws Exception {
        dao = KhuVucKhoDAO.getInstance();
        connection = dao.con;
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);  // Bắt đầu transaction
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

    @AfterClass
    public static void globalTearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Mã testcase: KVK_01
     * Mục tiêu: Test insert với dữ liệu hợp lệ
     * Input: dto có makhuvuc hợp lệ, tenkhuvuc không null, ghichu không null
     * Expected output: trả về 1 (chèn thành công)
     */
    @Test
    public void insert_HopLe() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV Test", "ghi chú");
        int result = dao.insert(dto);
        assertEquals(1, result);
    }

    @Test
    public void insert_TenkhuvucNull() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, null, "ghi chú");
        int result = dao.insert(dto);
        assertEquals(0, result);
    }

    @Test
    public void insert_TenkhuvucRong() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "", "ghi chú");
        int result = dao.insert(dto);
        assertEquals(0, result);
    }

    @Test
    public void insert_GhichuNull() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV Ghi chú null", null);
        int result = dao.insert(dto);
        assertEquals(1, result);
    }

    @Test
    public void insert_GhichuRong() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV Ghi chú rỗng", "");
        int result = dao.insert(dto);
        assertEquals(1, result);
    }

    @Test
    public void insert_MaKhuVucAm() {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(-1, "KV âm", "ghi chú");
        int result = dao.insert(dto);
        assertEquals(0, result);
    }

    @Test
    public void insert_MaKhuVucTrung() {
        int id = 99;
        KhuVucKhoDTO dto1 = new KhuVucKhoDTO(id, "KV Trùng 1", "ghi chú 1");
        KhuVucKhoDTO dto2 = new KhuVucKhoDTO(id, "KV Trùng 2", "ghi chú 2");

        dao.insert(dto1);
        int result = dao.insert(dto2);
        assertEquals(0, result);
    }

    @Test
    public void update_HopLe() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV update", "ghi chú ban đầu");
        dao.insert(dto);

        dto.setTenkhuvuc("KV update mới");
        dto.setGhichu("ghi chú mới");
        int result = dao.update(dto);
        assertEquals(1, result);
    }

    @Test
    public void update_MaKhuVucKhongTonTai() {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(999999, "Không tồn tại", "ghi chú");
        int result = dao.update(dto);
        assertEquals(0, result); // không có dòng nào được cập nhật
    }

    @Test
    public void update_TenKhuVucNull() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV test null", "ghi chú");
        dao.insert(dto);

        dto.setTenkhuvuc(null);
        int result = dao.update(dto);
        assertEquals(0, result);
    }

    @Test
    public void update_GhiChuNull() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV ghi chú null", "ghi chú");
        dao.insert(dto);

        dto.setGhichu(null);
        int result = dao.update(dto);
        assertEquals(1, result);
    }

    @Test
    public void update_TenKhuVucRong() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV rỗng", "ghi chú");
        dao.insert(dto);

        dto.setTenkhuvuc("");
        int result = dao.update(dto);
        assertEquals(0, result);
    }

    @Test
    public void update_GhiChuRong() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV ghi chú rỗng", "ghi chú cũ");
        dao.insert(dto);

        dto.setGhichu("");
        int result = dao.update(dto);
        assertEquals(1, result);
    }

    @Test
    public void delete_HopLe() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV delete", "test xóa");
        dao.insert(dto);

        int result = dao.delete(String.valueOf(id));
        assertEquals(1, result);
    }

    @Test
    public void delete_MaKhongTonTai() {
        int result = dao.delete("999999999");
        assertEquals(0, result);
    }

    @Test
    public void delete_NullMa() {
        int result = dao.delete(null);
        assertEquals(0, result);
    }

    @Test
    public void delete_EmptyString() {
        int result = dao.delete("");
        assertEquals(0, result);
    }

    @Test
    public void selectAll_HopLe() {
        // Chuẩn bị dữ liệu
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV selectAll", "ghi chú");
        dao.insert(dto);

        ArrayList<KhuVucKhoDTO> list = dao.selectAll();
        boolean exists = list.stream().anyMatch(k -> k.getMakhuvuc() == id);
        assertTrue(exists);
    }

    @Test
    public void selectById_HopLe() {
        int id = 99;
        KhuVucKhoDTO dto = new KhuVucKhoDTO(id, "KV ID", "ghi chú");
        dao.insert(dto);

        KhuVucKhoDTO found = dao.selectById(String.valueOf(id));
        assertNotNull(found);
        assertEquals("KV ID", found.getTenkhuvuc());
        assertEquals("ghi chú", found.getGhichu());
    }

    @Test
    public void selectById_KhongTonTai() {
        KhuVucKhoDTO result = dao.selectById("999999"); // giả sử không tồn tại
        assertNull(result);
    }

    @Test
    public void selectById_Null() {
        KhuVucKhoDTO result = dao.selectById(null);
        assertNull(result);
    }

    @Test
    public void selectById_EmptyString() {
        KhuVucKhoDTO result = dao.selectById("");
        assertNull(result);
    }

    @Test
    public void getAutoIncrement_HopLe() throws Exception {
        // Lần 1
        int id1 = dao.getAutoIncrement();
        System.out.println("id1: " + id1);
        assertTrue(id1 > 0);

        insertKhuVucKhoBangSQL(id1, "KV test auto 1", "ghi chú 1");

        KhuVucKhoDTO found1 = dao.selectById(String.valueOf(id1));
        assertNotNull(found1);
        assertEquals("KV test auto 1", found1.getTenkhuvuc());

        // Lần 2
        int id2 = dao.getAutoIncrement();
        System.out.println("id2: " + id2    );
        assertTrue(id2 > id1);

        insertKhuVucKhoBangSQL(id2, "KV test auto 2", "ghi chú 2");

        KhuVucKhoDTO found2 = dao.selectById(String.valueOf(id2));
        assertNotNull(found2);
        assertEquals("KV test auto 2", found2.getTenkhuvuc());
    }

    private void insertKhuVucKhoBangSQL(int id, String ten, String ghichu) throws SQLException {
        String sql = "INSERT INTO khuvuckho (makhuvuc, tenkhuvuc, ghichu, trangthai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, ten);
            stmt.setString(3, ghichu);
            stmt.setInt(4, 1); // trạng thái mặc định là 1 (hoạt động)
            int inserted = stmt.executeUpdate();
            assertEquals("Insert SQL phải thành công", 1, inserted);
        }
    }


}
