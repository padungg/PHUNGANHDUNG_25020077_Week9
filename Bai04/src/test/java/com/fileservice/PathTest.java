package com.fileservice;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
    @Test
    public void testWindowsHardcodedPath() {
        // Lấy đường dẫn chuẩn theo hệ điều hành hiện tại
        Path path = Paths.get("config", "app.properties");
        
        // LỖI SAI: So sánh với một chuỗi bị đóng cứng theo kiểu Windows (\)
        // Dòng này sẽ PASS trên Windows (vì path.toString() ra config\app.properties)
        // Nhưng sẽ FAILED trên Linux/Mac (vì path.toString() ra config/app.properties)
        assertEquals("config\\app.properties", path.toString());
    }
}
