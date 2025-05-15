package test.java.GUI.Panel;

import static org.assertj.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.assertj.swing.core.matcher.JButtonMatcher.withName;
import static org.assertj.swing.core.matcher.JLabelMatcher.withName;
import static org.assertj.swing.core.matcher.JLabelMatcher.withText;
import static org.assertj.swing.data.TableCell.row;
import static org.junit.Assert.fail;

import DTO.TaiKhoanDTO;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.*;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.timing.Timeout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import GUI.Main;
import DTO.NhanVienDTO;
import GUI.Component.MenuTaskbar;
import GUI.Component.itemTaskbar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class NhaCungCapTest {
    private FrameFixture window;
    private BasicRobot robot;
    private Main mainFrame;

    @Before
    public void setUp() {
        robot = (BasicRobot) robotWithNewAwtHierarchy();

        // Create mock TaiKhoanDTO
        TaiKhoanDTO user = new TaiKhoanDTO(2, "hgbaodev", "123456", 1, 1);

        // Create and show the main frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                mainFrame = new Main(user);
                mainFrame.setVisible(true);
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
        });

        // Wait for the frame to be visible
        robot.waitForIdle();

        // Find the main window
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return frame.isShowing() && frame instanceof Main;
            }
        }).using(robot);

        // Find and click the supplier menu item
        window.label(withText("Nhà cung cấp")).click();
        robot.waitForIdle();
    }

    @Test
    public void testInitialComponents() {
        // Test if table exists
        JTableFixture table = window.table("tableNhaCungCap");
        table.requireVisible();
        table.requireEnabled();

        // Test if buttons exist
        window.button("btnCreate").requireVisible().requireEnabled();
        window.button("btnUpdate").requireVisible().requireEnabled();
        window.button("btnDelete").requireVisible().requireEnabled();
        window.button("btnDetail").requireVisible().requireEnabled();
        window.button("btnImport").requireVisible().requireEnabled();
        window.button("btnExport").requireVisible().requireEnabled();

        // Test if search components exist
        window.textBox("txtSearch").requireVisible().requireEnabled();
        window.comboBox("cbxSearchType").requireVisible().requireEnabled();
        window.button("btnReset").requireVisible().requireEnabled();
    }

    @Test
    public void testSearchSupplierByName() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            String cellValue = table.cell(row(i).column(1)).value().toLowerCase();
            if (cellValue.contains("vivo")) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Tên nhà cung cấp");
        searchField.enterText("vivo");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term
        for (int i = 0; i < actualResults; i++) {
            String cellValue = table.cell(row(i).column(1)).value().toLowerCase();
            assert cellValue.contains("vivo") : "Row " + i + " does not contain 'vivo'";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }

    @Test
    public void testSearchSupplierByCode() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            String cellValue = table.cell(row(i).column(0)).value();
            if (cellValue.contains("NCC")) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Mã nhà cung cấp");
        searchField.enterText("NCC");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term
        for (int i = 0; i < actualResults; i++) {
            String cellValue = table.cell(row(i).column(0)).value();
            assert cellValue.contains("NCC") : "Row " + i + " does not contain 'NCC'";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }

    @Test
    public void testSearchSupplierByAddress() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            String cellValue = table.cell(row(i).column(2)).value().toLowerCase();
            if (cellValue.contains("hà nội")) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Địa chỉ");
        searchField.enterText("Hà Nội");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term
        for (int i = 0; i < actualResults; i++) {
            String cellValue = table.cell(row(i).column(2)).value().toLowerCase();
            assert cellValue.contains("hà nội") : "Row " + i + " does not contain 'Hà Nội'";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }

    @Test
    public void testSearchSupplierByEmail() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            String cellValue = table.cell(row(i).column(3)).value().toLowerCase();
            if (cellValue.contains("@gmail.com")) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Email");
        searchField.enterText("@gmail.com");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term
        for (int i = 0; i < actualResults; i++) {
            String cellValue = table.cell(row(i).column(3)).value().toLowerCase();
            assert cellValue.contains("@gmail.com") : "Row " + i + " does not contain '@gmail.com'";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }

    @Test
    public void testSearchSupplierByPhone() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            String cellValue = table.cell(row(i).column(4)).value();
            if (cellValue.contains("0123")) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Số điện thoại");
        searchField.enterText("0123");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term
        for (int i = 0; i < actualResults; i++) {
            String cellValue = table.cell(row(i).column(4)).value();
            assert cellValue.contains("0123") : "Row " + i + " does not contain '0123'";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }

    @Test
    public void testSearchSupplierByAll() {
        // Get search components
        JTextComponentFixture searchField = window.textBox("txtSearch");
        JComboBoxFixture searchType = window.comboBox("cbxSearchType");
        JTableFixture table = window.table("tableNhaCungCap");

        // Get initial row count
        int initialRowCount = table.rowCount();

        // Count expected results before search
        int expectedResults = 0;
        for (int i = 0; i < initialRowCount; i++) {
            boolean found = false;
            for (int col = 0; col < 5; col++) {
                String cellValue = table.cell(row(i).column(col)).value().toLowerCase();
                if (cellValue.contains("vivo")) {
                    found = true;
                    break;
                }
            }
            if (found) {
                expectedResults++;
            }
        }

        // Perform search
        searchType.selectItem("Tất cả");
        searchField.enterText("vivo");
        robot.waitForIdle();

        // Verify search results
        table.requireVisible();
        int actualResults = table.rowCount();
        assert actualResults == expectedResults : "Expected " + expectedResults + " results but got " + actualResults;

        // Verify each result contains the search term in at least one column
        for (int i = 0; i < actualResults; i++) {
            boolean found = false;
            for (int col = 0; col < 5; col++) {
                String cellValue = table.cell(row(i).column(col)).value().toLowerCase();
                if (cellValue.contains("vivo")) {
                    found = true;
                    break;
                }
            }
            assert found : "Row " + i + " does not contain 'vivo' in any column";
        }

        // Reset search
        window.button("btnReset").click();
        robot.waitForIdle();
        table.requireRowCount(initialRowCount);
    }
    @Test
    public void testAddSupplierSuccessfully() {
        // Click nút Thêm
        window.button("btnCreate").click();
        robot.waitForIdle();

        // Tìm dialog Thêm nhà cung cấp
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().contains("Thêm nhà cung cấp");
            }
        }).using(robot);

        // Điền dữ liệu hợp lệ
        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("Nhà cung cấp test");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("123 Đường ABC");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("test@supplier.com");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("0123456789");
        robot.waitForIdle();

        // Click nút Lưu
        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Thêm đơn vị");
            }
        }).click();
        robot.waitForIdle();

        // Dialog phải đóng (ko còn hiển thị)
        dialog.requireNotVisible();

        // Kiểm tra bảng có thêm dòng mới với tên vừa thêm
        JTableFixture table = window.table("tableNhaCungCap");
        boolean found = false;
        for (int i = 0; i < table.rowCount(); i++) {
            String tenNCC = table.cell(row(i).column(1)).value();
            if (tenNCC.equals("Nhà cung cấp test")) {
                found = true;
                break;
            }
        }
        assert found : "Không tìm thấy nhà cung cấp mới trong bảng";
    }

    @Test
    public void testAddSupplierValidationFailsEmptyName() {
        window.button("btnCreate").click();
        robot.waitForIdle();

        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().contains("Thêm nhà cung cấp");
            }
        }).using(robot);

        // Để trống tên nhà cung cấp (bắt buộc)
        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("123 Đường ABC");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("test@supplier.com");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("0123456789");
        robot.waitForIdle();

        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Thêm đơn vị");
            }
        }).click();
        robot.waitForIdle();

        // Kiểm tra có JOptionPane cảnh báo
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().using(robot);
        optionPane.requireVisible();
        optionPane.requireMessage("Tên nhà cung cấp không được rỗng");

        // Đóng dialog cảnh báo
        optionPane.okButton().click();

        // Dialog thêm nhà cung cấp vẫn còn mở
        dialog.requireVisible();

        // Đóng dialog thêm nhà cung cấp để cleanup
        dialog.close();
    }

    @Test
    public void testAddSupplierValidationFailsInvalidEmail() {
        window.button("btnCreate").click();
        robot.waitForIdle();

        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().contains("Thêm nhà cung cấp");
            }
        }).using(robot);

        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("Nhà cung cấp test");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("123 Đường ABC");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("invalid-email");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("0123456789");
        robot.waitForIdle();

        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Thêm đơn vị");
            }
        }).click();
        robot.waitForIdle();

        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().using(robot);
        optionPane.requireVisible();
        optionPane.requireMessage("Email không được rỗng và phải đúng cú pháp");

        optionPane.okButton().click();
        dialog.requireVisible();

        dialog.close();
    }

    @Test
    public void testAddSupplierValidationFailsInvalidPhone() {
        window.button("btnCreate").click();
        robot.waitForIdle();

        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().contains("Thêm nhà cung cấp");
            }
        }).using(robot);

        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("Nhà cung cấp test");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("123 Đường ABC");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("test@supplier.com");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("abc123"); // Số điện thoại không hợp lệ (chứa chữ)
        robot.waitForIdle();

        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Thêm đơn vị");
            }
        }).click();
        robot.waitForIdle();

        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().using(robot);
        optionPane.requireVisible();
        optionPane.requireMessage("Số điện thoại không được rỗng và phải là 10 ký tự số");

        optionPane.okButton().click();
        dialog.requireVisible();

        dialog.close();
    }

    @Test
    public void testAddSupplierValidationFailsShortPhone() {
        window.button("btnCreate").click();
        robot.waitForIdle();

        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().contains("Thêm nhà cung cấp");
            }
        }).using(robot);

        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("Nhà cung cấp test");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("123 Đường ABC");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("test@supplier.com");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("0123"); // Số điện thoại quá ngắn (chỉ có 4 số)
        robot.waitForIdle();

        // Click nút thêm và đợi dialog lỗi xuất hiện
        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Thêm đơn vị");
            }
        }).click();
        robot.waitForIdle();

        // Đợi và kiểm tra thông báo lỗi
        try {
            JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane()
                .withTimeout(5000) // Tăng thời gian chờ lên 5 giây
                .using(robot);
            optionPane.requireVisible();
            optionPane.requireMessage("Số điện thoại không được rỗng và phải là 10 ký tự số");
            optionPane.okButton().click();
            robot.waitForIdle();
        } catch (WaitTimedOutError e) {
            fail("Không tìm thấy thông báo lỗi khi số điện thoại quá ngắn");
        }

        // Kiểm tra dialog thêm nhà cung cấp vẫn còn mở
        dialog.requireVisible();

        // Kiểm tra bảng không có thêm dòng mới
        JTableFixture table = window.table("tableNhaCungCap");
        boolean found = false;
        for (int i = 0; i < table.rowCount(); i++) {
            String tenNCC = table.cell(row(i).column(1)).value();
            if (tenNCC.equals("Nhà cung cấp test")) {
                found = true;
                break;
            }
        }
        assert !found : "Nhà cung cấp không hợp lệ vẫn được thêm vào bảng";

        // Đóng dialog thêm nhà cung cấp
        dialog.close();
    }

    @Test
    public void testUpdateSupplierSuccessfully() {
        // Chọn dòng đầu tiên trong bảng
        JTableFixture table = window.table("tableNhaCungCap");
        table.selectRows(0);
        robot.waitForIdle();

        // Click nút Sửa
        window.button("btnUpdate").click();
        robot.waitForIdle();

        // Tìm dialog Sửa nhà cung cấp
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().equals("Chỉnh sửa nhà cung cấp");
            }
        }).using(robot);

        // Sửa thông tin
        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        String oldName = nameField.text();
        nameField.setText("Nhà cung cấp đã sửa");
        robot.waitForIdle();

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.setText("456 Đường XYZ");
        robot.waitForIdle();

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.setText("updated@supplier.com");
        robot.waitForIdle();

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("0987654321");
        robot.waitForIdle();

        // Click nút Lưu
        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Lưu thông tin");
            }
        }).click();
        robot.waitForIdle();

        // Dialog phải đóng
        dialog.requireNotVisible();

        // Kiểm tra bảng đã được cập nhật
        boolean found = false;
        for (int i = 0; i < table.rowCount(); i++) {
            String tenNCC = table.cell(row(i).column(1)).value();
            if (tenNCC.equals("Nhà cung cấp đã sửa")) {
                found = true;
                break;
            }
        }
        assert found : "Không tìm thấy thông tin đã sửa trong bảng";
    }

    @Test
    public void testUpdateSupplierValidationFailsEmptyName() {
        // Chọn dòng đầu tiên trong bảng
        JTableFixture table = window.table("tableNhaCungCap");
        table.selectRows(0);
        robot.waitForIdle();

        // Click nút Sửa
        window.button("btnUpdate").click();
        robot.waitForIdle();

        // Tìm dialog Sửa nhà cung cấp
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().equals("Chỉnh sửa nhà cung cấp");
            }
        }).using(robot);

        // Xóa tên nhà cung cấp
        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.setText("");
        robot.waitForIdle();

        // Click nút Lưu
        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Lưu thông tin");
            }
        }).click();
        robot.waitForIdle();

        // Kiểm tra thông báo lỗi
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().using(robot);
        optionPane.requireVisible();
        optionPane.requireMessage("Tên nhà cung cấp không được rỗng");

        // Đóng dialog cảnh báo
        optionPane.okButton().click();

        // Dialog sửa vẫn còn mở
        dialog.requireVisible();

        // Đóng dialog sửa
        dialog.close();
    }

    @Test
    public void testUpdateSupplierValidationFailsInvalidPhone() {
        // Chọn dòng đầu tiên trong bảng
        JTableFixture table = window.table("tableNhaCungCap");
        table.selectRows(0);
        robot.waitForIdle();

        // Click nút Sửa
        window.button("btnUpdate").click();
        robot.waitForIdle();

        // Tìm dialog Sửa nhà cung cấp
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().equals("Chỉnh sửa nhà cung cấp");
            }
        }).using(robot);

        // Nhập số điện thoại không hợp lệ
        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.setText("abc123"); // Số điện thoại không hợp lệ
        robot.waitForIdle();

        // Click nút Lưu
        dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals("Lưu thông tin");
            }
        }).click();
        robot.waitForIdle();

        // Kiểm tra thông báo lỗi
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().using(robot);
        optionPane.requireVisible();
        optionPane.requireMessage("Số điện thoại không được rỗng và phải là 10 ký tự số");

        // Đóng dialog cảnh báo
        optionPane.okButton().click();

        // Dialog sửa vẫn còn mở
        dialog.requireVisible();

        // Đóng dialog sửa
        dialog.close();
    }

    @Test
    public void testDeleteSupplierSuccessfully() {
        // Click vào menu Nhà cung cấp
        window.label(withText("Nhà cung cấp")).click();
        robot.waitForIdle();

        // Đợi bảng xuất hiện
        JTableFixture table = window.table("tableNhaCungCap");
        table.requireVisible();
        robot.waitForIdle();

        // Chọn dòng đầu tiên trong bảng
        table.selectRows(0);
        robot.waitForIdle();

        // Lưu thông tin nhà cung cấp trước khi xóa để kiểm tra
        String maNCC = table.cell(row(0).column(0)).value();
        String tenNCC = table.cell(row(0).column(1)).value();

        // Click nút Xóa
        window.button("btnDelete").click();
        robot.waitForIdle();

        // Kiểm tra dialog xác nhận xóa
        JOptionPaneFixture confirmDialog = JOptionPaneFinder.findOptionPane()
            .withTimeout(5000)
            .using(robot);
        confirmDialog.requireVisible();
        confirmDialog.requireMessage("Bạn có chắc chắn muốn xóa nhà cung cấp!");

        // Click nút OK để xác nhận xóa
        confirmDialog.okButton().click();
        robot.waitForIdle();

        // Đợi một chút để đảm bảo thao tác xóa hoàn tất
        robot.waitForIdle();
        robot.waitForIdle();

        // Kiểm tra nhà cung cấp đã bị xóa khỏi bảng
        boolean found = false;
        for (int i = 0; i < table.rowCount(); i++) {
            String currentMaNCC = table.cell(row(i).column(0)).value();
            String currentTenNCC = table.cell(row(i).column(1)).value();
            if (currentMaNCC.equals(maNCC) && currentTenNCC.equals(tenNCC)) {
                found = true;
                break;
            }
        }
        assert !found : "Nhà cung cấp vẫn còn trong bảng sau khi xóa";
    }

    @Test
    public void testViewSupplierDetails() {
        // Click vào menu Nhà cung cấp
        window.label(withText("Nhà cung cấp")).click();
        robot.waitForIdle();

        // Đợi bảng xuất hiện
        JTableFixture table = window.table("tableNhaCungCap");
        table.requireVisible();
        robot.waitForIdle();

        // Chọn dòng đầu tiên trong bảng
        table.selectRows(0);
        robot.waitForIdle();

        // Lưu thông tin nhà cung cấp để kiểm tra
        String maNCC = table.cell(row(0).column(0)).value();
        String tenNCC = table.cell(row(0).column(1)).value();
        String diaChi = table.cell(row(0).column(2)).value();
        String email = table.cell(row(0).column(3)).value();
        String sdt = table.cell(row(0).column(4)).value();

        // Click nút Chi tiết
        window.button("btnDetail").click();
        robot.waitForIdle();

        // Tìm dialog Chi tiết nhà cung cấp
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog d) {
                return d.isShowing() && d.getTitle().equals("Chi tiết nhà cung cấp");
            }
        }).using(robot);

        // Kiểm tra thông tin hiển thị trong dialog
        JTextComponentFixture nameField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Tên nhà cung cấp");
            }
        });
        nameField.requireText(tenNCC);

        JTextComponentFixture addressField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Địa chỉ");
            }
        });
        addressField.requireText(diaChi);

        JTextComponentFixture emailField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Email");
            }
        });
        emailField.requireText(email);

        JTextComponentFixture phoneField = dialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                return textField.getParent() instanceof GUI.Component.InputForm &&
                       ((JLabel)((GUI.Component.InputForm)textField.getParent()).getComponent(0)).getText().equals("Số điện thoại");
            }
        });
        phoneField.requireText(sdt);

        // Đóng dialog
        dialog.close();
    }

    @Test
    public void testExportExcel() {
        // Click vào menu Nhà cung cấp
        window.label(withText("Nhà cung cấp")).click();
        robot.waitForIdle();

        // Đợi bảng xuất hiện
        JTableFixture table = window.table("tableNhaCungCap");
        table.requireVisible();
        robot.waitForIdle();

        // Kiểm tra bảng có dữ liệu
        int rowCount = table.rowCount();
        assert rowCount > 0 : "Bảng không có dữ liệu để xuất";

        // Click nút Xuất Excel
        window.button("btnExport").click();
        robot.waitForIdle();

        // Đường dẫn file xuất
        File documentsFolder = new File(System.getProperty("user.home"), "Documents");
        File exportFile = new File(documentsFolder, "NhaCungCap_Test.xlsx");

        try {
            // Tìm dialog chọn file
            DialogFixture fileDialog = WindowFinder.findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
                @Override
                protected boolean isMatching(JDialog dialog) {
                    return dialog.isShowing();
                }
            }).withTimeout(Timeout.timeout(5000).duration()).using(robot);

            // Nhập tên file
            JTextComponentFixture fileNameBox = fileDialog.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
                @Override
                protected boolean isMatching(JTextField textField) {
                    return textField.isShowing();
                }
            });
            fileNameBox.setText(exportFile.getAbsolutePath());
            robot.waitForIdle();

            // Nhấn nút Save
            fileDialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
                @Override
                protected boolean isMatching(JButton button) {
                    String text = button.getText();
                    return button.isShowing() && text != null && (text.equals("Save") || text.equals("Lưu"));
                }
            }).click();
            robot.waitForIdle();
        } catch (WaitTimedOutError e) {
            fail("Không tìm thấy dialog chọn file");
        }

        // Kiểm tra file được tạo và không rỗng
        assert exportFile.exists() : "File Excel không được tạo";
        assert exportFile.length() > 0 : "File Excel trống";

        // Xóa file sau khi test
        exportFile.delete();
    }

    @Test
    public void testImportExcel() {
        // Click vào menu Nhà cung cấp
        window.label(withText("Nhà cung cấp")).click();
        robot.waitForIdle();

        // Đợi bảng xuất hiện
        JTableFixture table = window.table("tableNhaCungCap");
        table.requireVisible();
        robot.waitForIdle();

        // Lưu số lượng dòng trong bảng trước khi import
        int initialRowCount = table.rowCount();
        System.out.println("Số dòng ban đầu: " + initialRowCount);

        // Click nút Import
        window.button("btnImport").click();
        robot.waitForIdle();

        // Nhập đường dẫn file template
        File templateFile = new File("E:\\NhaCungCap_Template.xlsx");

        // Log thông tin chi tiết về file
        System.out.println("Template file path: " + templateFile.getAbsolutePath());
        System.out.println("Template file exists: " + templateFile.exists());
        System.out.println("Template file size: " + templateFile.length() + " bytes");

        // Kiểm tra file có tồn tại không
        if (!templateFile.exists()) {
            fail("File template không tồn tại tại đường dẫn: " + templateFile.getAbsolutePath() +
                 "\nVui lòng kiểm tra:\n" +
                 "1. File có đúng tên 'NhaCungCap_Template.xlsx' không\n" +
                 "2. File có nằm trong ổ đĩa E không\n" +
                 "3. File có bị ẩn không");
        }

        // Kiểm tra file có rỗng không
        if (templateFile.length() == 0) {
            fail("File template rỗng. Vui lòng kiểm tra lại nội dung file.");
        }

        // Nhập đường dẫn file
        robot.pressAndReleaseKeys(KeyEvent.VK_ALT, KeyEvent.VK_D);
        robot.waitForIdle();
        robot.enterText(templateFile.getAbsolutePath());
        robot.waitForIdle();

        // Nhấn Enter để chọn file
        robot.pressAndReleaseKey(KeyEvent.VK_ENTER);
        robot.waitForIdle();

        // Đợi một chút để đảm bảo import hoàn tất
        robot.waitForIdle();
        robot.waitForIdle();

        // Kiểm tra có thông báo lỗi không
        try {
            JOptionPaneFixture errorDialog = JOptionPaneFinder.findOptionPane()
                .withTimeout(5000)
                .using(robot);

        } catch (WaitTimedOutError e) {
            // Không có thông báo lỗi, tiếp tục kiểm tra
        }

        // Kiểm tra bảng đã được cập nhật
        int newRowCount = table.rowCount();
        System.out.println("Số dòng sau khi import: " + newRowCount);

        if (newRowCount <= initialRowCount) {
            fail("Import không thành công. Số dòng không tăng sau khi import.\n" +
                 "Số dòng ban đầu: " + initialRowCount + "\n" +
                 "Số dòng sau khi import: " + newRowCount);
        }
        
        // Log thông tin dòng mới được thêm vào
        System.out.println("Thông tin dòng mới:");
        // Log thông tin của dòng mới (5 cột: Mã, Tên, Địa chỉ, Email, SĐT)
        System.out.println("Mã NCC: " + table.cell(row(newRowCount-1).column(0)).value());
        System.out.println("Tên NCC: " + table.cell(row(newRowCount-1).column(1)).value());
        System.out.println("Địa chỉ: " + table.cell(row(newRowCount-1).column(2)).value());
        System.out.println("Email: " + table.cell(row(newRowCount-1).column(3)).value());
        System.out.println("SĐT: " + table.cell(row(newRowCount-1).column(4)).value());
    }

    @After
    public void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> mainFrame.dispose());
        }
    }
}