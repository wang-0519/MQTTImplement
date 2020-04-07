package helperTest;

import helperClass.PackageIdentify;
import org.junit.Test;

/**
 * Created by admin on 2020/2/5.
 */
public class PackageIdentifyTest {

    /**
     * 获取报文标识符
     */
    @Test
    public void packTest(){
        byte[] identify = PackageIdentify.getIdentify();
        for(int i = 0; i < identify.length; i++){
            System.out.print((int)identify[i] + " ");
        }
        System.out.println();
        identify = PackageIdentify.getIdentify();
        for(int i = 0; i < identify.length; i++){
            System.out.print((int)identify[i] + " ");
        }
        System.out.println();
    }
}
