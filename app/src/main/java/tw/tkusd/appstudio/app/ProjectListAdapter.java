package tw.tkusd.appstudio.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tw.tkusd.appstudio.R;


/**
 * Created by user on 2015/7/10.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder>{

    private List<Project> ListProject;


    public ProjectListAdapter(List<Project> ListProject) {
        this.ListProject=ListProject;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list, viewGroup, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(ProjectListAdapter.ViewHolder holder, int i) {
        Project project = ListProject.get(i);
        holder.user_id.setText(project.getUserId());

    }

    @Override
    public int getItemCount() {
        return ListProject.size();
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