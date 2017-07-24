package control;

import java.util.*;
import java.util.function.*;

import application.Main;
import expression.Expression;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class MainViewController {

    private Main main;
    private String expression = "";
    private String operation = "";

    private boolean newNumber = false;

    @FXML private TextArea display;

    @FXML private Button delete;

    private BinaryOperator<Double> last = (n1, n2) -> n2;

    private BinaryOperator<Double> current = last;

    private double[] calculations = {0.0, 0.0};

    private Map<String, Function<String, Double>> binaryOperationsMap = new HashMap<String, Function<String, Double>>() {
        {   put("÷",  (str) -> { current = (n1,n2) ->  n1 / n2;
                              operation = "/";
                              return preformBinaryOperation.apply(last, str);  });
            put("×",  (str) -> { current = (n1,n2) ->  n1 * n2;
                              operation = "×";
                              return preformBinaryOperation.apply(last, str);  });
            put("-",  (str) -> { current = (n1,n2) ->  n1 - n2;
                              operation = "-";
                              return preformBinaryOperation.apply(last, str);  });
            put("+",  (str) -> { current = (n1,n2) ->  n1 + n2;
                              operation = "+";
                              return preformBinaryOperation.apply(last, str);  });
            put("=",  (str) -> { newNumber = true;
                              return preformBinaryOperation.apply(current, str);  });
        }
    };
    private Map<String, Function<String, Double>> unaryOperationsMap = new HashMap<String, Function<String, Double>>() {
        {
            put("sign",   (str) ->   preformUnaryOperation.apply(n1 -> n1 * (-1), str));
            put("percent",(str) ->   preformUnaryOperation.apply(n1 -> n1 / 100,  str));
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

    public void setDisplay(Main main) {
        this.main = main;
        display.setEditable(false);
        display.setText("0");
    }

    @FXML
    public void handleDelete() {
        delete.setText("AC");
        display.setText("0");

        operation  = "";
        expression = "";

        calculations = Arrays.stream(calculations).parallel().map(value -> 0.0).toArray();

        last = (n1, n2) -> n2;
        current = last;

        newNumber = false;
    }

    @FXML
    public void handleDigit(Event event){

        Button btn = (Button) event.getSource();

        if (btn.getText().equals(".")) {
            if (!display.getText().contains(".")) display.appendText(".");
            return;
        }

        if (display.getText().equals("0")){
            delete.setText("C");
            display.setText("");
        }

        if (newNumber) display.setText(btn.getText());
        else display.appendText(btn.getText());

        newNumber = false;
    }

    private BiFunction<BinaryOperator<Double>, String, Double> preformBinaryOperation =
            (operator, displayText) -> operator.apply(calculations[0],Double.parseDouble(displayText));
    @FXML
    public void handleBinaryOperation(Event event) {
        Button btn = (Button) event.getSource();

        if (!operation.isEmpty())
        expression = numberParser.apply(calculations[0]) + " " + operation + " " + display.getText();

        calculations[0] = binaryOperationsMap.get(btn.getText()).apply(display.getText());
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

    private BiFunction<UnaryOperator<Double>, String, Double> preformUnaryOperation =
            (operator, displayText) -> operator.apply(Double.valueOf(displayText));
    @FXML
    public void handleUnaryOperation(Event event) {
        Button btn = (Button) event.getSource();

        if (btn.getId().equals("percent"))
        expression = display.getText();

        calculations[1] = unaryOperationsMap.get(btn.getId()).apply(display.getText());
        display.setText(numberParser.apply(calculations[1]));

        if (btn.getId().equals("percent")) {
            expression += " / " + 100;
            main.addHistory(new Expression(expression, numberParser.apply(calculations[1])));
            expression = "";
        }
    }
}
