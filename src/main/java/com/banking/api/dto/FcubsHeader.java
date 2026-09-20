package com.banking.api.dto;

import lombok.Data;

@Data
public class FcubsHeader {

    private String source;
    private String ubscomp;
    private String msgid;
    private String correlid;
    private String userid;
    private String entity;
    private String branch;
    private String moduleid;
    private String service;
    private String operation;
    private String sourceoperation;
    private String sourceuserid;
    private String destination;
    private String multitripid;
    private String functionid;
    private String action;
    private String msgstat;
    private String snapshotid;
    private String password;
    private Addl addl;
}
