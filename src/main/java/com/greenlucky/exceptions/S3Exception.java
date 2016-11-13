package com.greenlucky.exceptions;

/**
 * Created by Mr Lam on 13/11/2016.
 */
public class S3Exception extends RuntimeException {
    public S3Exception(Throwable e) {
        super(e);
    }

    public S3Exception(String s) {
        super(s);
    }
}
