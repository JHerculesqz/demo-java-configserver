package com.huawei.client2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class ConfigClient2Controller {
	// #region Fields

	@Autowired
	private ConfigClient2Service configClient2Service;

	@Value("${name}")
	private String name;

	// #endregion

	// #region hello

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return this.configClient2Service.hello(this.name);
	}

	// #endregion
}
