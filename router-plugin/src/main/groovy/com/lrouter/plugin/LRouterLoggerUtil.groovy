package com.lrouter.plugin

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

/**
 * @author lqc
 * @version 1.0.0
 * @desc 日志工具类
 * @date 2019/7/19 22:31
 */
class LRouterLoggerUtil {
    static Logger logger

    static void make(Logger l) {
        logger =l
    }

    static void i(String info) {
        if (null != logger) {
            logger.log(LogLevel.INFO,"Router::Register >>> " + info)
        }else {
            println("Router::Register >>> " + info)
        }
    }

    static void e(String error) {
        if (null != logger) {
            logger.log(LogLevel.ERROR,"Router::Register >>> " + error)
        }else {
            println("Router::Register >>> " + error)
        }

    }

    static void w(String warning) {
        if ( null != logger) {
            logger.log(LogLevel.WARN,"Router::Register >>> " + warning)
        }else {
            println("Router::Register >>> " + warning)
        }
    }
}
