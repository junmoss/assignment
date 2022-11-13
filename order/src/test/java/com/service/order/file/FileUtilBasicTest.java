package com.service.order.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

@SpringBootTest
public class FileUtilBasicTest {
    private void writeText(byte[] bytes) throws Exception {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(getDataFilePath() + "/test/sample.txt"))) {
            dataOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 삭제 or 수정 메소드
    private String updateText(int offset, int length, String targetStr) throws Exception {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/test/sample.txt"))) {
            byte[] prevBytes = new byte[offset];
            byte[] postBytes = new byte[targetStr.length()];
            StringBuilder stringBuilder = new StringBuilder();

            if (offset > 0) {
                dataInputStream.read(prevBytes, 0, offset);
                stringBuilder.append(new String(prevBytes));
            }

            if (length > 0) {
                dataInputStream.skip(length);
            }

            stringBuilder.append(targetStr);

            if (dataInputStream.available() > 0) {
                dataInputStream.read(postBytes, 0, targetStr.length());
                stringBuilder.append(new String(postBytes));
            }

            writeText(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @BeforeEach
    public void fileResetTest() throws Exception {
        writeText("".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void fileFullReadTest() throws Exception {
        String str1 = "hello world~";
        String str2 = "nice to meet you~";

        writeText((str1 + str2).getBytes(StandardCharsets.UTF_8));

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(getDataFilePath() + "/test/sample.txt"))) {
            byte[] bytes = new byte[str1.length()];
            dataInputStream.read(bytes, 0, str1.length());
            Assertions.assertEquals(new String(bytes), str1);

            byte[] bytes1 = new byte[str2.length()];
            dataInputStream.read(bytes1, 0, str2.length());
            Assertions.assertEquals(new String(bytes1), str2);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void updateTextTest1() throws Exception {
        String str1 = "hello world~"; // 대체 문자열 좌측의 모든 문자열
        String str2 = "nice to meet you~"; // 대체될 문자열
        String str3 = "hello worl~~~"; // 삽입될 문자열

        // 우측 문자열을 삽입 문자열로 대체
        writeText((str1 + str2).getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(updateText(str1.length(), str2.length(), str3), str1 + str3);
    }

    @Test
    public void updateTextTest2() throws Exception {
        String str1 = "test1okkk"; // 변경 대상 문자열
        String str3 = "so what?"; // 대체 문자열

        // 좌측 문자열을 삽입 문자열로 대체
        writeText((str1).getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(updateText(0, str1.length(), str3), str3);
    }

    private String getDataFilePath() {
        return FileSystems.getDefault().getPath("data").toAbsolutePath().toString();
    }
}
