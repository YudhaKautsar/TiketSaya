package com.tiketsayayudha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c, ArrayList<MyTicket> p) {

        context = c;
        myTickets = p;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.xticketName.setText(myTickets.get(position).getNama_wisata());
        holder.xticketLocation.setText(myTickets.get(position).getLokasi());
        holder.xnumberOfTicket.setText(myTickets.get(position).getJumlah_tiket() + " Tickets");

        final String getNamaWisata = myTickets.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticketdetail = new Intent(context, MyTicketDetailActivity.class);
                gotomyticketdetail.putExtra("nama_wisata", getNamaWisata);
                context.startActivity(gotomyticketdetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView xticketName, xticketLocation, xnumberOfTicket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xticketName = itemView.findViewById(R.id.xticket_name);
            xticketLocation = itemView.findViewById(R.id.xticket_location);
            xnumberOfTicket = itemView.findViewById(R.id.xnumber_of_ticket);

        }
    }
}
