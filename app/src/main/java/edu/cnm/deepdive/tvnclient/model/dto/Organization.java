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
  private boolean isFavorite;

/*  @Expose
  private String about;

  @Expose
  private String mission;

  @Expose
  private Date created ;*/

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
    return isFavorite;
  }

  public void setFavorite(boolean favorite) {
    isFavorite = favorite;
  }
}
