package com.synchrony.image.library.imgur.response.domain;

import java.util.ArrayList;
import java.util.List;

public class Data {
	  private String id;
	  private String deletehash;
	  private String account_id = null;
	  private String account_url = null;
	  private String ad_type = null;
	  private String ad_url = null;
	  private String title = null;
	  private String description = null;
	  private String name;
	  private String type;
	  private float width;
	  private float height;
	  private float size;
	  private float views;
	  private String section = null;
	  private String vote = null;
	  private float bandwidth;
	  private boolean animated;
	  private boolean favorite;
	  private boolean in_gallery;
	  private boolean in_most_viral;
	  private boolean has_sound;
	  private boolean is_ad;
	  private String nsfw = null;
	  private String link;
	  List<Object> tags = new ArrayList<Object>();
	  private float datetime;
	  private String mp4;
	  private String hls;


	 // Getter Methods 

	  public String getId() {
	    return id;
	  }

	  public String getDeletehash() {
	    return deletehash;
	  }

	  public String getAccount_id() {
	    return account_id;
	  }

	  public String getAccount_url() {
	    return account_url;
	  }

	  public String getAd_type() {
	    return ad_type;
	  }

	  public String getAd_url() {
	    return ad_url;
	  }

	  public String getTitle() {
	    return title;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public String getName() {
	    return name;
	  }

	  public String getType() {
	    return type;
	  }

	  public float getWidth() {
	    return width;
	  }

	  public float getHeight() {
	    return height;
	  }

	  public float getSize() {
	    return size;
	  }

	  public float getViews() {
	    return views;
	  }

	  public String getSection() {
	    return section;
	  }

	  public String getVote() {
	    return vote;
	  }

	  public float getBandwidth() {
	    return bandwidth;
	  }

	  public boolean getAnimated() {
	    return animated;
	  }

	  public boolean getFavorite() {
	    return favorite;
	  }

	  public boolean getIn_gallery() {
	    return in_gallery;
	  }

	  public boolean getIn_most_viral() {
	    return in_most_viral;
	  }

	  public boolean getHas_sound() {
	    return has_sound;
	  }

	  public boolean getIs_ad() {
	    return is_ad;
	  }

	  public String getNsfw() {
	    return nsfw;
	  }

	  public String getLink() {
	    return link;
	  }

	  public float getDatetime() {
	    return datetime;
	  }

	  public String getMp4() {
	    return mp4;
	  }

	  public String getHls() {
	    return hls;
	  }

	 // Setter Methods 

	  public void setId( String id ) {
	    this.id = id;
	  }

	  public void setDeletehash( String deletehash ) {
	    this.deletehash = deletehash;
	  }

	  public void setAccount_id( String account_id ) {
	    this.account_id = account_id;
	  }

	  public void setAccount_url( String account_url ) {
	    this.account_url = account_url;
	  }

	  public void setAd_type( String ad_type ) {
	    this.ad_type = ad_type;
	  }

	  public void setAd_url( String ad_url ) {
	    this.ad_url = ad_url;
	  }

	  public void setTitle( String title ) {
	    this.title = title;
	  }

	  public void setDescription( String description ) {
	    this.description = description;
	  }

	  public void setName( String name ) {
	    this.name = name;
	  }

	  public void setType( String type ) {
	    this.type = type;
	  }

	  public void setWidth( float width ) {
	    this.width = width;
	  }

	  public void setHeight( float height ) {
	    this.height = height;
	  }

	  public void setSize( float size ) {
	    this.size = size;
	  }

	  public void setViews( float views ) {
	    this.views = views;
	  }

	  public void setSection( String section ) {
	    this.section = section;
	  }

	  public void setVote( String vote ) {
	    this.vote = vote;
	  }

	  public void setBandwidth( float bandwidth ) {
	    this.bandwidth = bandwidth;
	  }

	  public void setAnimated( boolean animated ) {
	    this.animated = animated;
	  }

	  public void setFavorite( boolean favorite ) {
	    this.favorite = favorite;
	  }

	  public void setIn_gallery( boolean in_gallery ) {
	    this.in_gallery = in_gallery;
	  }

	  public void setIn_most_viral( boolean in_most_viral ) {
	    this.in_most_viral = in_most_viral;
	  }

	  public void setHas_sound( boolean has_sound ) {
	    this.has_sound = has_sound;
	  }

	  public void setIs_ad( boolean is_ad ) {
	    this.is_ad = is_ad;
	  }

	  public void setNsfw( String nsfw ) {
	    this.nsfw = nsfw;
	  }

	  public void setLink( String link ) {
	    this.link = link;
	  }

	  public void setDatetime( float datetime ) {
	    this.datetime = datetime;
	  }

	  public void setMp4( String mp4 ) {
	    this.mp4 = mp4;
	  }

	  public void setHls( String hls ) {
	    this.hls = hls;
	  }
}
