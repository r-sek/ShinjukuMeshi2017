package info.redspirit.shinjukumeshi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rj on 2017/01/20.
 */

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder> {
    private String[] list;
    private String[] walkList;
    private Context context;

    //アクティビティ取得
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private View v;

    public CardRecyclerAdapter(Context context, String[] stringArray, String[] walkArray) {
        super();
        this.list = stringArray;
        this.walkList = walkArray;
        this.context = context;
    }


//    public CardRecyclerAdapter(Context context) {
//        super();
//        mLayoutInflater = LayoutInflater.from(context);
//        this.context = context;
//    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        vh.textView_main.setText(list[position]);
        vh.imageView.setImageResource(R.mipmap.ic_app_icon);
        vh.textView_sub.setText(walkList[position]);
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(walkList[position]) + ":" + list[position], Toast.LENGTH_SHORT).show();
                infoView(list[position]);
            }
        });
    }

    protected void infoView(String name) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.layout_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_main;
        TextView textView_sub;
        LinearLayout layout;
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            textView_main = (TextView) v.findViewById(R.id.textView_main);
            textView_sub = (TextView) v.findViewById(R.id.textView_sub);
            layout = (LinearLayout) v.findViewById(R.id.layout);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
    }
}
