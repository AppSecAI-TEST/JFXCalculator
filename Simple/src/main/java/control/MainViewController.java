package control;

import java.util.IllegalFormatException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import application.Main;
import expression.Expression;

public class MainViewController {

    private String expResult;
    private String expression;
    private Main main;

    @FXML private TextArea display;
    @FXML private Button delete;

    private boolean[] operations = new boolean[5];

    private Double[] calculations = {0.0, 0.0};

    private boolean newNumber = false;

    private boolean firstOperation = true;

    public void setDisplay(Main main) {
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
        String numberId = btn.getId();

        switch(numberId) {
            case "_0" :
            case "_1" :
            case "_2" :
            case "_3" :
            case "_4" :
            case "_5" :
            case "_6" :
            case "_7" :
            case "_8" :
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
                    expression = parseNumber(Double.valueOf(display.getText())) + " / 100";

                    if(!firstOperation) {
                        calculations[1] = Double.parseDouble(display.getText()) / 100;
                        expResult = Double.toString(calculations[1]);
                        main.addHistory(new Expression(expression, expResult));
                        expression = "";
                        display.setText(parseNumber(calculations[1]));
                        operations[4] = true;
                        newNumber = true;
                        firstOperation = false;
                        return;
                    }

                    calculations[0] = Double.parseDouble(display.getText()) / 100;
                    expResult = Double.toString(calculations[0]);
                    main.addHistory(new Expression(expression, expResult));
                    expression = "";
                    operations[4] = true;
                    newNumber = true;
                    firstOperation = false;
                    break;
            }
        } catch (NumberFormatException e) {
            display.setText("Error");
            System.err.println(e.getMessage());
            System.err.println("[wrong numeric format]");
        }

        display.setText(parseNumber(calculations[0]));

        if (!firstOperation && !expression.isEmpty()) {
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
            display.setText(parseNumber(number));
        }
    }

    @FXML
    public void handleDelete() {

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
        double result;

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

            expression = parseNumber(calculations[0]) + " + " + parseNumber(calculations[1]);
        } else if (operations[1]) {
            operations[1] = false;
            result = calculations[0] - calculations[1];

            expression = parseNumber(calculations[0]) + " - " + parseNumber(calculations[1]);
        } else if (operations[2]) {
            operations[2] = false;
            result = calculations[0] * calculations[1];

            expression = parseNumber(calculations[0]) + " * " + parseNumber(calculations[1]);
        } else if (operations[3]) {
            operations[3] = false;
            result = calculations[0] / calculations[1];

            expression = parseNumber(calculations[0]) + " / " + parseNumber(calculations[1]);
        } else if (operations[4]) {
            operations[4] = false;
            return;
        } else {
            return;
        }

        display.setText(parseNumber(result));

        expResult = display.getText();
        main.addHistory(new Expression(expression,expResult));

        firstOperation = true;
        newNumber = true;
    }

    private void preformOperation() {
       try {
           if (firstOperation) {
               calculations[0] = Double.parseDouble(display.getText());
           } else {
               if (operations[0]) {
                   expression = parseNumber(calculations[0]) + " + " + display.getText();

                   calculations[0] += Double.parseDouble(display.getText());
               } else if (operations[1]) {
                   expression = parseNumber(calculations[0]) + " - " + display.getText();

                   calculations[0] -= Double.parseDouble(display.getText());
               } else if (operations[2]) {
                   expression = parseNumber(calculations[0]) + " * " + display.getText();

                   calculations[0] *= Double.parseDouble(display.getText());
               } else if (operations[3]) {
                   expression = parseNumber(calculations[0]) + " / " + display.getText();

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

    private String parseNumber(Double number) {
        if (number == Math.floor(number) && Double.isFinite(number)) {
            return Long.toString(number.longValue());
        } else {
            String num = Double.toString(number);
            String dec = num.substring(num.indexOf('.'), num.length());
            if (dec.length() < 7)
                return num;
            else {
                String i = num.substring(0, num.indexOf('.'));
                return i + dec.substring(0, 7);
            }
        }
    }

    private void displayDigit(String digit) {
        if (newNumber) {
            display.setText(digit.substring(1));
        } else {
            display.appendText(digit.substring(1));
        }
    }
}
