package edu.cnm.deepdive.tvnclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
  private final OrgClickListener listener;


  public SearchOrganizationAdapter(Context context, List<Organization> organizations,
      OrgClickListener listener) {
    this.organizations = organizations;

    layoutInflater = LayoutInflater.from(context);
    this.listener = listener;

  }


  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemSearchBinding binding = ItemSearchBinding.inflate(layoutInflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return organizations.size();
  }

  class Holder extends RecyclerView.ViewHolder implements OrgClickListener {

    private final ItemSearchBinding binding;

    public Holder(@NonNull ItemSearchBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

    }

    public void bind(int position) {
      Organization organization = organizations.get(position);
      binding.organizationName.setText(organization.getName());
      binding.getRoot().setOnClickListener((v) -> listener.onClick(position));

//      binding.description.setText(organization.getAbout());

    }

    @Override
    public void onClick(int position) {

    }
  }
  @FunctionalInterface
  public interface OrgClickListener {

    void onClick(int position);


  }
}







