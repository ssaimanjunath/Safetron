package com.nizhal.i_link;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ssaim on 20-10-2019.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.MyViewHolder>  {

    ArrayList<String> sourcename;

    Context context;

    public SourceAdapter(Context context, ArrayList<String> sourcename) {
        this.context = context;
        this.sourcename = sourcename;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.source_name.setText(sourcename.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
//                Intent i = new Intent(context, ConfirmSelection.class);
//                i.putExtra("stream", sourcename.get(position));
//                context.startActivity(i);

                Welcome.source.setText(sourcename.get(position));
                Welcome.sourceBottomSheetFragment.dismiss();
            }
        });

    }


    @Override
    public int getItemCount() {
        return sourcename.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView source_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            source_name = itemView.findViewById(R.id.source_name);

        }
    }
}
