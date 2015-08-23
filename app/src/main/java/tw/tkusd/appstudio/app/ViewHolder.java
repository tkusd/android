package tw.tkusd.appstudio.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tw.tkusd.appstudio.R;

/**
 * Created by wenlin on 2015/8/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    TextView title;

    public ViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.rowtitle);
    }

}
