package com.service.order.util.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;

@Component
@RequiredArgsConstructor
public class FileUtil {

    private String getDataFilePath() {
        return FileSystems.getDefault().getPath("data").toAbsolutePath().toString();
    }
}
