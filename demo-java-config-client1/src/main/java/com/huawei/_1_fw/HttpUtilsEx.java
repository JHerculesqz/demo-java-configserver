package com.huawei._1_fw;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpUtilsEx {
	// #region postByTemplate

	public static <T> T postByTemplate(String strUrl, Object oPostParam, Class<T> oResponseClazz) {
		RestTemplate oRestTemplate = new RestTemplate();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		oRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<Object> oRequest = new HttpEntity<Object>(oPostParam, headers);
		return oRestTemplate.postForObject(strUrl, oRequest, oResponseClazz);
	}

	// #endregion
}
