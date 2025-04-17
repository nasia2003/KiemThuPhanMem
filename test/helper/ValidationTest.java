/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package helper;

import com.formdev.flatlaf.util.StringUtils;
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
public class ValidationTest {
    
    public ValidationTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * TC01 - Test hàm isEmty kiểm tra dữ liệu đầu vào
     * Mục tiêu: Kiểm tra dữ liệu đầu vào có rỗng hay không
     */
    @Test
    public void testIsEmpty() {
        // Trường hợp null
        assertTrue(Validation.isEmpty(null));

        // Trường hợp chuỗi rỗng
        assertTrue(Validation.isEmpty(""));

        // Trường hợp chuỗi không rỗng
        assertFalse(Validation.isEmpty("hello  "));
    }


    /**
     * TC02 - Test hàm isEmail kiểm tra dữ liệu đầu vào
     * Mục tiêu: Kiểm tra dữ liệu email đầu vào có rỗng, có hợp lệ hay không
     */
    @Test
    public void testIsEmail() {
        // Trường hợp email null
        assertFalse(Validation.isEmail(null));
        
        // Các email hợp lệ
        assertTrue(Validation.isEmail("example@email.com"));
        assertTrue(Validation.isEmail("user.name123@domain.co"));
        assertTrue(Validation.isEmail("user_name+tag@sub.domain.com"));

        // Các email không hợp lệ
        assertFalse(Validation.isEmail("plainaddress"));
        assertFalse(Validation.isEmail("missingatsign.com"));
        assertFalse(Validation.isEmail("@missingusername.com"));
        assertFalse(Validation.isEmail("username@.com"));
        assertFalse(Validation.isEmail("username@domain..com"));
        assertFalse(Validation.isEmail("username@domain"));
        assertFalse(Validation.isEmail(""));
    }

    /**
      * TC03 - Test hàm isNumber kiểm tra dữ liệu đầu vào 
      * Mục tiêu: Kiểm tra dữ liệu đầu vào có rỗng, có là số nguyên không âm hay không
     */
    @Test
    public void testIsNumber() {
        // Trường hợp null
        assertFalse(Validation.isEmail(null));

        // Các trường hợp không hợp lệ
        assertFalse(Validation.isNumber(""));              // chuỗi rỗng
        assertFalse(Validation.isNumber("-1"));            // số âm
        assertFalse(Validation.isNumber("123abc"));        // chứa chữ
        assertFalse(Validation.isNumber("12.5"));          // số thực
        
        // Trường hợp hợp lệ
        assertTrue(Validation.isNumber("0"));
        assertTrue(Validation.isNumber("999999999999999999999"));
    }
}
