package assertj;

import DAO.ChiTietPhieuNhapDAO;
import DTO.ChiTietPhieuNhapDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ChiTietPhieuNhapDAOTestAsssertJ {

    private ChiTietPhieuNhapDAO dao;
    private Connection mockCon;
    private PreparedStatement mockPst;

    @Before
    public void setUp() throws SQLException {
        dao = new ChiTietPhieuNhapDAO();

        // Mock connection và prepared statement
        mockCon = mock(Connection.class);
        mockPst = mock(PreparedStatement.class);

        dao.setConnection(mockCon);
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenReturn(1);
    }

    /**
     * Test insert thành công với 1 chi tiết phiếu nhập
     */
    @Test
    public void testInsertSingleDetailSuccess() throws SQLException {
        // Chuẩn bị dữ liệu test
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(
                1, // phương thức nhập
                10014, // mã phiếu
                101, // mã phiên bản sp (dạng số)
                5, // số lượng
                100000 // đơn giá
        ));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Verify đã set các tham số đúng cách
        verify(mockPst).setInt(1, 1001);
        verify(mockPst).setInt(2, 101); // Kiểm tra set Int cho maphienbansp
        verify(mockPst).setInt(3, 5);
        verify(mockPst).setInt(4, 100000);
        verify(mockPst).setInt(5, 1);

        // Verify đã gọi executeUpdate
        verify(mockPst).executeUpdate();
    }

    /**
     * Test insert thành công với nhiều chi tiết phiếu nhập
     */
    @Test
    public void testInsertMultipleDetailsSuccess() throws SQLException {
        // Chuẩn bị dữ liệu test
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 102, 3, 150000));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1); // Trả về 1 cho mỗi lần insert thành công

        // Verify đã gọi executeUpdate 2 lần
        verify(mockPst, times(2)).executeUpdate();
    }

    /**
     * Test insert thất bại do SQLException
     */
    @Test
    public void testInsertWithSQLException() throws SQLException {
        // Chuẩn bị dữ liệu test
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Giả lập lỗi SQL
        when(mockPst.executeUpdate()).thenThrow(new SQLException("Insert error"));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test insert với danh sách rỗng
     */
    @Test
    public void testInsertEmptyList() throws SQLException {
        // Thực hiện insert với danh sách rỗng
        int result = dao.insert(new ArrayList<>());

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(0);

        // Verify KHÔNG gọi executeUpdate
        verify(mockPst, never()).executeUpdate();
    }

    /**
     * Test insert với giá trị 0 cho maphienbansp
     */
    @Test
    public void testInsertWithZeroMaphienbansp() throws SQLException {
        // Chuẩn bị dữ liệu test với maphienbansp = 0
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 0, 5, 100000));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả (tùy thiết kế có thể là 0 hoặc 1)
        assertThat(result).isEqualTo(1);

        // Verify đã set 0 cho maphienbansp
        verify(mockPst).setInt(2, 0);
    }

    /**
     * Test insert với giá trị âm cho maphienbansp
     */
    @Test
    public void testInsertWithNegativeMaphienbansp() throws SQLException {
        // Chuẩn bị dữ liệu test với maphienbansp âm
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, -1, 5, 100000));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Verify đã set giá trị âm
        verify(mockPst).setInt(2, -1);
    }

    /**
     * Test insert với giá trị lớn nhất cho maphienbansp
     */
    @Test
    public void testInsertWithMaxMaphienbansp() throws SQLException {
        // Chuẩn bị dữ liệu test với giá trị lớn nhất
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, Integer.MAX_VALUE, 5, 100000));

        // Thực hiện insert
        int result = dao.insert(details);

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Verify đã set giá trị lớn nhất
        verify(mockPst).setInt(2, Integer.MAX_VALUE);
    }
    /**
     * Test xóa thành công chi tiết phiếu nhập
     */
    @Test
    public void testDeleteSuccess() throws SQLException {
        // Mock connection và prepared statement
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockPst = mock(PreparedStatement.class);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenReturn(1); // Giả lập xóa thành công 1 bản ghi

        // Set mock connection cho DAO
        dao.setConnection(mockCon);

        // Thực hiện xóa
        int result = dao.delete("1001");

        // Kiểm tra kết quả
        assertThat(result).isEqualTo(1);

        // Verify đã set tham số đúng cách
        verify(mockPst).setString(1, "1001");

        // Verify đã gọi executeUpdate
        verify(mockPst).executeUpdate();
    }

    /**
     * Test xóa khi không có bản ghi nào khớp
     */
    @Test
    public void testDeleteNoMatchingRecord() throws SQLException {
        // Mock connection và prepared statement
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockPst = mock(PreparedStatement.class);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenReturn(0); // Giả lập không có bản ghi nào bị xóa

        dao.setConnection(mockCon);

        int result = dao.delete("9999");
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa với ID null
     */
    @Test
    public void testDeleteWithNullId() throws SQLException {
        // Mock connection
        Connection mockCon = mock(Connection.class);
        dao.setConnection(mockCon);

        int result = dao.delete(null);
        assertThat(result).isEqualTo(0);

        // Verify KHÔNG gọi executeUpdate khi ID null
        verify(mockCon, never()).prepareStatement(anyString());
    }

    /**
     * Test xóa khi có lỗi SQL
     */
    @Test
    public void testDeleteWithSQLException() throws SQLException {
        // Mock connection và prepared statement
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockPst = mock(PreparedStatement.class);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenThrow(new SQLException("Database error"));

        dao.setConnection(mockCon);

        int result = dao.delete("1001");
        assertThat(result).isEqualTo(0);
    }

    /**
     * Test xóa với ID không phải số
     */
    @Test
    public void testDeleteWithNonNumericId() throws SQLException {
        // Mock connection và prepared statement
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockPst = mock(PreparedStatement.class);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenReturn(0);

        dao.setConnection(mockCon);

        int result = dao.delete("ABC123");
        assertThat(result).isEqualTo(0);

        // Verify vẫn gọi executeUpdate với ID không phải số
        verify(mockPst).executeUpdate();
    }

    /**
     * Test ghi log khi có exception
     */
    @Test
    public void testDeleteLoggingWhenExceptionOccurs() throws SQLException {
        // Mock connection và prepared statement
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockPst = mock(PreparedStatement.class);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Mock logger để kiểm tra ghi log
        Logger mockLogger = mock(Logger.class);
        try (MockedStatic<ChiTietPhieuNhapDAO> mockedDao = mockStatic(ChiTietPhieuNhapDAO.class)) {
            mockedDao.when(() -> ChiTietPhieuNhapDAO.class.getName()).thenReturn("TestLogger");
            when(mockLogger.isLoggable(Level.SEVERE)).thenReturn(true);

            dao.setConnection(mockCon);
            int result = dao.delete("1001");

            // Kiểm tra đã ghi log
            verify(mockLogger).log(Level.SEVERE, null, any(SQLException.class));
        }
    }
    // Existing test cases (omitted for brevity, as provided in the original question)

    /**
     * Test update thành công với danh sách chi tiết phiếu nhập
     */
    @Test
    public void testUpdateSuccess() throws SQLException {
        // Chuẩn bị dữ liệu test
        String pk = "1001";
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Giả lập delete và insert thành công
        when(mockPst.executeUpdate()).thenReturn(1); // delete returns 1, insert returns 1

        // Thực hiện update
        int result = dao.update(details, pk);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 1 for success").isEqualTo(1);

        // Verify delete
        verify(mockPst).setString(1, pk);
        verify(mockPst).executeUpdate();

        // Verify insert
        verify(mockPst).setInt(1, 1001);
        verify(mockPst).setInt(2, 101);
        verify(mockPst).setInt(3, 5);
        verify(mockPst).setInt(4, 100000);
        verify(mockPst).setInt(5, 1);
        verify(mockPst, times(2)).executeUpdate(); // delete + insert
    }

    /**
     * Test update thất bại khi delete không thành công
     */
    @Test
    public void testUpdateWhenDeleteFails() throws SQLException {
        // Chuẩn bị dữ liệu test
        String pk = "1001";
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Giả lập delete thất bại
        when(mockPst.executeUpdate()).thenReturn(0); // delete returns 0

        // Thực hiện update
        int result = dao.update(details, pk);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 0 when delete fails").isEqualTo(0);

        // Verify chỉ gọi delete, không gọi insert
        verify(mockPst).setString(1, pk);
        verify(mockPst, times(1)).executeUpdate();
        verify(mockPst, never()).setInt(eq(2), anyInt()); // Không gọi set cho insert
    }

    /**
     * Test update với danh sách rỗng
     */
    @Test
    public void testUpdateWithEmptyList() throws SQLException {
        // Chuẩn bị dữ liệu test
        String pk = "1001";
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();

        // Giả lập delete thành công
        when(mockPst.executeUpdate()).thenReturn(1); // delete returns 1

        // Thực hiện update
        int result = dao.update(details, pk);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 0 for empty list").isEqualTo(0);

        // Verify gọi delete
        verify(mockPst).setString(1, pk);
        verify(mockPst, times(1)).executeUpdate();
        verify(mockPst, never()).setInt(eq(2), anyInt()); // Không gọi set cho insert
    }

    /**
     * Test update với pk null
     */
    @Test
    public void testUpdateWithNullPk() throws SQLException {
        // Chuẩn bị dữ liệu test
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Thực hiện update
        int result = dao.update(details, null);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 0 for null pk").isEqualTo(0);

        // Verify không gọi prepareStatement hoặc executeUpdate
        verify(mockCon, never()).prepareStatement(anyString());
        verify(mockPst, never()).executeUpdate();
    }

    /**
     * Test update với SQLException khi delete
     */
    @Test
    public void testUpdateWithSQLExceptionOnDelete() throws SQLException {
        // Chuẩn bị dữ liệu test
        String pk = "1001";
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Giả lập SQLException khi delete
        when(mockPst.executeUpdate()).thenThrow(new SQLException("Delete error"));

        // Thực hiện update
        int result = dao.update(details, pk);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 0 on SQLException during delete").isEqualTo(0);

        // Verify chỉ gọi delete
        verify(mockPst).setString(1, pk);
        verify(mockPst, times(1)).executeUpdate();
    }

    /**
     * Test update với SQLException khi insert
     */
    @Test
    public void testUpdateWithSQLExceptionOnInsert() throws SQLException {
        // Chuẩn bị dữ liệu test
        String pk = "1001";
        ArrayList<ChiTietPhieuNhapDTO> details = new ArrayList<>();
        details.add(new ChiTietPhieuNhapDTO(1, 1001, 101, 5, 100000));

        // Giả lập delete thành công, insert thất bại
        when(mockPst.executeUpdate()).thenReturn(1).thenThrow(new SQLException("Insert error"));

        // Thực hiện update
        int result = dao.update(details, pk);

        // Kiểm tra kết quả
        assertThat(result).as("Update should return 0 on SQLException during insert").isEqualTo(0);

        // Verify gọi delete và insert
        verify(mockPst).setString(1, pk);
        verify(mockPst).setInt(1, 1001);
        verify(mockPst).setInt(2, 101);
        verify(mockPst, times(2)).executeUpdate();
    }





}