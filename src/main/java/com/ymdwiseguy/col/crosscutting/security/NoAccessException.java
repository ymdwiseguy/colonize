package com.ymdwiseguy.col.crosscutting.security;

import com.ymdwiseguy.col.crosscutting.exceptions.ServiceException;

public class NoAccessException extends ServiceException {
    public NoAccessException(String message) {
        super(message);
    }
}
