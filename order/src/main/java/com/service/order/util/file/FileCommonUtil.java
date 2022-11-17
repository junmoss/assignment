package com.service.order.util.file;

import com.service.order.entity.file.IndexFile;
import com.service.order.entity.file.OrderFile;
import com.service.order.error.exception.file.FileServiceException;
import com.service.order.util.gson.GsonUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class FileCommonUtil {
    protected void deleteOrderData(IndexFile index) throws FileServiceException {
        if (index == null) {
            return;
        }

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "order.txt"))) {
            byte[] prevBytes = new byte[index.getOffset()];

            if (prevBytes.length > 0) {
                dataInputStream.read(prevBytes);
            }

            dataInputStream.skip(index.getLength());

            byte[] postBytes = new byte[dataInputStream.available()];

            if (postBytes.length > 0) {
                dataInputStream.read(postBytes);
            }

            StringBuilder stringBuilder = new StringBuilder(
                    (prevBytes.length > 0 ? new String(prevBytes) : "") + (postBytes.length > 0 ? new String(postBytes) : "")
            );
            writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "order.txt");
        } catch (Exception e) {
            throw new FileServiceException("주문데이터 삭제에 실패하였습니다.");
        }
    }

    protected IndexFile deleteIndexData(long orderId) throws Exception {
        String[] parsed = readAllText("index.txt");
        StringBuilder stringBuilder = new StringBuilder();
        IndexFile result = null;

        for (int i = 0; i < parsed.length; i++) {
            IndexFile index = GsonUtil.parseStrToObj(parsed[i], IndexFile.class);

            if (orderId == index.getOrderId()) {
                IndexFile prevIndex = null;
                IndexFile postIndex = null;

                if (i - 1 >= 0) {
                    prevIndex = GsonUtil.parseStrToObj(parsed[i - 1], IndexFile.class);
                }

                if (i + 1 < parsed.length) {
                    postIndex = GsonUtil.parseStrToObj(parsed[i + 1], IndexFile.class);
                }

                if (prevIndex == null && postIndex != null) {
                    postIndex.setOffset(0);
                    parsed[i + 1] = GsonUtil.parseObjToStr(postIndex);
                } else if (prevIndex != null && postIndex != null) {
                    postIndex.setOffset(prevIndex.getOffset() + prevIndex.getLength());
                    parsed[i + 1] = GsonUtil.parseObjToStr(postIndex);
                }

                for (int j = i + 2; j < parsed.length; j++) {
                    if (j - 1 >= 0) {
                        prevIndex = GsonUtil.parseStrToObj(parsed[j - 1], IndexFile.class);
                        index = GsonUtil.parseStrToObj(parsed[j], IndexFile.class);
                        index.setOffset(prevIndex.getOffset() + prevIndex.getLength());
                        parsed[j] = GsonUtil.parseObjToStr(index);
                    }
                }
                break;
            }
        }

        for (String parse : parsed) {
            IndexFile index = GsonUtil.parseStrToObj(parse, IndexFile.class);

            if (index.getOrderId() == orderId) {
                result = index;
                continue;
            }
            stringBuilder.append(parse + "\n");
        }
        writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "index.txt");
        return result;
    }

    protected void updateIndexDataText(long orderId, IndexFile repIndex) throws Exception {
        String[] parsed = readAllText("index.txt");
        StringBuilder stringBuilder = new StringBuilder();
        int prevOffset = 0;
        int prevLength = 0;

        for (String parse : parsed) {
            IndexFile index = GsonUtil.parseStrToObj(parse, IndexFile.class);

            if (index.getOrderId() == orderId) {
                prevOffset = repIndex.getOffset();
                prevLength = repIndex.getLength();
                String repIndexStr = GsonUtil.parseObjToStr(repIndex) + "\n";
                stringBuilder.append(repIndexStr);
            } else {
                if (prevOffset != 0 || prevLength != 0) {
                    index.setOffset(prevOffset + prevLength);
                    prevOffset = index.getOffset();
                    prevLength = index.getLength();
                }
                stringBuilder.append(GsonUtil.parseObjToStr(index) + "\n");
            }
        }
        writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "index.txt");
    }

    protected void updateOrderDataText(int offset, int length, String targetStr) throws FileServiceException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "order.txt"))) {
            byte[] prevBytes = new byte[offset];
            StringBuilder stringBuilder = new StringBuilder();

            if (offset > 0) {
                dataInputStream.read(prevBytes);
                stringBuilder.append(new String(prevBytes));
            }

            if (length > 0) {
                dataInputStream.skip(length);
            }

            stringBuilder.append(targetStr);

            if (dataInputStream.available() > 0) {
                byte[] postBytes = new byte[dataInputStream.available()];
                dataInputStream.read(postBytes);
                stringBuilder.append(new String(postBytes));
            }
            writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), "order.txt");
        } catch (Exception e) {
            throw new FileServiceException("주문데이터 수정 작업에 실패하였습니다.");
        }
    }

    protected List<IndexFile> readIndexPagingById(int page, int size) throws Exception {
        String[] parsed = readAllText("index.txt");
        List<IndexFile> indexList = new ArrayList<>();

        for (int i = (page * size), count = 0; count < size; count++, i++) {
            if (i >= parsed.length) {
                break;
            }
            indexList.add(GsonUtil.parseStrToObj(parsed[i], IndexFile.class));
        }
        return indexList;
    }

    protected OrderFile readOrderDataByIndex(IndexFile index) throws Exception {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "order.txt"))) {
            dataInputStream.skip(index.getOffset());
            byte[] bytes = new byte[index.getLength()];
            dataInputStream.read(bytes);
            OrderFile orderData = GsonUtil.parseStrToObj(new String(bytes), OrderFile.class);
            if (orderData.getOrderId() == index.getOrderId()) {
                return orderData;
            } else {
                throw new FileServiceException("주문 정보가 존재하지 않습니다.");
            }
        } catch (FileServiceException e) {
            throw e;
        }
    }

    protected IndexFile readIndexDataById(long orderId) throws FileServiceException {
        for (String parse : readAllText("index.txt")) {
            IndexFile index = GsonUtil.parseStrToObj(parse, IndexFile.class);
            if (index.getOrderId() == orderId) {
                return index;
            }
        }
        throw new FileServiceException("주문관련 인덱스 정보가 존재하지 않습니다.");
    }

    protected String[] readAllText(String textFile) throws FileServiceException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + textFile))) {
            if (dataInputStream.available() == 0) {
                return new String[0];
            }
            byte[] bytes = new byte[dataInputStream.available()];
            dataInputStream.read(bytes);
            return (new String(bytes)).split("\n");
        } catch (Exception e) {
            throw new FileServiceException("파일에서 데이터를 읽어오는데 실패하였습니다.");
        }
    }

    protected int writeOrderText(String orderJsonStr) throws FileServiceException {
        int result = 0;

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "order.txt"))) {
            result = dataInputStream.available();
            byte[] bytes = new byte[result];
            dataInputStream.read(bytes);
            String str = bytes.length > 0 ? (new String(bytes) + orderJsonStr) : orderJsonStr;
            writeText(str.getBytes(StandardCharsets.UTF_8), "order.txt");
        } catch (Exception e) {
            throw new FileServiceException("주문 데이터 파일 저장 중에 실패하였습니다.");
        }
        return result;
    }

    protected void writeIndexText(String indexJsonStr) throws FileServiceException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/dev/" + "index.txt"))) {
            byte[] bytes = new byte[dataInputStream.available()];
            dataInputStream.read(bytes);
            String str = bytes.length > 0 ? (new String(bytes) + indexJsonStr) : indexJsonStr;
            writeText(str.getBytes(StandardCharsets.UTF_8), "index.txt");
        } catch (Exception e) {
            throw new FileServiceException("주문 데이터 파일 저장 중에 실패하였습니다.");
        }
    }

    protected void writeText(byte[] bytes, String destFile) throws FileServiceException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(getDataFilePath() + "/dev/" + destFile))) {
            dataOutputStream.write(bytes);
        } catch (Exception e) {
            throw new FileServiceException("파일에 데이터를 저장 중에 실패하였습니다.");
        }
    }

    protected void writeText(byte[] bytes, String dir, String destFile) throws FileServiceException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(getDataFilePath() + dir + destFile))) {
            dataOutputStream.write(bytes);
        } catch (Exception e) {
            throw new FileServiceException("파일에 데이터를 저장 중에 실패하였습니다.");
        }
    }

    protected long getNextOrderId() throws FileServiceException {
        String[] parse = readAllText("index.txt");

        if (parse.length <= 0)
            return 1L;
        else
            return GsonUtil.parseStrToObj(parse[parse.length - 1], IndexFile.class).getOrderId() + 1;
    }

    protected byte[] getFileBytes(String dir, String file) throws FileServiceException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + dir + file))) {
            byte[] bytes = new byte[dataInputStream.available()];
            dataInputStream.read(bytes);
            return bytes;
        } catch (Exception e) {
            throw new FileServiceException("파일을 읽지 못햇습니다.");
        }
    }

    protected String getDataFilePath() {
        return FileSystems.getDefault().getPath("data").toAbsolutePath().toString();
    }
}
