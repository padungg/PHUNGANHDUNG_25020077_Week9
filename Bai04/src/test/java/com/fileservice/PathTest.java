package com.fileservice;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
    @Test
    public void testCrossPlatformPath() {
        String folder = "config";
        String fileName = "app.properties";
        
        // Java tự động nhận diện OS để dùng '/' hay '\'
        Path path = Paths.get(folder, fileName); 
        
        // Cách so sánh an toàn: không so sánh với chuỗi tĩnh nữa, mà so sánh thông qua API của Java
        Path expectedPath = Paths.get("config", "app.properties");
        
        assertEquals(expectedPath.toString(), path.toString());
    }
}
