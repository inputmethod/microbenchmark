package com.company;

import java.awt.*;

public class Main {
    interface CallBack<Params, Return> {
        Return invoke(Params... obj);
    }

    private static CallBack<Void, Integer> callBack = new CallBack<Void, Integer>() {
        @Override
        public Integer invoke(Void... obj) {
            return iconColor();
        }
    };

    private static CallBack newCallback() {
        return new CallBack<Void, Integer>() {
            @Override
            public Integer invoke(Void... obj) {
                return iconColor();
            }
        };
    }

    public static int iconColor() {
        return 0xFFFF4096;
    }

    private static int getColorIntDefault(String keyname, int defaultColor) {
        return defaultColor;
    }

    private static int getColorByKeyName(String keyName, CallBack<Void, Integer> callBack, int def) {
        int color = getColorIntDefault(keyName, def);
        if (color == def) {
            color = callBack.invoke();
        }

        return color;
    }

    private static int testMethodWithCallBack(int def) {
        int result = getColorByKeyName("test", /*newCallback()*/callBack, def);
        return result;
    }

    private static int getColorByKeyNameSimple(String keyName, int def) {
        int color = getColorIntDefault(keyName, def);
        if (color == def) {
            color = iconColor();
        }

        return color;
    }

    private static int testMethodSimple(int def) {
        int result = getColorByKeyNameSimple("test", def);
        return result;
    }

    private static int executeTestCases1(int loopCount) {
        long runTimeOfCallback;

        long start;

        start = System.nanoTime();
        int r = 0;
        for (int i = 0; i < loopCount; i++) {
            for (int j = 0; j < loopCount; j++) {
                r += testMethodWithCallBack(i+r);
            }
        }
        runTimeOfCallback = System.nanoTime() - start;
        System.out.print("Consumed nano(callback) " + runTimeOfCallback + " loop count = " + loopCount + "x\n");
        return r;
    }

    private static int executeTestCases2(int loopCount) {
        int r = 0;
        long runTimeOfSimple;
        long start = System.nanoTime();
        for(int i = 0; i < loopCount; i++) {
            for (int j = 0; j < loopCount; j++) {
                r += testMethodSimple(i + r);
            }
        }
        runTimeOfSimple = System.nanoTime() - start;

        System.out.print("Consumed nano(no callback) " + runTimeOfSimple + " loop count = " + loopCount + "x\n");
        return r;
    }

    public static void main(String[] args) {
        // write your code here
        final int loopCount = 100000;

        int count = 6;
        for (int i = 0; i < count; i++) {
            executeTestCases1(loopCount);
        }

        for (int i = 0; i < count; i++) {
            executeTestCases2(loopCount);
        }
    }
}
