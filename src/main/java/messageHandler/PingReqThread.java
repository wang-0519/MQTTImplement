package messageHandler;

import MQTTMessage.AbstractMess;
import MQTTMessage.impl.PingreqMessage;
import client.ClientInformation;
import client.HelpMess;

/**
 * Created by admin on 2020/4/8.
 */
public class PingReqThread implements Runnable {

    private boolean sign = true;

    private int keepAliveTime = 8;

    private SendMessageThread send = null;

    private ClientInformation ci = null;

    public PingReqThread(SendMessageThread thread, ClientInformation clientInformation){
        this.keepAliveTime = clientInformation.getKeepAlive() - 2;
        send = thread;
        this.ci = clientInformation;
    }

    @Override
    public void run() {
        try{
            AbstractMess message = null;
            Thread.currentThread().sleep(keepAliveTime*1000);
            while(sign){
                message = new PingreqMessage();
                if(ci.getState() == ClientInformation.CONN_STATE.CONN){
                    send.send(message);
                }
                Thread.currentThread().sleep(keepAliveTime*1000);
                if(send.contains(message)){
                    sign = false;
                    HelpMess helpMess = new HelpMess();
                    helpMess.setType(HelpMess.HELP_MESS_TYPE.ERROR);
                    helpMess.setId(ci.getId());
                    helpMess.setErrorMessage("网络错误!");
                    MessageObservable.getInstance().notifyObserver(helpMess);
                    ci.setState(ClientInformation.CONN_STATE.CONN_ERROR);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void close(){
        sign = false;
    }
}
