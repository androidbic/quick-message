package com.boi.bic.quickmessage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<Contact> mContactList;

    public ContactAdapter(ArrayList<Contact> mContactList){
        this.mContactList = mContactList;
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        ContactViewHolder holder = new ContactViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contactItem = mContactList.get(position);
        holder.name.setText(contactItem.getName());


    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_name)
        TextView name;
        View rootView;

        public ContactViewHolder(View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Context context = view.getContext();
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("name", mContactList.get(position).getName());
                    intent.putExtra("number", mContactList.get(position).getNumber());
                    context.startActivity(intent);
                }
            });
        }
    }
    public void removeItem(int position) {
        mContactList.remove(position);
        notifyItemRemoved(position);
    }
}
