package helperTest;

import helperClass.Translater;
import org.junit.Test;

/**
 * Created by admin on 2020/2/6.
 */
public class TranslaterTest {

    /**
     * 整数转化为byte[] 数组
     * byte[] 数组转化为整数
     * 无符号
     */
    @Test
    public void intToBytesTest(){
        //整数转换位二进制，8位
        byte[] bytes = Translater.intToBin(257,8);  //越界
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println("\n" + Translater.binToInt(bytes) + " ");

        //整数转换位二进制，16位
        bytes = Translater.intToBin(5);
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println("\n" + Translater.binToInt(bytes) + " ");

        //整数转换位二进制，24位
        bytes = Translater.intToBin(257,24);
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println("\n" + Translater.binToInt(bytes) + " ");
    }

    /**
     * 字符串转化为byte数组
     * byte数组转化为字符串
     * UTF-8编码方式
     */
    @Test
    public void strToBinTest(){
        String str = "hello";
        byte[] bytes = Translater.strToBin(str);
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println("\n" + Translater.binToString(bytes));
    }
}
