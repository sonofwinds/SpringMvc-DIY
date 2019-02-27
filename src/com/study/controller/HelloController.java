package com.study.controller;

import com.study.annotation.DIYController;
import com.study.annotation.DIYMapping;

@DIYController
public class HelloController {
	@DIYMapping(value = "hello")
	public String doHello() {
         return "hello";
	}
}
