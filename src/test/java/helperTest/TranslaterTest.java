package helperTest;

import helperClass.Translater;
import org.junit.Test;
import sun.awt.AWTCharset;

import java.beans.Encoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;

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
        try{
            String str = "你好";
            byte[] bytes = Translater.strToBin(str);
            for(int i = 0; i < bytes.length; i++){
                System.out.print(bytes[i] + " ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        byte[] bys = {-28,-67,-96,-27,-91,-67};
        System.out.println("\n" + Translater.binToString(bys));
    }



    @Test
    public void byteToHexTest(){
        String str = "FF AA";
        str = str.replaceAll(" ", "");
        byte[] bytes = Translater.hexTobytes(str);
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
        for(byte by : bytes){
            System.out.print(Translater.byteToHex(by) + " ");
        }
    }

    @Test
    public void translaterTest(){
        String hexString = "A0B1";
        byte[] bytes = Translater.hexTobytes(hexString);
        System.out.println("Translater.hexTobytes:" + Arrays.toString(bytes));
        StringBuilder sb = new StringBuilder();
        for(byte by : bytes){
            sb.append((char)by);
        }
        String utfStr = null;
        try{
            utfStr = URLEncoder.encode(sb.toString(), "utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        byte[] bys = Translater.strToBin(utfStr);
        String binString = "";
        for(byte by : bys){
            binString += (Translater.byteToBin(by) + " ");
        }
        System.out.println("Translater.byteToBin:" + binString);
        String str = Translater.binToString(bys);
        System.out.println("Translater.binToString:" + str);
        byte[] bys1 = Translater.strToBin(str);
        System.out.println("Translater.strToBin:" + Arrays.toString(bys));
    }
}
