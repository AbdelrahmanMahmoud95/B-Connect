package com.bconnect.b_connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomGridMain extends BaseAdapter {

    private Context mContext;



    TextView textViewAndroid;
    ImageView imageViewAndroid;
    LayoutInflater inflater;
    View gridViewAndroid;

    public CustomGridMain(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.grid_main_layout, null);
            textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
        } else {
            gridViewAndroid = (View) convertView;
        }
        textViewAndroid.setText(gridViewString[i]);

        imageViewAndroid.setImageResource(gridViewImageId[i]);

        return gridViewAndroid;
    }


    String[] gridViewString  = {
            "المبيعات", "الدعم الفني", "التركيبات الجديدة",
            "الوحدات الإضافية", "سداد المبالغ للشركة",
            "مبيعاتي","صندوق الرسائل الواردة"


    };
    int[] gridViewImageId  ={
            R.mipmap.cont, R.mipmap.sup, R.mipmap.inst,
            R.mipmap.order,R.mipmap.amount,R.mipmap.mysall,R.mipmap.message,

    };
}
