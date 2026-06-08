package com.phannhubao.logintest.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestResultListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.printf("[RUN]  %s%n", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.printf("[PASS] %s (%d ms)%n", result.getMethod().getMethodName(), elapsedTime(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.printf("[FAIL] %s (%d ms): %s%n",
                result.getMethod().getMethodName(),
                elapsedTime(result),
                result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf("[SKIP] %s%n", result.getMethod().getMethodName());
    }

    private long elapsedTime(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
