package com.asset.appwork;

import com.asset.appwork.dto.Filter;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by omaradl on 12/15/2020.
 */
public class QueryBuilder<T> {
    List<T> data;
    private static EntityManager entityManager;

    public QueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<T> runQuery(Filter filter) throws AppworkException {
        try {
            // Create query from certain table:
            Class<?> tableClass = Class.forName("com.asset.appwork.model." + filter.getTable());
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery query = criteriaBuilder.createQuery();
            Root<?> root = query.from(tableClass);

            ArrayList<Selection> columnsSelection = new ArrayList<>();

            // Add aggregation functions
            ArrayList<Selection> aggregationSelections = createAggregations(filter.getAggregations(), criteriaBuilder, root);
            columnsSelection.addAll(aggregationSelections);

            // Add columns
            filter.getColumns().stream().forEach( s -> columnsSelection.add(root.get(s).alias(s)) );

            // Add Where
            ArrayList<Predicate> predicates = createWhereConditions(filter.getWhere(), criteriaBuilder, root);
            if (predicates.size() > 0) {
                Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
                query.where(predicatesArray);
            }

            // Add Group By
            ArrayList<Selection> groupBySelection = new ArrayList<>();
            filter.getGroupBy().stream().forEach( s-> groupBySelection.add(root.get(s)) );
            query.groupBy(groupBySelection);

            // Run query
            if (columnsSelection.size() > 0) {
                query.multiselect(columnsSelection);
                List<?> list = entityManager.createQuery(query).getResultList();
                data = addAliasToList(list, columnsSelection);
            } else {
                query.select(root);
                data = entityManager.createQuery(query).getResultList();
            }

            return data;

        }catch (Exception e){
            throw new AppworkException(e.getMessage(), ResponseCode.QUERY_BUILDER_FAILURE);
        }

    }

    private List<T> addAliasToList(List<?> list, ArrayList<Selection> columns) {
        List<T> result = new ArrayList<>();

        list.stream().forEach(l -> {
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            int[] columnIndex = { 0 };
            columns.stream().forEach(column -> {
                String columnName = column.getAlias();
                Object value = null;
                if(l.getClass() != Object[].class){
                    value = l;
                } else {
                    value = ((Object[]) l)[columnIndex[0]];
                }
                row.put(columnName, value);
                columnIndex[0]++;
            });
            result.add((T)row);
        });
//        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
//            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
//            for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
//                String columnName = columns.get(columnNumber).getAlias();
//                Object value = null;
//                if (list.get(listIndex).getClass() != Object[].class) {
//                    value = list.get(listIndex);
//                } else {
//                    value = ((Object[]) list.get(listIndex))[columnNumber];
//                }
//                row.put(columnName, value);
//            }
//            result.add(row);
//        }

        return result;
    }

    private ArrayList<Selection> createAggregations(List<LinkedHashMap> aggregations, CriteriaBuilder criteriaBuilder, Root root){
        ArrayList<Selection> selections = new ArrayList<>();
        aggregations.stream().filter(s -> s.containsKey("function") && s.containsKey("column")).forEach(aggregation -> {
            try {
                String aggregationFunction = (String) aggregation.get("function");
                String aggregationBy = (String) aggregation.get("column");

                Method method = CriteriaBuilder.class.getMethod(aggregationFunction, Expression.class);
                Selection aggregationExpression = (Selection) method.invoke(criteriaBuilder, root.get(aggregationBy));
                aggregationExpression.alias(aggregationFunction + "_" + aggregationBy);

                selections.add(aggregationExpression);
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
        });
        return selections;
    }

    private ArrayList<Predicate> createWhereConditions(List<LinkedHashMap> conditions, CriteriaBuilder criteriaBuilder, Root root){
        ArrayList<Predicate> predicates = new ArrayList<>();
        conditions.stream().forEach(condition -> {
            try {
                if(condition.containsKey("or")){
                    ArrayList<Predicate> orPredicates = null;
                    orPredicates = createWhereConditions( (List<LinkedHashMap>) condition.get("or"),criteriaBuilder,root);
                    Predicate[] orPredicatesArray = orPredicates.toArray(new Predicate[orPredicates.size()]);
                    predicates.add(criteriaBuilder.or(orPredicatesArray));
                }else if(condition.containsKey("and")){
                    ArrayList<Predicate> andPredicates = createWhereConditions( (List<LinkedHashMap>) condition.get("and"),criteriaBuilder,root);
                    Predicate[] andPredicatesArray = andPredicates.toArray(new Predicate[andPredicates.size()]);
                    predicates.add(criteriaBuilder.and(andPredicatesArray));
                }else{
                    Optional<Predicate> predicate = createPredicate(criteriaBuilder, root, condition);
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

    private Optional<Predicate> createPredicate(CriteriaBuilder criteriaBuilder, Root root, LinkedHashMap condition) throws AppworkException{
        try {
            Optional<Predicate> predicate = null;
            if(condition.containsKey("type") && condition.containsKey("column")){
                String conditionFunction = (String) condition.get("type");
                String column = (String) condition.get("column");

                if (condition.containsKey("value")){
                    String value = (String) condition.get("value");
                    Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, getRequiredClassFromFunction(conditionFunction));
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder, root.get(column), value));
                }else if(condition.containsKey("column2")){
                    String column2 = (String) condition.get("column");
                    Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, Expression.class);
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder, root.get(column), root.get(column2)));
                }
            }
            return predicate;
        } catch (Exception e) {
            throw new AppworkException(e.getMessage(),ResponseCode.QUERY_BUILDER_FAILURE);
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
}

// Add Sort By
//            List<?> sortByList = SystemUtil.readJSONArray(queryJson, "sortBy");
//            if (sortByList != null) {
//                ArrayList<Order> sortByOrder = new ArrayList<>();
//                for(Object sortBy: sortByList){
//                    String[] sortBySplitStrings = ((String) sortBy).split(":");
//
//                    if(sortBySplitStrings.length > 1){
//                        String sortByDirection = sortBySplitStrings[0];
//                        String sortByColumn = sortBySplitStrings[1];
//
//                        if(sortByDirection == "desc"){
//                            sortByOrder.add(criteriaBuilder.desc(root.get(sortByColumn)));
//                        }else{
//                            sortByOrder.add(criteriaBuilder.asc(root.get(sortByColumn)));
//                        }
//                    }else{
//                        sortByOrder.add(criteriaBuilder.asc(root.get(sortBySplitStrings[0])));
//                    }
//                }
//                if(sortByOrder.size() > 0){
//                    query.orderBy(sortByOrder);
//                }
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
//            =========== Old Creation Of Where Conditions ====================
//            List<?> conditions = SystemUtil.readJSONArray(queryJson, "where");
//            if(conditions != null){
//                ArrayList<Predicate> predicates = new ArrayList<>();
//                for(Object condition : conditions) {
//                    String conditionString = (String) condition;
//                    Predicate predicate = null;
//                    if(conditionString.startsWith("or:")){
//                        String[] orConditions = conditionString.split(":");
//                        if(orConditions.length > 1){
//                            Predicate[] orPredicates = new Predicate[orConditions.length-1];
//                            for(int i = 1; i < orConditions.length;  i++){
//                                orPredicates[i-1] = createPredicate(criteriaBuilder, root, orConditions[i]);
//                            }
//                            predicate = criteriaBuilder.or(orPredicates);
//                        }
//                    }else if(conditionString.startsWith("not:")){
//                        String[] notConditions = conditionString.split(":");
//                        if(notConditions.length == 2) {
//                            predicate = createPredicate(criteriaBuilder, root, notConditions[1]);
//                        }
//                    }else {
//                        predicate = createPredicate(criteriaBuilder, root, conditionString);
//                    }
//                    if(predicate != null){
//                        predicates.add(predicate);
//                    }
//                }
//                if(predicates.size() > 0){
//                    Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
//                    query.where(predicatesArray);
//                }
//            }
//            ======================= Old creation of Aggregations ===================
//            if (aggregationList != null) {
//                for (Object aggregation : aggregationList) {
//                    String aggregationFunction = ((String) aggregation).split(":")[0];
//                    String aggregationBy = ((String) aggregation).split(":")[1];
//
//                    Method method = CriteriaBuilder.class.getMethod(aggregationFunction, Expression.class);
//                    Selection aggregationExpression = (Selection) method.invoke(criteriaBuilder, root.get(aggregationBy));
//                    aggregationExpression.alias(aggregationFunction + "_" + aggregationBy);
//
//                    columnsSelection.add(aggregationExpression);
//                }
//            }

