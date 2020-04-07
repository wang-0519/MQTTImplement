package helperClass;

/**
 * Created by admin on 2020/2/5.
 */
public class PackageIdentify {

    //报文标识符 16位
    private static byte[] identify =  new byte[2];

    //获取报文标识符
    public static byte[] getIdentify(){
        addIdentify();
        return identify;
    }
//
//
//    private static void addIdentify(){
//        int i = 15;
//        while(i >= 0 && identify[i] == 1){
//            identify[i] = 0;
//            i--;
//        }
//        if(i >= 0){
//            identify[i] = 1;
//        }
//    }

    private static int n = 0;

    private static void addIdentify(){
        n = ++n >= 65536 ? 0: n;
        identify = Translater.intToBin(n);
    }
}
