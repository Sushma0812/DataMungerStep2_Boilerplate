package com.stackroute.datamunger.query.parser;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.lang.*;
/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setOrderBy(getOrderBy(queryString));
		queryParameter.setGroupBy(getGroupBy(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {
		String stringOne = queryString.split("from")[1].trim().split(" ")[0].trim();
		System.out.println(stringOne);
		return stringOne;
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */

	public String getBaseQuery(String queryString) {
		String stringTwo = queryString.split("where")[0].trim();
		System.out.println(stringTwo);
		return stringTwo;

	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */

	public List<String> getOrderBy(String queryString) {
		if(queryString.contains("order by")) {
			String[] stringThree = queryString.split("order by")[1].trim().split(",");
			List<String> list = Arrays.asList(stringThree);
			return list;
		}
		else
			return null;
	}

	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupBy(String queryString) {

		if(queryString.contains("group by")) {
			String[] stringFour = queryString.split("group by")[1].trim().split("order by")[0].trim().split(",");
			List<String> listOne = Arrays.asList(stringFour);
			return listOne;

		}
		else
			return null;
	}
	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List <String> getFields(String queryString){
		queryString = queryString.toLowerCase();
		String[] stringFive = queryString.split(" ")[1].split(",");
		List<String> listTwo = Arrays.asList(stringFive);
		return listTwo;
	}


	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */


	public List<Restriction> getRestrictions(String queryString) {

		String restrict=queryString.trim();
		String old[]=restrict.trim().split("where");

		if(old.length==1)
		{
			return null ;
		}

		String condition[]=old[1].trim().split("order by|group by");
		String array[]=condition[0].trim().split(" and | or ");
		LinkedList<Restriction>restrictions=new LinkedList<Restriction>();
		for(int i=0;i<array.length;i++)
		{

			if(array[i].contains(">"))
			{
				String[]test=array[i].trim().split(">");
				Restriction restriction=new Restriction(test[0].trim(),test[1].trim(),">");
				restrictions.add(restriction);
			}
			if(array[i].contains("<"))
			{
				String[]test=array[i].trim().split("<");
				Restriction restriction=new Restriction(test[0].trim(),test[1].trim(),"<");
				restrictions.add(restriction);
			}
			if(array[i].contains("="))
			{
				String[]test=array[i].trim().split("=");
				Restriction restriction=new Restriction(test[0].trim(),test[1].trim(),"=");
				restrictions.add(restriction);
			}
			if(array[i].contains(">="))
			{
				String[]test=array[i].trim().split(">=");
				Restriction restriction=new Restriction(test[0].trim(),test[1].trim(),">=");
				restrictions.add(restriction);
			}
			if(array[i].contains("<="))
			{
				String[]test=array[i].trim().split("<=");
				Restriction restriction=new Restriction(test[0].trim(),test[1].trim(),"<=");
				restrictions.add(restriction);
			}


		}

		return restrictions;
	}
	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {

		LinkedList<String>logicalOperators=new LinkedList<String>();

		String lowerCase=queryString.trim().toLowerCase();
		String[]strings=lowerCase.split(" where ");
		if(strings.length==1)return null;

		String[]arrayOne= strings[1].split(" ");

		for(int i=0;i<arrayOne.length;i++)
		{
			if(arrayOne[i].equals("and")||arrayOne[i].equals("or"))
				logicalOperators.add(arrayOne[i]);
		}



		return logicalOperators;

	}
	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */

	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		String[] splitString=queryString.split("[\\s,;)(]+");
		List<AggregateFunction> functionList=new LinkedList<AggregateFunction>();
		for(int i=0;i<splitString.length;i++)
		{
			if(splitString[i].equals("sum")) {AggregateFunction aggregateFunction=new AggregateFunction(splitString[i+1],"sum");functionList.add(aggregateFunction);}
			else if(splitString[i].equals("count")) {AggregateFunction aggregateFunction=new AggregateFunction(splitString[i+1],"count");functionList.add(aggregateFunction);}
			else if(splitString[i].equals("min")) {AggregateFunction aggregateFunction=new AggregateFunction(splitString[i+1],"min");functionList.add(aggregateFunction);}
			else if(splitString[i].equals("max")) {AggregateFunction aggregateFunction=new AggregateFunction(splitString[i+1],"max");functionList.add(aggregateFunction);}
			else if(splitString[i].equals("avg")) {AggregateFunction aggregateFunction=new AggregateFunction(splitString[i+1],"avg");functionList.add(aggregateFunction);}
			else {continue;
			}
		}
		if(functionList.size()!=0)
		{
			return functionList;
		}
		else {return null;}
	}
}