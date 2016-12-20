package com.huawei.client1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RefreshScope
@Controller
public class ConfigClient1Controller {
	// #region Fields

	@Autowired
	private ConfigClient1Service configClientService;

	@Value("${name}")
	private String name;

	// #endregion

	// #region hello

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return this.configClientService.hello(this.name);
	}

	// #endregion
}
