package com.nizhal.i_link;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ssaim on 20-10-2019.
 */

public class DestAdapter extends RecyclerView.Adapter<DestAdapter.MyViewHolder>  {

    ArrayList<String> destname;

    Context context;

    public DestAdapter(Context context, ArrayList<String> destname) {
        this.context = context;
        this.destname = destname;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutdes, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.dest_name.setText(destname.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
//                Intent i = new Intent(context, ConfirmSelection.class);
//                i.putExtra("stream", sourcename.get(position));
//                context.startActivity(i);

                Welcome.destn.setText(destname.get(position));
                Welcome.destBottomSheetFragment.dismiss();
            }
        });

    }


    @Override
    public int getItemCount() {
        return destname.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dest_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            dest_name = itemView.findViewById(R.id.dest_name);

        }
    }
}
