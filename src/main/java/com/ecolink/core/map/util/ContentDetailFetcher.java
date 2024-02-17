package com.ecolink.core.map.util;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecolink.core.map.service.ContentDetailService;
import com.ecolink.core.map.service.ContentListService;

@Component
public class ContentDetailFetcher {

	private final ContentDetailService contentDetailService;
	private final ContentListService contentListService;

	@Autowired
	public ContentDetailFetcher(ContentDetailService contentDetailService, ContentListService contentListService) {
		this.contentDetailService = contentDetailService;
		this.contentListService = contentListService;
	}

	public void fetchAndStoreContentDetail() {
		List<String> contentIds = contentListService.getContentIds();

		HttpClient httpClient = HttpClients.createDefault();
		for (String contentId : contentIds) {

			try {
				URI uri =
					new URI("https",
						"//map.seoul.go.kr/smgis/apps/poi.do?cmd=getNewContentsDetail&key=90d9ef047980430297ec7fa3a8377710&theme_id=11103395&conts_id="
						+ contentId, null);
				HttpGet httpGet = new HttpGet(uri);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == 200) {
					String responseData = EntityUtils.toString(response.getEntity());

					contentDetailService.saveContentDetail(responseData);
				} else {
					System.err.println(
						"Failed to fetch data for content id: " + contentId + ", Status Code: " + statusCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}