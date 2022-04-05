package edu.cnm.deepdive.tvnclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter.Holder;
import edu.cnm.deepdive.tvnclient.databinding.ItemSearchBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import java.util.List;

public class SearchOrganizationAdapter extends RecyclerView.Adapter<Holder> {

  private final List<Organization> organizations;
  private final LayoutInflater layoutInflater;


  public SearchOrganizationAdapter(Context context, List<Organization> organizations) {
    this.organizations = organizations;

    layoutInflater = LayoutInflater.from(context);
  }


  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemSearchBinding binding= ItemSearchBinding.inflate(layoutInflater);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }
  class Holder extends RecyclerView.ViewHolder {
    private ItemSearchBinding binding;

    public Holder(@NonNull  ItemSearchBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

    }
    public void bind (int position) {
      Organization organization = organizations.get(position);

    }
  }
}
