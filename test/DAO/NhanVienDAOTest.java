package DAO;

import DTO.NhanVienDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;

public class NhanVienDAOTest {
    // insert
    @Test
    public void Insert_HoVaTenNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
            IllegalArgumentException.class,
            ()-> dao.insert(dto)
        );
        Assert.assertEquals("Họ và tên không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_HoVaTenRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Họ và tên không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_GenderNegative() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(-1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Không xác định giới tính", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_Gender3() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(3);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Không xác định giới tính", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_SdtNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Số được thoại không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_SdtRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Số được thoại không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_DoBNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Ngày sinh không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_EmailNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Email không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_EmailRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_EmailKhongDungDinhDang() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiDaXoa() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(-1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Không khởi tạo trạng thái đã xóa", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiNegative2() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(-2);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không hợp lệ", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiZero() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(0);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không hợp lệ", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThai2() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(2);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không hợp lệ", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_BinhThuong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(0);
        dto.setSdt("098080980");
        dto.setEmail("test@gmail.com");

        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // update
    @Test
    public void Update_MaNhanVienNegative() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(-1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(0);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Mã nhân viên không thể âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhanVienZero() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(0);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(0);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Mã nhân viên không thể bằng 0", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhanVienKhongTonTai() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(99999);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(0);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Update_HoVaTenNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Họ và tên không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_HoVaTenRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Họ và tên không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_GenderNegative() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(-1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Không xác định giới tính", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_Gender3() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("098080980");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(3);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Không xác định giới tính", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_SdtNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Số được thoại không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_SdtRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setSdt("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Số được thoại không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_DoBNull() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Ngày sinh không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_EmailNull() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Email không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_EmailRong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_EmailKhongDungDinhDang() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThaiNegative2() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(-2);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Trạng thái không hợp lệ", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThaiDaXoa() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(-2);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Không cập nhật thành trạng thái -1", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThai2() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(2);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(1);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.update(dto)
        );
        Assert.assertEquals("Trạng thái không hợp lệ", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_BinhThuong() throws SQLException, ParseException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        NhanVienDTO dto = new NhanVienDTO();
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setHoten("Nguyen Hong Hai");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DoB = new Date(sdf.parse("2003-05-09").getTime());
        dto.setNgaysinh(DoB);
        dto.setGioitinh(0);
        dto.setSdt("098080980");
        dto.setEmail("1@gmail.com");

        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // delete
    @Test
    public void Delete_MaNhanVienRong() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.delete(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienKhongPhaiInteger() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "Abcd";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.delete(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên phải là số", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienNegative() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "-1";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.delete(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không thể âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienZero() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "0";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.delete(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không thể bằng 0", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienKhongTonTai() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "99999";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienTonTaiChuaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "2";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienTonTaiDaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "49";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    // selectAll
    @Test
    public void SelectAll_Test() {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        ArrayList<NhanVienDTO> result = dao.selectAll();
        Assert.assertFalse(result.isEmpty()); // empty thì fail.
    }

    // selectAllNV
    @Test
    public void SelectAllNV_Test() {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        ArrayList<NhanVienDTO> result = dao.selectAllNV();
        Assert.assertFalse(result.isEmpty()); // empty thì fail.
    }

    // selectById
    @Test
    public void SelectById_MaNhanVienRong() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectById(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienKhongPhaiInteger() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "Abcd";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectById(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không phải là số", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienNegative() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "-1";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectById(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không thể âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienZero() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "0";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectById(MaNhanVien)
        );
        Assert.assertEquals("Mã nhân viên không thể bằng 0", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienKhongTonTai() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "99999";
        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNull(dto);
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienTonTaiChuaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "2";
        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNotNull(dto);
        Assert.assertEquals(MaNhanVien, Integer.toString(dto.getManv()));
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienTonTaiDaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String MaNhanVien = "49";
        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNull(dto);
        dao.con.rollback();
    }

    // selectByEmail
    @Test
    public void SelectByEmail_EmailRong() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String email = "";

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectByEmail(email)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailKhongDungDinhDang() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String email = "absc@";

        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectByEmail(email)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailKhongTonTai() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String email = "abhcn@gmail.com";

        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectByEmail(email);
        Assert.assertNull(dto); // null thì pass
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailTonTaiNhanVienDaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String email = "1@gmail.com";

        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectByEmail(email);
        Assert.assertNull(dto); // null thì pass
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailTonTaiNhanVienChuaXoa() throws SQLException {
        NhanVienDAO dao = NhanVienDAO.getInstance();
        String email = "musicanime2501@gmail.com";

        dao.con.setAutoCommit(false);
        NhanVienDTO dto = dao.selectByEmail(email);
        Assert.assertNotNull(dto);
        Assert.assertEquals(email, dto.getEmail());
        dao.con.rollback();
    }
}