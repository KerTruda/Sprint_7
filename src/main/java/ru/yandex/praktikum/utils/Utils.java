package ru.yandex.praktikum.utils;

import java.util.Random;

public class Utils {

    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; ++i) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }

        return buffer.toString();
    }

    public static int randomInt(int max,int min){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String randomPhoneNumber(){

        int num1, num2, num3;
        int set2, set3;

        Random generator = new Random();

        num1 = generator.nextInt(7) + 1;
        num2 = generator.nextInt(8);
        num3 = generator.nextInt(8);

        set2 = generator.nextInt(643) + 100;

        set3 = generator.nextInt(8999) + 1000;

        String randomPhoneNumber = "+7 (" + num1 + "" + num2 + "" + num3 + ")" + "-" + set2 + "-" + set3 ;

        return randomPhoneNumber;
    }
    public static String randomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        String randomDate = year + "-" + month + "-" + day;
        return randomDate;
    }
    public static int createRandomIntBetween(int start, int end){
        return start + (int) Math.round(Math.random() * (end - start));
    }
}
