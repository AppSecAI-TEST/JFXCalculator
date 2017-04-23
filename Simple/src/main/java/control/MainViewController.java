package control;

import java.math.BigInteger;
import java.util.IllegalFormatException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import application.Main;
import expression.Expression;

public class MainViewController {

    private Main main;

    private String expResult;
    private String expression;

    @FXML private TextArea display;
    @FXML private Button delete;

    private boolean[] operations = new boolean[4];

    private Double[] calculations = {0.0, 0.0};

    //Новое число или все еще цифра
    private boolean newNumber = false;

    private boolean firstOperation = true;

    public void setMainView(Main main) {
        this.main = main;
        display.setEditable(false);
        display.setText("0");
    }

    @FXML
    public void handleDigit(Event event){
        if (display.getText().equals("0")){
            delete.setText("C");
            display.setText("");
        }

        Button btn = (Button) event.getSource();
        String operation = btn.getId();

        switch(operation) {
            case "_0" :
                displayDigit(operation);
                break;
            case "_1" :
                displayDigit(operation);
                break;
            case "_2" :
                displayDigit(operation);
                break;
            case "_3" :
                displayDigit(operation);
                break;
            case "_4" :
                displayDigit(operation);
                break;
            case "_5" :
                displayDigit(operation);
                break;
            case "_6" :
                displayDigit(operation);
                break;
            case "_7" :
                displayDigit(operation);
                break;
            case "_8" :
                displayDigit(operation);
                break;
            case "_9" :
                displayDigit(operation);
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

    @FXML
    public void handleOperation(Event event) {
        Button btn = (Button) event.getSource();
        String operation = btn.getId();

        try {
            switch (operation) {
                case "plus":
                    preformOperation();
                    operations[0] = true;
                    break;
                case "minus":
                    preformOperation();
                    operations[1] = true;
                    break;
                case "multiply":
                    preformOperation();
                    operations[2] = true;
                    break;
                case "division":
                    preformOperation();
                    operations[3] = true;
                    break;
                case "percent":
                    calculations[0] = Double.parseDouble(display.getText()) / 100;
                    break;
            }
        } catch (NumberFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
        }

        displayNumber(Double.toString(calculations[0]));

        if (!firstOperation) {
            expResult = display.getText();
            main.addHistory(new Expression(expression, expResult));
        }
        newNumber = true;
        firstOperation = false;
    }

    @FXML
    public void handleChangeSign() {
        double number;
        try {
            number = Double.parseDouble(display.getText());
        } catch (IllegalFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
            return;
        }

        if (number != 0) {
            number = number * (-1);
            displayNumber(Double.toString(number));
        }
    }

    @FXML
    public void handleDelete() {
        //restores default state
        delete.setText("AC");
        display.setText("0");
        for(int i = 0; i < 2; i++) {
            calculations[i] = 0.0;
        }
        for(int i = 0; i < 4; i++) {
            operations[i] = false;
        }

        expression = "";
        expResult = "";

        firstOperation = true;
        newNumber = false;
    }

    @FXML
    public void handleEquals() {
        double result = 0;

        try {
            calculations[1] = Double.parseDouble(display.getText());
        } catch (IllegalFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
            return;
        }

        if (operations[0]) {
            operations[0] = false;
            result = calculations[0] + calculations[1];

            expression = calculations[0] + " + " + calculations[1];

        } else if (operations[1]) {
            operations[1] = false;
            result = calculations[0] - calculations[1];

            expression = calculations[0] + " - " + calculations[1];

        } else if (operations[2]) {
            operations[2] = false;
            result = calculations[0] * calculations[1];

            expression = calculations[0] + " * " + calculations[1];

        } else if (operations[3]) {
            operations[3] = false;
            result = calculations[0] / calculations[1];

            expression = calculations[0] + " / " + calculations[1];

        }

        displayNumber(Double.toString(result));
 //TODO
        expResult = display.getText();
        main.addHistory(new Expression(expression,expResult));

        firstOperation = true;
        newNumber = true;
    }

//////////////////////////////////////////////////////////////////////
    //ПРОИЗВЕСТИ ВЫЧИСЛЕНИЕ
    private void preformOperation() {
       try {
           if (firstOperation) {
               calculations[0] = Double.parseDouble(display.getText());
           } else {
               if (operations[0]) {
                   expression = Double.toString(calculations[0]) + " + " + display.getText();
                  //TODO
                   calculations[0] += Double.parseDouble(display.getText());
               } else if (operations[1]) {
                   expression = Double.toString(calculations[0]) + " - " + display.getText();

                   calculations[0] -= Double.parseDouble(display.getText());
               } else if (operations[2]) {
                   expression = Double.toString(calculations[0]) + " * " + display.getText();

                   calculations[0] *= Double.parseDouble(display.getText());
               } else if (operations[3]) {
                   expression = Double.toString(calculations[0]) + " / " + display.getText();

                   calculations[0] /= Double.parseDouble(display.getText());
               }
           }
           for (int i = 0; i < operations.length; i++) {
               operations[i] = false;
           }
       } catch (IllegalFormatException e) {
           display.setText("Error");
           System.err.println(e.getMessage());
           System.err.println("[wrong numeric format]");
       }
    }

    //ВЫВОД ЧИСЛА С ТОЧКОЙ ИЛИ БЕЗ
    private void displayNumber(String numberToDisplay) {
        Double number;

        try {
            number = Double.parseDouble(numberToDisplay);
        } catch (IllegalFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
            return;
        }

        if (firstOperation) {
            if (number == Math.floor(number) && Double.isFinite(number)) {
                display.setText(Long.toString(number.longValue()));
            } else {
                String parsedDecimal = Double.toString(number);
                display.setText(parsedDecimal);
            }
        } else {
            if (number == Math.floor(number) && Double.isFinite(number)) {
                display.setText(Long.toString(number.longValue()));
            } else {
                String parsedDecimal = Double.toString(number);
                if (parsedDecimal.length() < parsedDecimal.indexOf('.') + 6) {
                    display.setText(parsedDecimal);
                } else {
                    display.setText(parsedDecimal.substring(0, parsedDecimal.indexOf('.') + 6));
                }
            }
        }
    }

    //ВЫВОД ЦИФРЫ ПОСЛЕ НАЖАТИЯ КНОПКИ
    private void displayDigit(String digit) {
        if (newNumber) {
            display.setText(digit.substring(1));
        } else {
            display.appendText(digit.substring(1));
        }
    }
}
