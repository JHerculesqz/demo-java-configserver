package com.huawei.client2;

import org.springframework.stereotype.Service;

import com.huawei.client2.provider.MainProvider;

@Service
public class ConfigClient2Service {
	// #region hello

	public String hello(String strName) {
		return MainProvider.hello(strName);
	}

	// #endregion
}
