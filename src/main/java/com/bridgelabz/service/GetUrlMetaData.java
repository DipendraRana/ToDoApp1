package com.bridgelabz.service;

import java.io.IOException;

import com.bridgelabz.model.UrlDataObject;

public interface GetUrlMetaData {
	
	public UrlDataObject getMetadataFromUrl(String url) throws IOException ;

}
