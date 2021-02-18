package com.asset.appwork;

import com.asset.appwork.dto.Filter;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;

import javax.persistence.EntityManager;
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

            // Add Sort By
            ArrayList<Order> sortByOrder = createSortBy(filter.getSortBy(), criteriaBuilder, root);
            query.orderBy(sortByOrder);

            // Add Group By
            ArrayList<Selection> groupBySelection = new ArrayList<>();
            filter.getGroupBy().stream().forEach( s-> groupBySelection.add(root.get(s)) );
            query.groupBy(groupBySelection);

            // Run query
            if (columnsSelection.size() > 0) {
                query.multiselect(columnsSelection);
                List<T> list = entityManager.createQuery(query).getResultList();
                data = addAliasToList(list, columnsSelection, tableClass);
            } else {
                query.select(root);
                data = entityManager.createQuery(query).getResultList();
            }

            return data;

        }catch (Exception e){
            throw new AppworkException(e.getMessage(), ResponseCode.QUERY_BUILDER_FAILURE);
        }

    }

    private List<T> addAliasToList(List<?> list, ArrayList<Selection> columns, Class<?> tableClass) {
        List<T> result = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

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
//            result.add((T)mapper.convertValue(row,tableClass));
        });

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
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        });
        return selections;
    }

    private ArrayList<Order> createSortBy(List<LinkedHashMap> sortByList, CriteriaBuilder criteriaBuilder, Root root){
        ArrayList<Order> orderList = new ArrayList<>();
        sortByList.stream().filter(s -> s.containsKey("column") && s.containsKey("direction")).forEach(sortBy ->{
            if(((String)sortBy.get("direction")).toLowerCase().equals("desc")){
                orderList.add(criteriaBuilder.desc(root.get((String)sortBy.get("column"))));
            }else{
                orderList.add(criteriaBuilder.asc(root.get((String)sortBy.get("column"))));
            }
        });
        return orderList;
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
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder, root.get(column), getValueObjectFromColumn(root,column,value)));
                }else if(condition.containsKey("column2")){
                    String column2 = (String) condition.get("column");
                    Method method = CriteriaBuilder.class.getMethod(conditionFunction, Expression.class, Expression.class);
                    predicate = Optional.ofNullable((Predicate) method.invoke(criteriaBuilder, root.get(column), root.get(column2)));
                }
            }
            return predicate;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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

    private T getValueObjectFromColumn(Root root, String column, String value) throws AppworkException{
        try{
            T val = (T)value;

            if(root.get(column).getJavaType().getSimpleName().equals("Date")){
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
//            ======== between function ========
//            if(conditionFunction.equals("between") && conditionLength == 4){
//                String column = condition.split(",")[1];
//                Object value1 = condition.split(",")[2];
//                Object value2 = condition.split(",")[3];
//
//                Class<?> neededType = Class.forName(root.get(column).getJavaType().getCanonicalName());
//                return criteriaBuilder.between(root.get(column),(Comparable)neededType.cast(value1),(Comparable)neededType.cast(value2));
//            }else

