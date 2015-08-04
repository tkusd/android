package tw.tkusd.appstudio.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import tw.tkusd.appstudio.Project_Util;
import tw.tkusd.appstudio.R;


/**
 * Created by user on 2015/7/10.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    List<Project_Util> projectUtil = Collections.emptyList();
    public final LayoutInflater mLayoutInflater;
    public final ProjectListActivity context;

    public Adapter(ProjectListActivity context, List<Project_Util> pro_util) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.projectUtil = pro_util;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view =  mLayoutInflater.inflate(R.layout.activity_project_list,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Project_Util pro_util = projectUtil.get(i);
        viewHolder.user_id.setText(pro_util.user_id);
        viewHolder.project_id.setText(pro_util.project_id);

    }

    @Override
    public int getItemCount() {
        return projectUtil.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public TextView user_id;
        public TextView project_id;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            project_id = (TextView) itemView.findViewById(R.id.project_id);
        }
    }}