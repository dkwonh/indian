package cs.kangwon.com.indian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs.kangwon.com.teamsw.R;

/**
 * Created by Administrator on 2015-12-03.
 */
public class ResultAdapter extends ArrayAdapter<Result> {
    private List<Result> items;
    private LayoutInflater inflater;
    private int[] Img = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10,};
    public ResultAdapter(Context context, int textViewResourceId,List<Result> items){
        super(context, textViewResourceId,items);
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        if(view ==null){
            view = inflater.inflate(R.layout.result_item,null);
        }

        Result item = (Result)items.get(position);

        if(item !=null){
            TextView Round = (TextView) view.findViewById(R.id.round);
            Round.setText(item.getRound());

            TextView Result = (TextView) view.findViewById(R.id.result);
            Result.setText(item.getResult());

            TextView myCard = (TextView)view.findViewById(R.id.mc);
            if(item.getMyCard()=="");
            else
            myCard.setBackgroundResource(Img[Integer.parseInt(item.getMyCard())-1]);


            TextView yCard = (TextView)view.findViewById(R.id.yc);
            if(item.getYourCard()=="");
            else
            yCard.setBackgroundResource(Img[Integer.parseInt(item.getYourCard()) - 1]);

            TextView chip = (TextView)view.findViewById(R.id.tchip);
            chip.setText(item.getChip());
        }

        return view;
    }
}
