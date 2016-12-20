package com.huawei.client1;

import org.springframework.stereotype.Service;

import com.huawei.client1.provider.MainProvider;

@Service
public class ConfigClient1Service {
	// #region hello

	public String hello(String strName) {
		return MainProvider.hello(strName);
	}

	// #endregion
}
