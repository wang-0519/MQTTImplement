package helperTest;

import helperClass.BytesHandler;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by admin on 2020/2/7.
 */
public class ArrayConnectTest {

    /**
     * 多数组连接
     */
    @Test
    public void connAllTest(){
        byte[] a = {1,0,0,0,0,10};
        byte[] b = {1,0,0,0,0,1};
        byte[] c = {1,0,0,0,1,0};
        byte[] d = BytesHandler.connAll(a,b);
        System.out.println(Arrays.toString(d));
        byte[] e = BytesHandler.connAll(a,b,c);
        System.out.println(Arrays.toString(e));
        byte[] f = BytesHandler.connAll(d,c);
        System.out.println(Arrays.toString(f));
    }
}
