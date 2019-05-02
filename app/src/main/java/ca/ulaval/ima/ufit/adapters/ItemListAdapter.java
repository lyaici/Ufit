package ca.ulaval.ima.ufit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ca.ulaval.ima.ufit.R;
import ca.ulaval.ima.ufit.utils.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {
  private Context context;
  private List<Item> items;
  private int lastPos = -1;

  private static class ViewHolder {
    TextView brandView;
    TextView titleView;
    TextView descriptionView;
    ImageView imageView;
  }

  public ItemListAdapter(Context context, int resource, List<Item> objects) {
    super(context, resource, objects);
    this.items = objects;
    this.context = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Item item = getItem(position);
    ViewHolder viewHolder;
    final View result;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater layoutInflater = LayoutInflater.from(getContext());
      convertView = layoutInflater.inflate(R.layout.adapter_itemlist_layout, parent, false);
      viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImage);
      viewHolder.brandView = (TextView) convertView.findViewById(R.id.itemBrand);
      viewHolder.titleView = (TextView) convertView.findViewById(R.id.itemTitle);
      viewHolder.descriptionView = (TextView) convertView.findViewById(R.id.itemDescription);
      result = convertView;
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
      result = convertView;
    }
    lastPos = position;
    viewHolder.brandView.setText(item.getBrand());
    viewHolder.titleView.setText(item.getTitle());
    viewHolder.descriptionView.setText(item.getDescription());
    Picasso.get().load(item.getImage()).into(viewHolder.imageView);
    return convertView;
  }
}
