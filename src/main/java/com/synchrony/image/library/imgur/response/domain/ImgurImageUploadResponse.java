package com.synchrony.image.library.imgur.response.domain;

public class ImgurImageUploadResponse {
	  private float status;
	  private boolean success;
	  Data DataObject;


	 // Getter Methods 

	  public float getStatus() {
	    return status;
	  }

	  public boolean getSuccess() {
	    return success;
	  }

	  public Data getData() {
	    return DataObject;
	  }

	 // Setter Methods 

	  public void setStatus( float status ) {
	    this.status = status;
	  }

	  public void setSuccess( boolean success ) {
	    this.success = success;
	  }

	  public void setData( Data dataObject ) {
	    this.DataObject = dataObject;
	  }
	}
