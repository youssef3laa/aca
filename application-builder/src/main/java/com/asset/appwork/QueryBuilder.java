package com.asset.appwork;

import com.asset.appwork.dto.Query;
import com.asset.appwork.dto.QueryResult;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by omaradl on 12/15/2020.
 */
public class QueryBuilder<T> {
    List<T> data;
    private static EntityManager entityManager;

    public QueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public QueryResult runQuery(Query query) throws AppworkException {
        try {
            // Create query from certain table:
            Class<?> tableClass = Class.forName("com.asset.appwork.model." + query.getTable());
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
            Root<?> root = criteriaQuery.from(tableClass);


            List<Join<T, T>> joins = new ArrayList<>();
            // Add Joins
            query.getJoins().stream().filter(s->s.getType()!=null&&s.getOn()!=null).forEach(join->{
                Join<T, T> joinObject=root.join(join.getJoinable(),getJoinType(join.getType()));
                joinObject.alias(join.getTable());
                joins.add(joinObject);

                ArrayList<Predicate> predicates=createWhereConditions(join.getOn(),criteriaBuilder,root,joins);
                if(predicates.size()>0){
                    Predicate[]predicatesArray=predicates.toArray(new Predicate[predicates.size()]);
                    joinObject.on(predicatesArray);
                }
            });

            ArrayList<Expression> columnsSelection = new ArrayList<>();

            // Add aggregation functions
            ArrayList<Expression> aggregationSelections = createAggregations(query.getAggregations(), criteriaBuilder, root, joins);
            columnsSelection.addAll(aggregationSelections);

            // Add columns
            query.getColumns().stream().forEach( s -> {
                Expression expression = getTable(joins, root, s).get(s.getName());
//                Expression expression = root.get(s.getName());
                String alias = "";
                if(!joins.isEmpty()){
                    alias += s.getTable() + "_";
                }
                alias += s.getName();
                expression.alias(alias);
                columnsSelection.add(expression);
            } );

            // Add Where
            ArrayList<Predicate> predicates = createWhereConditions(query.getWhere(), criteriaBuilder, root, joins);
            if (predicates.size() > 0) {
                Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
                criteriaQuery.where(predicatesArray);
            }

            // Add Sort By
            ArrayList<Order> sortByOrder = createSortBy(query.getSortBy(), criteriaBuilder, root, joins);
            criteriaQuery.orderBy(sortByOrder);

            // Add Group By
            ArrayList<Expression> groupBySelection = new ArrayList<>();
            query.getGroupBy().stream().forEach( s-> groupBySelection.add(getTable(joins, root, s).get(s.getName())) );
//            query.getGroupBy().stream().forEach( s-> groupBySelection.add(root.get(s.getName())) );
            criteriaQuery.groupBy(groupBySelection);

            //Total Count
            Long totalCount = 0L;

            // Run query
            if (columnsSelection.size() > 0) {
                criteriaQuery.multiselect(columnsSelection);
                totalCount = (long)entityManager.createQuery(criteriaQuery).getResultList().size();
                List<T> list = createPagination(criteriaQuery, query.getPage()).getResultList();
                data = addAliasToList(list, columnsSelection);
            } else {
                criteriaQuery.select(root);
                data = createPagination(criteriaQuery, query.getPage()).getResultList();
                totalCount = (long)entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root))).getSingleResult();
            }

            QueryResult queryResult = new QueryResult();
            queryResult.setContent(data);
            queryResult.setTotalCount(totalCount);

            return queryResult;

        }catch (Exception e){
            throw new AppworkException(e.getMessage(), ResponseCode.QUERY_BUILDER_FAILURE);
        }

    }

    private List<T> addAliasToList(List<T> list, ArrayList<Expression> columns) {
        List<T> result = new ArrayList<>();

        list.stream().forEach(l -> {
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            int[] columnIndex = { 0 };
            columns.stream().forEach(column -> {
                String columnName = column.getAlias();
                Object value = null;
                if(l == null || l.getClass() != Object[].class){
                    value = l;
                } else {
                    value = ((Object[]) l)[columnIndex[0]];
                }
                row.put(columnName, value);
                columnIndex[0]++;
            });
            result.add((T)row);
        });

        return result;
    }

    private From getTable(List<Join<T,T>> joins,Root root, Query.Column column){
        if(column.getTable().equals(root.getJavaType().getSimpleName())){
            return root;
        }else{
            for(Join join : joins){
                if(join.getAlias().equals(column.getTable())){
                    return join;
                }
            }
        }
        return null;
    }

    private TypedQuery createPagination(CriteriaQuery criteriaQuery,Query.Page page){
        if(page != null){
            return entityManager.createQuery(criteriaQuery).setFirstResult(page.getNumber()* page.getSize()).setMaxResults(page.getSize());
        }
        return entityManager.createQuery(criteriaQuery);
    }

    private ArrayList<Expression> createAggregations(List<Query.Aggregate> aggregations, CriteriaBuilder criteriaBuilder, Root root, List<Join<T,T>> joins){
        ArrayList<Expression> selections = new ArrayList<>();
        aggregations.stream().filter(s -> s.getFunction() != null && s.getColumn() != null).forEach(aggregation -> {
            try {
                String aggregationFunction = aggregation.getFunction();
                Query.Column aggregationBy = aggregation.getColumn();
//                String aggregationBy = aggregation.getColumn().getName();

                Method method = CriteriaBuilder.class.getMethod(aggregationFunction, Expression.class);
                Expression aggregationExpression = (Expression) method.invoke(criteriaBuilder, getTable(joins, root, aggregationBy).get(aggregationBy.getName()));
                String alias = aggregationFunction + "_";
                if(!joins.isEmpty()){
                    alias += aggregation.getColumn().getTable() + "_";
                }
                alias += aggregationBy.getName();
                aggregationExpression.alias(alias);

                selections.add(aggregationExpression);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        });
        return selections;
    }

    private ArrayList<Order> createSortBy(List<Query.SortBy> sortByList, CriteriaBuilder criteriaBuilder, Root root, List<Join<T,T>> joins){
        ArrayList<Order> orderList = new ArrayList<>();
        sortByList.stream().filter(s -> s.getColumn() != null && s.getDirection() != null).forEach(sortBy ->{
            if((sortBy.getDirection()).toLowerCase().equals("desc")){
                orderList.add(criteriaBuilder.desc(getTable(joins, root, sortBy.getColumn()).get(sortBy.getColumn().getName())));
//                orderList.add(criteriaBuilder.desc(root.get(sortBy.getColumn().getName())));
            }else{
                orderList.add(criteriaBuilder.asc(getTable(joins, root, sortBy.getColumn()).get(sortBy.getColumn().getName())));
//                orderList.add(criteriaBuilder.asc(root.get(sortBy.getColumn().getName())));
            }
        });
        return orderList;
    }

    private ArrayList<Predicate> createWhereConditions(List<Query.Condition> conditions, CriteriaBuilder criteriaBuilder, Root root, List<Join<T,T>> joins){
        ArrayList<Predicate> predicates = new ArrayList<>();
        conditions.stream().forEach(condition -> {
            try {
                if(condition.getOr() != null){
                    ArrayList<Predicate> orPredicates = null;
                    orPredicates = createWhereConditions(condition.getOr(), criteriaBuilder, root, joins);
                    Predicate[] orPredicatesArray = orPredicates.toArray(new Predicate[orPredicates.size()]);
                    predicates.add(criteriaBuilder.or(orPredicatesArray));
                }else if(condition.getAnd() != null){
                    ArrayList<Predicate> andPredicates = createWhereConditions(condition.getAnd(), criteriaBuilder, root, joins);
                    Predicate[] andPredicatesArray = andPredicates.toArray(new Predicate[andPredicates.size()]);
                    predicates.add(criteriaBuilder.and(andPredicatesArray));
                }else{
                    Optional<Predicate> predicate = createPredicate(criteriaBuilder, root, joins, condition);
                    if(predicate.isPresent()){
                        predicates.add(predicate.get());
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
        });
        return predicates;
    }

    private Optional<Predicate> createPredicate(CriteriaBuilder criteriaBuilder, Root root, List<Join<T,T>> joins, Query.Condition condition) throws AppworkException{
        try {
            Optional<Predicate> predicate = null;
            if(condition.getType() != null && condition.getColumn() != null){
                String conditionFunction = condition.getType();
                Query.Column column = condition.getColumn();

                if (condition.getValue() != null){
                    String value = (String) condition.getValue();
                    Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, getRequiredClassFromFunction(conditionFunction));
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder,
                                                                getTable(joins, root, column).get(column.getName()),
                                                                getValueObjectFromColumn(getTable(joins, root, column), column.getName(), value)));
                }else if(condition.getColumn2() != null){
                    String column2 = condition.getColumn2().getName();
                    Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, Expression.class);
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder,
                                                                getTable(joins, root, column).get(column.getName()),
                                                                getTable(joins, root, column).get(column.getName())));
                }
            }
            return predicate;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(),ResponseCode.QUERY_BUILDER_FAILURE);
        }
    }

    private JoinType getJoinType(String type){
        switch (type){
            case "left":
                return JoinType.LEFT;
            case "right":
                return JoinType.RIGHT;
            default:
                return JoinType.INNER;
        }
    }

    private Class getRequiredClassFromFunction(String whereFunction){
        switch (whereFunction){
            case "like":
            case "notLike":
                return String.class;
            case "greaterThan":
            case "greaterThanOrEqualTo":
            case "lessThan":
            case "lessThanOrEqualTo":
                return Comparable.class;
            default:
                return Object.class;
        }
    }

    private T getValueObjectFromColumn(From from, String column, String value) throws AppworkException{
        try{
            T val = (T)value;

            if(from.get(column).getJavaType().getSimpleName().equals("Date")){
                Date date;
                SimpleDateFormat sdf;
                if(value.contains("T")){
                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                }else{
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }
                date = sdf.parse(value);
                val = (T)date;
            }

            return val;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(),ResponseCode.QUERY_BUILDER_FAILURE);
        }
    }

}
//            ======== Joins Creation =======
//            List<Join<T, T>> joins = new ArrayList<>();
//            query.getJoins().stream().filter(s->s.getType()!=null&&s.getOn()!=null).forEach(join->{
//                Join<T, T> joinObject=root.join(join.getJoinable(),getJoinType(join.getType()));
//                joinObject.alias(join.getJoinable());
//                joins.add(joinObject);
//                ArrayList<Predicate> predicates=createWhereConditions(join.getOn(),criteriaBuilder,root);
//                if(predicates.size()>0){
//                    Predicate[]predicatesArray=predicates.toArray(new Predicate[predicates.size()]);
//                    joinObject.on(predicatesArray);
//                }
//            });
//            ========= New Get Column ========
//            private Path getColumn(Root root, List<Join<T,T>> joins, Query.Column column){
//                Path[] path = {null};
//                if(root.getJavaType().getSimpleName().equals(column.getTable())){
//                    path[0] = root.get(column.getName());
//                }else{
//                    joins.stream().forEach(s -> {
//                        if(s.getAlias().equals(column.getTable())){
//                            path[0] = s.get(column.getName());
//                        }
//                    });
//                }
//                return path[0];
//            }
//            ======== between function ========
//            if(conditionFunction.equals("between") && conditionLength == 4){
//                String column = condition.split(",")[1];
//                Object value1 = condition.split(",")[2];
//                Object value2 = condition.split(",")[3];
//
//                Class<?> neededType = Class.forName(root.get(column).getJavaType().getCanonicalName());
//                return criteriaBuilder.between(root.get(column),(Comparable)neededType.cast(value1),(Comparable)neededType.cast(value2));
//            }else

