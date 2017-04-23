package expression;

public class Expression {
    private String exp;
    private String result;

    public Expression(String expression, String result) {
        this.exp = expression;
        this.result = result;
    }

    public String getExp() {
        return exp;
    }

    public String getResult() {
        return result;
    }
}
