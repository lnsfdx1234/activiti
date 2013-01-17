package org.activiti.controller.web.cache;

import org.activiti.extension.cache.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/story")
public class RedisController {

	@Autowired
	private RedisManager redisManager;

	@RequestMapping(value = "/redis")
	public String console(Model model) {
		long dbsize = redisManager.dbSize();
		String ping = redisManager.ping();
		
		model.addAttribute("dbsize", dbsize);
		model.addAttribute("ping", ping);
		
		return "story/redis";
	}

	@RequestMapping(value = "/redis/save")
	public String updateDefaultLogger(
			@RequestParam("redisKey") String redisKey,
			@RequestParam("redisValue") String redisValue, Model model) {
		redisManager.set(redisKey, redisValue);
		return "redirect:/story/redis";
	}

	@RequestMapping(value = "/redis/del")
	public String updateLogger(@RequestParam("redisKey") String redisKey, Model model) {
		redisManager.del(redisKey);
		return "redirect:/story/redis";
	}

}
