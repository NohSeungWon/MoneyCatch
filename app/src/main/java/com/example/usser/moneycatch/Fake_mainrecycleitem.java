package com.example.usser.moneycatch;

public class Fake_mainrecycleitem {

    private int name; // 입금값
    private String breakdown; //내역
    private String cal; // 달력
    private String url; // 이미지

    public Fake_mainrecycleitem(int name,String breakdown, String cal,String url){
        this.name = name;
        this.breakdown = breakdown;
        this.cal = cal;
        this.url = url;
    }

    public int getName() {
        return name;
    }

    public String getBreakdown() {
        return breakdown;
    }

    public String getCal() {
        return cal;
    }

    public String getUrl(){return url;}

    public void setName(int name) {
        this.name = name;
    }
}
