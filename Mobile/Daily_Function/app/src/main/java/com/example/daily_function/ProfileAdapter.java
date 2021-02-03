package com.example.daily_function;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter{

    /* 아이템을 세트로 담기 위한 어레이리스트 */
    private ArrayList<Profile> profiles = new ArrayList<>();

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Profile getItem(int position) {
        return profiles.get(position);
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
            convertView = inflater.inflate(R.layout.prolist, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView nameText = (TextView) convertView.findViewById(R.id.tv_name) ;
        TextView heightText = (TextView) convertView.findViewById(R.id.tv_height) ;
        TextView weightText = (TextView) convertView.findViewById(R.id.tv_weight) ;
        TextView genderText = (TextView) convertView.findViewById(R.id.tv_gender) ;
        TextView ageText = (TextView) convertView.findViewById(R.id.tv_age) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        Profile pro = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        nameText.setText("이름 : "+pro.getName());
        heightText.setText("키 : "+String.valueOf(pro.getHeight()));
        weightText.setText("몸무게 : "+String.valueOf(pro.getWeight()));
        genderText.setText("성별 : "+pro.getGender());
        ageText.setText("나이 : "+String.valueOf(pro.getAge()));

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */

    public void addItem(String name, double height,double weight, String gender, int age) {

        Profile Pro = new Profile();

        Pro.setName(name);
        Pro.setHeight(height);
        Pro.setWeight(weight);
        Pro.setGender(gender);
        Pro.setAge(age);

        profiles.add(Pro);

    }


}