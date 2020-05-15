package messageHandler;

import MQTTMessage.AbstractMess;

/**
 * Created by admin on 2020/3/3.
 */
public class ReSendMessage implements Runnable {

    private AbstractMess mess = null;

    private SendMessageThread thread = null;

    private int timeOut = 0;
    private int waitTime = 0;

    public ReSendMessage(AbstractMess mess, SendMessageThread thread, int timeOut, int waitTime){
        this.thread = thread;
        this.mess = mess;
        this.timeOut = timeOut;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {
        try{
            int i = 0;
            Thread.currentThread().sleep(waitTime);
            while(thread.contains(mess) && i < timeOut/waitTime){
                Thread.currentThread().sleep(waitTime);
                thread.send(mess);
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
