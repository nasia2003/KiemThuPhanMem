package DAO;

import DTO.NhomQuyenDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class NhomQuyenDAOTest {
    // insert
    @Test
    public void Insert_TenNhomQuyenNull() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Tên nhóm quyền không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TenNhomQuyenRong() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("");
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.insert(dto)
        );
        Assert.assertEquals("Tên nhóm quyền không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Insert_TenNhomQuyenHopLe() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm quyền hợp lệ");
        dao.con.setAutoCommit(false);
        int ok = dao.insert(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // update
    @Test
    public void Update_TenNhomQuyenNull() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setManhomquyen(1);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Tên nhóm quyền không được null", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_TenNhomQuyenRong() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("");
        dto.setManhomquyen(1);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Tên nhóm quyền không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhomQuyenNegative() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm quyền");
        dto.setManhomquyen(-1);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không được âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_MaNhomQuyenZero() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm quyền");
        dto.setManhomquyen(0);
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.update(dto)
        );
        Assert.assertEquals("Mã nhóm quyền không được bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Update_NhomQuyenDaXoa() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm quyền");
        dto.setManhomquyen(4);
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Update_HopLe() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setManhomquyen(1);
        dto.setTennhomquyen("Nhóm quyền hợp lệ");
        dao.con.setAutoCommit(false);
        int ok = dao.update(dto);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    // delete
    @Test
    public void Delete_MaNhomQuyenRong() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.delete(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenKhongPhaiInteger() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "Abjc";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.delete(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền phải là số", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenNegative() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "-1";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.delete(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenZero() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "0";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.delete(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenKhongTonTai() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "99999";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhomQuyen);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenTonTaiChuaXoa() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "1";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhomQuyen);
        Assert.assertEquals(1, ok);
        dao.con.rollback();
    }

    @Test
    public void Delete_MaNhomQuyenTonTaiDaXoa() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "4";
        dao.con.setAutoCommit(false);
        int ok = dao.delete(MaNhomQuyen);
        Assert.assertEquals(0, ok);
        dao.con.rollback();
    }

    // selectAll
    @Test
    public void SelectAll_Test() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        dao.con.setAutoCommit(false);
        ArrayList<NhomQuyenDTO> result = dao.selectAll();
        Assert.assertFalse(result.isEmpty()); // empty thi fail
        dao.con.rollback();
    }

    // selectById
    @Test
    public void SelectById_MaNhomQuyenRong() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectById(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được rỗng", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenKhongPhaiInteger() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "Abjc";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectById(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền phải là số", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenNegative() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "-1";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectById(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được âm", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenZero() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "0";
        dao.con.setAutoCommit(false);
        IllegalArgumentException ex = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> dao.selectById(MaNhomQuyen)
        );
        Assert.assertEquals("Mã nhóm quyền không được bằng không", ex.getMessage());
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenKhongTonTai() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "99999";
        dao.con.setAutoCommit(false);
        NhomQuyenDTO result = dao.selectById(MaNhomQuyen);
        Assert.assertNull(result);
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenTonTaiChuaXoa() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "1";
        dao.con.setAutoCommit(false);
        NhomQuyenDTO result = dao.selectById(MaNhomQuyen);
        Assert.assertNotNull(result);
        Assert.assertEquals(MaNhomQuyen, Integer.toString(result.getManhomquyen()));
        dao.con.rollback();
    }

    @Test
    public void SelectById_MaNhomQuyenTonTaiDaXoa() throws SQLException {
        NhomQuyenDAO dao = NhomQuyenDAO.getInstance();
        String MaNhomQuyen = "4";
        dao.con.setAutoCommit(false);
        NhomQuyenDTO result = dao.selectById(MaNhomQuyen);
        Assert.assertNull(result);
        dao.con.rollback();
    }
}