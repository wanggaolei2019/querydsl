/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;

/**
 * AliasAwareExprFactory extends the SimpleExprFactory to return thread bound
 * alias expressions, when present
 * 
 * @author tiwe
 * @version $Id$
 */
class AliasAwarePathFactory extends SimplePathFactory {

    private final AliasFactory aliasFactory;

    public AliasAwarePathFactory(AliasFactory aliasFactory) {
        this.aliasFactory = aliasFactory;
    }

    @SuppressWarnings("unchecked")
    public <D> Expr<D> createAny(D arg) {
        Expr<D> current = (Expr<D>) aliasFactory.getCurrentAndReset();
        if (current != null) {
            return current;
        } else if (arg instanceof ManagedObject) {
            return (Expr<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            throw new IllegalArgumentException("No path mapped to " + arg);
        }
    }

    public PBoolean createBoolean(Boolean arg) {
        PBoolean rv = aliasFactory.<PBoolean> getCurrentAndReset();
        return rv != null ? rv : super.createBoolean(arg);
    }

    public PBooleanArray createBooleanArray(Boolean[] args) {
        PBooleanArray rv = aliasFactory.<PBooleanArray> getCurrentAndReset();
        return rv != null ? rv : super.createBooleanArray(args);
    }

    public <D> PEntityCollection<D> createEntityCollection(Collection<D> arg) {
        PEntityCollection<D> rv = aliasFactory.<PEntityCollection<D>> getCurrentAndReset();
        return rv != null ? rv : super.createEntityCollection(arg);
    }

    public <D extends Comparable<?>> PComparable<D> createComparable(D arg) {
        PComparable<D> rv = aliasFactory.<PComparable<D>> getCurrentAndReset();
        return rv != null ? rv : super.createComparable(arg);
    }

    public <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg) {
        PNumber<D> rv = aliasFactory.<PNumber<D>> getCurrentAndReset();
        return rv != null ? rv : super.createNumber(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg) {
        PEntity<D> rv = aliasFactory.<PEntity<D>> getCurrentAndReset();
        if (rv != null) {
            return rv;
        } else if (arg instanceof ManagedObject) {
            return (PEntity<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            return super.createEntity(arg);
        }
    }

    public <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args) {
        PComparableArray<D> rv = aliasFactory.<PComparableArray<D>> getCurrentAndReset();
        return rv != null ? rv : super.createComparableArray(args);
    }

    public <K, V> PEntityMap<K, V> createEntityMap(Map<K, V> arg) {
        PEntityMap<K, V> rv = aliasFactory.<PEntityMap<K, V>> getCurrentAndReset();
        return rv != null ? rv : super.createEntityMap(arg);
    }

    public <D> PEntityList<D> createEntityList(List<D> arg) {
        PEntityList<D> rv = aliasFactory.<PEntityList<D>> getCurrentAndReset();
        return rv != null ? rv : super.createEntityList(arg);
    }

    public PString createString(String arg) {
        PString rv = aliasFactory.<PString> getCurrentAndReset();
        return rv != null ? rv : super.createString(arg);
    }

    public PStringArray createStringArray(String[] args) {
        PStringArray rv = aliasFactory.<PStringArray> getCurrentAndReset();
        return rv != null ? rv : super.createStringArray(args);
    }

}
