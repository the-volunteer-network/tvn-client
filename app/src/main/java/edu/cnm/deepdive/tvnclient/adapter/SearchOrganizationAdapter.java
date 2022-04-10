package edu.cnm.deepdive.tvnclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter.Holder;
import edu.cnm.deepdive.tvnclient.databinding.ItemSearchBinding;
import edu.cnm.deepdive.tvnclient.controller.SearchOrganizationFragment;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import java.util.List;

/**
 * Provides access to a recycler view for the {@link SearchOrganizationFragment} and creates a view for each item in the data set.
 */
public class SearchOrganizationAdapter extends RecyclerView.Adapter<Holder> {

  private final List<Organization> organizations;
  private final LayoutInflater layoutInflater;
  private final OnDetailsClickListener detailsClickListener;
  private final OnFavoriteClickListener favoriteClickListener;
  private final OnVolunteerClickListener volunteerClickListener;
  private final OnMapClickListener mapClickListener;
  private final int favoriteColor;
  private final int volunteerColor;
  private final int unselectedColor;
  /**
   *Initialize this instance of {@link SearchOrganizationAdapter} with the injected below parameters.
   * @param context
   * @param organizations
   * @param detailsClickListener
   * @param favoriteClickListener
   * @param volunteerClickListener
   * @param mapClickListener
   */
  public SearchOrganizationAdapter(Context context, List<Organization> organizations,
      OnDetailsClickListener detailsClickListener,
      OnFavoriteClickListener favoriteClickListener,
      OnVolunteerClickListener volunteerClickListener,
      OnMapClickListener mapClickListener) {
    this.organizations = organizations;
    layoutInflater = LayoutInflater.from(context);
    this.detailsClickListener = detailsClickListener;
    this.favoriteClickListener = favoriteClickListener;
    this.volunteerClickListener = volunteerClickListener;
    this.mapClickListener = mapClickListener;
    favoriteColor = ContextCompat.getColor(context, R.color.favorite);
    volunteerColor = ContextCompat.getColor(context, R.color.volunteer);
    unselectedColor= Color.GRAY;
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

  /**
   * Provides access to a recycler view and creates a view for each item in the data set.
   */
  class Holder extends RecyclerView.ViewHolder {

    private final ItemSearchBinding binding;

    /**
     * Wraps around the View that contains the {@code fragment_advice.xml} layout
     * @param binding Attaches to the root of the layout,
     */
    public Holder(@NonNull ItemSearchBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

    }

    /**
     * Binds the item according to its position
     * @param position
     */
    public void bind(int position) {
      Organization organization = organizations.get(position);
      binding.organizationName.setText(organization.getName());
      binding.favorite.setColorFilter(organization.isFavorite()? favoriteColor : unselectedColor );
      binding.volunteer.setColorFilter(organization.isVolunteer() ? volunteerColor : unselectedColor);
      binding.info.setOnClickListener((v) -> detailsClickListener.onClick(position, organization));
      binding.favorite.setOnClickListener((v) -> favoriteClickListener.onClick(position, organization, !organization.isFavorite()));
      binding.volunteer.setOnClickListener((v) -> volunteerClickListener.onClick(position, organization, !organization.isVolunteer()));
      binding.map.setOnClickListener((v) -> mapClickListener.onClick(position, organization));

//      binding.description.setText(organization.getAbout());

    }


  }

  @FunctionalInterface
  public interface OnDetailsClickListener {

    void onClick(int position, Organization organization);


  }

  @FunctionalInterface
  public interface OnFavoriteClickListener {

    void onClick(int position, Organization organization, boolean favorite);


  }

  @FunctionalInterface
  public interface OnVolunteerClickListener {

    void onClick(int position, Organization organization, boolean volunteer);


  }

  @FunctionalInterface
  public interface OnMapClickListener {

    void onClick(int position, Organization organization);


  }

}







