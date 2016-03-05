package com.BlackBerryJuice;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

// adapter class for custom order list
class AdapterCart extends BaseAdapter {

	private LayoutInflater inflater;
	Context c;

	public AdapterCart(Context context) {
		inflater = LayoutInflater.from(context);
		c  =context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return ActivityCart.Menu_ID.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if(convertView == null){
			convertView = inflater.inflate(R.layout.order_list_item3, null);
			holder = new ViewHolder();
			holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
			holder.main_1 = (RelativeLayout) convertView.findViewById(R.id.main_1);
			holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(ActivityCart.Menu_name.get(position).contains("رزرو"))
		{
			ActivityCart.RES_NAME_F = ActivityCart.Menu_name.get(position);
			double t = ActivityCart.Sub_total_price.get(position);
			ActivityCart.RES_PRICE_F = NumberFormat.getNumberInstance(Locale.US).format((int) t) + " " + ActivityCart.Currency ;
			ActivityCart.RES_B = true;
			SharedData.RES_B(true,c);
			holder.main_1.setVisibility(View.GONE);
			Log.e(" ", "" + ActivityCart.RES_B);
		}
		else {
			holder.txtMenuName.setText(ActivityCart.Menu_name.get(position));
			holder.txtQuantity.setText(String.valueOf(ActivityCart.Quantity.get(position)));
			double t = ActivityCart.Sub_total_price.get(position);
			holder.txtPrice.setText(NumberFormat.getNumberInstance(Locale.US).format((int) t) + " " + ActivityCart.Currency);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView txtMenuName, txtQuantity, txtPrice;
		RelativeLayout main_1;
		Button plus,minez;
	}



}