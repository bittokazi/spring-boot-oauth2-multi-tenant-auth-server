package com.bittokazi.oauth2.auth.server.utils;

import com.zaxxer.hikari.HikariConfig;

import java.io.*;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Bitto Kazi
 */

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static HikariConfig getDbConfig(String dataSourceClassName, String url, String port, String databaseName,
                                           String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceClassName);
        config.addDataSourceProperty("serverName", url);
        config.addDataSourceProperty("portNumber", port);
        config.addDataSourceProperty("databaseName", databaseName);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMinimumIdle(2);
        config.setIdleTimeout(120000);
        config.setMaximumPoolSize(5);
        return config;
    }

    public static byte[] convertFileToByte(final File file) {
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray); // read file into bytes[]
            fis.close();
        } catch (IOException e) {
            logger.error("ERROR converting file to byte ", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("ERROR closing file input stream ", e);
                }
            }
        }
        return bytesArray;
    }

    public static String randomNumberGenerator(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static Integer randomIntGenerator(int length) {
        String SALTCHARS = "123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return Integer.valueOf(saltStr);
    }

    @SuppressWarnings("unchecked")
    public static <T> Specification<T> searchSpecificationBuilder(String search, Class<?> c) {
        try {

            Object builder = Class.forName(c.getName()).getDeclaredConstructor().newInstance();
            Method method = null;
            for (Method m : builder.getClass().getMethods()) {
                if (m.getName().equals("with"))
                    method = m;
            }
            method.setAccessible(true);

            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)(:|<|>|=|~)([^,]+),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                method.invoke(builder, matcher.group(1), matcher.group(2), matcher.group(3));
            }

            Method methodBuild = builder.getClass().getMethod("build");
            methodBuild.setAccessible(true);
            return (Specification<T>) methodBuild.invoke(builder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate getCurrentMonthFirstDate() {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).withDayOfMonth(1);
    }

    public static LocalDate getCurrentMonthLastDate() {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).plusMonths(1).withDayOfMonth(1)
                .minusDays(1);
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.ofEpochDay(System.currentTimeMillis());
    }

    public static Date getNextDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(sdf.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String entryCouponGenerator(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String formatFileName(String fileName) {
        String pattern = "([A-Z,a-z,.,0-9,_,-])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileName);
        String newFileName = "";
        while (m.find()) {
            newFileName += m.group(1);
        }
        return Utils.randomNumberGenerator(30) + newFileName;
    }

    public static String getMD5(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(data.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString((int) (b & 0xff)));
        }
        return sb.toString();
    }

    public static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

}
