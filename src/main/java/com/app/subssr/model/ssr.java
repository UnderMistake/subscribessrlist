package com.app.subssr.model;

public class ssr {
    protected long d;
    protected int enable;
    protected String forbidden_port;
    protected String method;

    public String getMethod(){
        return this.method;
    }
    protected String obfs;

    public String getObfs(){
        return this.obfs;
    }
    protected String passwd;

    public String getPasswd(){
        return this.passwd;
    }
    protected int port;

    public int getPort(){
        return this.port;
    }
    protected String protocol;

    public String getProtocol(){
        return this.protocol;
    }
    protected String protocol_param;

    public String getProtocol_param(){
        return this.protocol_param;
    }
    protected int speed_limit_per_con;
    protected int speed_limit_per_user;
    protected long transfer_enable;
    protected int u;
    protected String user;

    public String getUser(){
        return this.user;
    }

}
