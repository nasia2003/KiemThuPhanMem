
package DAO;

import DTO.NhaCungCapDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class NhaCungCapDAOTest {
    private Connection testCon;

    @Before
    public void setUp() throws SQLException {
        // Khởi tạo kết nối và vô hiệu hóa auto-commit trước mỗi test
        testCon = ConnectionCustom.getInstance().getConnect();
        testCon.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        // Rollback tất cả thay đổi và đóng kết nối sau mỗi test
        try {
            testCon.rollback();
        } finally {
            testCon.setAutoCommit(true);
//            testCon.close();
        }
    }
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        NhaCungCapDAO dao = NhaCungCapDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("NhaCungCapDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của NhaCungCapDAO
        Assert.assertTrue("Object should be an instance of NhaCungCapDAO", dao instanceof NhaCungCapDAO);
    }

    // Test cases cho manhacungcap
    @Test
    public void insertNhaCungCap_ValidManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với mã hợp lệ (manhacungcap = 1001) → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty TNHH ABC");
        ncc.setDiachi("123 Đường A, Quận B");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0909123456");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_ManhacungcapZero() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với mã bằng 0 → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(0);
        ncc.setTenncc("Công ty Zero");
        ncc.setDiachi("789 Đường E, Quận F");
        ncc.setEmail("zero@example.com");
        ncc.setSdt("0923456789");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_ManhacungcapNegative() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với mã âm → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(-1);
        ncc.setTenncc("Công ty Âm");
        ncc.setDiachi("101 Đường G, Quận H");
        ncc.setEmail("negative@example.com");
        ncc.setSdt("0934567890");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_DuplicateManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với mã trùng (manhacungcap = 1001) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty TNHH XYZ");
        ncc.setDiachi("456 Đường C, Quận D");
        ncc.setEmail("xyz@example.com");
        ncc.setSdt("0912345678");

        int result = dao.insert(ncc);
        assertEquals("First insert should return 1", 1, result);
        testCon.commit();

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty Trùng");
        ncc.setDiachi("202 Đường I, Quận K");
        ncc.setEmail("duplicate@example.com");
        ncc.setSdt("0945678901");

        dao.insert(ncc);
    }

    // Test cases cho tennhacungcap
    @Test
    public void insertNhaCungCap_ValidTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên hợp lệ (tennhacungcap = "Công ty A") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1002);
        ncc.setTenncc("Công ty A");
        ncc.setDiachi("123 Đường X, Quận Y");
        ncc.setEmail("companyA@example.com");
        ncc.setSdt("0951234567");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NullTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1003);
        ncc.setTenncc(null);
        ncc.setDiachi("456 Đường Z, Quận W");
        ncc.setEmail("nulltest@example.com");
        ncc.setSdt("0962345678");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_EmptyTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1004);
        ncc.setTenncc("");
        ncc.setDiachi("789 Đường P, Quận Q");
        ncc.setEmail("emptytest@example.com");
        ncc.setSdt("0973456789");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_OverlongTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên quá dài (>100 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        String longName = "A".repeat(101);
        ncc.setMancc(1005);
        ncc.setTenncc(longName);
        ncc.setDiachi("101 Đường R, Quận S");
        ncc.setEmail("longtest@example.com");
        ncc.setSdt("0984567890");

        dao.insert(ncc);
    }

    @Test
    public void insertNhaCungCap_SpecialCharTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên chứa ký tự đặc biệt (tennhacungcap = "Công ty @#$%") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1006);
        ncc.setTenncc("Công ty @#$%");
        ncc.setDiachi("202 Đường T, Quận U");
        ncc.setEmail("specialtest@example.com");
        ncc.setSdt("0995678901");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_WhitespaceTenncc() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với tên chỉ chứa khoảng trắng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1007);
        ncc.setTenncc(" ");
        ncc.setDiachi("303 Đường V, Quận W");
        ncc.setEmail("whitespacetest@example.com");
        ncc.setSdt("0906789012");

        dao.insert(ncc);
    }

    // Test cases cho diachi
    @Test
    public void insertNhaCungCap_ValidDiachi() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với địa chỉ hợp lệ (diachi = "123 Đường ABC") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1008);
        ncc.setTenncc("Công ty Valid");
        ncc.setDiachi("123 Đường ABC");
        ncc.setEmail("valid@example.com");
        ncc.setSdt("0911234567");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NullDiachi() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với địa chỉ null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1009);
        ncc.setTenncc("Công ty Null");
        ncc.setDiachi(null);
        ncc.setEmail("nulladdr@example.com");
        ncc.setSdt("0922345678");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_EmptyDiachi() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với địa chỉ rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1010);
        ncc.setTenncc("Công ty Empty");
        ncc.setDiachi("");
        ncc.setEmail("emptyaddr@example.com");
        ncc.setSdt("0933456789");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_OverlongDiachi() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với địa chỉ quá dài (>200 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        String longAddress = "A".repeat(201);
        ncc.setMancc(1011);
        ncc.setTenncc("Công ty Long");
        ncc.setDiachi(longAddress);
        ncc.setEmail("longaddr@example.com");
        ncc.setSdt("0944567890");

        dao.insert(ncc);
    }

    @Test
    public void insertNhaCungCap_SpecialCharDiachi() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với địa chỉ chứa ký tự đặc biệt (diachi = "123 @#$%^&*()") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1012);
        ncc.setTenncc("Công ty Special");
        ncc.setDiachi("123 @#$%^&*()");
        ncc.setEmail("specialaddr@example.com");
        ncc.setSdt("0955678901");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    // Test cases cho email
    @Test
    public void insertNhaCungCap_ValidEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email hợp lệ (email = "test@example.com") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1013);
        ncc.setTenncc("Công ty Email");
        ncc.setDiachi("123 Đường Email, Quận E");
        ncc.setEmail("test@example.com");
        ncc.setSdt("0966789012");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NullEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1014);
        ncc.setTenncc("Công ty Null Email");
        ncc.setDiachi("456 Đường Null, Quận N");
        ncc.setEmail(null);
        ncc.setSdt("0977890123");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_EmptyEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1015);
        ncc.setTenncc("Công ty Empty Email");
        ncc.setDiachi("789 Đường Empty, Quận M");
        ncc.setEmail("");
        ncc.setSdt("0988901234");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NoAtEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email không có @ (email = "invalid-email") → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1016);
        ncc.setTenncc("Công ty No At");
        ncc.setDiachi("101 Đường NoAt, Quận O");
        ncc.setEmail("invalid-email");
        ncc.setSdt("0999012345");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NoDomainEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email không có domain (email = "test@") → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1017);
        ncc.setTenncc("Công ty No Domain");
        ncc.setDiachi("202 Đường NoDomain, Quận P");
        ncc.setEmail("test@");
        ncc.setSdt("0900123456");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_OverlongEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email quá dài (>100 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        String longEmail = "a@b." + "c".repeat(96);
        ncc.setMancc(1018);
        ncc.setTenncc("Công ty Long Email");
        ncc.setDiachi("303 Đường Long, Quận Q");
        ncc.setEmail(longEmail);
        ncc.setSdt("0911234567");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_WhitespaceEmail() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với email chứa khoảng trắng (email = "test @example.com") → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1019);
        ncc.setTenncc("Công ty Whitespace Email");
        ncc.setDiachi("404 Đường Whitespace, Quận R");
        ncc.setEmail("test @example.com");
        ncc.setSdt("0922345678");

        dao.insert(ncc);
    }

    // Test cases cho sdt
    @Test
    public void insertNhaCungCap_ValidSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại hợp lệ (sdt = "0901234567") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1020);
        ncc.setTenncc("Công ty Sdt");
        ncc.setDiachi("123 Đường Sdt, Quận S");
        ncc.setEmail("sdt@example.com");
        ncc.setSdt("0901234567");

        int result = dao.insert(ncc);
        assertEquals("Insert should return 1 for successful insertion", 1, result);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_NullSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1021);
        ncc.setTenncc("Công ty Null Sdt");
        ncc.setDiachi("456 Đường Null, Quận T");
        ncc.setEmail("nullsdt@example.com");
        ncc.setSdt(null);

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_EmptySdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1022);
        ncc.setTenncc("Công ty Empty Sdt");
        ncc.setDiachi("789 Đường Empty, Quận U");
        ncc.setEmail("emptysdt@example.com");
        ncc.setSdt("");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_ShortSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại quá ngắn (<10 số) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1023);
        ncc.setTenncc("Công ty Short Sdt");
        ncc.setDiachi("101 Đường Short, Quận V");
        ncc.setEmail("shortsdt@example.com");
        ncc.setSdt("123");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_OverlongSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại quá dài (>20 số) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        String longSdt = "123".repeat(10); // 30 ký tự
        ncc.setMancc(1024);
        ncc.setTenncc("Công ty Long Sdt");
        ncc.setDiachi("202 Đường Long, Quận W");
        ncc.setEmail("longsdt@example.com");
        ncc.setSdt(longSdt);

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_LettersSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại chứa chữ cái → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1025);
        ncc.setTenncc("Công ty Letters Sdt");
        ncc.setDiachi("303 Đường Letters, Quận X");
        ncc.setEmail("letterssdt@example.com");
        ncc.setSdt("090ABC1234");

        dao.insert(ncc);
    }

    @Test(expected = SQLException.class)
    public void insertNhaCungCap_SpecialCharSdt() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với số điện thoại chứa ký tự đặc biệt → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con = testCon;

        ncc.setMancc(1026);
        ncc.setTenncc("Công ty Special Sdt");
        ncc.setDiachi("404 Đường Special, Quận Y");
        ncc.setEmail("specialsdt@example.com");
        ncc.setSdt("090-123-456");

        dao.insert(ncc);
    }
    // Test cases cho manhacungcap
    @Test
    public void updateNhaCungCap_ValidManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với mã hợp lệ (manhacungcap = 1001) → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường ABC, Quận A");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0912345678");

        int result = dao.update(ncc);
        assertEquals("Update should return 1 for successful update", 1, result);
    }

    @Test
    public void updateNhaCungCap_NonExistentManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với mã không tồn tại (manhacungcap = 9999) → kết quả mong đợi: 0
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(9999);
        ncc.setTenncc("Công ty Không Tồn Tại");
        ncc.setDiachi("456 Đường XYZ, Quận Z");
        ncc.setEmail("xyz@example.com");
        ncc.setSdt("0923456789");

        int result = dao.update(ncc);
        assertEquals("Update should return 0 for non-existent manhacungcap", 0, result);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NegativeManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với mã âm (manhacungcap = -1) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(-1);
        ncc.setTenncc("Công ty Âm");
        ncc.setDiachi("789 Đường DEF, Quận D");
        ncc.setEmail("negative@example.com");
        ncc.setSdt("0934567890");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_ZeroManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với mã bằng 0 (manhacungcap = 0) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(0);
        ncc.setTenncc("Công ty Zero");
        ncc.setDiachi("101 Đường GHI, Quận G");
        ncc.setEmail("zero@example.com");
        ncc.setSdt("0945678901");

        dao.update(ncc);
    }

    // Test cases cho tennhacungcap
    @Test
    public void updateNhaCungCap_ValidTenncc() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với tên hợp lệ (tennhacungcap = "Công ty ABC") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường ABC, Quận A");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0912345678");

        int result = dao.update(ncc);
        assertEquals("Update should return 1 for successful update", 1, result);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NullTenncc() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với tên null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc(null);
        ncc.setDiachi("456 Đường XYZ, Quận Z");
        ncc.setEmail("xyz@example.com");
        ncc.setSdt("0923456789");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_EmptyTenncc() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với tên rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("");
        ncc.setDiachi("789 Đường DEF, Quận D");
        ncc.setEmail("def@example.com");
        ncc.setSdt("0934567890");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_WhitespaceTenncc() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với tên chỉ chứa khoảng trắng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc(" ");
        ncc.setDiachi("101 Đường GHI, Quận G");
        ncc.setEmail("ghi@example.com");
        ncc.setSdt("0945678901");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_OverlongTenncc() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với tên quá dài (>100 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        String longName = "A".repeat(101);
        ncc.setMancc(1001);
        ncc.setTenncc(longName);
        ncc.setDiachi("123 Đường JKL, Quận J");
        ncc.setEmail("jkl@example.com");
        ncc.setSdt("0956789012");

        dao.update(ncc);
    }

    // Test cases cho diachi
    @Test
    public void updateNhaCungCap_ValidDiachi() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với địa chỉ hợp lệ (diachi = "123 Đường ABC") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường ABC");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0912345678");

        int result = dao.update(ncc);
        assertEquals("Update should return 1 for successful update", 1, result);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NullDiachi() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với địa chỉ null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty XYZ");
        ncc.setDiachi(null);
        ncc.setEmail("xyz@example.com");
        ncc.setSdt("0923456789");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_EmptyDiachi() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với địa chỉ rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty DEF");
        ncc.setDiachi("");
        ncc.setEmail("def@example.com");
        ncc.setSdt("0934567890");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_OverlongDiachi() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với địa chỉ quá dài (>200 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        String longAddress = "A".repeat(201);
        ncc.setMancc(1001);
        ncc.setTenncc("Công ty GHI");
        ncc.setDiachi(longAddress);
        ncc.setEmail("ghi@example.com");
        ncc.setSdt("0945678901");

        dao.update(ncc);
    }

    // Test cases cho email
    @Test
    public void updateNhaCungCap_ValidEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email hợp lệ (email = "abc@example.com") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường ABC, Quận A");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0912345678");

        int result = dao.update(ncc);
        assertEquals("Update should return 1 for successful update", 1, result);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NullEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty XYZ");
        ncc.setDiachi("456 Đường XYZ, Quận Z");
        ncc.setEmail(null);
        ncc.setSdt("0923456789");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_EmptyEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty DEF");
        ncc.setDiachi("789 Đường DEF, Quận D");
        ncc.setEmail("");
        ncc.setSdt("0934567890");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NoAtEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email không có @ (email = "invalid-email") → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty GHI");
        ncc.setDiachi("101 Đường GHI, Quận G");
        ncc.setEmail("invalid-email");
        ncc.setSdt("0945678901");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NoDomainEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email không có domain (email = "test@") → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty JKL");
        ncc.setDiachi("123 Đường JKL, Quận J");
        ncc.setEmail("test@");
        ncc.setSdt("0956789012");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_OverlongEmail() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với email quá dài (>100 ký tự) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        String longEmail = "a@b." + "c".repeat(96);
        ncc.setMancc(1001);
        ncc.setTenncc("Công ty MNO");
        ncc.setDiachi("456 Đường MNO, Quận M");
        ncc.setEmail(longEmail);
        ncc.setSdt("0967890123");

        dao.update(ncc);
    }

    // Test cases cho sdt
    @Test
    public void updateNhaCungCap_ValidSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại hợp lệ (sdt = "0901234567") → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường ABC, Quận A");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0901234567");

        int result = dao.update(ncc);
        assertEquals("Update should return 1 for successful update", 1, result);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_NullSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại null → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty XYZ");
        ncc.setDiachi("456 Đường XYZ, Quận Z");
        ncc.setEmail("xyz@example.com");
        ncc.setSdt(null);

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_EmptySdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại rỗng → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty DEF");
        ncc.setDiachi("789 Đường DEF, Quận D");
        ncc.setEmail("def@example.com");
        ncc.setSdt("");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_ShortSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại quá ngắn (<10 số) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty GHI");
        ncc.setDiachi("101 Đường GHI, Quận G");
        ncc.setEmail("ghi@example.com");
        ncc.setSdt("123");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_OverlongSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại quá dài (>20 số) → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        String longSdt = "123".repeat(10);
        ncc.setMancc(1001);
        ncc.setTenncc("Công ty JKL");
        ncc.setDiachi("123 Đường JKL, Quận J");
        ncc.setEmail("jkl@example.com");
        ncc.setSdt(longSdt);

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_LettersSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại chứa chữ cái → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty MNO");
        ncc.setDiachi("456 Đường MNO, Quận M");
        ncc.setEmail("mno@example.com");
        ncc.setSdt("090ABC1234");

        dao.update(ncc);
    }

    @Test(expected = SQLException.class)
    public void updateNhaCungCap_SpecialCharSdt() throws SQLException {
        // Mục đích: Kiểm tra cập nhật với số điện thoại chứa ký tự đặc biệt → kết quả mong đợi: SQLException
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ncc.setMancc(1001);
        ncc.setTenncc("Công ty PQR");
        ncc.setDiachi("789 Đường PQR, Quận P");
        ncc.setEmail("pqr@example.com");
        ncc.setSdt("090-123-456");

        dao.update(ncc);
    }
    // Existing test cases for insert and update (omitted for brevity)

    // Test cases for delete method
    @Test
    public void deleteNhaCungCap_ValidManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã hợp lệ (manhacungcap = "1001") → kết quả mong đợi: 1
        // First, insert a record to ensure it exists
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        ncc.setMancc(1001);
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường A, Quận B");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0909123456");

        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        int insertResult = dao.insert(ncc);
        assertEquals("Insert should succeed", 1, insertResult);

        // Test delete
        int result = dao.delete("1001");
        assertEquals("Delete should return 1 for successful deletion", 1, result);
    }

    @Test
    public void deleteNhaCungCap_NonExistentManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã không tồn tại (manhacungcap = "9999") → kết quả mong đợi: 0
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        int result = dao.delete("9999");
        assertEquals("Delete should return 0 for non-existent manhacungcap", 0, result);
    }

    @Test(expected = SQLException.class)
    public void deleteNhaCungCap_EmptyManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã rỗng (manhacungcap = "") → kết quả mong đợi: SQLException
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        dao.delete("");
    }

    @Test(expected = SQLException.class)
    public void deleteNhaCungCap_NullManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã NULL (manhacungcap = null) → kết quả mong đợi: SQLException
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        dao.delete(null);
    }

    @Test(expected = SQLException.class)
    public void deleteNhaCungCap_NegativeManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã âm (manhacungcap = "-1001") → kết quả mong đợi: SQLException
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        dao.delete("-1001");
    }

    @Test(expected = SQLException.class)
    public void deleteNhaCungCap_SpecialCharManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã chứa ký tự đặc biệt (manhacungcap = "@#1001") → kết quả mong đợi: SQLException
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        dao.delete("@#1001");
    }

    @Test(expected = SQLException.class)
    public void deleteNhaCungCap_WhitespaceManhacungcap() throws SQLException {
        // Mục đích: Kiểm tra xóa nhà cung cấp với mã chỉ chứa khoảng trắng (manhacungcap = " ") → kết quả mong đợi: SQLException
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);
        dao.delete(" ");
    }
    // Test cases for selectAll method
    @Test
    public void selectAll_ValidData_ReturnsList() throws SQLException {
        // Mục đích: Kiểm tra selectAll trả về danh sách nhà cung cấp hợp lệ khi có dữ liệu
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        // Insert test data
        NhaCungCapDTO ncc1 = new NhaCungCapDTO(1001, "Công ty A", "123 Đường A", "a@example.com", "0901234567");
        NhaCungCapDTO ncc2 = new NhaCungCapDTO(1002, "Công ty B", "456 Đường B", "b@example.com", "0901234568");
        dao.insert(ncc1);
        dao.insert(ncc2);

        ArrayList<NhaCungCapDTO> result = dao.selectAll();

        assertEquals("Should return 2 suppliers", 2, result.size());
        assertEquals("First supplier ID should be 1001", 1001, result.get(0).getMancc());
        assertEquals("Second supplier name should be Công ty B", "Công ty B", result.get(1).getTenncc());
    }

    @Test
    public void selectAll_NoData_ReturnsEmptyList() throws SQLException {
        // Mục đích: Kiểm tra selectAll trả về danh sách rỗng khi không có dữ liệu
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        ArrayList<NhaCungCapDTO> result = dao.selectAll();

        assertTrue("Should return empty list when no data", result.isEmpty());
    }

    @Test
    public void selectAll_OnlyActiveSuppliers_ReturnsActiveOnly() throws SQLException {
        // Mục đích: Kiểm tra selectAll chỉ trả về nhà cung cấp có trạng thái = 1
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        // Insert test data (one active, one inactive)
        NhaCungCapDTO ncc1 = new NhaCungCapDTO(1001, "Công ty A", "123 Đường A", "a@example.com", "0901234567");
        dao.insert(ncc1);

        // Insert inactive supplier directly to bypass potential DAO logic
        String sql = "INSERT INTO nhacungcap (manhacungcap, tennhacungcap, diachi, email, sdt, trangthai) " +
                "VALUES (1002, 'Công ty B', '456 Đường B', 'b@example.com', '0901234568', 0)";
        testCon.createStatement().executeUpdate(sql);

        ArrayList<NhaCungCapDTO> result = dao.selectAll();

        assertEquals("Should return only 1 active supplier", 1, result.size());
        assertEquals("Supplier ID should be 1001", 1001, result.get(0).getMancc());
    }

    // Test cases for selectById method
    @Test
    public void selectById_ValidId_ReturnsSupplier() throws SQLException {
        // Mục đích: Kiểm tra selectById trả về nhà cung cấp đúng khi ID hợp lệ
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        // Insert test data
        NhaCungCapDTO ncc = new NhaCungCapDTO(1001, "Công ty A", "123 Đường A", "a@example.com", "0901234567");
        dao.insert(ncc);

        NhaCungCapDTO result = dao.selectById("1001");

        assertNotNull("Should return a supplier", result);
        assertEquals("Supplier ID should be 1001", 1001, result.getMancc());
        assertEquals("Supplier name should be Công ty A", "Công ty A", result.getTenncc());
    }

    @Test
    public void selectById_NonExistentId_ReturnsNull() throws SQLException {
        // Mục đích: Kiểm tra selectById trả về null khi ID không tồn tại
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        NhaCungCapDTO result = dao.selectById("9999");

        assertNull("Should return null for non-existent ID", result);
    }

    @Test(expected = SQLException.class)
    public void selectById_NullId_ThrowsSQLException() throws SQLException {
        // Mục đích: Kiểm tra selectById ném SQLException khi ID là null
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        dao.selectById(null);
    }

    @Test(expected = SQLException.class)
    public void selectById_EmptyId_ThrowsSQLException() throws SQLException {
        // Mục đích: Kiểm tra selectById ném SQLException khi ID rỗng
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        dao.selectById("");
    }

    @Test(expected = SQLException.class)
    public void selectById_NonNumericId_ThrowsSQLException() throws SQLException {
        // Mục đích: Kiểm tra selectById ném SQLException khi ID không phải số
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        dao.selectById("abc");
    }

    // Test cases for getAutoIncrement method
    @Test
    public void getAutoIncrement_ValidTable_ReturnsPositiveValue() throws SQLException {
        // Mục đích: Kiểm tra getAutoIncrement trả về giá trị AUTO_INCREMENT hợp lệ
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        int result = dao.getAutoIncrement();

        assertTrue("AUTO_INCREMENT should be positive", result > 0);
    }

    @Test
    public void getAutoIncrement_AfterInsert_ReturnsIncrementedValue() throws SQLException {
        // Mục đích: Kiểm tra getAutoIncrement tăng giá trị sau khi chèn bản ghi
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        int initialValue = dao.getAutoIncrement();

        // Insert a record
        NhaCungCapDTO ncc = new NhaCungCapDTO(1001, "Công ty A", "123 Đường A", "a@example.com", "0901234567");
        dao.insert(ncc);

        int newValue = dao.getAutoIncrement();

        assertTrue("AUTO_INCREMENT should increase after insert", newValue > initialValue);
    }

    @Test
    public void getAutoIncrement_NonExistentTable_ReturnsNegativeOne() throws SQLException {
        // Mục đích: Kiểm tra getAutoIncrement trả về -1 khi bảng không tồn tại
        NhaCungCapDAO dao = new NhaCungCapDAO(testCon);

        // Temporarily rename the table to simulate non-existent table
        testCon.createStatement().executeUpdate("RENAME TABLE nhacungcap TO nhacungcap_temp");

        int result = dao.getAutoIncrement();

        // Restore table name
        testCon.createStatement().executeUpdate("RENAME TABLE nhacungcap_temp TO nhacungcap");

        assertEquals("Should return -1 for non-existent table", -1, result);
    }
}