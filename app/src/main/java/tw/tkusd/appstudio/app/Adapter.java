package tw.tkusd.appstudio.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tw.tkusd.appstudio.R;


/**
 * Created by user on 2015/7/10.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private ArrayList<Project> Listproject;
    private Context mContext;

    public Adapter(Context context, ArrayList<Project> Listproject) {
        this.Listproject=Listproject;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_project_list, null);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int i) {
        Project project = Listproject.get(i);
        holder.user_id.setText(project.getUserId());
        holder.project_id.setText(project.getProject_id());

    }

    @Override
    public int getItemCount() {
        return Listproject.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        public TextView user_id;
        public TextView project_id;

        public ViewHolder(View itemView) {
            super(itemView);
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            project_id = (TextView) itemView.findViewById(R.id.project_id);
        }
    }}