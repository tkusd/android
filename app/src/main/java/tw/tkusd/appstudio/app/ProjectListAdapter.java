package tw.tkusd.appstudio.app;

import android.content.Context;
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
public class ProjectListAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<Project> ListProject;
    private Context context;

    public ProjectListAdapter(Context context,List<Project> ListProject) {
        this.ListProject=ListProject;
        this.context=context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView =  LayoutInflater.from(context).
                inflate(R.layout.list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = ListProject.get(position);
        holder.title.setText(project.gettitle());
    }

    @Override
    public int getItemCount() {
        return ListProject.size();
    }

}
