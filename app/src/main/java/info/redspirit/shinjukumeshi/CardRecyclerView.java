package info.redspirit.shinjukumeshi;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by rj on 2017/01/20.
 */

public class CardRecyclerView extends RecyclerView {

    Activity host;
    Global global;
    ArrayList<String> array;
    private String[] list;
    private Integer[] iList;


    public CardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setRecyclerAdapter(context);
    }
//
//    public void setRecyclerAdapter(Context context) {
//        setLayoutManager(new LinearLayoutManager(context));
//        setAdapter(new CardRecyclerAdapter(context, list));
//    }

    public void setRecyclerAdapterB(Context context, ArrayList<String> list, ArrayList<Integer> iList) {
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter(context, list.toArray(new String[list.size()]), iList.toArray(new Integer[iList.size()])));
    }

    //listセット
    private String[] setList(){
//        Context ct = getContext();
//        host = getActivity();
//        Activity activity = (Activity)this.getContext();
//        global = (Global)ct.getApplicationContext();
//        if (isInEditMode()) {
//            // 編集モードだったら処理終了
//            return global.testArray.toArray(new String[global.testArray.size()]);
//        }
//        if(global.nameArray.size()!=0){
//            return global.nameArray.toArray(new String[global.nameArray.size()]);
//        }else{
//            return global.testArray.toArray(new String[global.testArray.size()]);
//        }
        return array.toArray(new String[array.size()]);
    }

    //アクティビティが取得できるっぽい？
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
