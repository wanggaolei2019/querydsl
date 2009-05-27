/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class CNumber<T extends Number & Comparable<?>> extends
        ENumber<T> implements Custom<T> {
    public CNumber(Class<T> type) {
        super(type);
    }

    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }
}