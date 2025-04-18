/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package GUI.Dialog;

import java.awt.event.MouseEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MaiLoan
 */
public class KhachHangDialogTest {
    
    public KhachHangDialogTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * TC01
     * Mục tiêu: Kiểm tra dữ liệu số diện thoại đầu vào có 10 chữ số hợp lệ
     */
    
    @Test
    public void testValidPhoneNumbers_10Digits() {
        assertTrue(KhachHangDialog.isPhoneNumber("1234567890"));
    }
    
    /**
     * TC02
     * Mục tiêu: Kiểm tra dữ liệu số diện thoại hợp lệ đầu vào có định dạng xxx-xxx-xxxx, x là số
     */
    
    @Test
    public void testValidPhoneNumbers_Hyphens() {
        assertTrue(KhachHangDialog.isPhoneNumber("123-456-7890"));
    }
    /**
     * TC03
     * Mục tiêu: Kiểm tra dữ liệu số diện thoại hợp lệ đầu vào có định dạng (xxx)xxx-xxxx, x là số
     */
    
    @Test
    public void testValidPhoneNumbers_Parentheses() {
        assertTrue(KhachHangDialog.isPhoneNumber("(123)456-7890"));
    }

    /**
     * TC04
     * Mục tiêu: Kiểm tra dữ liệu số điện thoại không hợp lệ đầu vào có ít hơn 10 ký tự
     */
    @Test
    public void testInvalidPhoneNumbers_Less10Digits() {
        assertFalse(KhachHangDialog.isPhoneNumber("123456789")); 
    }
    
    /**
     * TC05
     * Mục tiêu: Kiểm tra dữ liệu số điện thoại không hợp lệ đầu vào có nhiều hơn 10 ký tự
     */
    @Test
    public void testInvalidPhoneNumbers_More10Digits() {
        assertFalse(KhachHangDialog.isPhoneNumber("12345678901"));
    }
    
    /**
     * TC06
     * Mục tiêu: Kiểm tra dữ liệu số điện thoại không hợp lệ đầu vào có chứa ký tự khác ký tự số
     */
    @Test
    public void testInvalidPhoneNumbers_OtherDigits() {
        assertFalse(KhachHangDialog.isPhoneNumber("12312#7890"));
    }
    
    /**
     * TC07
     * Mục tiêu: Kiểm tra dữ liệu số điện thoại không hợp lệ đầu vào thiếu dấu ngoặc đơn
     */
    @Test
    public void testInvalidPhoneNumbers_Hyphens() {
        assertFalse(KhachHangDialog.isPhoneNumber("123)456-7890"));
    }
    
    /**
     * TC08
     * Mục tiêu: Kiểm tra dữ liệu số điện thoại không hợp lệ đầu vào có dấu cách
     */
    @Test
    public void testInvalidPhoneNumbers_Spaces() {
        assertFalse(KhachHangDialog.isPhoneNumber("(123)    456-7890"));
    }
}