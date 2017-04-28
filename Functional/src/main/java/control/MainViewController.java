package control;

//TODO HISTORY
//TODO HIGHER ORDER ?

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Логика обработки взаимодействий с {@code GUI} калькулятора
 *
 * Класс берет на себя управление элементами графического интерфейса, с fxml документом.
 * {@link #handleDigit(Event)} выодит нажатые цифры на экран, {@link #handleBinaryOperation(Event)} производит необходимое
 * действие над числами и др.
 */
public class MainViewController {

    //Хранят операции и их результаты
//    private String expResult;
//    private String expression;

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
                              return preformBinaryOperation(last);  });
            put("×",  () -> { current = (n1,n2) ->  n1 * n2;
                              return preformBinaryOperation(last);  });
            put("-",  () -> { current = (n1,n2) ->  n1 - n2;
                              return preformBinaryOperation(last);  });
            put("+",  () -> { current = (n1,n2) ->  n1 + n2;
                              return preformBinaryOperation(last);  });
            put("=",  () ->   preformBinaryOperation(current));
        }
    };

    private Map<String, Supplier<Double>> unaryOperationsMap = new HashMap<String, Supplier<Double>>() {
        {
            put("sign",  () ->   preformUnaryOperation(n1 -> n1 * (-1)));
            put("percent",() ->   preformUnaryOperation(n1 -> n1 / 100));
        }
    };

    //Замена свичу с нажатием цифр
    private Consumer<String> consumer = (x) -> displayDigit(x);

    //Новое число или все еще цифра
    private boolean newNumber = false;

    /**
     * Настройка экрана вывода
     */
    public void setDisplay() {
        display.setEditable(false);
        display.setText("0");
    }

    /**
     * Обработка нажатия цифры
     *
     * @param event нажатая кнопка
     */
    @FXML
    public void handleDigit(Event event){

        Button btn = (Button) event.getSource();

        //Не позволяет 0(0)
        if (display.getText().equals("0")){
            delete.setText("C");
            display.setText("");
        }
        //Вывод точки т.к с ней особая ситуация
        if (btn.getText().equals(".")) {
            if (!newNumber) display.setText("0.");
            else if (!display.getText().contains(".")) display.appendText(".");
            newNumber = false;
            return;
        }

        consumer.accept(btn.getText());

        newNumber = false;
    }

    /**
     * Обработка операций
     *
     * @param event нажатая кнопка
     */
    @FXML
    public void handleBinaryOperation(Event event) {
        Button btn = (Button) event.getSource();
        calculations[0] = binaryOperationsMap.get(btn.getText()).get();
        display.setText(parseNumber(calculations[0]));
        newNumber = true;
    }

    @FXML
    public void handleUnaryOperation(Event event) {
        Button btn = (Button) event.getSource();
        calculations[1] = unaryOperationsMap.get(btn.getId()).get();
        display.setText(parseNumber(calculations[1]));
    }

    @FXML
    public void handleDelete() {
        //restores default state
        delete.setText("AC");
        display.setText("0");

        calculations = Arrays.stream(calculations).parallel().map(value -> 0.0).toArray();

        last = (n1, n2) -> n2;
        current = last;

        newNumber = false;
    }

    /**
     * Производит унарную операцию с данными.
     */
    private Double preformUnaryOperation(UnaryOperator<Double> operator) {
        return operator.apply(Double.valueOf(display.getText()));
    }

    /**
     * Производит бинарную операцию с введенными данными.
     *
     * Метод вызывается из {@link #handleBinaryOperation(Event)} и непосредственно выполняет расчеты.
     */
    private Double preformBinaryOperation(BinaryOperator<Double> operator) {
           last = current;
           return operator.apply(calculations[0],Double.parseDouble(display.getText()));
    }

    /**
     * Вывод числа на экран.
     *
     * Метод проверяет имеет ли полученный {@code Double} дробную часть.
     * Если дробная часть отсутсвует, то число выводится без нее (Прим: 322).
     *
     * @param number Double число, которое следует подготовить к выводу
     * @return  Обработанное строковое представление полученного числа
     */
    private String parseNumber(Double number) {
        if (number == Math.floor(number) && Double.isFinite(number)) {
            return Long.toString(number.longValue());
        } else {
            return Double.toString(number);
        }
    }

    /**
     * Выводит цифру на экран
     *
     * @param digit Кнопка, которой соответсвует цифра
     */
    private void displayDigit(String digit) {
        if (newNumber) display.setText(digit);
        else display.appendText(digit);
    }
}