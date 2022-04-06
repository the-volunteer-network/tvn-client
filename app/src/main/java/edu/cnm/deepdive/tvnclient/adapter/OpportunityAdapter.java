package edu.cnm.deepdive.tvnclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.tvnclient.adapter.OpportunityAdapter.Holder;
import edu.cnm.deepdive.tvnclient.databinding.ItemOpportunityBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<Holder> {

  private final List<Opportunity> opportunities;
  private final LayoutInflater inflater;

  public OpportunityAdapter( Context context,
      List<Opportunity> opportunities) {
    this.opportunities = opportunities;
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
  ItemOpportunityBinding binding = ItemOpportunityBinding.inflate(inflater);
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
    private ItemOpportunityBinding binding;

    public Holder(@NonNull ItemOpportunityBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
    public void bind (int position) {
      Opportunity opportunity = opportunities.get(position);
    }
  }

}
