package com.example.daily_function;

public class Board_info { // 게시판 메뉴 정보(공지사항, 전체글, 인기글 등등)


    int menu_num; ///메뉴 번호
    String menu; //메뉴이름

    public Board_info() { //default 생성자
    }
    
    
    public Board_info(int menu_num,String menu) {
        this.menu_num = menu_num;
        this.menu=menu;
    }
    

    public int getMenu_num() {
        return menu_num;
    }

    public void setMenu_num(int menu_num) {
        this.menu_num = menu_num;
    }
    

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
    
    
}
