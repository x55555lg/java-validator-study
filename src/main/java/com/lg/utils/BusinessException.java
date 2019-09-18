package com.lg.utils;

/**
 * 业务异常
 *
 * @author Xulg
 * Created in 2019-05-29 12:46
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * errCode:错误码
     */
    private String code;

    /**
     * errParams:错误参数
     */
    private Object[] errParams;

    /**
     * 是否进行堆栈信息跟踪
     */
    private boolean shouldFillInStackTrace = false;

    @SuppressWarnings("WeakerAccess")
    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(String code, String msg, boolean shouldFillInStackTrace) {
        super(msg);
        this.code = code;
        this.shouldFillInStackTrace = shouldFillInStackTrace;
    }

    public BusinessException(String code, Object[] errParams) {
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String message, String code, Object[] errParams) {
        super(message);
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String message, Throwable cause, String code, Object[] errParams) {
        super(message, cause);
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(Throwable cause, String code, Object[] errParams) {
        super(cause);
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace, String code, Object[] errParams) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.errParams = errParams;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getErrParams() {
        return errParams;
    }

    public void setErrParams(Object[] errParams) {
        this.errParams = errParams;
    }

    /**
     * fillInStackTrace()方法耗费性能，重写可以提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        if (shouldFillInStackTrace) {
            synchronized (this) {
                return super.fillInStackTrace();
            }
        } else {
            return this;
        }
    }

    public static BusinessException build(String code, String message) {
        return new BusinessException(code, message);
    }
}
