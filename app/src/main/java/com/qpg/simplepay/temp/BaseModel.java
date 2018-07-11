package com.qpg.simplepay.temp;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/5/9.
 */

public class BaseModel<T> {

    private String re;
    private String info;
    private T data;

    public String getRe() {
        return re;
    }

    public String getInfo() {
        return info;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "re='" + re + '\'' +
                ", info='" + info + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
