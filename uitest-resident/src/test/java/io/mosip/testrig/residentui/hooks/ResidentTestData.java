package io.mosip.testrig.residentui.hooks;

import io.mosip.testrig.apirig.utils.NotificationListener;
import io.mosip.testrig.residentui.config.ResidentUtil;
import io.mosip.testrig.residentui.config.ResidentConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Holds generated or pre-configured UIN and VIDs for the test run.
 * Call {@link #ensureReady()} once before tests use these values.
 */
public class ResidentTestData {

    private static final Logger logger = LoggerFactory.getLogger(ResidentTestData.class);
    private static final AtomicBoolean ready = new AtomicBoolean(false);

    public static volatile String uin = "";
    public static volatile String perpetualVid = "";
    public static volatile String temporaryVid = "";
    public static volatile String oneTimeVid = "";

    /**
     * Email actually registered against the current identity — use this for OTP fetches
     * (login, Get My UIN, Verify Email, Update Email revert), never resident.properties'
     * externalemail directly. A dynamically generated identity is created with
     * valueMapping.properties' "email", not externalemail — that's only correct for the
     * pre-configured (identityGenManual=yes) identity.
     */
    public static volatile String loginEmail = "";

    /**
     * Whatever loginEmail held right before the Update My Data email-change scenario replaced
     * it. The backend takes an unknown amount of time to start sending to the new address, so a
     * later OTP wait can listen on both candidates at once instead of guessing a fixed delay.
     */
    public static volatile String previousLoginEmail = "";

    public static synchronized void ensureReady() {
        if (ready.get()) return;
        if (ResidentConfigManager.isIdentityGenManual()) {
            uin = ResidentConfigManager.getUin();
            perpetualVid = ResidentConfigManager.getPerpetualVid();
            temporaryVid = ResidentConfigManager.getTemporaryVid();
            oneTimeVid = ResidentConfigManager.getOneTimeVid();
            loginEmail = ResidentConfigManager.getExternalEmail();
            logger.info("Using pre-configured test identities");
        } else {
            logger.info("Generating UIN and VIDs via MOSIP APIs ...");
            uin = ResidentUtil.generateUIN();
            if (uin.isEmpty()) throw new IllegalStateException("UIN generation failed — check MOSIP API connectivity");
            perpetualVid = ResidentUtil.generateVID(uin, "PERPETUAL");
            temporaryVid = ResidentUtil.generateVID(uin, "TEMPORARY");
            oneTimeVid = ResidentUtil.generateVID(uin, "onetimeuse"); // API's vidType enum value — "ONCE" is rejected
            loginEmail = ResidentConfigManager.getproperty("email"); // matches buildAddIdentityRequest()
            logger.info("Generated UIN ...{}", uin.length() > 4 ? uin.substring(uin.length() - 4) : uin);

            long settleMillis = ResidentConfigManager.getIdentitySettleMillis();
            if (settleMillis > 0) {
                logger.info("Waiting {} ms for the new identity to propagate through MOSIP before logging in", settleMillis);
                try {
                    Thread.sleep(settleMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        ready.set(true);
    }

    /** Alphanumeric-only label used as a reason/text field value in tests.
     *  No underscores — some portal fields reject non-alphanumeric characters. */
    public static String testLabel() {
        return "Test" + LocalDate.now().toString().replace("-", "");
    }

    /**
     * A fresh, never-before-used mailinator address for the Update My Data email-change
     * scenario. OTP delivery is keyed off the "to" address of whatever notification the
     * environment sends, so any address works; a timestamped one also gives each run its own
     * inbox instead of reusing one static address every time.
     */
    public static String generateUniqueEmail() {
        return "resident.ui." + System.currentTimeMillis() + "@mailinator.com";
    }

    /**
     * Polls for an OTP on {@link #loginEmail} and {@link #previousLoginEmail} in parallel and
     * returns whichever actually receives it, since a real message short-circuits
     * NotificationListener.getOtp() well before its timeout — if the first poll to finish comes
     * back empty, the other is given a chance too before giving up.
     */
    public static String getOtpFromCurrentOrPreviousEmail() {
        String current = loginEmail;
        String previous = previousLoginEmail;
        if (previous == null || previous.isBlank() || previous.equalsIgnoreCase(current)) {
            return NotificationListener.getOtp(current);
        }

        logger.info("Polling both the current ({}) and previous ({}) registered email for the OTP — "
                + "unsure yet which one the backend is actually sending to", current, previous);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        try {
            completionService.submit(() -> NotificationListener.getOtp(current));
            completionService.submit(() -> NotificationListener.getOtp(previous));
            for (int i = 0; i < 2; i++) {
                String otp = completionService.take().get();
                if (otp != null && !otp.isEmpty()) return otp;
            }
            return "";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while polling for OTP from either email", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Error while polling for OTP from either email", e.getCause());
        } finally {
            executor.shutdownNow();
        }
    }
}
