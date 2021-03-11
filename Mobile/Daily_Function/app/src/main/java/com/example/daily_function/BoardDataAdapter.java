package com.example.daily_function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardDataAdapter extends BaseAdapter implements Filterable {

    /* 아이템을 세트로 담기 위한 어레이리스트 */
    private ArrayList<BoardData> Boards = new ArrayList<>();
    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    private ArrayList<BoardData> filteredItemList = Boards ;
    Filter listFilter ;

    @Override
    public int getCount() {
        return Boards.size();
    }

    @Override
    public BoardData getItem(int position) {
        return Boards.get(position);
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
            convertView = inflater.inflate(R.layout.item_board, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView getText = (TextView) convertView.findViewById(R.id.board_get) ;
        TextView titleText = (TextView) convertView.findViewById(R.id.board_title) ;
        TextView hitText = (TextView)convertView.findViewById(R.id.board_hit);



        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        BoardData board = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        getText.setText(String.valueOf(board.getGet()));
        titleText.setText(board.getTitle());
        hitText.setText(String.valueOf(board.getHits()));

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */

    public void addItem(String board_No, String title, String content, String date, int hits, int get, String id) {

        BoardData board = new BoardData();

        board.setBoard_No(board_No);
        board.setTitle(title);
        board.setContent(content);
        board.setDate(date);
        board.setHits(hits);
        board.setGet(get);
        board.setId(id);

        Boards.add(board);

    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }

        return listFilter ;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = Boards ;
                results.count = Boards.size() ;
            } else {
                ArrayList<BoardData> itemList = new ArrayList<BoardData>() ;

                for (BoardData item : Boards) {
                    if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getId().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<BoardData>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }


}