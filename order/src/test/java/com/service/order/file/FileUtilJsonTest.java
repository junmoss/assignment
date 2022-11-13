package com.service.order.file;

import com.service.order.file.entity.Index;
import com.service.order.file.entity.OrderData;
import com.service.order.util.gson.GsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class FileUtilJsonTest {
    // json, index/data 2개 파일을 통한 관리
    private String getDataFilePath() {
        return FileSystems.getDefault().getPath("data").toAbsolutePath().toString();
    }

    private void writeText(byte[] bytes, String textFile) throws Exception {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(getDataFilePath() + "/dev/" + textFile))) {
            dataOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int writeOrderText(String orderJsonStr, String textFile) {
        int result = 0;

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + textFile))) {
            result = dataInputStream.available();
            byte[] bytes = new byte[result];
            dataInputStream.read(bytes);
            String str = bytes.length > 0 ? (new String(bytes) + orderJsonStr) : orderJsonStr;
            writeText(str.getBytes(StandardCharsets.UTF_8), textFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void writeIndexText(String indexJsonStr, String textFile) {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + textFile))) {
            byte[] bytes = new byte[dataInputStream.available()];
            dataInputStream.read(bytes);
            String str = bytes.length > 0 ? (new String(bytes) + indexJsonStr) : indexJsonStr;
            writeText(str.getBytes(StandardCharsets.UTF_8), textFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeOrderDataIndexText(OrderData orderData) {
        String orderJsonStr = GsonUtil.parseObjToStr(orderData);
        int orderId = orderData.getOrderId();
        int offset = writeOrderText(orderJsonStr, "order.txt");
        int length = orderJsonStr.getBytes(StandardCharsets.UTF_8).length;

        writeIndexText(GsonUtil.parseObjToStr(
                Index.builder()
                        .orderId(orderId)
                        .offset(offset)
                        .length(length)
                        .build()) + "\n", "index.txt");
    }

    private String readAllText(String textFile) {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + textFile))) {
            if (dataInputStream.available() == 0) {
                throw new IOException("파일이 비어있습니다.");
            }
            byte[] bytes = new byte[dataInputStream.available()];
            dataInputStream.read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private OrderData readOrderDataByIndex(Index index) {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "order.txt"))) {
            dataInputStream.skip(index.getOffset());
            byte[] bytes = new byte[index.getLength()];
            dataInputStream.read(bytes);
            OrderData orderData = GsonUtil.parseStrToObj(new String(bytes), OrderData.class);
            if (orderData.getOrderId() == index.getOrderId()) {
                return orderData;
            } else {
                throw new RuntimeException("id가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 or null return
            return null;
        }
    }

    private Index readIndexById(long orderId) {
        for (String parse : readAllText("index.txt").split("\n")) {
            Index index = GsonUtil.parseStrToObj(parse, Index.class);
            if (index.getOrderId() == orderId) {
                return index;
            }
        }
        // 예외 발생 or null return
        return null;
    }

    private List<Index> readIndexPagingById(int page, int size) {
        String[] parsed = readAllText("index.txt").split("\n");
        List<Index> indexList = new ArrayList<>();

        for (int i = (page * size), count = 0; count < size; count++, i++) {
            if (i >= parsed.length) {
                break;
            }
            indexList.add(GsonUtil.parseStrToObj(parsed[i], Index.class));
        }
        return indexList;
    }

    @BeforeEach
    public void fileResetTest() throws Exception {
        writeText("".getBytes(StandardCharsets.UTF_8), "index.txt");
        writeText("".getBytes(StandardCharsets.UTF_8), "order.txt");
    }

    @Test
    public void readAndWriteIndexTest() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((GsonUtil.parseObjToStr(Index.builder().orderId(1).length(15).offset(0).build()) + "\n"));
        stringBuilder.append((GsonUtil.parseObjToStr(Index.builder().orderId(2).length(18).offset(95).build()) + "\n"));
        stringBuilder.append((GsonUtil.parseObjToStr(Index.builder().orderId(3).length(125434).offset(545343).build()) + "\n"));
        writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "index.txt");
        String result = readAllText("index.txt");
        String[] parsed = result.split("\n");
        for (String parse : parsed) {
            Index index = GsonUtil.parseStrToObj(parse, Index.class);
            Assertions.assertNotNull(index);
        }
        Assertions.assertNotNull(result);
    }

    @Test
    public void readAndWriteOrderDataTest() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GsonUtil.parseObjToStr(OrderData.builder().orderId(1).name("강준모")
                .address("서울특별시 강서구 등촌동 631-21").request("종이컵 주세요").productKey(1).build()));
        stringBuilder.append(
                GsonUtil.parseObjToStr(
                        OrderData.builder().orderId(2).name("양철수")
                                .address("충북 청주시 홍덕동").request("치킨주세요").productKey(2).build()));
        stringBuilder.append(
                GsonUtil.parseObjToStr(
                        OrderData.builder().orderId(3).name("이하연")
                                .address("부산 서면").request("시끄러워요. 조용히 좀 해주세요.").productKey(3).build()));
        writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "order.txt");
        String result = readAllText("order.txt");
        Assertions.assertNotNull(result);
    }

    @Test
    public void readIndexOrderDataTest() throws Exception {
        // order, index 반환
        // order, index paging 반환
        IndexAndOrderReadAndWriteTest();

        OrderData orderData = readOrderDataByIndex(readIndexById(1));
        Assertions.assertNotNull(orderData);

        orderData = readOrderDataByIndex(readIndexById(4));
        Assertions.assertNotNull(orderData);

        orderData = readOrderDataByIndex(readIndexById(3));
        Assertions.assertNotNull(orderData);

        orderData = readOrderDataByIndex(readIndexById(1));
        Assertions.assertNotNull(orderData);

        orderData = readOrderDataByIndex(readIndexById(5));
        Assertions.assertNotNull(orderData);

        orderData = readOrderDataByIndex(readIndexById(2));
        Assertions.assertNotNull(orderData);
    }

    @Test
    public void readPagingIndexOrderDataTest() throws Exception {
        IndexAndOrderReadAndWriteTest();

        List<Index> indexList = readIndexPagingById(5, 5);
        List<OrderData> orderDataList = new ArrayList<>();

        for (Index index : indexList) {
            orderDataList.add(readOrderDataByIndex(index));
        }

        Assertions.assertEquals(orderDataList.size(), indexList.size());
    }

    @Test
    public void IndexAndOrderReadAndWriteTest() throws Exception {
        // order, index 정보 저장
        writeOrderDataIndexText(OrderData.builder().orderId(1).name("양철수")
                .address("충북 충주시 19전부비행단").request("584~ 랜딩합니다.").productKey(2).build());

        writeOrderDataIndexText(OrderData.builder().orderId(2).name("강준모")
                .address("서울 특별시 강서구 등촌동").request("치킨주세요").productKey(2).build());

        writeOrderDataIndexText(OrderData.builder().orderId(3).name("깅예솔")
                .address("충북 청주시 홍덕동").request("치킨주세요").productKey(3).build());

        writeOrderDataIndexText(OrderData.builder().orderId(4).name("하두링")
                .address("서울 특별시 강남구 테헤란구 1278-548").request("여기로 배달 한번 보내주세요. 피클은 빼구요.").productKey(5).build());

        writeOrderDataIndexText(OrderData.builder().orderId(5).name("호빵맨")
                .address("세균맨이 살고있지 않는 평화로운 행성").request("뜨끈하게 노릇노릇하게 잘 구워주세요.").productKey(1).build());
    }

    @Test
    public void IndexAndOrderUpdateTest() {
        // order, index 수정 및 삭제

    }

    @Test
    public void indexSaveTest() throws Exception {
        writeText((GsonUtil.parseObjToStr(Index.builder().orderId(1).length(15).offset(0).build()) + "\n").getBytes(StandardCharsets.UTF_8), "index.txt");
    }
}
