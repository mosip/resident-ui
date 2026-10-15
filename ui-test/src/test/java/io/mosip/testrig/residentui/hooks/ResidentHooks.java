package io.mosip.testrig.residentui.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.mosip.testrig.residentui.config.ResidentConfigManager;
import io.mosip.testrig.uitestrig.core.MosipTestDataManager;
import io.mosip.testrig.uitestrig.listeners.CucumberHooks;
import io.mosip.testrig.uitestrig.reporting.UiExtentReportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ResidentHooks {

    private static final Logger logger = LoggerFactory.getLogger(ResidentHooks.class);

    // Only each worker thread's first scenario needs staggering — by its next scenario,
    // timing has already drifted apart from the other threads.
    private static final AtomicInteger nextWorkerSlot = new AtomicInteger(0);
    private static final ThreadLocal<Boolean> staggered = ThreadLocal.withInitial(() -> false);

    @BeforeAll(order = 5)
    public static void initSuite() {
        ResidentConfigManager.init();
        MosipTestDataManager.initialize();
        UiExtentReportManager.initReport();
        CucumberHooks.startOtpListener();
        logger.info("Resident suite ready — base URL: {}", ResidentConfigManager.getBaseUrl());
    }

    @BeforeAll(order = 10)
    public static void initTestData() {
        ResidentTestData.ensureReady();
        logger.info("Resident test identities ready — UIN: ...{}",
                ResidentTestData.uin.length() > 4
                        ? ResidentTestData.uin.substring(ResidentTestData.uin.length() - 4)
                        : ResidentTestData.uin);
    }

    @Before
    public void scenarioSetup(Scenario scenario) {
        if (!staggered.get()) {
            staggered.set(true);
            long delay = nextWorkerSlot.getAndIncrement() * ResidentConfigManager.getScenarioStaggerMillis();
            if (delay > 0) {
                logger.info("Staggering this worker thread's first scenario by {} ms", delay);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        CucumberHooks.setupScenario(scenario);
    }

    @BeforeStep
    public void stepSetup() {
        CucumberHooks.beforeStep();
    }

    @After
    public void scenarioTeardown(Scenario scenario) {
        CucumberHooks.teardownScenario(scenario);
    }

    @AfterAll
    public static void teardownSuite() {
        CucumberHooks.teardownSuite();
    }
}