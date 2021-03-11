package com.example.daily_function;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class BoardData implements Serializable { //게시판 글 내용 정보

    public BoardData(){

    }

    public BoardData(String board_No, String title, String content, String date, int hits, int get, String id,String download) {
        Board_No = board_No;
        this.title = title;
        Content = content;
        Date = date;
        Hits = hits;
        this.get = get;
        this.id = id;
        this.download=download;

    }

    public String getBoard_No() {
        return Board_No;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }

    public int getHits() {
        return Hits;
    }

    public int getGet() {
        return get;
    }

    public String getId() {
        return id;
    }

    public void setBoard_No(String Board_NO) {
        this.Board_No = Board_NO;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public void setHits(int Hits) {
        this.Hits = Hits;
    }

    public void setGet(int get) {
        this.get = get;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }


    String Board_No; //게시물 번호(key사용)
    String title; // 제목
    String Content; //내용
    String Date; // 등록 시간
    int Hits; // 조회수
    int get; // 좋아요 숫자
    String id; // 등록 아이디
    String download; //등록한 사진

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> BoardInfo = new HashMap<>();
        BoardInfo.put("Board_No", Board_No);
        BoardInfo.put("title", title);
        BoardInfo.put("Content", Content);
        BoardInfo.put("Date", Date);
        BoardInfo.put("Hits", Hits);
        BoardInfo.put("get", get);
        BoardInfo.put("id", id);
        BoardInfo.put("download",download);

        return BoardInfo;
    }

}

