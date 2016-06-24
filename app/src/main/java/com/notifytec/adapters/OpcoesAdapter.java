package com.notifytec.adapters;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import com.notifytec.R;

        import java.util.List;

/**
 * Created by Bruno on 19/05/2016.
 */
public class OpcoesAdapter extends RecyclerView.Adapter<OpcoesAdapter.MyViewHolder> {
    private List<String> mList;
    private LayoutInflater mLayoutInflater;

    public OpcoesAdapter(Context ctx, List<String> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_opcaoenquete, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    //vincula os dados da nossa lista a view
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        Log.i("LOG", "onBindViewHolder()");
        myViewHolder.tv_opcao.setText(mList.get(position));

        //Implementando o botão remove
        myViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext() , "Teste Position: " + String.valueOf(position), Toast.LENGTH_LONG).show();
                removeListItem(myViewHolder.getAdapterPosition());
            }
        });
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_opcao;
        public ImageButton btnRemove;

        //itemView é o item root. É cada "linha" do RecyclerView
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_opcao = (TextView) itemView.findViewById(R.id.tv_opcao);
            btnRemove = (ImageButton) itemView.findViewById(R.id.btnRemove);
        }
    }
}