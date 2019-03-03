package com.study.test;

import com.study.annotation.DIYController;

public class Test {

	public static void main(String[] args) {
		try {
//			System.out.println(DIYController.class.getName());
			Class.forName("com.study.annotation.DIYController");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
