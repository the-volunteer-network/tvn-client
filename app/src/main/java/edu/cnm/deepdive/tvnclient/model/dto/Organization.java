package edu.cnm.deepdive.tvnclient.model.dto;

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
  private String about;

  @Expose
  private String mission;

  @Expose
  private Date created ;

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

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
