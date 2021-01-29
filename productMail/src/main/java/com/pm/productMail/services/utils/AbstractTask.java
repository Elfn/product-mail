package com.pm.productMail.services.utils;

public abstract class AbstractTask implements Runnable {
    @Override
    public void run() {
        try{
            this.send();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public abstract void send();
}
