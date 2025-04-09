package TestModule;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThongKeTest {
    @Test
    public void ThongKe_ThanhCong() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Vivo Y22s";
            Date StartDay = new Date(2023,1,1);
            Date EndDay = new Date(2023,5,10);
            // Act
            HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = dao.getThongKeTonKho(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertNotNull(result);

            for (Map.Entry<Integer, ArrayList<ThongKeTonKhoDTO>> entry : result.entrySet()) {
                ArrayList<ThongKeTonKhoDTO> list = entry.getValue();
                for (ThongKeTonKhoDTO dto : list) {
                    Assert.assertTrue(dto.getTensanpham().toLowerCase().contains(NameExpected.toLowerCase()));
                    Assert.assertTrue(dto.getNhaptrongky() - dto.getXuattrongky() >= 0);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ThongKe_KhongTonTaiTrongThoiGianXet() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Vivo Y22s";
            Date StartDay = new Date(2025,1,1);
            Date EndDay = new Date(2025,5,10);
            // Act
            HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = dao.getThongKeTonKho(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ThongKe_KhongTonTaiSanPham() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Vivo Y22ssssss";
            Date StartDay = new Date(2023,1,1);
            Date EndDay = new Date(2023,5,10);
            // Act
            HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = dao.getThongKeTonKho(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ThongKe_StartLonHonEnd() {
        ThongKeDAO dao = ThongKeDAO.getInstance();
        try {
            String NameExpected = "Vivo Y22s";
            Date StartDay = new Date(2023,12,1);
            Date EndDay = new Date(2023,5,10);
            // Act
            HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = dao.getThongKeTonKho(NameExpected, StartDay, EndDay);

            // Assert
            Assert.assertTrue(result.isEmpty());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
