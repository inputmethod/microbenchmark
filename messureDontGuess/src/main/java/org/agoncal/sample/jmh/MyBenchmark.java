/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.agoncal.sample.jmh;

import org.openjdk.jmh.annotations.Benchmark;

// precondition: install maven and then create project:
// mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh \
//      -DarchetypeArtifactId=jmh-java-benchmark-archetype -DarchetypeVersion=1.4.1 \
//              -DgroupId=org.agoncal.sample.jmh -DartifactId=logging -Dversion=1.0
//
// run command: mvn clean install && java -jar target/benchmarks.jar
// SA: http://www.hollischuang.com/archives/1072
//     http://www.importnew.com/12548.html
//     http://irfen.me/java-jmh-simple-microbenchmark/
//     https://stackoverflow.com/questions/504103/how-do-i-write-a-correct-micro-benchmark-in-java
public class MyBenchmark {

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

    private static int getColorByKeyNameSimple(String keyName, int def) {
        int color = getColorIntDefault(keyName, def);
        if (color == def) {
            color = iconColor();
        }

        return color;
    }

    @Benchmark
    public int testMethodSimple() {
        int result = getColorByKeyNameSimple("test", 0);
        return result;
    }


    @Benchmark
    public int testMethodWithCallBack() {
        return getColorByKeyName("test", /*newCallback()*/callBack, 0);
    }
}
