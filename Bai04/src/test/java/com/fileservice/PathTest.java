package com.fileservice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
    @Test
    public void testWindowsHardcodedPath() {
        String folder = "config";
        String fileName = "app.properties";
        
        // LỖI SAI: Hardcode dấu phân cách của riêng Windows (\)
        String fullPath = folder + "\\" + fileName; 
        
        // Code này chỉ PASS trên Windows, sẽ FAILED trên Linux/Mac
        assertEquals("config\\app.properties", fullPath);
    }
}
