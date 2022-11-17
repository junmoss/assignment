package com.service.product.service.file;

import com.service.product.entity.file.ProductFile;
import com.service.product.error.exception.file.FileServiceException;
import com.service.product.input.ProductInput;
import com.service.product.util.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileUtil fileUtil;

    public void writeBackUpFile() throws FileServiceException{
        fileUtil.writeBackUpFile();
    }
    public void restoreBackUpFile() throws FileServiceException{
        fileUtil.restoreBackupFile();
    }

    public long saveProduct(ProductInput productInput) throws FileServiceException {
        return fileUtil.saveProductData(productInput);
    }

    public long saveProduct(ProductFile productFile) throws Exception {
        return fileUtil.saveProductData(productFile);
    }

    public long updateProduct(ProductInput productInput) throws Exception {
        return fileUtil.updateProductData(productInput);
    }

    public long deleteProduct(long productId) throws Exception {
        return fileUtil.deleteProductData(productId);
    }

    public ProductFile findProductById(long productId) throws Exception {
        return fileUtil.findProductFileById(productId);
    }

    public List<ProductFile> findTotalProduct() throws Exception {
        return fileUtil.findTotalProductFile();
    }

    public List<ProductFile> findProductPaging(int page, int size) throws Exception {
        return fileUtil.findPagingProductFiles(page, size);
    }
}
