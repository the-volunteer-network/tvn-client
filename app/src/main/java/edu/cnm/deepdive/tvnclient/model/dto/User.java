package edu.cnm.deepdive.tvnclient.model.dto;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class User {

  @Expose
  private UUID id;

  @Expose
  private String name;

  @Expose
  private String displayName;

  @Expose
  private boolean isVolunteer;

  @Expose
  private boolean isFavorite;

/*
  @Expose
  private int age;

  @Expose
  private String location;

  @Expose
  private String email;

  @Expose
  private String phoneNumber;

  @Expose
  private Date created;
*/

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

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public boolean isVolunteer() {
    return isVolunteer;
  }

  public void setVolunteer(boolean volunteer) {
    isVolunteer = volunteer;
  }

  public boolean isFavorite() {
    return isFavorite;
  }

  public void setFavorite(boolean favorite) {
    isFavorite = favorite;
  }
}
