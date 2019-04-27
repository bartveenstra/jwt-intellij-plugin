package com.github.novotnyr.idea.jwt.core;

import com.auth0.jwt.algorithms.Algorithm;
import com.github.novotnyr.idea.jwt.HS256SignatureContext;
import com.github.novotnyr.idea.jwt.SecretNotSpecifiedException;
import com.github.novotnyr.idea.jwt.validation.UnknownAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class AlgorithmResolver {
    public static final Logger logger = LoggerFactory.getLogger(Algorithm.class);

    // TODO search duplicates
    public static Algorithm resolve(String algorithmName, Object signatureContext) {
        Algorithm algorithm = null;
        switch (algorithmName) {
            case "HS256" : {
                if(signatureContext == null) {
                    throw new SecretNotSpecifiedException();
                } else if (signatureContext instanceof byte[]) {
                    byte[] byteArraySecret = (byte[]) signatureContext;
                    algorithm = Algorithm.HMAC256(byteArraySecret);
                } else if (signatureContext instanceof String) {
                    try {
                        String secret = (String) signatureContext;
                        algorithm = Algorithm.HMAC256(secret);
                    } catch (UnsupportedEncodingException e) {
                        // UTF-8 should be supported everywhere on JVM, so this
                        // won't happen
                        logger.error("Unsupported encoding", e);
                        throw new UnknownAlgorithmException(algorithmName);
                    }

                } else if (signatureContext instanceof HS256SignatureContext) {
                    try {
                        String secret = ((HS256SignatureContext) signatureContext).getSecret();
                        algorithm = Algorithm.HMAC256(secret);
                    } catch (UnsupportedEncodingException e) {
                        // UTF-8 should be supported everywhere on JVM, so this
                        // won't happen
                        logger.error("Unsupported encoding", e);
                        throw new UnknownAlgorithmException(algorithmName);
                    }
                }
                break;
            }
            default:
                throw new UnknownAlgorithmException(algorithmName);
        }
        return algorithm;
    }
}
