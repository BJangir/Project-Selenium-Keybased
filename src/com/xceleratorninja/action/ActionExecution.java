package com.xceleratorninja.action;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ActionExecution {

	final static Logger logger = Logger.getLogger(ActionExecution.class);

	public boolean executeAction(TestStepModel tm) {
		/*
		 * String precondition_action = tm.getTestcase_action();
		 * if(!precondition_action.equalsIgnoreCase("NA")){
		 * executeActionSequence(precondition_action); }
		 */

		List<Object> executeActionSequence2 = executeActionSequence(tm);

		String testcase_action = tm.getTestcase_action();
		boolean testcasestatus = false;

		switch (testcase_action) {
		case "isTextPresentInPage":
			String res = (String) executeActionSequence2.get(0);
			logger.info(" isTextPresentInPage result is " + res);
			testcasestatus = Boolean.parseBoolean(res);
			break;

		case "countLinks":

			String result = (String) executeActionSequence2.get(0);
			logger.info(" Total actual count of links is" + result);

			int expectedlinkCount;
			if (tm.getdata().contains(".")) {
				String substring = tm.getdata().substring(0, tm.getdata().indexOf("."));
				expectedlinkCount = Integer.parseInt(substring);
			} else {
				expectedlinkCount = Integer.parseInt(tm.getdata());
			}
			String expectedtotalCount = String.valueOf(expectedlinkCount);

			logger.info(" Total expected count of links is  " + expectedtotalCount);
			testcasestatus = result.equalsIgnoreCase(expectedtotalCount) ? true : false;
			break;

		case "getTitle":

			String resultitle = (String) executeActionSequence2.get(0);
			logger.info(" title of the page is " + resultitle);
			String expectedtitle = tm.getdata();

			testcasestatus = resultitle.equalsIgnoreCase(expectedtitle) ? true : false;
			break;

		case "getBrokenLinks":

			String resultBrokenLinks = (String) executeActionSequence2.get(0);
			logger.info(" Total actual count of  broken links is " + resultBrokenLinks);
			int expectedValLinks = 0;
			if (tm.getdata().contains(".")) {
				String substring = tm.getdata().substring(0, tm.getdata().indexOf("."));
				expectedValLinks = Integer.parseInt(substring);
			} else {
				expectedValLinks = Integer.parseInt(tm.getdata());
			}
			String expectedBrokenLinks = String.valueOf(expectedValLinks);
			logger.info(" Total expected count of  broken links is " + expectedBrokenLinks);
			testcasestatus = resultBrokenLinks.equalsIgnoreCase(expectedBrokenLinks) ? true : false;
			break;
		default:
			break;
		}

		return testcasestatus;
		// Assert.assertEquals((Boolean)executeActionSequence.get(0),
		// Boolean.parseBoolean(tm.getExpectation_check()));

	}

	public List<Object> executeActionSequence(TestStepModel tm) {

		String action = tm.getTestcase_action();
		List<Object> result = new ArrayList<>();
		try {
			Method declaredMethod;
			Actions actions = new Actions();
			declaredMethod = Actions.class.getDeclaredMethod(action, TestStepModel.class);
			Object invoke = declaredMethod.invoke(actions, tm);
			if (null != invoke) {
				result.add((String) invoke);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	String getMethodName(String actionText) {
		if (actionText.contains("(") && actionText.contains(")")) {
			return actionText.substring(0, actionText.indexOf("("));
		} else {
			return actionText;
		}
	}

	boolean isMethodHasParamter(String actionText) {

		return null != actionText ? true : false;

	}

	String getMethodParamter(String actionText) {
		String substring = actionText.substring(actionText.indexOf("("), actionText.indexOf(""));
		String[] split = substring.split(",");
		if (actionText.contains("(") && actionText.contains(")")) {
			return actionText.substring(0, actionText.indexOf("("));
		} else {
			return actionText;
		}
	}

	boolean isClassPresent(String classname) {
		try {
			Class.forName("com.automation.page.welcome." + classname);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}

	}
}
