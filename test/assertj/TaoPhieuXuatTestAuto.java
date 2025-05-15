package GUI.Panel;

import DTO.TaiKhoanDTO;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.JFrame;
import javax.swing.JTable;
import static org.assertj.swing.core.matcher.DialogMatcher.withTitle;
import static org.assertj.swing.core.matcher.JButtonMatcher.withText;
import org.assertj.swing.data.TableCell;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.edt.GuiQuery;
import static org.assertj.swing.finder.JOptionPaneFinder.findOptionPane;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaoPhieuXuatTestAuto {

    private FrameFixture window;
    private TaoPhieuXuat panel;

    @Before
    public void setUp() {
        TaiKhoanDTO tk = new TaiKhoanDTO();
        tk.setManv(2);
        panel = GuiActionRunner.execute(new GuiQuery<TaoPhieuXuat>() {
            @Override
            protected TaoPhieuXuat executeInEDT() {
                return new TaoPhieuXuat(null, tk, "xuat");
            }
        });

        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {
            @Override
            protected JFrame executeInEDT() {
                JFrame f = new JFrame();
                f.setContentPane(panel);
                f.pack();
                f.setVisible(true);
                return f;
            }
        });

        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

//    Mã testcase: PX_77
    @Test
    public void testTimKiemTuDongNhapVaTim() throws InterruptedException {
        String keyword = "5";

        // Expected output để so sánh
        Object[][] expectedOutput = {
            {2, "Samsung Galaxy A53 5G", 5},
            {5, "Samsung Galaxy A54 5G", 39},
            {13, "Samsung Galaxy S22+ 5G", 20},
            {14, "OPPO Reno6 Pro 5G", 7},
            {15, "OPPO A95", 5},};

        // Tìm JTextField có name = "txtTimKiem"
        JTextComponentFixture txtTimKiem = window.textBox("txtTimKiem");
        txtTimKiem.setText("");
        txtTimKiem.enterText(keyword);

        // Chờ UI xử lý
        Thread.sleep(2000);

        // Lấy bảng dữ liệu sản phẩm
        JTable table = panel.tableSanPham;
        int rowCount = table.getRowCount();

        // Kiểm tra số dòng đúng như expectedOutput
        assertThat(rowCount)
                .withFailMessage("Số lượng dòng hiển thị không đúng. Expected: %d, Actual: %d", expectedOutput.length, rowCount)
                .isEqualTo(expectedOutput.length);

        // So sánh từng dòng, từng cột
        for (int i = 0; i < expectedOutput.length; i++) {
            for (int col = 0; col < expectedOutput[i].length; col++) {
                Object expectedValue = expectedOutput[i][col];
                Object actualValue = table.getValueAt(i, col);

                // So sánh equals (có thể cast về String hoặc Integer tùy kiểu cột)
                assertThat(actualValue)
                        .withFailMessage("Dòng %d, cột %d không khớp. Expected: %s, Actual: %s",
                                i, col, expectedValue, actualValue)
                        .isEqualTo(expectedValue);
            }
        }
    }

//    @Test
//    public void test_px_78() {
//        // Start GUI test
//        JTableFixture tableSanPham = window.table("tableSanPham");
//
//        // Click chọn dòng đầu tiên trong bảng sản phẩm
//        tableSanPham.cell(TableCell.row(0).column(0)).click();
//
//        // Giả định dữ liệu test (mock DB hoặc hardcode tạm)
//        String expectedMaSP = "1";
//        String expectedTenSP = "Vivo Y22s";
//        String expectedCauHinh = "32GB - 3GB - Xanh";
//        String expectedGia = "5500000";
//        String expectedTonKho = "4";
//
//        // Kiểm tra các JTextField hiển thị thông tin
//        window.textBox("txtMaSp").requireText(expectedMaSP);
//        window.textBox("txtTenSp").requireText(expectedTenSP);
//        window.textBox("txtGiaXuat").requireText(expectedGia);
//        window.textBox("txtSoluongTon").requireText(expectedTonKho);
//
//        // Kiểm tra combobox cấu hình hiển thị đúng định dạng và đầy đủ tổ hợp ROM - RAM - Màu sắc
//        JComboBoxFixture comboCauHinh = window.comboBox("cbxPhienBan");
//        List<String> items = Arrays.asList(comboCauHinh.contents());
//
//        // Kiểm tra có ít nhất 1 cấu hình đúng định dạng ROM - RAM - Màu
//        for (String cauHinh : items) {
//            assertThat(cauHinh).matches("(?i)^\\d+GB - \\d+GB - .+$"); // Ví dụ: 128GB - 6GB - Đen
//        }
//
//        // Kiểm tra combo chứa cấu hình đã chọn
//        comboCauHinh.requireSelection(expectedCauHinh);
//
////    // Giả sử bạn có phương thức tính tổng tồn kho của từng cấu hình
////    int tongSoLuongTonTheoCauHinh = getTongSoLuongTonCuaCauHinh(items);
////    assertThat(tongSoLuongTonTheoCauHinh).isEqualTo(Integer.parseInt(expectedTonKho));
//    }
    @Test
    public void testMainFlow_TaoPhieuXuat_ThanhCong() throws InterruptedException {
        // 1. Chọn sản phẩm (hàng đầu tiên)
        JTableFixture tableSanPham = window.table("tableSanPham");
        tableSanPham.cell(TableCell.row(0).column(0)).click();
//        Thread.sleep(1000); // Cho bảng cấu hình và IMEI load xong

        // 2. Chọn cấu hình đầu tiên (comboBox cbxPhienBan)
        JComboBoxFixture comboCauHinh = window.comboBox("cbxPhienBanCombo");
        comboCauHinh.selectItem(0);
//        Thread.sleep(1000); // Cho IMEI load lại

        window.button("chonImei").click();
        DialogFixture selectImeiDialog = window.dialog(withTitle("Chọn IMEI")).requireVisible();
        JListFixture imeiList = selectImeiDialog.list("listImei"); // hoặc table nếu là JTable
        imeiList.clickItem(0);

        // Hoặc chọn phần tử thứ nhất, tuỳ ý thao tác
        // imeiList.selectItem(0);
        // Click button xác nhận trong dialog
        selectImeiDialog.button(withText("Xác nhận")).click();
        Thread.sleep(1000);
        // 4. Click "Thêm sản phẩm"
        window.button("btnAddSp").click();
        JTableFixture tableSanPhamDaChon = window.table("tablePhieuXuat");
        assertThat(tableSanPhamDaChon.rowCount()).isGreaterThan(0);
        Thread.sleep(1000);
        // 5. Click "Chọn khách hàng"
        window.button("btnKh").click();

        // 5.1 Mở dialog "Chọn khách hàng" và chọn khách đầu tiên
        DialogFixture dialogKhach = window.dialog(withTitle("Chọn khách hàng")).requireVisible();
        JTableFixture tableKhach = dialogKhach.table("tableKhachHang"); // Đảm bảo tên chính xác
        tableKhach.cell(TableCell.row(0).column(0)).click(); // Chọn dòng đầu tiên

        window.button("buttonAdd").click();
        // 6. Kiểm tra tổng tiền có giá trị
        JTextComponentFixture txtTongTien = window.textBox("lbltongtien");
        String tongTienStr = txtTongTien.text();
        assertThat(tongTienStr).isNotBlank();

        // 7. Click nút "Xuất hàng"
        window.button("btnXuatHang").click();
        // 8. Kiểm tra thông báo JOptionPane xuất hiện
        JOptionPaneFixture successDialog = findOptionPane().using(window.robot());
        successDialog.requireMessage("Xuất hàng thành công !");
        successDialog.button(withText("OK")).click();

    }

}
