package control;

import java.util.*;
import java.util.function.*;

import application.Main;
import expression.Expression;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Логика обработки взаимодействий с {@code GUI} калькулятора
 *
 * Класс берет на себя управление элементами графического интерфейса, с fxml документом.
 * {@link #handleDigit(Event)} выодит нажатые цифры на экран, {@link #handleBinaryOperation(Event), {@link #handleUnaryOperation(Event)}} производят необходимое
 * действие над числами и др.
 */
public class MainViewController {

    private Main main;
    private String expression = "";
    private String operation = "";

    //Новое число или все еще цифра
    private boolean newNumber = false;

    //Элементы GUI с fxml док-та
    @FXML private TextArea display;

    @FXML private Button delete;
    //Фактически нажатие на кнопку операции -> выполнить предыдущею
    //Поэтому выполняем предыдущею, и сохраняем текущею в предыдущею
    private BinaryOperator<Double> last = (n1, n2) -> n2;

    private BinaryOperator<Double> current = last;

    //Сохранение рез-в.
    private double[] calculations = {0.0, 0.0};

    //Замена свичу с нажатием операций
    private Map<String, Supplier<Double>> binaryOperationsMap = new HashMap<String, Supplier<Double>>() {
        {   put("÷",  () -> { current = (n1,n2) ->  n1 / n2;
                              operation = "/";
                              return preformBinaryOperation.apply(last);  });
            put("×",  () -> { current = (n1,n2) ->  n1 * n2;
                              operation = "×";
                              return preformBinaryOperation.apply(last);  });
            put("-",  () -> { current = (n1,n2) ->  n1 - n2;
                              operation = "-";
                              return preformBinaryOperation.apply(last);  });
            put("+",  () -> { current = (n1,n2) ->  n1 + n2;
                              operation = "+";
                              return preformBinaryOperation.apply(last);  });
            put("=",  () ->          preformBinaryOperation.apply(current) );
        }
    };
    private Map<String, Supplier<Double>> unaryOperationsMap = new HashMap<String, Supplier<Double>>() {
        {   //            () -> n1 -> n1 * (-1)); Функции высшего порядка
            put("sign",   () ->   preformUnaryOperation.apply(n1 -> n1 * (-1)));
            put("percent",() ->   preformUnaryOperation.apply(n1 -> n1 / 100));
        }
    };

    private Function<Double, String> numberParser = (number) ->
            number == Math.floor(number) && Double.isFinite(number) ? Long.toString(number.longValue()) :
            Double.toString(number).substring(0,Double.toString(number).indexOf('.'))
                 + Double.toString(number).substring(Double.toString(number).indexOf('.'))
                                          .chars()
                                          .limit(7)
                                          .collect(StringBuilder::new,StringBuilder::appendCodePoint, StringBuilder::append)
                                          .toString();
    /**
     * Настройка экрана вывода
     */
    public void setDisplay(Main main) {
        this.main = main;
        display.setEditable(false);
        display.setText("0");
    }

    @FXML
    public void handleDelete() {
        //restores default state
        delete.setText("AC");
        display.setText("0");

        operation  = "";
        expression = "";

        calculations = Arrays.stream(calculations).parallel().map(value -> 0.0).toArray();

        last = (n1, n2) -> n2;
        current = last;

        newNumber = false;
    }

    /**
     * Выводит цифру на экран
     *
     * Вызывается из {@link #handleDigit(Event)} /
     */
    private Consumer<String> displayDigit = (digit) -> {
        if (newNumber) display.setText(digit);
        else           display.appendText(digit);
    };

    @FXML
    public void handleDigit(Event event){

        Button btn = (Button) event.getSource();

        //Вывод точки т.к с ней особая ситуация
        if (btn.getText().equals(".")) {
            if (!display.getText().contains(".")) display.appendText(".");
            return;
        }

        //Не позволяет 0(0)
        if (display.getText().equals("0")){
            delete.setText("C");
            display.setText("");
        }
        displayDigit.accept(btn.getText());
        newNumber = false;
    }

    /**
     * Производит бинарную операцию с введенными данными.
     *
     * Вызывается из {@link #handleBinaryOperation(Event)} и непосредственно выполняет расчеты.
     */
    private Function<BinaryOperator<Double>, Double> preformBinaryOperation =
            operator -> operator.apply(calculations[0],Double.parseDouble(display.getText()));
    @FXML
    public void handleBinaryOperation(Event event) {
        Button btn = (Button) event.getSource();

        if (!operation.isEmpty())
        expression = numberParser.apply(calculations[0]) + " " + operation + " " + display.getText();

        calculations[0] = binaryOperationsMap.get(btn.getText()).get();
        display.setText(numberParser.apply(calculations[0]));

        if (!expression.isEmpty())
        main.addHistory(new Expression(expression, numberParser.apply(calculations[0])));

        if (btn.getText().equals("=")) {
            operation  = "";
            expression = "";
            last = (n1, n2) -> n2;
            current = last;
        } else {
            newNumber = true;
            last = current;
        }
    }

    /**
     * Производит унарную операцию с данными.
     *
     * Вызывается из {@link #handleUnaryOperation(Event)} (Event)} и непосредственно выполняет расчеты.
     */
    private Function<UnaryOperator<Double>, Double> preformUnaryOperation =
            operator -> operator.apply(Double.valueOf(display.getText()));
    @FXML
    public void handleUnaryOperation(Event event) {
        Button btn = (Button) event.getSource();

        if (btn.getId().equals("percent"))
        expression = display.getText();

        calculations[1] = unaryOperationsMap.get(btn.getId()).get();
        display.setText(numberParser.apply(calculations[1]));

        if (btn.getId().equals("percent")) {
            expression += " / " + 100;
            main.addHistory(new Expression(expression, numberParser.apply(calculations[1])));
        }
    }
}