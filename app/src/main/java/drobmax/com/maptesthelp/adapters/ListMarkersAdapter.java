package drobmax.com.maptesthelp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import drobmax.com.maptesthelp.R;
import drobmax.com.maptesthelp.fragments.Fragment2;
import drobmax.com.maptesthelp.models.MarkerModel;
import drobmax.com.maptesthelp.utils.ListnersHosting;

/**
 * Created by Admin on 06.08.2015.
 */
public class ListMarkersAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MarkerModel> markers;
    @Override
    public int getCount() {
        return markers.size();
    }
    public ListMarkersAdapter(List<MarkerModel> markers, Context context){
        this.markers = (ArrayList<MarkerModel>)markers;
        this.context = context;
        inflater  = LayoutInflater.from(context);
    }
    class ViewHolder {
        private ImageButton menu;
        private TextView tvTitle, tvDescription, tvLocation;
        private PopupWindow popupWindow;
        private String popupWindowContent[] = {"edit", "delete"};
        private ListView lv;
        public ViewHolder() {

        }

        public void initViewHolder(View view) {
            tvDescription = (TextView) view.findViewById(R.id.textDescription);
            tvLocation = (TextView) view.findViewById(R.id.textLocation);
            tvTitle = (TextView) view.findViewById(R.id.textTitle);
            menu = (ImageButton)view.findViewById(R.id.imageButton);
            popupWindow = new PopupWindow(context);
            popupWindow.setFocusable(true);
            popupWindow.setWidth(100);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            lv = new ListView(context);
            lv.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,popupWindowContent));
            popupWindow.setContentView(lv);
        }
    }
    @Override
    public MarkerModel getItem(int position) {
        return markers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_marker, null);
            viewHolder = new ViewHolder();
            viewHolder.initViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MarkerModel marker = getItem(position);
        viewHolder.tvTitle.setText(marker.getName());
        viewHolder.tvDescription.setText(marker.getDescription());
        viewHolder.tvLocation.setText("coord : ( " + marker.getLat() + " ; " + marker.getLng() + " )");
        final PopupWindow popupWindow = viewHolder.popupWindow;
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v, -5, 0);
            }
        });
        final int index = position;
        viewHolder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ListnersHosting.getInstance().getOnDataEditingListner().onEditData(
                                marker.getId(), marker.getName(),marker.getDescription(),
                                new LatLng(marker.getLat(),marker.getLng()));
                        ListnersHosting.getInstance().getOnReplaceFragmentListener().onReplaceFragment(0);
                        break;
                    case 1:
                        ListnersHosting.getInstance().getOnDataEditingListner().onDeleteData(marker.getId());
                        break;
                }
                popupWindow.dismiss();

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListnersHosting.getInstance().getOnMyEventListener1().
                        onLocationSelected(new LatLng(marker.getLat(),marker.getLng()));
            }
        });
        return view;
    }
}
