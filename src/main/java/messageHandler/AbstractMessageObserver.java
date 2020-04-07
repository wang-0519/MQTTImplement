package messageHandler;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by admin on 2020/3/10.
 */
public class AbstractMessageObserver implements Observer {

    //报文
    protected String message;

    @Override
    public void update(Observable o, Object arg) {
    }

}
