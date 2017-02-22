package com.incra.sparkui.template;

import com.github.jknack.handlebars.Options;
import com.incra.sparkui.utils.Standard;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.sql.Timestamp;

/**
 * http://jknack.github.io/handlebars.java/helpers.html
 *
 * @author Jeff Risberg
 * @since 2016-01-22
 */
public class MyTemplateHelpers {
    /*
    public static CharSequence prettyActivityStatus(ActivityStatus status) {
        if(status == null) {
            return "";
        }
        switch(status) {
            case READY: return "Pending";
            case INPROGRESS: return "Open";
            case ERROR: return "Error";
            case DONE: return "Done";
            default: return "Unknown";
        }
    }

    public static CharSequence prettyLifecycleStatus(ActivityLifecycleStatus status) {
        if(status == null) {
            return "";
        }
        switch(status) {
            case INPROGRESS: return "Open";
            case ERROR: return "Error";
            case DONE: return "Done";
            default: return "Unknown";
        }
    }

    public static CharSequence prettyActivityType(Byte activityType) {
        if(activityType == null) {
            return "";
        }
        switch (activityType) {
            case 1: return "BidExport";
            default: return "Unknown";
        }
    }
    */

    public static String hideNegativeInteger(Integer i) {
        if (i == null) {
            return "";
        }

        if (i < 0) {
            return "";
        }

        return String.valueOf(i);
    }

    private static long sqlTsToUtc(Timestamp sqlTs) {
        return new LocalDateTime(sqlTs).toDateTime(DateTimeZone.UTC).getMillis();
    }

    private static String updatedSince(long updatedTimed) {
        long currTime = System.currentTimeMillis();
        long elapsedTime = currTime - updatedTimed;

        return Standard.prettyTimeInterval(elapsedTime) + " ago";
    }

    /*
    public static CharSequence activityUpdatedSince(ActivityRecord activity) {
        if(activity == null) {
            return "";
        }

        return updatedSince(sqlTsToUtc(activity.getUpdatedTime()));
    }

    public static CharSequence lifecycleUpdatedSince(ActivityLifecycleRecord lifecycle) {
        if(lifecycle == null) {
            return "";
        }

        return updatedSince(sqlTsToUtc(lifecycle.getUpdatedTime()));
    }
    */

    private static String runningTime(long startTime, long endTime) {
        long elapsedTime = endTime - startTime;

        return Standard.prettyTimeInterval(elapsedTime);
    }

    public static CharSequence possiblyBigText(String text, Options options) {
        int maxLength = options.param(0, 15);
        if (text.length() < maxLength) {
            return text;
        }

        String shortText = text.substring(0, maxLength) + "...";

        return "<div class='qtip-tooltip-trigger'>" + shortText + "</div>" +
                "<div style='display:none;' class='qtip-tooltip' ><pre>" + text + "</pre></div>";
    }

}
