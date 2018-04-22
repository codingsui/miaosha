package com.syl.exception;

/**
 * 秒杀关闭异常(运行期异常)
 */
public class SeckillCloseException extends RuntimeException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
