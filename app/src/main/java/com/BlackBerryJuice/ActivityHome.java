package com.BlackBerryJuice;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

public class ActivityHome extends Fragment implements OnItemClickListener {
	GridView gridview;
	AdapaterGridView gridviewAdapter;
	ArrayList<GridViewItem> data = new ArrayList<GridViewItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_list, container, false);

		gridview = (GridView) v.findViewById(R.id.gridView1);
		gridview.setOnItemClickListener(this);

		setDataAdapter();

		return v;
	}

	// Set the Data Adapter
	private void setDataAdapter() {
		gridviewAdapter = new AdapaterGridView(getActivity(), R.layout.fragment_list_item, data);
		gridview.setAdapter(gridviewAdapter);
	}

	@Override
	public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
		if (position==0){
			startActivity(new Intent(getActivity(), ActivityCategoryList.class));
			getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_next);
		}
		else if (position==1){
			startActivity(new Intent(getActivity(), ActivityCart.class));
			getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_next);
		}
		else if (position==2){
			startActivity(new Intent(getActivity(), ActivityCheckout.class));
			getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_next);
		}

	}

}