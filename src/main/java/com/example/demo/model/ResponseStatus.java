package com.example.demo.model;

public class ResponseStatus {
    public int status;
    public String message;
    Object data;

    public ResponseStatus(int status,String message){
        this.status=status;
        this.message=message;
        this.data= null;
    }

    public ResponseStatus(int status,String message,Object data){
        this.status=status;
        this.message=message;
        this.data= data;
    }
}
