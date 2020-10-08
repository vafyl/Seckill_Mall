package com.zh.api.utils;

import java.text.SimpleDateFormat;
import org.joda.time.DateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Space_Pig
 * @date 2020/10/05 15:59
 */
public class RandomNumbers {

    //精确到毫秒级别处理高并发问题
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSS");
    public static final ThreadLocalRandom  THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    /**
     * 采用时间戳+N位随机流水号 生产订单唯一编号
     * @return
     */
    public static String generateOrderCode(){
        return DATE_FORMAT.format(DateTime.now().toDate())+generateNumber(4);
    }

    public static String generateNumber(final int num){
        //线程安全
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= num; i++) {
            sb.append(THREAD_LOCAL_RANDOM.nextInt(9));
        }
        return sb.toString();
    }


}
