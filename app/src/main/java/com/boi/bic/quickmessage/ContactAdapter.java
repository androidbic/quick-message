package com.boi.bic.quickmessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        holder.mTextViewName.setText(contactItem.getName());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.text_view_name);
        }
    }
}
