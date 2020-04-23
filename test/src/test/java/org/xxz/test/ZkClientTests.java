package org.xxz.test;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author jsbxyyx
 */
public class ZkClientTests {

    @Test
    public void test() {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000, 500, new ZkSerializer() {
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                return ((String) data).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        });
        Object o = zkClient.readData("/seata/store.db.url");
        System.out.println(o);
    }

}
