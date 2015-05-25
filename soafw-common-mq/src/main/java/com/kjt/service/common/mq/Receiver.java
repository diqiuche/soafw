package com.kjt.service.common.mq;


public abstract class Receiver extends MQClient implements IReceiver {

    public Receiver(String id) {
        super(id);
    }

}
