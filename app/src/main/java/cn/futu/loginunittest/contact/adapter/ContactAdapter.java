package cn.futu.loginunittest.contact.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.futu.loginunittest.R;
import cn.futu.loginunittest.data.model.Contact;

/**
 * contact adapter
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
{

    private List<Contact> mData = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (position < mData.size())
        {
            holder.fill(mData.get(position));
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public List<Contact> getData()
    {
        return mData;
    }

    public void setData(List<Contact> data)
    {
        if (data != null)
        {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView name;
        private TextView phone;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

        public void fill(Contact contact)
        {
            name.setText(contact.getName());
            phone.setText(contact.getPhone());
        }
    }
}
