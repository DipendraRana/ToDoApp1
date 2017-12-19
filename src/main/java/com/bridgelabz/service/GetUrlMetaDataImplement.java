package com.bridgelabz.service;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.bridgelabz.model.UrlDataObject;

@Service
public class GetUrlMetaDataImplement implements GetUrlMetaData {

	@Override
	public UrlDataObject getMetadataFromUrl(String url) throws IOException {
		UrlDataObject urlDataObject=new UrlDataObject();
	    Connection connection = Jsoup.connect(url);
	    Document document = connection.userAgent("Mozilla").get();
	    URL urlObject=new URL(url);
	    
	    String title = null;
	    Elements metaOgTitle = document.select("meta[property=og:title]");
	    if (metaOgTitle!=null) 
	        title = metaOgTitle.attr("content");
	    else 
	        title = document.title();
	    
	    String imageUrl = null;
	    Elements metaOgImage = document.select("meta[property=og:image]");
	    if (metaOgImage!=null)
	        imageUrl = metaOgImage.attr("content");

	    String host = urlObject.getHost();
	    urlDataObject.setImageUrl(imageUrl);
	    urlDataObject.setTitle(title);
	    urlDataObject.setHost(host);
	    urlDataObject.setUrl(url);
	    return urlDataObject;
	}       
}
