package com.incra.sparkui.utils;

//import com.amazonaws.auth.AWSCredentials;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.math.DoubleMath;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.incra.sparkui.functional.UncheckedConsumer;
import com.incra.sparkui.functional.UncheckedFunction;
import com.incra.sparkui.functional.UncheckedRunnable;
import com.incra.sparkui.functional.UncheckedSupplier;
import org.apache.commons.configuration.Configuration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Jeff Risberg
 * @since 01/08/2016
 */
public class Standard {

    private final static Logger logger = LoggerFactory.getLogger(Standard.class);

    public static String getString(Object o, String defaultVal) {
        return Optional.ofNullable(o).map(Object::toString).orElse(defaultVal);
    }

    public static boolean getBoolean(Object obj, boolean defaultVal) {
        try {
            if(obj instanceof Boolean) {
                return ((Boolean) obj).booleanValue();
            } else if(obj instanceof Optional) {
                Optional opt = ((Optional) obj);
                return getBoolean(opt.orElseGet(() -> defaultVal), defaultVal);
            }

            return getBoolean(obj.toString(), defaultVal);
        } catch(Exception e) {
            return defaultVal;
        }
    }

    public static boolean getBoolean(String obj, boolean defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        String b = obj.toLowerCase();

        if(b.startsWith("y") || b.equals("true") || b.equals("on") || b.equals("1")) return true;
        if(b.startsWith("n") || b.equals("false") || b.equals("off")|| b.equals("0")) return false;

        return defaultVal;
    }

    public static long getLong(String str, long defaultVal) {
        try {
            return Long.parseLong(str);
        } catch(Exception e) {
            return defaultVal;
        }
    }

    public static long getLong(Object obj, long defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        if(obj instanceof String) {
            return getLong(obj.toString(), defaultVal);
        } else if(obj instanceof Long) {
            return ((Long) obj).longValue();
        } else if(obj instanceof Double) {
            return ((Double) obj).longValue();
        } else if(obj instanceof Optional) {
            Optional opt = ((Optional) obj);
            return getLong(opt.orElseGet(() -> defaultVal), defaultVal);
        }

        return getLong(obj.toString(), defaultVal);
    }

    public static double getDouble(String str, double defaultVal) {
        try {
            return Double.parseDouble(str);
        } catch(Exception e) {
            return defaultVal;
        }
    }

    public static double getDouble(Object obj, double defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        if(obj instanceof String) {
            return getDouble(obj.toString(), defaultVal);
        } else if(obj instanceof Double) {
            return ((Double) obj).doubleValue();
        } else if(obj instanceof Integer) {
            return new Double((Integer)obj);
        } else if(obj instanceof Long) {
            return new Double((Long)obj);
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal)obj).doubleValue();
        } else if(obj instanceof Optional) {
            Optional opt = ((Optional) obj);
            return getDouble(opt.orElseGet(() -> defaultVal), defaultVal);
        }

        return getDouble(obj.toString(), defaultVal);
    }

    public static Class forName(String theClass) {
        try {
            return Class.forName(theClass);
        }catch(Exception e) {
            return null;
        }
    }

    public static int getInt(Object obj, int defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        if(obj instanceof String) {
            return getInt(obj.toString(), defaultVal);
        } else if(obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else if(obj instanceof Optional) {
            Optional opt = ((Optional) obj);
            return getInt(opt.orElseGet(() -> defaultVal), defaultVal);
        }

        return getInt(obj.toString(), defaultVal);
    }

    public static int getInt(String str, int defaultVal) {
        try {
            return Integer.parseInt(str);
        } catch(Exception e) {
            logger.warn("Attempted to parse non-integer value from string: " + str);
            return defaultVal;
        }
    }

    public static String stackTraceAsString(Throwable e) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }

    public static Properties loadPropsFromClassPath(String path) {
        Properties props = new Properties();

        try {
            props.load(Standard.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties from "+path, e);
        }

        return props;
    }

    public static String getProcessId() {
        try {
            //http://stackoverflow.com/a/35885
            return ManagementFactory.getRuntimeMXBean().getName();
        } catch(Throwable e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    public static String getCurrentThreadName() {
        try {
            Thread t = Thread.currentThread();
            return t.getName() + " (" + t.getId() + ")";
        } catch(Throwable e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    public static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLocalIpAddr() {
        InetAddress localhost = getLocalHost();
        return localhost == null ? "127.0.0.1" : localhost.getHostAddress();
    }

    public static String getLocalHostname() {
        InetAddress localhost = getLocalHost();
        return localhost == null ? "localhost" : localhost.getHostName();
    }

    public static String asCapitalized(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static <T> T getInstance(Injector injector, Class<T> type, Class<? extends Annotation> option) {
        return injector.getInstance(Key.get(type, option));
    }

    public static <T> List<T> mergeLists(List<T> a, List<T> b) {
       return new ImmutableList.Builder<T>().addAll(a).addAll(b).build();
    }

    public static boolean approxEquals(double a, double b, double epsilon) {
        return DoubleMath.fuzzyEquals(a, b, epsilon);
    }

    public static boolean approxEquals(double a, double b) {
        final double EPSILON = 1E-5;

        return approxEquals(a,b, EPSILON);
    }

    private static final int DEFAULT_DELAY_SECONDS = 30;

    /**
     * @return a multiplier between 0.8 and 1.
     */
    public static double randomMultiplier() {
        return 0.8 + Math.random() * 0.2;
    }

    // calculate next exponential ms
    public static long nextExpMs(long initialTimeMs, int retryCount) {
        return nextExpMs(initialTimeMs, retryCount, randomMultiplier());
    }

    // calculate next exponential ms
    public static long nextExpMs(long initialTimeMs, int retryCount, double randomMultiplier) {
        return (long) (initialTimeMs + Math.pow(2, retryCount) * DEFAULT_DELAY_SECONDS * 1000 * randomMultiplier);
    }

    /**
     * @return true if a starts with b regardless of case
     */
    public static boolean startsWithIgnoreCase(String a, String b) {
        return a.toLowerCase().startsWith(b.toLowerCase());
    }

    public static DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }

    public static int toSecondsSinceEpoch(DateTime dateTime) {
        return (int) (dateTime.getMillis() / 1000);
    }

    public static DateTime fromSecondsSinceEpoch(int seconds) {
        return new DateTime(seconds * 1000L, DateTimeZone.UTC);
    }

    public static Runnable unchecked(UncheckedRunnable work) {
        return work::run;
    }


    public static <T,R> java.util.function.Function<T,R> unchecked(UncheckedFunction<T,R> f) {
        return f::apply;
    }

    public static <T> java.util.function.Consumer<T> unchecked(UncheckedConsumer<T> f) {
        return f::accept;
    }

    public static <T> java.util.function.Supplier<T> unchecked(UncheckedSupplier<T> f) {
        return f::get;
    }

    public static String checkNotNullAndNotEmpty(String str) throws NullPointerException {
        if(str == null || str.isEmpty()) {
            throw new NullPointerException();
        }
        return str;
    }

    //public static String constructAwsCredentialsString(AWSCredentials credentials) {
    //    return String.format("aws_access_key_id=%s;aws_secret_access_key=%s",
    //        credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey());
    //}

    public static String join(Collection<String> coll) {
        return coll.stream().map(String::toString).collect(Collectors.joining(" "));
    }

    public static String join(Collection<String> coll, String delim) {
        return coll.stream().map(String::toString).collect(Collectors.joining(delim));

    }

    public static Properties toStringProperties(Configuration configuration) {
        Properties props = new Properties();
        Sets.newHashSet(configuration.getKeys()).forEach(k -> {
            props.setProperty(k, configuration.getString(k));
        });
        return props;
    }

    public static <T> T coalesce(T... values) {
        if(values == null || values.length == 0) {
            return null;
        }

        for(T t: values) {
            if(t != null) {
                return t;
            }
        }
        return null;
    }

    public static String prettyTimeInterval(long elapsedTime) {
        double t = elapsedTime / 1000.0;

        if(t < 60) {
            return String.format("%.2f %s", t, " secs");
        }

        t /= 60;

        if(t < 60) {
            return String.format("%.2f %s", t, " mins");
        }

        t /= 60;

        if(t < 24) {
            return String.format("%.2f %s", t, " hrs");
        }

        t /= 24;

        return String.format("%.2f %s", t, " days");
    }

    public static <T> Optional<T> headOption(List<T> list) {
        if(list != null && list.size() > 0) {
            return Optional.ofNullable(list.get(0));
        }
        return Optional.empty();
    }

}
