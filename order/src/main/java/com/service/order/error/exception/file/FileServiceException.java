package com.service.order.error.exception.file;

import java.io.IOException;

public class FileServiceException extends IOException {
    public FileServiceException(String error) {
        super(error);
    }
}
