package mytextview.example.com.customer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PurchaseHistory extends Fragment {


    ListView list;
    ArrayList<String> ar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        list=view.findViewById(R.id.list);
        dbHelper d=new dbHelper(getActivity());
        ArrayList<purchasepojo> a=d.getdata();

        Customlist cv=new Customlist(a);
        list.setAdapter(cv);
        return view;
    }

    private class Customlist extends BaseAdapter {
        ArrayList<purchasepojo> arrayList;
        public Customlist(ArrayList<purchasepojo> arrayList) {
            this.arrayList=arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.history_row,parent,false);
            TextView tv1= (TextView) convertView.findViewById(R.id.datetime);
            TextView tv2= (TextView) convertView.findViewById(R.id.productname);
            TextView tv3= (TextView) convertView.findViewById(R.id.unique);
            TextView number1= (TextView) convertView.findViewById(R.id.id);

            final purchasepojo p=arrayList.get(position);
            tv1.setText(p.getDate());
            tv2.setText(p.getProductname());
            tv3.setText(p.getUnique());
            number1.setText((position+1)+"");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Please wait", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),QrcodeGen.class).putExtra("unique",p.getUnique()));
                }
            });
            return convertView;
        }
    }

}
