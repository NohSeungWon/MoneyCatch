package com.example.usser.moneycatch;

public class Mainrecycleitem_minus {

    private int input_minus; // 입금값
    private String breakdown_minus; //내역
    private String cal_minus; // 달력
    private String url_minus; // 이미지
    private String time;
    private int number ; //

    public Mainrecycleitem_minus(int input_minus, String breakdown, String cal, String url_minus,String time){
        this.input_minus = input_minus;
        this.breakdown_minus = breakdown;
        this.cal_minus = cal;
        this.url_minus = url_minus;
        this.time = time;
    }

    public int getName() {
        return input_minus;
    }

    public String getBreakdown() {
        return breakdown_minus;
    }

    public String getCal() {
        return cal_minus;
    }

    public String geturl_minus(){return url_minus;}

    public String getTime(){return time;}

    public void setName(int name) {
        this.input_minus = input_minus;
    }

}
