/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.GrammarWithAlias.$;
import static com.mysema.query.functions.MathFunctions.add;
import static com.mysema.query.functions.MathFunctions.div;
import static com.mysema.query.functions.MathFunctions.mult;
import static com.mysema.query.functions.MathFunctions.sub;
import static com.mysema.query.hql.HQLGrammar.current_date;
import static com.mysema.query.hql.HQLGrammar.current_time;
import static com.mysema.query.hql.HQLGrammar.current_timestamp;
import static com.mysema.query.hql.HQLGrammar.day;
import static com.mysema.query.hql.HQLGrammar.hour;
import static com.mysema.query.hql.HQLGrammar.maxelement;
import static com.mysema.query.hql.HQLGrammar.maxindex;
import static com.mysema.query.hql.HQLGrammar.minelement;
import static com.mysema.query.hql.HQLGrammar.minindex;
import static com.mysema.query.hql.HQLGrammar.minute;
import static com.mysema.query.hql.HQLGrammar.month;
import static com.mysema.query.hql.HQLGrammar.second;
import static com.mysema.query.hql.HQLGrammar.sum;
import static com.mysema.query.hql.HQLGrammar.year;
import static com.mysema.query.types.Grammar.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * FeaturesTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FeaturesTest {

    // AuditLog
    QAuditLog log = new QAuditLog("log");

    // QCat
    QCat cat = new QCat("cat");
    QCat cat1 = new QCat("cat1");
    QCat cat2 = new QCat("cat2");
    QCat cat3 = new QCat("cat3");
    QCat cat4 = new QCat("cat4");
    QCat cat5 = new QCat("cat5");

    QCat kitten = new QCat("kitten");
    QCat kitten2 = new QCat("kitten2");
    QCat child = new QCat("child");
    QCat mate = new QCat("mate");

    // QCatalog
    QCatalog catalog = new QCatalog("catalog");

    // QCompany
    QCompany company = new QCompany("company");
    QCompany company1 = new QCompany("company1");
    QCompany company2 = new QCompany("company2");
    QCompany company3 = new QCompany("company3");
    QCompany company4 = new QCompany("company4");
    QCompany company5 = new QCompany("company5");

    // Customer
    QCustomer cust = new QCustomer("cust");

    // QDocument
    QDocument doc = new QDocument("doc");

    // DomesticQCat
    QDomesticCat domesticCat = new QDomesticCat("domesticCat");

    // QItem
    QItem item = new QItem("item");

    // Order
    QOrder order = new QOrder("order");

    // Payment
    QPayment payment = new QPayment("payment");

    // Price
    QPrice price = new QPrice("price");

    // Product
    QProduct product = new QProduct("product");

    // User
    QUser user = new QUser("user");
    QUser user1 = new QUser("user1");
    QUser user2 = new QUser("user2");
    QUser user3 = new QUser("user3");
    QUser user4 = new QUser("user4");
    QUser user5 = new QUser("user5");

    private HQLSerializer visitor = new HQLSerializer(new HQLPatterns());

    @Test
    public void testDomainConstruction() {
        QInheritatedProperties i = new QInheritatedProperties("i");
        assertNotNull(i.superclassProperty);
        assertNotNull(i.classProperty);

        QAccount a = new QAccount("a");
        assertNotNull(a.embeddedData.someData);
    }

    @Test
    public void testBasicStructure() {
        assertNull(cat.getMetadata().getParent());
        assertEquals(cat, cat.alive.getMetadata().getParent());
        assertEquals("cat", cat.getMetadata().getExpression().toString());
    }

    @Test
    public void testArgumentHandling() {
        // Kitty is reused, so it should be used via one named parameter
        toString(
                "cat.name = :a1 or cust.name.firstName = :a2 or kitten.name = :a1",
                cat.name.eq("Kitty").or(cust.name.firstName.eq("Hans")).or(
                        kitten.name.eq("Kitty")));
    }

    @Test
    public void testArithmeticOperationsInFunctionalWay() {
        toString("cat.bodyWeight + :a1", add(cat.bodyWeight, 10));
        toString("cat.bodyWeight - :a1", sub(cat.bodyWeight, 10));
        toString("cat.bodyWeight * :a1", mult(cat.bodyWeight, 10));
        toString("cat.bodyWeight / :a1", div(cat.bodyWeight, 10));

        toString("cat.bodyWeight + :a1 < :a1", add(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight - :a1 < :a1", sub(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight * :a1 < :a1", mult(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight / :a1 < :a2", div(cat.bodyWeight, 10).lt(10d));

        toString("(cat.bodyWeight + :a1) * :a2", mult(add(cat.bodyWeight, 10),
                20));
        toString("(cat.bodyWeight - :a1) * :a2", mult(sub(cat.bodyWeight, 10),
                20));
        toString("cat.bodyWeight * :a1 + :a2",
                add(mult(cat.bodyWeight, 10), 20));
        toString("cat.bodyWeight * :a1 - :a2",
                sub(mult(cat.bodyWeight, 10), 20));

        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        toString("c1.id + c2.id * c3.id", add(c1.id, mult(c2.id, c3.id)));
        toString("c1.id * (c2.id + c3.id)", mult(c1.id, add(c2.id, c3.id)));
        toString("(c1.id + c2.id) * c3.id", mult(add(c1.id, c2.id), c3.id));
    }

    @Test
    public void testBasicOperations() {
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight
                .eq(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight", cat.bodyWeight
                .ne(kitten.bodyWeight));

        toString("cat.bodyWeight + kitten.bodyWeight = kitten.bodyWeight", add(
                cat.bodyWeight, kitten.bodyWeight).eq(kitten.bodyWeight));
    }

    @Test
    public void testBinaryComparisonOperations() {
        // binary comparison operators =, >=, <=, <>, !=, like
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight
                .eq(kitten.bodyWeight));
        toString("cat.bodyWeight >= kitten.bodyWeight", cat.bodyWeight
                .goe(kitten.bodyWeight));
        toString("cat.bodyWeight > kitten.bodyWeight", cat.bodyWeight
                .gt(kitten.bodyWeight));
        toString("cat.bodyWeight <= kitten.bodyWeight", cat.bodyWeight
                .loe(kitten.bodyWeight));
        toString("cat.bodyWeight < kitten.bodyWeight", cat.bodyWeight
                .lt(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight", cat.bodyWeight
                .ne(kitten.bodyWeight));
        toString("cat.name like :a1", cat.name.like("Kitty"));
    }

    @Test
    public void testBooleanOpeations() {
        toString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
        toString("cust is null and cat is null", cust.isNull()
                .and(cat.isNull()));
        toString("not (cust is null)", not(cust.isNull()));
        cat.name.eq(cust.name.firstName).and(
                cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name.firstName).or(
                cat.bodyWeight.eq(kitten.bodyWeight));
    }

    /**
     * The Class MyCustomExpr.
     */
    public class MyCustomExpr extends CString {
        private final List<Expr<?>> args;

        public MyCustomExpr(Expr<?>... args) {
            this.args = Arrays.asList(args);
        }

        public List<Expr<?>> getArgs() {
            return args;
        }

        public java.lang.String getPattern() {
            return "myCustom(%s,%s)";
        }

        public Expr<?> getArg(int index) {
            return args.get(index);
        }
    }

    @Test
    public void testCustomExpressions() {
        toString("myCustom(cust,cat)", new MyCustomExpr(cust, cat));
    }

    @Test
    public void testCastOperations() {
        // cast(... as ...), where the second argument is the name of a
        // Hibernate type, and extract(... from ...) if ANSI cast() and
        // extract() is supported by the underlying database
    }

    @Test
    public void testCollectionOperations() {
        // HQL functions that take collection-valued path expressions: size(),
        // minelement(), maxelement(), minindex(), maxindex(), along with the
        // special elements() and indices functions which may be quantified
        // using some, all, exists, any, in.
        cat.kittens.size();
        minelement(cat.kittens);
        maxelement(cat.kittens);
        minindex(cat.kittens);
        maxindex(cat.kittens);
        toString("cat.kittens[0]", cat.kittens(0));
        toString("cat.kittens[0]", cat.kittens.get(0));

        // some, all, exists, any, in.
    }

    @Test
    public void testConstructors() {
        EConstructor<com.mysema.query.hql.Domain.Cat> c = new EConstructor<com.mysema.query.hql.Domain.Cat>(
                com.mysema.query.hql.Domain.Cat.class, cat.name);
        toString("new " + com.mysema.query.hql.Domain.Cat.class.getName()
                + "(cat.name)", c);
        toString("new " + getClass().getName() + "$BookmarkDTO()",
                new _BookmarkDTO());
        toString("new " + getClass().getName() + "$BookmarkDTO(cat.name)",
                new _BookmarkDTO(cat.name));
    }

    @Test
    public void testDateOperations() {
        // current_date(), current_time(), current_timestamp()
        toString("current_date()", current_date());
        toString("current_time()", current_time());
        toString("current_timestamp()", current_timestamp());
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
        second(catalog.effectiveDate);
        minute(catalog.effectiveDate);
        hour(catalog.effectiveDate);
        day(catalog.effectiveDate);
        month(catalog.effectiveDate);
        year(catalog.effectiveDate);
    }

    @Test
    public void testEJBQL3Functions() {
        // Any function or operator defined by EJB-QL 3.0: substring(), trim(),
        // lower(), upper(), length(), locate(), abs(), sqrt(), bit_length(),
        // mod()
        // substring(),
        // trim(),
        // lower(),
        // upper(),
        // length(),
        // locate(),
        // abs(),
        // sqrt(),
        // bit_length(),
        // mod()
        toString("trim(cat.name)", cat.name.trim());
        toString("lower(cat.name)", cat.name.lower());
        toString("upper(cat.name)", cat.name.upper());
        // cat.name.length();
    }

    @Test
    public void testEqualsAndNotEqualsForAllExpressions() {
        toString("cat.name = cust.name.firstName", cat.name
                .eq(cust.name.firstName));
        toString("cat.name != cust.name.firstName", cat.name
                .ne(cust.name.firstName));
    }

    @Test
    public void testGrammarConstructs() {
        add(cat.bodyWeight, kitten.bodyWeight);
    }

    @Test
    public void testGroupingOperationsAndNullChecks() {
        // in, not in, between, is null, is not null, is empty, is not empty,
        // member of and not member of
        // in,
        // not in,
        // between,
        // is null,
        // is not null,
        // is empty,
        // is not empty,
        // member of
        // not member of
        kitten.in(cat.kittens);
        not(kitten.in(cat.kittens));
        kitten.bodyWeight.between(10, 20);
        kitten.bodyWeight.isNull();
        kitten.bodyWeight.isNotNull();
        cat.kittens.isEmpty();
        cat.kittens.isNotEmpty();
    }

    @Test
    public void testHQLIndexOperations() {
        // the HQL index() function, that applies to aliases of a joined indexed
        // collection
    }

    @Test
    public void testIsNullAndIsNotNullInFunctionalWay() {
        toString("cat.bodyWeight is null", cat.bodyWeight.isNull());
    }

    @Test
    public void testLogicalOperations() {
        // logical operations and, or, not
        toString("cat = kitten or kitten = cat", cat.eq(kitten).or(
                kitten.eq(cat)));
        toString("cat = kitten and kitten = cat", cat.eq(kitten).and(
                kitten.eq(cat)));
        toString("cat is null and (kitten is null or kitten.bodyWeight > :a1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }

    @Test
    public void testMathematicalOperations() {
        // mathematical operators +, -, *, /
        add(cat.bodyWeight, kitten.bodyWeight);
        sub(cat.bodyWeight, kitten.bodyWeight);
        mult(cat.bodyWeight, kitten.bodyWeight);
        div(cat.bodyWeight, kitten.bodyWeight);
    }

    // Parentheses ( ), indicating grouping

    @Test
    public void testOrderExpressionInFunctionalWay() {
        cat.bodyWeight.asc();
        add(cat.bodyWeight, kitten.bodyWeight).asc();
    }

    // "Simple" case, case ... when ... then ... else ... end, and "searched"
    // case, case when ... then ... else ... end

    @Test
    public void testSimpleAliasForNonEntityPaths() {
        toString("cat.bodyWeight as catbodyWeight", cat.bodyWeight
                .as("catbodyWeight"));
        toString("count(*) as numPosts", Grammar.count().as("numPosts"));
        toString("cat.bodyWeight + kitten.bodyWeight as abc", add(
                cat.bodyWeight, kitten.bodyWeight).as("abc"));
    }

    @Test
    public void testSQLScalarOperations() {
        // Any database-supported SQL scalar function like sign(), trunc(),
        // rtrim(), sin()
    }

    @Test
    public void testStringConcatenations() {
        // string concatenation ...||... or concat(...,...)
        toString("cat.name || kitten.name", cat.name.concat(kitten.name));
    }

    // coalesce() and nullif()

    @Test
    public void testStringConversionOperations() {
        // str() for converting numeric or temporal values to a readable string
    }

    @Test
    public void testStringOperationsInFunctionalWay() {
        toString("cat.name || cust.name.firstName", cat.name
                .concat(cust.name.firstName));
        toString("cat.name like :a1", cat.name.like("A%"));
        toString("lower(cat.name)", cat.name.lower());
    }

    @Test
    public void testToString() {
        toString("cat", cat);
        toString("cat.alive", cat.alive);
        toString("cat.bodyWeight", cat.bodyWeight);
        toString("cat.name", cat.name);

        toString("cust.name", cust.name);
        toString("cust.name.firstName = :a1", cust.name.firstName.eq("Martin"));

        toString("cat.kittens as kitten", cat.kittens.as(kitten));

        toString("cat.bodyWeight + :a1", add(cat.bodyWeight, 10));
        toString("cat.bodyWeight - :a1", sub(cat.bodyWeight, 10));
        toString("cat.bodyWeight * :a1", mult(cat.bodyWeight, 10));
        toString("cat.bodyWeight / :a1", div(cat.bodyWeight, 10));

        toString("cat.bodyWeight as bw", cat.bodyWeight.as("bw"));

        toString("kitten in elements(cat.kittens)", kitten.in(cat.kittens));

        // toString("distinct cat.bodyWeight", distinct(cat.bodyWeight));

        toString("count(*)", Grammar.count());
        // toString("count(distinct cat.bodyWeight)",
        // Grammar.count(distinct(cat.bodyWeight)));
        toString("count(cat)", Grammar.count(cat));
    }

    /**
     * specs :
     * http://opensource.atlassian.com/projects/hibernate/browse/HHH-1538
     */
    @Test
    public void testBug326650() {
        assertEquals(Long.class, sum($((byte) 0)).getType());
        assertEquals(Long.class, sum($((short) 0)).getType());
        assertEquals(Long.class, sum($((int) 0)).getType());
        assertEquals(Long.class, sum($((long) 0)).getType());

        assertEquals(Double.class, sum($((float) 0)).getType());
        assertEquals(Double.class, sum($((double) 0)).getType());

        assertEquals(BigInteger.class, sum($(new BigInteger("0"))).getType());
        assertEquals(BigDecimal.class, sum($(new BigDecimal("0"))).getType());

        // sum to var

        ENumber<Long> sum = (ENumber) sum($(0)); // via Java level cast
        sum = sum($(0)).longValue();

        // sum comparison

        sum($(0)).gt(0);
        sum($(0)).intValue().gt(0);

    }

    @Test
    public void testNumericCast() {
        ENumber<Integer> expr = $(0);
        assertEquals(Byte.class, expr.byteValue().getType());
        assertEquals(Double.class, expr.doubleValue().getType());
        assertEquals(Float.class, expr.floatValue().getType());
        assertEquals(Integer.class, expr.intValue().getType());
        assertEquals(Long.class, expr.longValue().getType());
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void testStringCast() {
        ENumber<Integer> expr = $(0);
        assertEquals(String.class, expr.stringValue().getType());
    }

    private void toString(String expected, Expr<?> expr) {
        assertEquals(expected, visitor.handle(expr).toString());
        // visitor.clear();
        visitor = new HQLSerializer(new HQLPatterns());
    }

    /**
     * The Class _BookmarkDTO.
     */
    public static final class _BookmarkDTO extends EConstructor<BookmarkDTO> {
        public _BookmarkDTO() {
            super(BookmarkDTO.class);
        }

        public _BookmarkDTO(Expr<java.lang.String> address) {
            super(BookmarkDTO.class, address);
        }
    }

    /**
     * The Class BookmarkDTO.
     */
    public static final class BookmarkDTO {

    }

}
