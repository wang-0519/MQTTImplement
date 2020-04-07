package helperTest;

import helperClass.BytesHandler;
import helperClass.Translater;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by admin on 2020/2/12.
 */
public class BytesTest {

    @Test
    public void f(){
        byte[] b = null;
        if(b != null){
            System.out.println(b.length + " ");
        }
        byte[] typeOfMess = {0};
        byte a = Byte.parseByte("-1111111",2);
        int k = typeOfMess[0] | a;
        typeOfMess = Translater.intToBin(k, 8);

        for(int i = 0; i < typeOfMess.length; i++){
            System.out.print((int)typeOfMess[i] + " ");
        }
    }

    /**
     * 或运算测试
     * 返回byte[] 数组
     * 方便连接
     */
    @Test
    public void orTest(){
        byte[] sign = {0};
        byte[] bytes = BytesHandler.or(sign,"10000010");
        System.out.println((int)bytes[0] + " ");
    }


    /**
     *  -1 = 1 1111111
     *  -2 =  1 0000010 => 1 1111110
     *  -4 = 1 0000100  => 1 1111100
     *  1 0000010 => 1 0000001  => 1 1111110  -126
     *  -63   1 0111111   => 1 1000001
     *  00001000
     */
    @Test
    public void byteTest01(){
        byte a = -1;
        if(Byte.toUnsignedInt(a) >= 128){
            System.out.println("+++++++" + Byte.toUnsignedInt(a));
        }
        byte by = BytesHandler.and(a,"01111111");
        by >>= 4;
        by = BytesHandler.or(by, "00001000");
        System.out.println(by + " ");
        a =(byte) (a >> 1);
        System.out.println(a + " ");
//        byte a = Byte.parseByte("-0000000",2);
//        System.out.print((int)a + " ");
//        byte b = Byte.parseByte("10000000",2);
//        System.out.print((int)b + " ");
//        byte c = Byte.parseByte("-0000001",2);
//        System.out.print((int)c + " ");
//        byte d = Byte.parseByte("10000001",2);
//        System.out.print((int)d + " ");
    }


    /**
     * 编辑剩余长度测试
     */
    @Test
    public void editRemainLenTest(){
        byte[] bytes = BytesHandler.editRemainLen(268435455);
        System.out.println(bytes.length + " ");
        for( int i = 0; i < bytes.length; i++){
            System.out.print((int)bytes[i] + " ");
        }
        System.out.println("\n" + BytesHandler.analysisRemainLen(bytes));
    }
}
