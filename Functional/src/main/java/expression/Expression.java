package expression;

/**
 * Представление выражения и его результа
 *
 * Объекты данного класса сохраняются в {@code ObservableList}, который используется
 * для отображения всех вычислений текущей сессии.
 */
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
