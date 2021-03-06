package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URLDecoder;

public class LambdaHandler implements RequestHandler<S3Event, String> {

    private static final Logger LOG = LogManager.getLogger(LambdaHandler.class);

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        String result = "";
        try {
            S3EventNotificationRecord record = s3event.getRecords().stream().findFirst().get();

            String srcBucket = record.getS3().getBucket().getName();
            // Object key may have spaces or unicode non-ASCII characters.
            String srcKey = record.getS3().getObject().getKey()
                    .replace('+', ' ');
            srcKey = URLDecoder.decode(srcKey, "UTF-8");

            String dstBucket = srcBucket;
            String dstKey = srcKey;
            result = dstBucket + ":" + dstKey;
            LOG.info("Get information : " + result);
            return result;
        } catch (Exception e) {
            LOG.error("error:", e);
        } finally {
            return result;
        }
    }
}
