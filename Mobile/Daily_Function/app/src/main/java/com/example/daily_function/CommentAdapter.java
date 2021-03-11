package com.example.daily_function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

        /* 아이템을 세트로 담기 위한 어레이리스트 */
        private ArrayList<Comment> Comments = new ArrayList<>();

        @Override
        public int getCount() {
            return Comments.size();
        }

        @Override
        public Comment getItem(int position) {
            return Comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Context context = parent.getContext();

            /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_comment, parent, false);
            }

            /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
            TextView userText = (TextView) convertView.findViewById(R.id.cmt_userid_tv) ;
            TextView dateText = (TextView) convertView.findViewById(R.id.cmt_date_tv) ;
            TextView commentText = (TextView)convertView.findViewById(R.id.cmt_content_tv);



            /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
            Comment cmt = getItem(position);

            /* 각 위젯에 세팅된 아이템을 뿌려준다 */
            userText.setText(String.valueOf(cmt.getcId()));
            dateText.setText(cmt.getcDate());
            commentText.setText(String.valueOf(cmt.getcContent()));

            /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


            return convertView;
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */

        public void addItem( String id,String date,  String content) {

            Comment cmt = new Comment();

            cmt.setcId(id);
            cmt.setcDate(date);
            cmt.setcContent(content);

            Comments.add(cmt);

        }



}
