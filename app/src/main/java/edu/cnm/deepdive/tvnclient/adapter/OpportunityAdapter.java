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

/**
 * Provides access to a recycler view for the {@link Opportunity} fragment and creates a view for
 * each item in the data set.
 */
public class OpportunityAdapter extends RecyclerView.Adapter<Holder> {

  private final List<Opportunity> opportunities;
  private final LayoutInflater inflater;
  private final boolean organizationVisible;

  /**
   * Initialize this instance of {@link OpportunityAdapter} with the injected below parameters.
   *  @param context
   * @param opportunities
   * @param organizationVisible
   */
  public OpportunityAdapter(Context context, List<Opportunity> opportunities,
      boolean organizationVisible) {
    this.opportunities = opportunities;
    inflater = LayoutInflater.from(context);
    this.organizationVisible = organizationVisible;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemOpportunityBinding binding = ItemOpportunityBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);

  }

  @Override
  public int getItemCount() {
    return opportunities.size();
  }

  /**
   * Provides access to a recycler view and creates a view for each item in the data set.
   */
  class Holder extends RecyclerView.ViewHolder {

    private ItemOpportunityBinding binding;

    /**
     * Wraps around the View that contains the {@code fragment_advice.xml} layout
     *
     * @param binding Attaches to the root of the layout,
     */
    public Holder(@NonNull ItemOpportunityBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    /**
     * Binds the item according to its position
     *
     * @param position
     */
    public void bind(int position) {
      Opportunity opportunity = opportunities.get(position);
      if (organizationVisible) {
        binding.organizationName.setVisibility(View.VISIBLE);
        binding.organizationName.setText(opportunity.getOrganization().getName());
      } else {
        binding.organizationName.setVisibility(View.GONE);
      }
      binding.opportunityName.setText(opportunity.getName());
      binding.description.setText(opportunity.getDescription());
    }
  }

}
