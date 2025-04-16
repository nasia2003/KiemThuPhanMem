package DAO;

import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class TaiKhoanDAOTest {
    // insert
    @Test
    public void Insert_UserNameNull() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("UserName không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_UserNameRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("UserName không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_UserNameDaTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhanvienNegative() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(-1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mã nhân viên không được âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhanvienZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(0);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mã nhân viên không được bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhanvienDaTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhomQuyenNegative() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(-1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không thể âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhomQuyenZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(0);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không thể bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MaNhomQuyenKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(99);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiNegative2() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(-2);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiDaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(-1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Không khởi tạo trạng thái đã xóa", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThaiZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TrangThai2() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MatKhauNull() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mật khẩu không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_MatKhauRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Mật khẩu không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_HopLe() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("123");
        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // update
    @Test
    public void Update_UserNameNull() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("UserName không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_UserNameRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("UserName không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_UserNameDaTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhanvienNegative() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(-1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhân viên không được âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhanvienZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(0);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhân viên không được bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhanvienDaTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(1);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhomQuyenNegative() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(-1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không thể âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhomQuyenZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(0);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không thể bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhomQuyenKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(99);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThaiNegative2() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(-2);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThaiDaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(-1);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Không khởi tạo trạng thái đã xóa", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThaiZero() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TrangThai2() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("password");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Trạng thái không xác định", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MatKhauNull() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mật khẩu không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MatKhauRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mật khẩu không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_HopLe() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setManhomquyen(1);
        dto.setUsername("hgbaodev1234");
        dto.setManv(6);
        dto.setTrangthai(0);
        dto.setMatkhau("123");
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // updatePass
    @Test
    public void UpdatePass_EmailRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "";
        String Password = "12323";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.updatePass(Email, Password)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void UpdatePass_EmailKhongDungDinhDang() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "123124@g";
        String Password = "12323";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.updatePass(Email, Password)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void UpdatePass_EmailKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "123124@gmail.com";
        String Password = "12323";
        dao.con.setAutoCommit(false);
        int ok = dao.updatePass(Email, Password);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void UpdatePass_MatKhauRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "123124@gmail.com";
        String Password = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.updatePass(Email, Password)
        );
        Assert.assertEquals("Mật khẩu không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void UpdatePass_HopLe() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "chinchin@gmail.com";
        String Password = "12323";
        dao.con.setAutoCommit(false);
        int ok = dao.updatePass(Email, Password);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // selectByEmail
    @Test
    public void SelectByEmail_EmailRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectByEmail(Email)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailKhongDungDinhDang() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "1@gmai";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectByEmail(Email)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "123456789@gmail.com";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectByEmail(Email);
        Assert.assertNull(dto); // null thì pass
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailTonTaiNhanVienDaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "1@gmail.com";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectByEmail(Email);
        Assert.assertNull(dto); // null thì pass
        dao.con.rollback();
    }

    @Test
    public void SelectByEmail_EmailTonTaiNhanVienChuaXoa() throws SQLException {
        TaiKhoanDAO TKdao = TaiKhoanDAO.getInstance();
        String Email = "musicanime2501@gmail.com";
        TKdao.con.setAutoCommit(false);
        TaiKhoanDTO TKdto = TKdao.selectByEmail(Email);
        Assert.assertNotNull(TKdto);
        NhanVienDAO NVdao = NhanVienDAO.getInstance();
        NhanVienDTO NVDTO = NVdao.selectById(Integer.toString(TKdto.getManv()));
        Assert.assertEquals(Email, NVDTO.getEmail());
        TKdao.con.rollback();
    }

    // checkOtp
    @Test
    public void CheckOtp_EmailRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "";
        String Password = "123213";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.checkOtp(Email, Password)
        );
        Assert.assertEquals("Email không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void CheckOtp_EmailKhongDungDinhDang() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "1@gmai";
        String Password = "123213";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.checkOtp(Email, Password)
        );
        Assert.assertEquals("Email không đúng định dạng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void CheckOtp_EmailKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String Email = "123456789@gmai.com";
        String Password = "123213";
        dao.con.setAutoCommit(false);
        boolean ok = dao.checkOtp(Email, Password);
        Assert.assertFalse(ok); // false thì pass
        dao.con.rollback();
    }

    // delete
    @Test
    public void Delete_MaNhanVienRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "99999";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienTonTaiChuaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "2";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhanVienTonTaiDaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "49";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhanVien);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }
    // selectAll()
    @Test
    public void SelectAll_Test() {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        ArrayList<TaiKhoanDTO> result = dao.selectAll();
        Assert.assertFalse(result.isEmpty()); // empty thì fail
    }

    // selectById
    @Test
    public void SelectById_MaNhanVienRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
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
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "99999";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNull(dto);
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienTonTaiChuaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "2";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNotNull(dto);
        Assert.assertEquals(MaNhanVien, Integer.toString(dto.getManv()));
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhanVienTonTaiDaXoa() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String MaNhanVien = "49";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectById(MaNhanVien);
        Assert.assertNull(dto);
        dao.con.rollback();
    }

    // selectByUser
    @Test
    public void SelectByUser_TenDangNhapRong() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String TenDangNhap = "-1";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                ()-> dao.selectByUser(TenDangNhap)
        );
        Assert.assertEquals("Tên đăng nhập không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectByUser_TenDangNhapKhongTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String TenDangNhap = "DungNguyen";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectByUser(TenDangNhap);
        Assert.assertNull(dto);
        dao.con.rollback();
    }

    @Test
    public void SelectByUser_TenDangNhapTonTai() throws SQLException {
        TaiKhoanDAO dao = TaiKhoanDAO.getInstance();
        String TenDangNhap = "hgbaodev";
        dao.con.setAutoCommit(false);
        TaiKhoanDTO dto = dao.selectByUser(TenDangNhap);
        Assert.assertNotNull(dto);
        Assert.assertEquals(TenDangNhap, dto.getUsername());
        dao.con.rollback();
    }
}