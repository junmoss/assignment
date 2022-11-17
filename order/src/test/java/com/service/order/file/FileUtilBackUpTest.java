package com.service.order.file;

import com.service.order.error.exception.file.FileServiceException;
import com.service.order.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileUtilBackUpTest {
    @Autowired
    FileService fileService;

    @Test
    public void writeText() throws FileServiceException {
        fileService.writeBackUpFile();
    }

    @Test
    public void restoreTest() throws FileServiceException {
        fileService.restoreBackUpFile();
    }
}
