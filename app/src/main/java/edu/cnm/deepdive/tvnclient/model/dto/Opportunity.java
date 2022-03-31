package edu.cnm.deepdive.tvnclient.model.dto;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class Opportunity {

  @Expose
  private UUID id;

  @Expose
  private String name;



/*  @Expose
  private String title;

  @Expose
  private String neededSkill;

  @Expose
  private  String description;

  @Expose
  private Date created;*/


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
}
