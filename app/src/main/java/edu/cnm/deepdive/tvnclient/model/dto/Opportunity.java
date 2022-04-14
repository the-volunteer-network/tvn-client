package edu.cnm.deepdive.tvnclient.model.dto;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class Opportunity {

  @Expose
  private UUID id;

  @Expose
  private String name;

 @Expose
  private String title;

  @Expose
  private String neededSkill;

  @Expose
  private  String description;

  @Expose
  private Date created;

  @Expose
  private Organization organization;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNeededSkill() {
    return neededSkill;
  }

  public void setNeededSkill(String neededSkill) {
    this.neededSkill = neededSkill;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }
}
