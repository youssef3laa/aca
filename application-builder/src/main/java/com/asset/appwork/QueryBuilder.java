package com.asset.appwork;

import com.asset.appwork.util.SystemUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by omaradl on 12/15/2020.
 */
public class QueryBuilder {
    private static EntityManager entityManager;

    public QueryBuilder(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public List<?> runQuery(String queryJson) throws Exception {
        List<?> result = new ArrayList<>();
        String tableName = SystemUtil.readJSONField(queryJson, "table");

        if(tableName != null) {
            Class<?> tableClass = Class.forName("com.asset.appwork.model." + tableName);
            // Create query from certain table:
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery query = criteriaBuilder.createQuery();
            Root<?> root = query.from(tableClass);

            ArrayList<Selection> columnsSelection = new ArrayList<>();

            // Add aggregation function
            List<?> aggregationList = SystemUtil.readJSONArray(queryJson, "aggregations");
            if(aggregationList != null){
                for(Object aggregation: aggregationList){
                    String aggregationFunction = ((String)aggregation).split(":")[0];
                    String aggregationBy = ((String)aggregation).split(":")[1];

                    Method method = CriteriaBuilder.class.getMethod(aggregationFunction, Expression.class);
                    Selection aggregationExpression = (Selection) method.invoke(criteriaBuilder, root.get(aggregationBy));
                    aggregationExpression.alias(aggregationFunction + "_" + aggregationBy);

                    columnsSelection.add(aggregationExpression);
                }
            }

            //Add columns
            List<?> columns = SystemUtil.readJSONArray(queryJson, "columns");
            if(columns != null){
                for(Object column: columns){
                    columnsSelection.add(root.get((String)column).alias((String)column));
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

            // Add Group By
            List<?> groupByList = SystemUtil.readJSONArray(queryJson, "groupBy");
            if (groupByList != null) {
                ArrayList<Selection> groupBySelection = new ArrayList<>();
                for(Object groupBy: groupByList){
                    groupBySelection.add(root.get(((String) groupBy)));
                }
                if(groupBySelection.size() > 0) {
                    query.groupBy(groupBySelection);
                }
            }

            // Run query
            if(columnsSelection.size() > 0){
                query.multiselect(columnsSelection);
                List<?> list = entityManager.createQuery(query).getResultList();
                result = addAliasToList(list, columnsSelection);
            }else{
                query.select(root);
                result = entityManager.createQuery(query).getResultList();
            }
        }

        return result;
    }

    private List<LinkedHashMap> addAliasToList(List<?> list, ArrayList<Selection> columns){
        List<LinkedHashMap> result = new ArrayList<>();

        for(int listIndex = 0; listIndex < list.size() ; listIndex++){
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            for(int columnNumber = 0; columnNumber < columns.size() ; columnNumber++){
                String columnName = columns.get(columnNumber).getAlias();
                Object value = null;
                if(list.get(listIndex).getClass() != Object[].class){
                    value = list.get(listIndex);
                }else{
                    value = ((Object[])list.get(listIndex))[columnNumber];
                }

                row.put(columnName,value);
            }
            result.add(row);
        }

        return result;
    }
}
