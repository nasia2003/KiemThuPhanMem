package assertj;

import DAO.ChiTietPhieuNhapDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuNhapDAO;
import DTO.ChiTietPhieuNhapDTO;
import DTO.PhieuNhapDTO;
import config.JDBCUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Lớp test kiểm thử các chức năng của PhieuNhapDAO
 */
public class PhieuNhapDAOTestAssertJ {

    private Connection testCon;
    private PhieuNhapDAO dao;

    // Hằng số dùng cho test
    private static final int TEST_MA_PHIEU = 9999;
    private static final int TEST_MA_NCC = 101;
    private static final int TEST_NGUOI_TAO = 1;
    private static final long TEST_TONG_TIEN = 1000000L;
    private static final Timestamp TEST_THOI_GIAN = new Timestamp(new Date().getTime());

    /**
     * Thiết lập môi trường test trước mỗi test case
     */
    @Before
    public void setUp() throws SQLException {
        testCon = JDBCUtil.getConnection();
        testCon.setAutoCommit(false); // Sử dụng transaction để rollback
        dao = PhieuNhapDAO.getInstance();
        dao.setConnection(testCon);

        // Xóa dữ liệu test trước mỗi test case
        dao.delete(String.valueOf(TEST_MA_PHIEU));
    }

    /**
     * Dọn dẹp môi trường test sau mỗi test case
     */
    @After
    public void tearDown() throws SQLException {
        testCon.rollback();
        testCon.close();
    }

    /**
     * Test thêm phiếu nhập với dữ liệu hợp lệ
     */
    @Test
    public void testInsertValidData() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1 // trạng thái
        );

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Kiểm tra dữ liệu đã được thêm chính xác
        PhieuNhapDTO inserted = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(inserted)
                .isNotNull()
                .extracting(
                        "maphieu", "manhacungcap", "manguoitao", "tongTien", "trangthai"
                )
                .containsExactly(
                        TEST_MA_PHIEU, TEST_MA_NCC, TEST_NGUOI_TAO, TEST_TONG_TIEN, 1
                );
    }

    /**
     * Test thêm phiếu nhập với mã phiếu trùng
     */
    @Test
    public void testInsertDuplicateId() throws SQLException {
        // Thêm phiếu nhập đầu tiên
        PhieuNhapDTO firstInsert = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(firstInsert);

        // Thử thêm phiếu nhập thứ hai với cùng mã phiếu
        PhieuNhapDTO duplicateInsert = new PhieuNhapDTO(
                TEST_MA_NCC + 1,
                TEST_MA_PHIEU, // Mã phiếu trùng
                TEST_NGUOI_TAO,
                new Timestamp(System.currentTimeMillis()),
                TEST_TONG_TIEN + 100000,
                1
        );
        int result = dao.insert(duplicateInsert);

        // Kiểm tra không thêm được bản ghi trùng
        assertThat(result).isEqualTo(0);

        // Kiểm tra chỉ có một bản ghi tồn tại
        ArrayList<PhieuNhapDTO> allRecords = dao.selectAll();
        assertThat(allRecords)
                .filteredOn(p -> p.getMaphieu() == TEST_MA_PHIEU)
                .hasSize(1);
    }

    /**
     * Test thêm phiếu nhập với tổng tiền âm
     */
    @Test
    public void testInsertNegativeTotal() {
        // Chuẩn bị phiếu nhập với tổng tiền âm
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                -1000L, // Tổng tiền âm
                1
        );

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);

        // Kiểm tra không thêm được bản ghi
        assertThat(result).isEqualTo(0);

        // Kiểm tra không có bản ghi nào được thêm
        PhieuNhapDTO shouldBeNull = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(shouldBeNull).isNull();
    }

    /**
     * Test thêm phiếu nhập với thời gian null
     */
    @Test
    public void testInsertNullTimestamp() throws InterruptedException {
        // Chuẩn bị phiếu nhập không có thời gian
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                null, // Thời gian null
                TEST_TONG_TIEN,
                1
        );

        long beforeInsert = System.currentTimeMillis();
        Thread.sleep(10); // Đảm bảo timestamp sẽ khác nhau

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);
        Thread.sleep(10);
        long afterInsert = System.currentTimeMillis();

        // Kiểm tra thêm thành công
        assertThat(result).isEqualTo(1);

        // Kiểm tra thời gian tự động được gán
        PhieuNhapDTO inserted = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(inserted.getThoigiantao())
                .isNotNull()
                .isBetween(new Timestamp(beforeInsert), new Timestamp(afterInsert));
    }

    /**
     * Test thêm phiếu nhập với nhà cung cấp không tồn tại
     */
    @Test
    public void testInsertNonExistentSupplier() {
        // Giả sử 9999 là mã nhà cung cấp không tồn tại
        int nonExistentSupplier = 9999;
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                nonExistentSupplier,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);

        // Kiểm tra không thêm được do ràng buộc khóa ngoại
        assertThat(result).isEqualTo(0);

        // Kiểm tra không có bản ghi nào được thêm
        PhieuNhapDTO shouldBeNull = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(shouldBeNull).isNull();
    }

    /**
     * Test thêm phiếu nhập với tổng tiền bằng 0
     */
    @Test
    public void testInsertZeroTotal() {
        // Chuẩn bị phiếu nhập với tổng tiền = 0
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                0L, // Tổng tiền = 0
                1
        );

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);

        // Kiểm tra thêm thành công
        assertThat(result).isEqualTo(1);

        // Kiểm tra tổng tiền được lưu chính xác
        PhieuNhapDTO inserted = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(inserted.getTongTien()).isEqualTo(0L);
    }

    /**
     * Test thêm phiếu nhập với các giá trị lớn nhất
     */
    @Test
    public void testInsertMaxValues() {
        // Chuẩn bị phiếu nhập với các giá trị cực đại
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                new Timestamp(Long.MAX_VALUE),
                Long.MAX_VALUE,
                1
        );

        // Thực hiện thêm phiếu nhập
        int result = dao.insert(phieuNhap);

        // Kiểm tra thêm thành công
        assertThat(result).isEqualTo(1);

        // Kiểm tra các giá trị được lưu chính xác
        PhieuNhapDTO inserted = dao.selectById(String.valueOf(Integer.MAX_VALUE));
        assertThat(inserted)
                .extracting(
                        "manhacungcap", "maphieu", "manguoitao", "tongTien"
                )
                .containsExactly(
                        Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE
                );
    }
    /**
     * Test cập nhật phiếu nhập với dữ liệu hợp lệ
     */
    @Test
    public void testUpdateValidData() {
        // Chuẩn bị dữ liệu test ban đầu
        PhieuNhapDTO original = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(original);

        // Dữ liệu cập nhật
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                TEST_MA_NCC + 1, // Thay đổi nhà cung cấp
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                new Timestamp(System.currentTimeMillis()), // Thời gian mới
                TEST_TONG_TIEN + 500000, // Tăng tổng tiền
                0 // Thay đổi trạng thái
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Kiểm tra dữ liệu đã được cập nhật chính xác
        PhieuNhapDTO updated = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(updated)
                .extracting(
                        "manhacungcap", "tongTien", "trangthai"
                )
                .containsExactly(
                        TEST_MA_NCC + 1, TEST_TONG_TIEN + 500000, 0
                );
    }

    /**
     * Test cập nhật phiếu nhập không tồn tại
     */
    @Test
    public void testUpdateNonExistentRecord() {
        // Chuẩn bị dữ liệu cập nhật cho phiếu không tồn tại
        int nonExistentId = 999999;
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                TEST_MA_NCC,
                nonExistentId,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);

        // Kiểm tra không có bản ghi nào được cập nhật
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test cập nhật với tổng tiền âm
     */
    @Test
    public void testUpdateWithNegativeTotal() {
        // Chuẩn bị dữ liệu ban đầu
        PhieuNhapDTO original = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(original);

        // Dữ liệu cập nhật với tổng tiền âm
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                -1000L, // Tổng tiền âm
                1
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);

        // Kiểm tra không cập nhật được
        assertThat(result).isEqualTo(0);

        // Kiểm tra dữ liệu gốc không bị thay đổi
        PhieuNhapDTO unchanged = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(unchanged.getTongTien()).isEqualTo(TEST_TONG_TIEN);
    }

    /**
     * Test cập nhật với thời gian null
     */
    @Test
    public void testUpdateWithNullTimestamp() throws InterruptedException {
        // Chuẩn bị dữ liệu ban đầu
        PhieuNhapDTO original = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(original);

        long beforeUpdate = System.currentTimeMillis();
        Thread.sleep(10);

        // Dữ liệu cập nhật với thời gian null
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                null, // Thời gian null
                TEST_TONG_TIEN,
                1
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);
        Thread.sleep(10);
        long afterUpdate = System.currentTimeMillis();

        // Kiểm tra cập nhật thành công
        assertThat(result).isEqualTo(1);

        // Kiểm tra thời gian tự động được cập nhật
        PhieuNhapDTO updated = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(updated.getThoigiantao())
                .isNotNull()
                .isBetween(new Timestamp(beforeUpdate), new Timestamp(afterUpdate));
    }

    /**
     * Test cập nhật với nhà cung cấp không tồn tại
     */
    @Test
    public void testUpdateWithNonExistentSupplier() {
        // Chuẩn bị dữ liệu ban đầu
        PhieuNhapDTO original = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(original);

        // Dữ liệu cập nhật với NCC không tồn tại
        int nonExistentSupplier = 9999;
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                nonExistentSupplier,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);

        // Kiểm tra không cập nhật được
        assertThat(result).isEqualTo(0);

        // Kiểm tra dữ liệu gốc không bị thay đổi
        PhieuNhapDTO unchanged = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(unchanged.getManhacungcap()).isEqualTo(TEST_MA_NCC);
    }

    /**
     * Test chỉ cập nhật trạng thái
     */
    @Test
    public void testUpdateStatusOnly() {
        // Chuẩn bị dữ liệu ban đầu
        PhieuNhapDTO original = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(original);

        // Dữ liệu chỉ cập nhật trạng thái
        PhieuNhapDTO updatedData = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                0 // Chỉ thay đổi trạng thái
        );

        // Thực hiện cập nhật
        int result = dao.update(updatedData);

        // Kiểm tra cập nhật thành công
        assertThat(result).isEqualTo(1);

        // Kiểm tra chỉ trạng thái thay đổi, các trường khác giữ nguyên
        PhieuNhapDTO updated = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(updated)
                .hasFieldOrPropertyWithValue("trangthai", 0)
                .hasFieldOrPropertyWithValue("manhacungcap", TEST_MA_NCC)
                .hasFieldOrPropertyWithValue("tongTien", TEST_TONG_TIEN);
    }
    /**
     * Test xóa (vô hiệu hóa) phiếu nhập hợp lệ
     */
    @Test
    public void testDeleteValidRecord() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1 // trạng thái active
        );
        dao.insert(phieuNhap);

        // Thực hiện xóa
        int result = dao.delete(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Kiểm tra trạng thái đã được cập nhật
        PhieuNhapDTO deleted = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(deleted)
                .isNotNull()
                .hasFieldOrPropertyWithValue("trangthai", 0);
    }

    /**
     * Test xóa phiếu nhập không tồn tại
     */
    @Test
    public void testDeleteNonExistentRecord() {
        // Thực hiện xóa với ID không tồn tại
        int nonExistentId = 999999;
        int result = dao.delete(String.valueOf(nonExistentId));

        // Kiểm tra không có bản ghi nào bị ảnh hưởng
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa phiếu nhập đã bị xóa trước đó
     */
    @Test
    public void testDeleteAlreadyDeletedRecord() {
        // Chuẩn bị phiếu nhập với trạng thái đã xóa
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                0 // trạng thái đã xóa
        );
        dao.insert(phieuNhap);

        // Thực hiện xóa lần nữa
        int result = dao.delete(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra không có bản ghi nào bị ảnh hưởng
        assertThat(result).isEqualTo(0);

        // Kiểm tra trạng thái vẫn giữ nguyên
        PhieuNhapDTO record = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(record.getTrangthai()).isEqualTo(0);
    }

    /**
     * Test xóa với ID null
     */
    @Test
    public void testDeleteWithNullId() {
        int result = dao.delete(null);

        // Kiểm tra không có bản ghi nào bị ảnh hưởng
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa với ID không phải số
     */
    @Test
    public void testDeleteWithNonNumericId() {
        int result = dao.delete("abc123");

        // Kiểm tra không có bản ghi nào bị ảnh hưởng
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa phiếu nhập có ràng buộc khóa ngoại
     */
    @Test
    public void testDeleteRecordWithForeignKeyConstraint() {
        // Giả sử phiếu này có chi tiết phiếu nhập liên quan
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Giả lập có chi tiết phiếu nhập
        // (Trong thực tế cần mock hoặc thêm dữ liệu test)

        // Thực hiện xóa
        int result = dao.delete(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra tùy vào thiết kế:
        // 1. Nếu cho phép xóa mềm dù có ràng buộc:
        assertThat(result).isEqualTo(1);
        // Hoặc 2. Nếu không cho phép:
        // assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa khi kết nối database lỗi
     */
    @Test
    public void testDeleteWithDatabaseError() throws SQLException {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Giả lập lỗi kết nối
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        // Thực hiện xóa
        int result = dao.delete(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra không xóa được
        assertThat(result).isEqualTo(0);

        // Khôi phục kết nối
        dao.setConnection(testCon);

        // Kiểm tra dữ liệu không bị thay đổi
        PhieuNhapDTO record = dao.selectById(String.valueOf(TEST_MA_PHIEU));
        assertThat(record.getTrangthai()).isEqualTo(1);
    }

    /**
     * Test lấy tất cả phiếu nhập với dữ liệu có sẵn
     */
    @Test
    public void testSelectAllWithData() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieu1 = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        PhieuNhapDTO phieu2 = new PhieuNhapDTO(
                TEST_MA_NCC + 1,
                TEST_MA_PHIEU + 1,
                TEST_NGUOI_TAO,
                new Timestamp(System.currentTimeMillis()),
                TEST_TONG_TIEN + 100000,
                0
        );

        dao.insert(phieu1);
        dao.insert(phieu2);

        // Thực hiện lấy danh sách
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra kết quả
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting("maphieu")
                .containsExactly(TEST_MA_PHIEU + 1, TEST_MA_PHIEU); // Kiểm tra sắp xếp DESC

        // Kiểm tra thông tin chi tiết
        assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("manhacungcap", TEST_MA_NCC + 1)
                .hasFieldOrPropertyWithValue("tongTien", TEST_TONG_TIEN + 100000)
                .hasFieldOrPropertyWithValue("trangthai", 0);
    }

    /**
     * Test lấy tất cả phiếu nhập khi có lỗi kết nối database
     */
    @Test
    public void testSelectAllWithDatabaseError() throws SQLException {
        // Giả lập lỗi kết nối
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        // Thực hiện lấy danh sách
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra kết quả
        assertThat(result)
                .isNotNull()
                .isEmpty();

        // Khôi phục kết nối
        dao.setConnection(testCon);
    }

    /**
     * Test lấy tất cả phiếu nhập với dữ liệu lớn
     */
    @Test
    public void testSelectAllWithLargeData() {
        // Chuẩn bị 100 bản ghi test
        for (int i = 0; i < 100; i++) {
            PhieuNhapDTO phieu = new PhieuNhapDTO(
                    TEST_MA_NCC + i,
                    TEST_MA_PHIEU + i,
                    TEST_NGUOI_TAO,
                    new Timestamp(System.currentTimeMillis()),
                    TEST_TONG_TIEN + (i * 1000),
                    i % 2
            );
            dao.insert(phieu);
        }

        // Thực hiện lấy danh sách
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra kết quả
        assertThat(result)
                .isNotNull()
                .hasSize(100)
                .isSortedAccordingTo((a, b) -> b.getMaphieu() - a.getMaphieu()); // Kiểm tra sắp xếp DESC

        // Kiểm tra bản ghi đầu tiên (mới nhất)
        assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("maphieu", TEST_MA_PHIEU + 99);
    }

    /**
     * Test lấy tất cả phiếu nhập với các trạng thái khác nhau
     */
    @Test
    public void testSelectAllWithDifferentStatuses() {
        // Chuẩn bị dữ liệu test với các trạng thái khác nhau
        PhieuNhapDTO active = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1 // Active
        );

        PhieuNhapDTO inactive = new PhieuNhapDTO(
                TEST_MA_NCC + 1,
                TEST_MA_PHIEU + 1,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                0 // Inactive
        );

        dao.insert(active);
        dao.insert(inactive);

        // Thực hiện lấy danh sách
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra kết quả
        assertThat(result)
                .hasSize(2)
                .extracting("trangthai")
                .containsExactly(0, 1); // Kiểm tra cả 2 trạng thái đều được trả về
    }

    /**
     * Test kiểm tra thứ tự sắp xếp theo maphieunhap DESC
     */
    @Test
    public void testSelectAllOrdering() {
        // Chuẩn bị dữ liệu test với các ID không theo thứ tự
        PhieuNhapDTO phieu1 = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU + 2,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        PhieuNhapDTO phieu2 = new PhieuNhapDTO(
                TEST_MA_NCC + 1,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                0
        );

        PhieuNhapDTO phieu3 = new PhieuNhapDTO(
                TEST_MA_NCC + 2,
                TEST_MA_PHIEU + 1,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );

        dao.insert(phieu1);
        dao.insert(phieu2);
        dao.insert(phieu3);

        // Thực hiện lấy danh sách
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra thứ tự sắp xếp
        assertThat(result)
                .extracting("maphieu")
                .containsExactly(TEST_MA_PHIEU + 2, TEST_MA_PHIEU + 1, TEST_MA_PHIEU);
    }
    /**
     * Test tìm kiếm phiếu nhập bằng ID tồn tại
     */
    @Test
    public void testSelectByIdWithExistingId() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO expected = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(expected);

        // Thực hiện tìm kiếm
        PhieuNhapDTO actual = dao.selectById(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra kết quả
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    /**
     * Test tìm kiếm phiếu nhập bằng ID không tồn tại
     */
    @Test
    public void testSelectByIdWithNonExistentId() {
        // Thực hiện tìm kiếm với ID không tồn tại
        PhieuNhapDTO result = dao.selectById("999999");

        // Kiểm tra kết quả
        assertThat(result).isNull();
    }

    /**
     * Test tìm kiếm với ID null
     */
    @Test
    public void testSelectByIdWithNullId() {
        // Thực hiện tìm kiếm với ID null
        PhieuNhapDTO result = dao.selectById(null);

        // Kiểm tra kết quả
        assertThat(result).isNull();
    }

    /**
     * Test tìm kiếm với ID không phải số
     */
    @Test
    public void testSelectByIdWithNonNumericId() {
        // Thực hiện tìm kiếm với ID không phải số
        PhieuNhapDTO result = dao.selectById("ABC123");

        // Kiểm tra kết quả
        assertThat(result).isNull();
    }

    /**
     * Test tìm kiếm phiếu nhập đã bị xóa (trạng thái = 0)
     */
    @Test
    public void testSelectByIdWithDeletedRecord() {
        // Chuẩn bị phiếu nhập đã xóa
        PhieuNhapDTO deleted = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                0 // Trạng thái đã xóa
        );
        dao.insert(deleted);

        // Thực hiện tìm kiếm
        PhieuNhapDTO result = dao.selectById(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra vẫn tìm thấy dù đã xóa (nếu thiết kế cho phép)
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("trangthai", 0);
    }

    /**
     * Test tìm kiếm khi có lỗi kết nối database
     */
    @Test
    public void testSelectByIdWithDatabaseError() throws SQLException {
        // Giả lập lỗi kết nối
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        // Thực hiện tìm kiếm
        PhieuNhapDTO result = dao.selectById(String.valueOf(TEST_MA_PHIEU));

        // Kiểm tra kết quả
        assertThat(result).isNull();

        // Khôi phục kết nối
        dao.setConnection(testCon);
    }

    /**
     * Test tìm kiếm với các giá trị biên của ID
     */
    @Test
    public void testSelectByIdWithBoundaryValues() {
        // Test với ID nhỏ nhất
        PhieuNhapDTO minId = new PhieuNhapDTO(
                TEST_MA_NCC,
                1, // ID nhỏ nhất
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(minId);

        assertThat(dao.selectById("1"))
                .isNotNull()
                .hasFieldOrPropertyWithValue("maphieu", 1);

        // Test với ID lớn nhất
        PhieuNhapDTO maxId = new PhieuNhapDTO(
                TEST_MA_NCC,
                Integer.MAX_VALUE, // ID lớn nhất
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(maxId);

        assertThat(dao.selectById(String.valueOf(Integer.MAX_VALUE)))
                .isNotNull()
                .hasFieldOrPropertyWithValue("maphieu", Integer.MAX_VALUE);
    }

    /**
     * Test thống kê với khoảng giá trị không có dữ liệu
     */
    @Test
    public void testStatisticalWithNoDataInRange() {
        // Chuẩn bị dữ liệu test ngoài khoảng
        PhieuNhapDTO phieu = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                2000000L, // Tổng tiền ngoài khoảng
                1
        );
        dao.insert(phieu);

        // Thực hiện thống kê từ 500,000 đến 1,500,000
        ArrayList<PhieuNhapDTO> result = dao.statistical(500000L, 1500000L);

        // Kiểm tra kết quả
        assertThat(result).isEmpty();
    }

    /**
     * Test thống kê với min = max
     */
    @Test
    public void testStatisticalWithMinEqualsMax() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieu = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                1000000L, // Tổng tiền đúng bằng giá trị tìm kiếm
                1
        );
        dao.insert(phieu);

        // Thực hiện thống kê với min = max = 1,000,000
        ArrayList<PhieuNhapDTO> result = dao.statistical(1000000L, 1000000L);

        // Kiểm tra kết quả
        assertThat(result)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("maphieu", TEST_MA_PHIEU);
    }

    /**
     * Test thống kê với min > max
     */
    @Test
    public void testStatisticalWithMinGreaterThanMax() {
        // Thực hiện thống kê với min > max
        ArrayList<PhieuNhapDTO> result = dao.statistical(1500000L, 500000L);

        // Kiểm tra kết quả (tùy thiết kế có thể trả về list rỗng hoặc ném exception)
        assertThat(result).isEmpty();
    }

    /**
     * Test thống kê với giá trị âm
     */
    @Test
    public void testStatisticalWithNegativeValues() {
        // Thực hiện thống kê với giá trị âm
        ArrayList<PhieuNhapDTO> result = dao.statistical(-100000L, -50000L);

        // Kiểm tra kết quả
        assertThat(result).isEmpty();
    }

    /**
     * Test thống kê với giá trị biên
     */
    @Test
    public void testStatisticalWithBoundaryValues() {
        // Chuẩn bị dữ liệu test với giá trị biên
        PhieuNhapDTO minPhieu = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                Long.MIN_VALUE,
                1
        );

        PhieuNhapDTO maxPhieu = new PhieuNhapDTO(
                TEST_MA_NCC + 1,
                TEST_MA_PHIEU + 1,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                Long.MAX_VALUE,
                1
        );

        dao.insert(minPhieu);
        dao.insert(maxPhieu);

        // Thực hiện thống kê với giá trị lớn nhất
        ArrayList<PhieuNhapDTO> result = dao.statistical(Long.MAX_VALUE - 1000, Long.MAX_VALUE);

        // Kiểm tra kết quả
        assertThat(result)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("maphieu", TEST_MA_PHIEU + 1);
    }

    /**
     * Test thống kê khi có lỗi kết nối database
     */
    @Test
    public void testStatisticalWithDatabaseError() throws SQLException {
        // Giả lập lỗi kết nối
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        // Thực hiện thống kê
        ArrayList<PhieuNhapDTO> result = dao.statistical(0L, 1000000L);

        // Kiểm tra kết quả
        assertThat(result).isEmpty();

        // Khôi phục kết nối
        dao.setConnection(testCon);
    }
    /**
     * Test kiểm tra hủy phiếu nhập khi không có chi tiết sản phẩm
     */
    @Test
    public void testCheckCancelPnWithNoProducts() {
        // Tạo phiếu nhập test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Thực hiện kiểm tra
        boolean result = dao.checkCancelPn(TEST_MA_PHIEU);

        // Kiểm tra kết quả
        assertThat(result).isTrue();
    }

    /**
     * Test kiểm tra hủy phiếu nhập khi tất cả sản phẩm chưa xuất
     */
    @Test
    public void testCheckCancelPnWithAllProductsNotExported() {
        // Tạo phiếu nhập test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Thêm chi tiết sản phẩm (chưa xuất)
        insertTestProduct(TEST_MA_PHIEU, 0); // maphieuxuat = 0

        // Thực hiện kiểm tra
        boolean result = dao.checkCancelPn(TEST_MA_PHIEU);

        // Kiểm tra kết quả
        assertThat(result).isTrue();
    }

    /**
     * Test kiểm tra hủy phiếu nhập khi có ít nhất 1 sản phẩm đã xuất
     */
    @Test
    public void testCheckCancelPnWithOneProductExported() {
        // Tạo phiếu nhập test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Thêm chi tiết sản phẩm (1 đã xuất, 1 chưa xuất)
        insertTestProduct(TEST_MA_PHIEU, 0);     // Chưa xuất
        insertTestProduct(TEST_MA_PHIEU, 123);   // Đã xuất (maphieuxuat = 123)

        // Thực hiện kiểm tra
        boolean result = dao.checkCancelPn(TEST_MA_PHIEU);

        // Kiểm tra kết quả
        assertThat(result).isFalse();
    }

    /**
     * Test kiểm tra hủy phiếu nhập không tồn tại
     */
    @Test
    public void testCheckCancelPnWithNonExistentReceipt() {
        // Thực hiện kiểm tra với phiếu không tồn tại
        boolean result = dao.checkCancelPn(999999);

        // Kiểm tra kết quả
        assertThat(result).isTrue();
    }

    /**
     * Test kiểm tra hủy khi có lỗi kết nối database
     */
    @Test
    public void testCheckCancelPnWithDatabaseError() throws SQLException {
        // Giả lập lỗi kết nối
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        // Thực hiện kiểm tra
        boolean result = dao.checkCancelPn(TEST_MA_PHIEU);

        // Kiểm tra kết quả (tùy thiết kế có thể trả về true/false)
        assertThat(result).isFalse();

        // Khôi phục kết nối
        dao.setConnection(testCon);
    }

    /**
     * Test kiểm tra hủy với giá trị biên của mã phiếu
     */
    @Test
    public void testCheckCancelPnWithBoundaryValues() {
        // Test với mã phiếu nhỏ nhất
        boolean resultMin = dao.checkCancelPn(1);
        assertThat(resultMin).isTrue();

        // Test với mã phiếu lớn nhất
        boolean resultMax = dao.checkCancelPn(Integer.MAX_VALUE);
        assertThat(resultMax).isTrue();
    }

    // Helper method để thêm dữ liệu test vào bảng ctsanpham
    private void insertTestProduct(int maphieunhap, int maphieuxuat) {
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO ctsanpham (maimei, maphienbansp, maphieunhap, maphieuxuat, tinhtrang) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "IMEI_" + System.currentTimeMillis());
            pst.setInt(2, 1); // mã cấu hình
            pst.setInt(3, maphieunhap);
            pst.setInt(4, maphieuxuat);
            pst.setInt(5, 1); // tình trạng
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test hủy phiếu nhập thành công khi không có sản phẩm đã xuất
     */
    @Test
    public void testCancelPhieuNhapSuccess() {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Thêm chi tiết phiếu nhập
        ChiTietPhieuNhapDTO ctpn = new ChiTietPhieuNhapDTO(
                1, // phương thức nhập
                TEST_MA_PHIEU,
                101, // mã phiên bản sp
                5, // số lượng
                100000 // đơn giá
        );
        ChiTietPhieuNhapDAO.getInstance().insert(new ArrayList<>(Arrays.asList(ctpn)));

        // Mock các DAO liên quan
        ChiTietSanPhamDAO ctspDao = mock(ChiTietSanPhamDAO.class);
        // Thực hiện hủy phiếu nhập
        int result = dao.cancelPhieuNhap(TEST_MA_PHIEU);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Kiểm tra phiếu đã bị xóa
        assertThat(dao.selectById(String.valueOf(TEST_MA_PHIEU))).isNull();

        // Verify các DAO được gọi
        verify(ctspDao).deletePn(TEST_MA_PHIEU);
    }

    /**
     * Test hủy phiếu nhập không tồn tại
     */
    @Test
    public void testCancelNonExistentPhieuNhap() {
        int result = dao.cancelPhieuNhap(999999);
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test hủy phiếu nhập khi có lỗi database
     */
    @Test
    public void testCancelPhieuNhapWithDatabaseError() throws SQLException {
        // Chuẩn bị dữ liệu test
        PhieuNhapDTO phieuNhap = new PhieuNhapDTO(
                TEST_MA_NCC,
                TEST_MA_PHIEU,
                TEST_NGUOI_TAO,
                TEST_THOI_GIAN,
                TEST_TONG_TIEN,
                1
        );
        dao.insert(phieuNhap);

        // Giả lập lỗi database
        Connection mockCon = mock(Connection.class);
        when(mockCon.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));
        dao.setConnection(mockCon);

        int result = dao.cancelPhieuNhap(TEST_MA_PHIEU);
        assertThat(result).isEqualTo(0);

        // Khôi phục kết nối
        dao.setConnection(testCon);
    }

    /**
     * Test lấy giá trị auto increment khi có lỗi
     */
    @Test
    public void testGetAutoIncrementWithError() {
        try (MockedStatic<JDBCUtil> mocked = mockStatic(JDBCUtil.class)) {
            when(JDBCUtil.getConnection()).thenThrow(new SQLException("Connection error"));

            int result = dao.getAutoIncrement();
            assertThat(result).isEqualTo(-1);
        }
    }

}
