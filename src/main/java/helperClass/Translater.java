package helperClass;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by admin on 2020/2/4.
 * 数据转化工具类
 */
public class Translater {

    /**
     * 将String字符串转化为UTF-8编码的byte数组
     * @param str
     * @return
     */
    static public byte[] strToBin(String str){
        byte[] bytes = null;
        try{
            String UTFstr = URLEncoder.encode(str,"UTF-8");
            char[] charSet = UTFstr.toCharArray();
            bytes = new byte[charSet.length];
            for(int i = 0; i < charSet.length; i++){
                System.arraycopy(intToBin((int)charSet[i],8), 0, bytes, i, 1);
            }
        }catch(Exception e){
            System.out.println("Translater.strToBin===>");
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 整数二进制编码  无符号
     * @param k   k 位二进制编码    k bit
     * @param n   数字
     * @return
     */
    static public byte[] intToBin(int n,int k){
        byte[] bytes = new byte[k/8];
        int i = k/8-1;
        while(i >= 0 && n != 0){
            int temp = n%256;
            n = n/256;
            if(temp < 128){
                bytes[i] = (byte)(temp);
            } else{
                bytes[i] = (byte)(temp - 256);
            }
            i--;
        }
        return bytes;
    }

    /**
     * 整数二进制编码   默认16位
     * @param n
     * @return
     */
    static public byte[] intToBin(int n){
        return intToBin(n,16);
    }


    /**
     * 将接收的二进制报文转化为String字符串
     * @param bytes
     * @return
     */
    static public String binToString(byte[] bytes){
//        if(bytes.length%8 != 0){
//            return null;
//        }
        String str = null;
        try{
            char[] charSet = new char[bytes.length];
            int i = 0;
            while(i < bytes.length){
                charSet[i] = (char)binToInt(Arrays.copyOfRange(bytes,i,i+1));
                i++;
            }
            str = URLDecoder.decode(String.valueOf(charSet),"UTF-8");
        }catch(Exception e){
            System.out.print("Translater.binToString===>");
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 二进制报文转化为无符号整数
     * @param bytes
     * @return
     */
    static public int binToInt(byte[] bytes){
        int sum = 0;
        int i = 0;
        while(i < bytes.length){
            sum *= 256;
            if(bytes[i] >= 0){
                sum += bytes[i];
            }else {
                sum += (bytes[i] + 256);
            }
            i++;
        }
        return sum;
    }

    /**
     * byte 转整数
     * @param by
     * @return
     */
    static public int binToInt(byte by){
        return by >= 0 ? by : by + 256;
    }

    /**
     * byte 数组转二进制 01 字符串
     */
    static public String byteToBin(byte ubyte){
        return Integer.toBinaryString((ubyte & 0xff) - 0x100).substring(24,32);
    }
}
