package org.xxz.test;

import com.google.common.io.ByteSink;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class Tests {

    @Test
    public void test1() {
        ByteSink sink = Files.asByteSink(new File(System.getProperty("user.dir") + "/tmp.txt"), FileWriteMode.APPEND);
        byte[] bytes = new byte[1024];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 1;
        }
        int size = 40;
        for (int i = 0; i < size * 1024; i++) {
            try {
                sink.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end...");
    }

}
