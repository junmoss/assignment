package com.service.product.util.file;

import com.service.product.entity.file.IndexFile;
import com.service.product.entity.file.ProductFile;
import com.service.product.error.exception.file.FileServiceException;
import com.service.product.input.ProductInput;
import com.service.product.util.Util;
import com.service.product.util.gson.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUtil extends FileCommonUtil {
    public long saveProductData(ProductInput productInput) throws FileServiceException {
        ProductFile productFile = ProductFile.from(productInput);
        productFile.setProductId(getNextProductId());

        String productJsonStr = GsonUtil.parseObjToStr(productFile);
        long productId = productFile.getProductId();
        int offset = writeProductText(productJsonStr);
        int length = productJsonStr.getBytes(StandardCharsets.UTF_8).length;

        writeIndexText(GsonUtil.parseObjToStr(
                IndexFile.builder()
                        .productId(productId)
                        .offset(offset)
                        .length(length)
                        .build()) + "\n");
        return productId;
    }

    public long saveProductData(ProductFile productFile) throws Exception {
        IndexFile index = readIndexDataById(productFile.getProductId());
        String productJsonStr = GsonUtil.parseObjToStr(productFile);
        IndexFile repIndex = IndexFile.builder()
                .productId(productFile.getProductId())
                .offset(index.getOffset())
                .length(productJsonStr.getBytes(StandardCharsets.UTF_8).length)
                .build();

        updateIndexDataText(repIndex.getProductId(), repIndex);
        updateProductDataText(repIndex.getOffset(), index.getLength(), productJsonStr);
        return productFile.getProductId();
    }

    public long updateProductData(ProductInput productInput) throws Exception {
        IndexFile index = readIndexDataById(productInput.getId());
        ProductFile updateProductData = readProductDataByIndex(index);

        updateProductData.setCount(productInput.getCount());
        updateProductData.setDescription(productInput.getDescription());
        updateProductData.setPrice(productInput.getPrice());
        updateProductData.setName(productInput.getName());
        updateProductData.setUpdatedTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()));

        IndexFile repIndex = IndexFile.builder()
                .productId(updateProductData.getProductId())
                .offset(index.getOffset())
                .length(GsonUtil.parseObjToStr(updateProductData).getBytes(StandardCharsets.UTF_8).length)
                .build();

        updateIndexDataText(repIndex.getProductId(), repIndex);
        updateProductDataText(repIndex.getOffset(), index.getLength(), GsonUtil.parseObjToStr(updateProductData));
        return updateProductData.getProductId();
    }

    public long deleteProductData(long productId) throws Exception {
        IndexFile deleteIndex = deleteIndexData(productId);
        deleteProductData(deleteIndex);
        return productId;
    }

    public List<ProductFile> findTotalProductFile() throws Exception {
        String[] parsed = readAllText("index.txt");
        List<ProductFile> productFiles = new ArrayList<>();

        for (String parse : parsed) {
            productFiles.add(readProductDataByIndex(GsonUtil.parseStrToObj(parse, IndexFile.class)));
        }
        return productFiles;
    }

    public ProductFile findProductFileById(long productId) throws Exception {
        return readProductDataByIndex(readIndexDataById(productId));
    }

    public List<ProductFile> findPagingProductFiles(int page, int size) throws Exception {
        List<IndexFile> indexList = readIndexPagingById(page, size);
        List<ProductFile> productFiles = new ArrayList<>();

        for (IndexFile index : indexList) {
            productFiles.add(readProductDataByIndex(index));
        }
        return productFiles;
    }
}
