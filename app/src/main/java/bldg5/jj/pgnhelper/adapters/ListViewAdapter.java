package bldg5.jj.pgnhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import bldg5.jj.pgnhelper.R;

// http://stackoverflow.com/questions/19466757/hashmap-to-listview
public class ListViewAdapter extends BaseAdapter {
        private final ArrayList mData;

        public ListViewAdapter(Map<String, String> map) {
            mData = new ArrayList();
            mData.addAll(map.entrySet());
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Map.Entry<String, String> getItem(int position) {
            return (Map.Entry) mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO implement you own logic with ID
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View result;

            if (convertView == null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_players, parent, false);
            } else {
                result = convertView;
            }

            Map.Entry<String, String> item = getItem(position);

            // TODO replace findViewById by ViewHolder
            ((TextView) result.findViewById(R.id.txtName)).setText(item.getKey());
            ((TextView) result.findViewById(R.id.txtId)).setText(item.getValue());

            return result;
        }
    }