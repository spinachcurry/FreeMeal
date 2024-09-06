package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class WebController {
	
	@GetMapping("/main")
	public String main() {
		return "main";
	}
}
