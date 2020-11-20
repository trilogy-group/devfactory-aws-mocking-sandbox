package com.smockin.admin.service.utils.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AwsCredentialsProvider {
    private final static Logger logger = LoggerFactory.getLogger(AwsCredentialsProvider.class);

    private final Map<String, AwsCredentials> credentials = new HashMap<>();
    private final AwsProfile defaultProfile;

    private static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
    private static final String AWS_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY";
    private static final String AWS_DEFAULT_REGION = "AWS_DEFAULT_REGION";

    public AwsCredentialsProvider(AwsProfileProvider awsProfileProvider) throws IOException {
        // check environment variables first
        final String awsAccessKeyFromEnv = System.getenv(AWS_ACCESS_KEY_ID);
        final String awsSecretAccessKeyFromEnv = System.getenv(AWS_SECRET_ACCESS_KEY);
        final String awsRegionFromEnv = System.getenv(AWS_DEFAULT_REGION);

        if (awsAccessKeyFromEnv != null && awsSecretAccessKeyFromEnv != null && awsRegionFromEnv != null) {
            this.defaultProfile = new AwsProfile("default",
                    awsAccessKeyFromEnv, awsSecretAccessKeyFromEnv,
                    awsRegionFromEnv
            );
            add(defaultProfile);
            logger.info("Loaded credentials data for [" + awsAccessKeyFromEnv + "] from environment variables");
        } else {

            // then, if not found in env variables, check credentials file
            this.defaultProfile = awsProfileProvider.findDefaultProfile();
            if (defaultProfile != null) {
                add(defaultProfile);
            }
        }
    }

    public AwsProfile getDefaultProfile() {
        return defaultProfile;
    }

    public final void add(AwsProfile profile) {
        add(profile.getAwsAccessKey(), profile.getAwsSecretKey(), null);
    }

    public final void add(String awsAccessKey, String awsSecretKey, String securityToken) {
        logger.debug("Storing new credentials: [" + awsAccessKey + "]: [" + awsSecretKey + "]");
        credentials.put(awsAccessKey, new AwsCredentials(awsSecretKey, securityToken));
    }

    public String getSecretKey(String awsAccessKey) {
        if (!credentials.containsKey(awsAccessKey)) {
            logger.debug("No credentials registered for: [" + awsAccessKey + "]");
            return null;
        }
        String awsSecretKey = credentials.get(awsAccessKey).getSecretKey();
        logger.debug("SecretKey for [" + awsAccessKey + "]: [" + awsSecretKey + "]");
        return awsSecretKey;
    }

    public String getSecurityToken(String awsAccessKey) {
        String securityToken = credentials.get(awsAccessKey).getSecurityToken();
        logger.debug("SecurityToken for [" + awsAccessKey + "]: [" + securityToken + "]");
        return securityToken;
    }

    private static class AwsCredentials {
        private final String secretKey;
        private final String securityToken;

        public AwsCredentials(String secretKey, String securityToken) {
            this.secretKey = secretKey;
            this.securityToken = securityToken;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public String getSecurityToken() {
            return securityToken;
        }
    }
}