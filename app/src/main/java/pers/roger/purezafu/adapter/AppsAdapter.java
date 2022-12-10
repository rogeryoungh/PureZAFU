package pers.roger.purezafu.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import pers.roger.purezafu.R;
import pers.roger.purezafu.activity.MainActivity;
import pers.roger.purezafu.activity.WebActivity;
import pers.roger.purezafu.model.bean.AppDataBean;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsViewHolder> {
    AppDataBean.DataBean data;
    List<AppDataBean.CatAppsBean> catApps;
    Context context;

    public AppsAdapter(Context context) {
        this.context = context;
    }

    public void setAppData(AppDataBean appData) {
        data = appData.getData();
        catApps = data.getCatApps();
    }

    @NonNull
    @Override
    public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_apps, parent, false);
        return new AppsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position) {
        holder.setApps(catApps.get(position));
    }

    @Override
    public int getItemCount() {
        return catApps.size();
    }

    class AppsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView apps_title;
        AppDataBean.CatAppsBean catApps;
        List<Chip> list_button;
        ChipGroup chipgroup;
        boolean hasset = false;

        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            apps_title = itemView.findViewById(R.id.apps_title);
            chipgroup = itemView.findViewById(R.id.chip_group);
            list_button = new ArrayList<>();
        }

        public void setApps(AppDataBean.CatAppsBean catApps) {
            if (hasset)
                return;
            this.catApps = catApps;
            apps_title.setText(catApps.getName());
            for (int i = 0; i < catApps.getApps().size(); i++) {
                AppDataBean.AppsBean app = catApps.getApps().get(i);
                Chip button = new Chip(apps_title.getContext());
                button.setText(app.getShortName());
                button.setTag(app.getUrl());
                button.setOnClickListener(v -> {
                    String url = v.getTag().toString();
                    Log.i("zafu", url);
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                });
                list_button.add(button);
                chipgroup.addView(button);
            }
            hasset = true;
        }
    }
}