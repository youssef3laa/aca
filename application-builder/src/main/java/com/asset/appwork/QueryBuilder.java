package com.asset.appwork;

import com.asset.appwork.util.SystemUtil;
import org.hibernate.Criteria;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by omaradl on 12/15/2020.
 */
public class QueryBuilder {
    private static EntityManager entityManager;

    public QueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<?> runQuery(String queryJson) throws Exception {
        List<?> result = new ArrayList<>();
        String tableName = SystemUtil.readJSONField(queryJson, "table");

        if (tableName != null) {
            Class<?> tableClass = Class.forName("com.asset.appwork.model." + tableName);
            // Create query from certain table:

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery query = criteriaBuilder.createQuery();
            Root<?> root = query.from(tableClass);

            ArrayList<Selection> columnsSelection = new ArrayList<>();

            // Add aggregation functions
            List<?> aggregationList = SystemUtil.readJSONArray(queryJson, "aggregations");
            if (aggregationList != null) {
                for (Object aggregation : aggregationList) {
                    String aggregationFunction = ((String) aggregation).split(":")[0];
                    String aggregationBy = ((String) aggregation).split(":")[1];

                    Method method = CriteriaBuilder.class.getMethod(aggregationFunction, Expression.class);
                    Selection aggregationExpression = (Selection) method.invoke(criteriaBuilder, root.get(aggregationBy));
                    aggregationExpression.alias(aggregationFunction + "_" + aggregationBy);

                    columnsSelection.add(aggregationExpression);
                }
            }

            // Add columns
            List<?> columns = SystemUtil.readJSONArray(queryJson, "columns");
            if (columns != null) {
                for (Object column : columns) {
                    columnsSelection.add(root.get((String) column).alias((String) column));
                }
            }

            // Add Where
            List<?> conditions = SystemUtil.readJSONArray(queryJson, "where");
            if(conditions != null){
                ArrayList<Predicate> predicates = new ArrayList<>();
                for(Object condition : conditions) {
                    String conditionString = (String) condition;
                    Predicate predicate = null;
                    if(conditionString.startsWith("or:")){
                        String[] orConditions = conditionString.split(":");
                        if(orConditions.length > 1){
                            Predicate[] orPredicates = new Predicate[orConditions.length-1];
                            for(int i = 1; i < orConditions.length;  i++){
                                orPredicates[i-1] = createPredicate(criteriaBuilder, root, orConditions[i]);
                            }
                            predicate = criteriaBuilder.or(orPredicates);
                        }
                    }else if(conditionString.startsWith("not:")){
                        String[] notConditions = conditionString.split(":");
                        if(notConditions.length > 1) {
                            predicate = createPredicate(criteriaBuilder, root, notConditions[1]);
                        }
                    }else {
                        predicate = createPredicate(criteriaBuilder, root, conditionString);
                    }
                    if(predicate != null){
                        predicates.add(predicate);
                    }
                }
                if(predicates.size() > 0){
                   Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
                   query.where(predicatesArray);
                }
            }

            // Add Group By
            List<?> groupByList = SystemUtil.readJSONArray(queryJson, "groupBy");
            if (groupByList != null) {
                ArrayList<Selection> groupBySelection = new ArrayList<>();
                for (Object groupBy : groupByList) {
                    groupBySelection.add(root.get(((String) groupBy)));
                }
                if (groupBySelection.size() > 0) {
                    query.groupBy(groupBySelection);
                }
            }

            // Run query
            if (columnsSelection.size() > 0) {
                query.multiselect(columnsSelection);
                List<?> list = entityManager.createQuery(query).getResultList();
                result = addAliasToList(list, columnsSelection);
            } else {
                query.select(root);
                result = entityManager.createQuery(query).getResultList();
            }
        }

        return result;
    }

    private List<LinkedHashMap> addAliasToList(List<?> list, ArrayList<Selection> columns) {
        List<LinkedHashMap> result = new ArrayList<>();

        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
                String columnName = columns.get(columnNumber).getAlias();
                Object value = null;
                if (list.get(listIndex).getClass() != Object[].class) {
                    value = list.get(listIndex);
                } else {
                    value = ((Object[]) list.get(listIndex))[columnNumber];
                }
                row.put(columnName, value);
            }
            result.add(row);
        }

        return result;
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root root, String condition){
        try {
            int conditionLength = condition.split(",").length;
            if(conditionLength == 3) {
                String conditionFunction = condition.split(",")[0];
                String column = condition.split(",")[1];
                String value = condition.split(",")[2];
                Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, getRequiredClassFromFunction(conditionFunction));
                return (Predicate) method.invoke(criteriaBuilder, root.get(column), value);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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