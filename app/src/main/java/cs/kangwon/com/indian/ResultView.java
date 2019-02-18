package cs.kangwon.com.indian;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cs.kangwon.com.teamsw.R;

/**
 * Created by Administrator on 2015-12-02.
 */
public class ResultView extends ListActivity {

    protected static List<Result> items;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       // items = new ArrayList<Result>();

        resultAdapter = new ResultAdapter(this,R.layout.result_item,items);
        setListAdapter(resultAdapter);

       ListView listView = getListView();
    }
}
