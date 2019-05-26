package com.GFPCommand.UAFA.Calculator;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;
import java.util.ArrayList;
import java.util.Iterator;
public class Calculator {
    private Function sqrt = new Function("sqrt", 1);
    private Function factorial = new Function("!", 1);
    private Function cuberoot = new Function("crt", 1);
    private Function combination = new Function("comb", 2);
    private Function permutation = new Function("permu", 2);
    private Parameters params;
    private DoubleEvaluator evaluator;
    private double previousSum = 0;
    private double currentSum = 0;
    private String currentDisplay = "";
    private boolean isRadians = false;
    public Calculator() {
        addFunctions();
        evaluator = new DoubleEvaluator(params) {
            @Override
            protected Double evaluate(Function function, Iterator arguments, Object evaluationContext) {
                if (function == sqrt)
                    return Math.sqrt((Double) arguments.next());
                else if(function == cuberoot){
                    return Math.cbrt((Double) arguments.next());
                }
                else if(function == combination){
                    double numberInputs = 0;
                    ArrayList<Double> saveValue = new ArrayList<Double>();
                    while(arguments.hasNext()) {
                        numberInputs = (Double) arguments.next();
                        saveValue.add(numberInputs);
                    }
                    double firstArgument = saveValue.get(0);
                    double secondArgument = saveValue.get(1);
                    double denominator = getFactorial((int) firstArgument);
                    double nominator = getFactorial((int)secondArgument) * (getFactorial((int)(firstArgument - secondArgument)));
                    return denominator / nominator;
                }
                else if(function == permutation){
                    double numberInputs = 0;
                    ArrayList<Double> saveValue = new ArrayList<Double>();
                    while(arguments.hasNext()) {
                        numberInputs = (Double) arguments.next();
                        saveValue.add(numberInputs);
                    }
                    double firstArgument = saveValue.get(0);
                    double secondArgument = saveValue.get(1);
                    double denominator = getFactorial((int) firstArgument);
                    double nominator = (getFactorial((int)(firstArgument - secondArgument)));
                    return denominator / nominator;
                }
                else if (function == factorial) {
                    double result = 1;
                    double num = (Double) arguments.next();
                    for (int i = 2; i <= num; i++) {
                        result = result * i;
                    }
                    return result;
                } else
                    return super.evaluate(function, arguments, evaluationContext);
            }
        };
    }
    private int getFactorial(int n)    {
        //int result;
        if(n==0 || n==1)
            return 1;
        return getFactorial(n-1) * n;
    }
    public void addFunctions() {
        params = DoubleEvaluator.getDefaultParameters();
        params.add(sqrt);
        params.add(factorial);
        params.add(cuberoot);
        params.add(combination);
        params.add(permutation);
    }
    public String getResult(String currentDisplay, String expressionUsedForParsing) {
        try {
            currentSum = evaluator.evaluate(fixExpression(expressionUsedForParsing));
            currentSum = convertToRadians(currentSum);
            currentDisplay = String.valueOf(currentSum);
            //previousSum = currentSum;
        } catch (Exception e) {
            currentDisplay = "Error";
        }
        return currentDisplay;
    }
    public double convertToRadians(double sum){
        double newSum = sum;
        if(isRadians)
            newSum = Math.toRadians(sum);
        return newSum;
    }
    public String getCurrentDisplay() {
        return currentDisplay;
    }

    public String fixExpression(String exp) {
        int openParens = 0;
        int closeParens = 0;
        char openP = '(';
        char closeP = ')';
        String expr = exp;
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == openP)
                openParens++;
            else if (exp.charAt(i) == closeP)
                closeParens++;
        }
        while (openParens > 0) {
            expr += closeP;
            openParens--;
        }
        while (closeParens > 0) {
            expr = openP + expr;
            closeParens--;
        }
        expr = multiplicationForParens(expr);
        return expr;
    }

    public String multiplicationForParens(String s) {
        String fixed = "";
        for (int position = 0; position < s.length(); position++) {
            fixed += s.charAt(position);
            if (position == s.length() - 1)
                continue;
            if (s.charAt(position) == ')' && s.charAt(position + 1) == '(')
                fixed += '*';
            if (s.charAt(position) == '(' && s.charAt(position + 1) == ')')
                fixed += '1';
        }
        return fixed;
    }
}