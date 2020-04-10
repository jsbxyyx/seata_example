import com.alibaba.fastjson.JSON;
import io.seata.common.util.StringUtils;
import io.seata.core.protocol.HeartbeatMessage;
import org.junit.Test;

/**
 * @author jsbxyyx
 */
public class StringUtilsTests {

    @Test
    public void toStringTest() throws Exception {
        HeartbeatMessage message = HeartbeatMessage.PING;
//        StringUtils.toString(message);

        String s = JSON.toJSONString(message);
        System.out.println(s);
    }

    public class AClass {

        private AClass a = new AClass();

        private AClass b = new AClass();


        public String toString() {

            return StringUtils.toString(this);
        }
    }
}
