package com.huawei.client1.provider;

import com.huawei._1_fw.HttpUtilsEx;

public class MainProvider {
	// #region Const

	private static final String URL_CLIENT2 = "http://ShitConfigClient2:8000/hello";

	// #endregion

	// #region hello1

	public static String hello(String strName) {
		// 1.get client2 name
		String strClient2Name = HttpUtilsEx.postByTemplate(URL_CLIENT2, null, String.class);

		// 2.return name+client2Name
		String strRes = strName + "\n" + strClient2Name;
		return strRes;
	}

	// #endregion
}
