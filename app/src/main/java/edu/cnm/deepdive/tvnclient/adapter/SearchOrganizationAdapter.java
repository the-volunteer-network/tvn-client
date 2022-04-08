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
  private final OnDetailsClickListener detailsClickListener;
  private final OnFavoriteClickListener favoriteClickListener;
  private final OnVolunteerClickListener volunteerClickListener;
  private final OnMapClickListener mapClickListener;


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

  class Holder extends RecyclerView.ViewHolder {

    private final ItemSearchBinding binding;

    public Holder(@NonNull ItemSearchBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

    }

    public void bind(int position) {
      Organization organization = organizations.get(position);
      binding.organizationName.setText(organization.getName());
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







