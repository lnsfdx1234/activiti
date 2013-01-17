/**
 * 
 */
package org.activiti.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenlg
 *
 */
@Controller
@RequestMapping(value = "/story/home")
public class HomeController {
	
	@RequestMapping(value = "")
	public String list() { 
		return "home";
	}

}
