package helperTest;

import helperClass.TimeGetter;
import org.junit.Test;

/**
 * Created by admin on 2020/3/25.
 */
public class TimeGetterTest {

    @Test
    public void getTimeTest(){
        System.out.println(TimeGetter.getTime());
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(TimeGetter.getTime());
    }

}
