package company.best.the.hangman;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
    private ArrayList<String> dataset;
    private static ClickListener clickListener;
    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView niceCard;
        public myViewHolder(CardView cv){
            super(cv);
            cv.setOnClickListener(this);
            niceCard = cv;
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public myAdapter(ArrayList<String> myDataset) {
        dataset = myDataset;
    }

    /*@NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text, parent, false);
        myViewHolder vh = new myViewHolder(v);
        return vh;
    }*/

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_element, parent, false);
        myViewHolder vh = new myViewHolder(cv);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        TextView tv = holder.niceCard.findViewById(R.id.text_view_id);
        tv.setText(dataset.get(position));
        ImageView iv = holder.niceCard.findViewById(R.id.difficulty);
        if(dataset.get(position).length() <4){
            //easy
            iv.setImageResource(R.drawable.easy);
        }else if(dataset.get(position).length() >= 4 && dataset.get(position).length() <8){
            //medium
            iv.setImageResource(R.drawable.medium);
        }else{
            //hard
            iv.setImageResource(R.drawable.hard);
        }
        //holder.niceText.setText(dataset[position]);

    }

    public void removeAt(int position){
        dataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataset.size());
    }

    public void setOnItemClickListener(ClickListener clickListener){
        myAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        //void onItemLongClick(int position, View v);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
