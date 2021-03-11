package com.example.daily_function;

public class Comment{
    String Comment_No; //댓글 번호
    String cDate; //댓글 등록시간
    String cId; //댓글 등록 id
    String cContent; //댓글 내용

    public String getComment_No() {
        return Comment_No;
    }

    public void setComment_No(String comment_No) {
        Comment_No = comment_No;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public Comment(){

    }
    public Comment(String Cno, String Cid, String Cdate, String Ccontent){
        this.Comment_No=Cno;
        this.cId=Cid;
        this.cDate=Cdate;
        this.cContent=Ccontent;
    }

}
