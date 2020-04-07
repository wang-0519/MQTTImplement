package helperClass;

import java.util.Arrays;

/**
 * Created by admin on 2020/2/12.
 */
public class BytesHandler {

    //bytes数组连接
    public static byte[] connAll(byte[]... arrays){
        int totalLength = 0;
        for(byte[] arr : arrays){
            if(arr != null){
                totalLength += arr.length;
            }
        }
        byte[] result = new byte[totalLength];
        int sign = 0;
        for(byte[] arr : arrays){
            if(arr != null){
                System.arraycopy(arr,0,result,sign,arr.length);
                sign += arr.length;
            }
        }
        return result;
    }


    /**
     * bytes 数组或运算
     * 将某一位设置为1
     * -128表示为 10000000
     * @param bytes
     * @param str
     * @return
     */
    public static byte[] or(byte[] bytes, String str){
        if(str.charAt(0) == '1'){
            String temp = new String(str.substring(1,str.length()));
            byte a = (byte)((Byte.parseByte(temp,2)) - 128);
            return Translater.intToBin(bytes[0] | a, 8);
        }
        return Translater.intToBin((bytes[0] | Byte.parseByte(str,2)), 8);
    }

    /**
     * byte 或运算,传入byte与或的字符串，返回byte
     * @param by
     * @param str
     * @return
     */
    public static byte or(byte by, String str){
        if(str.charAt(0) == '1'){
            String temp = new String(str.substring(1,str.length()));
            byte a = (byte)((Byte.parseByte(temp,2)) - 128);
            return (byte)(by | a);
        }
        return (byte)(by | Byte.parseByte(str,2));
    }

    /**
     * bytes 与运算
     * 将某一位设置为0
     * @param bytes
     * @param str
     * @return
     */
    public static byte and(byte bytes, String str){
        if(str.charAt(0) == '1'){
            String temp = new String(str.substring(1,str.length()));
            byte a = (byte)((Byte.parseByte(temp,2)) - 128);
            return Translater.intToBin(bytes & a, 8)[0];
        }
        return Translater.intToBin((bytes & Byte.parseByte(str,2)), 8)[0];
    }


    /**
     * 带有字符长度的字符串转换
     * @param str
     * @return
     */
    public static byte[] editStrWithLen(String str){
        byte[] temp = Translater.strToBin(str);
        return connAll(Translater.intToBin(temp.length),temp);
    }

    /**
     * 编辑固定报头中的剩余长度
     * @param num
     * @return
     */
    public static byte[] editRemainLen(int num){
        byte[] bytes = new byte[4];
        int count = 0;
        if(num <= 268435455){
            try{
                while(num > 0){
                    bytes[count] = (byte)(num % 128);
                    num /= 128;
                    if(num > 0){
                        bytes[count] = or(bytes[count], "10000000");
                        count++;
                    }
                }
            }catch (Exception e){
                System.out.print("BytesHandler.editRemainLen ==>");
                e.printStackTrace();
            }
        }
        return Arrays.copyOf(bytes, count + 1 );
    }

    /**
     * 解码剩余长度
     * @param bytes
     * @return
     */
    public static int analysisRemainLen(byte[] bytes){
        int i = bytes.length - 1;
        int sum = 0;
        while(i >= 0){
            sum *= 128;
            bytes[i] = and(bytes[i],"01111111");
            sum += bytes[i--];
        }
        return sum;
    }

    /**
     * 计算报文类型
     * @param by
     * @return
     */
    public static int getTypeOfMessage(byte by){
        if(by < 0){
            by = BytesHandler.and(by,"01111111");
            by >>= 4;
            by = BytesHandler.or(by, "00001000");
        } else {
            by >>= 4;
        }
        return by;
    }
}
