package company.best.the.hangman;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
    private ArrayList<String> dataset;
    public static class myViewHolder extends RecyclerView.ViewHolder{
        public CardView niceCard;
        public myViewHolder(CardView cv){
            super(cv);
            niceCard = cv;
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
        //holder.niceText.setText(dataset[position]);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
