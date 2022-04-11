package edu.cnm.deepdive.tvnclient.model.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class Organization {

  @Expose
  private UUID id;

  @Expose
  private String name;

  @Expose
  private boolean favorite;

  @Expose
  private boolean volunteer;

  @Expose
  private boolean owned;

  @Expose
  private String about;

  @Expose
  private String mission;

  @Expose
  private double latitude;

  @Expose
  private double longitude;

  @Expose
  private Date created;

  @Expose
  private User owner;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public boolean isVolunteer() {
    return volunteer;
  }

  public void setVolunteer(boolean volunteer) {
    this.volunteer = volunteer;
  }

  public boolean isOwned() {
    return owned;
  }

  public void setOwned(boolean owned) {
    this.owned = owned;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getMission() {
    return mission;
  }

  public void setMission(String mission) {
    this.mission = mission;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  @Override
  public int hashCode() {
    return (id != null) ? id.hashCode() : 0;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean result;
    if (obj == this) {
      result = true;
    } else if (obj instanceof Organization) {
      Organization other = (Organization) obj;
      result = id.equals(other.id);
    } else {
      result = false;
    }

    return result;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
