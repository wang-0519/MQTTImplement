package MQTTMessage.impl;

import MQTTMessage.AbstractMess;

/**
 * Created by admin on 2020/2/20.
 */
public class PingrespMessage extends AbstractMess {

    public PingrespMessage(byte[] bytes){
        uBytes = bytes;
    }

    @Override
    public boolean analysisMess() {
        super.analysisMess();
        return true;
    }
}
