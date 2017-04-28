package control;

//TODO HISTORY
//TODO REMOVE SWITCH AND IF
//TODO HIGHER ORDER ?

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import application.Main;
import oracle.jvm.hotspot.jfr.Producer;

/**
 * Логика обработки взаимодействий с {@code GUI} калькулятора
 *
 * Класс берет на себя управление элементами графического интерфейса, с fxml документом.
 * {@link #handleDigit(Event)} выодит нажатые цифры на экран, {@link #handleOperation(Event)} производит необходимое
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

    //Замена свичу с нажатием цифр
    private Map<String, Consumer<Integer>> switchMap = new HashMap<String, Consumer<Integer>>() {
        {
            //put("_0",(x) -> displayDigit(x));
        }
    };

    //Новое число или все еще цифра
    private boolean newNumber = false;

    /**
     * Настройка экрана вывода
     */
    public void setDisplay(Main main) {
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
        //Не позволяет 0(0)
        if (display.getText().equals("0")){
            delete.setText("C");
            display.setText("");
        }

        Button btn = (Button) event.getSource();
        String numberId = btn.getId();

        switch(numberId) {
            case "_0" :
                displayDigit(numberId);
                break;
            case "_1" :
                displayDigit(numberId);
                break;
            case "_2" :
                displayDigit(numberId);
                break;
            case "_3" :
                displayDigit(numberId);
                break;
            case "_4" :
                displayDigit(numberId);
                break;
            case "_5" :
                displayDigit(numberId);
                break;
            case "_6" :
                displayDigit(numberId);
                break;
            case "_7" :
                displayDigit(numberId);
                break;
            case "_8" :
                displayDigit(numberId);
                break;
            case "_9" :
                displayDigit(numberId);
                break;
            case "dot" :
                if (display.getText().equals("")) {
                    display.setText("0.");
                }
                else if (!display.getText().contains(".")) {
                    display.appendText(".");
                }
                break;
        }
        newNumber = false;
    }

    /**
     * Обработка операций
     *
     * @param event нажатая кнопка
     */
    @FXML
    public void handleOperation(Event event) {
        Button btn = (Button) event.getSource();
        String operation = btn.getId();

        try {
            switch (operation) {
                case "plus":
                    current = (n1,n2) ->  n1 + n2;
                    preformBinaryOperation(last);
                    break;
                case "minus":
                    current = (n1,n2) ->  n1 - n2;
                    preformBinaryOperation(last);
                    break;
                case "multiply":
                    current = (n1,n2) ->  n1 * n2;
                    preformBinaryOperation(last);
                    break;
                case "division":
                    current = (n1,n2) ->  n1 / n2;
                    preformBinaryOperation(last);
                    break;
                case "percent":
                    preformUnaryOperation(n1 -> n1 / 100);
                    return;
                case "sign" :
                    preformUnaryOperation(n1 -> n1 * (-1));
                    return;
                case "equals":
                    preformBinaryOperation(current);
                    break;
            }
        } catch (NumberFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
        }

        display.setText(parseNumber(calculations[0]));

        newNumber = true;
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
    private void preformUnaryOperation(UnaryOperator<Double> operator) {
        calculations[1] = operator.apply(Double.valueOf(display.getText()));
        display.setText(parseNumber(calculations[1]));
    }

    /**
     * Производит бинарную операцию с введенными данными.
     *
     * Метод вызывается из {@link #handleOperation(Event)} и непосредственно выполняет расчеты.
     */
    private void preformBinaryOperation(BinaryOperator<Double> operator) {
       try {
           calculations[0] = operator.apply(calculations[0],Double.parseDouble(display.getText()));
           last = current;

       } catch (IllegalFormatException e) {
           display.setText("Error");
           System.err.println(e.getMessage());
           System.err.println("[wrong numeric format]");
       }
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
        if (newNumber) {
            display.setText(digit.substring(1));
        } else {
            display.appendText(digit.substring(1));
        }
    }
}