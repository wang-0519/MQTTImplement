package messageHandler;

import client.HelpMess;

import java.util.Observable;

/**
 * Created by admin on 2020/3/27.
 */
public class MessageObservable extends Observable {

    private static MessageObservable instance = new MessageObservable();

    private MessageObservable(){

    }

    public static MessageObservable getInstance(){
        return instance;
    }

    public void addObserver(AbstractMessageObserver observer){
        getInstance().add(observer);
    }

    private void add(AbstractMessageObserver observer){
        super.addObserver(observer);
    }

    public void notifyObserver(HelpMess helpMess){
        setChanged();
        notifyObservers(helpMess);
    }
}
