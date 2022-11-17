package com.service.product;

import com.service.product.error.exception.file.FileServiceException;
import com.service.product.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileUtilTest {
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
