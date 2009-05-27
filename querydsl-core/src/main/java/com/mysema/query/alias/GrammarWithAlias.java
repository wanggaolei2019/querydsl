/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PathMetadata;

/**
 * GrammarWithAlias extends the base grammar to include alias factory methods
 * 
 * @author tiwe
 * @version $Id$
 */
public class GrammarWithAlias extends Grammar {

    private static final AliasFactory aliasFactory = new SimpleAliasFactory();

    private static final PathFactory pathFactory = new AliasAwarePathFactory(aliasFactory);

    private static final PSimple<Object> it = new PSimple<Object>(Object.class,PathMetadata.forVariable("it"));

    public static <A> A alias(Class<A> cl, String var) {
        return aliasFactory.createAliasForVar(cl, var);
    }

    public static <A> A alias(Class<A> cl, Expr<? extends A> expr) {
        return aliasFactory.createAliasForExpr(cl, expr);
    }

    public static void resetAlias() {
        aliasFactory.reset();
    }

    public static PBoolean $(Boolean arg) {
        return pathFactory.createBoolean(arg);
    }

    public static <D extends Comparable<?>> PComparable<D> $(D arg) {
        return pathFactory.createComparable(arg);
    }

    public static PNumber<BigDecimal> $(BigDecimal arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<BigInteger> $(BigInteger arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Byte> $(Byte arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Double> $(Double arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Float> $(Float arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Integer> $(Integer arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Long> $(Long arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Short> $(Short arg) {
        return pathFactory.createNumber(arg);
    }

    public static PString $(String arg) {
        return pathFactory.createString(arg);
    }

    public static PBooleanArray $(Boolean[] args) {
        return pathFactory.createBooleanArray(args);
    }

    public static <D extends Comparable<?>> PComparableArray<D> $(D[] args) {
        return pathFactory.createComparableArray(args);
    }

    public static PStringArray $(String[] args) {
        return pathFactory.createStringArray(args);
    }

    public static <D> PEntityCollection<D> $(Collection<D> args) {
        return pathFactory.createEntityCollection(args);
    }

    public static <K, V> PEntityMap<K, V> $(Map<K, V> args) {
        return pathFactory.createEntityMap(args);
    }

    public static <D> PEntityList<D> $(List<D> args) {
        return pathFactory.createEntityList(args);
    }

    public static <D> PEntity<D> $(D arg) {
        return pathFactory.createEntity(arg);
    }

    public static <D> Expr<D> getAny(D arg) {
        return pathFactory.createAny(arg);
    }

    @SuppressWarnings("unchecked")
    public static <D> PSimple<D> $() {
        return (PSimple<D>) it;
    }

}
