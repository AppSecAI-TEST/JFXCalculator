package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;

import java.awt.*;
import java.util.IllegalFormatException;

public class MainViewController {

    @FXML private TextArea display;
	@FXML private Button clear;
	
	private boolean[] operator = new boolean[5];

	private int operatorCount = 0;
	
	private Double[] temporary = {0.0, 0.0};
	
	//private String actualText;

	private Boolean newNumber = false;

	private Main main;

	void setMain(Main main) {
		this.main = main;
        display.setEditable(false);
        display.setText("0");

    }


	@FXML
	public void handleDigit(Event event){

		//No repeated zeroes aka (000) or (0111)
		if (display.getText().equals("0")){
			clear.setText("C");
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

//		actualText = display.getText();
//		System.out.println("Input data: " + actualText);
	}
	
	@FXML
	public void handleDelete() {
		clear.setText("AC");
		display.setText("0");
		for(int i = 0; i < 2; i++) {
			temporary[i] = 0.0;
		}
		for(int i = 0; i < 5; i++) {
			operator[i] = false;
		}
	}

	@FXML
	public void handleOperation(Event event) {

		newNumber = true;

		Button btn = (Button) event.getSource();
		String operation = btn.getId();

		try {
			switch (operation) {
				case "plus":
//					actualText = handleOperation;
//					System.out.println(actualText);
					operator[0] = true;
					temporary[0] = Double.parseDouble(display.getText());
					break;
				case "minus":
//					actualText = handleOperation;
//					System.out.println(actualText);
					operator[1] = true;
					temporary[0] = Double.parseDouble(display.getText());
					break;
				case "multiply":
//					actualText = handleOperation;
//					System.out.println(actualText);
					operator[2] = true;
					temporary[0] = Double.parseDouble(display.getText());
					break;
				case "division":
//					actualText = handleOperation;
//					System.out.println(actualText);
					operator[3] = true;
					temporary[0] = Double.parseDouble(display.getText());
					break;
                case "percent":
                    operator[4] = true;
                    temporary[0] = Double.parseDouble(display.getText());
                    break;
			}
		} catch (NumberFormatException e) {
			System.err.println(e.getMessage());
		}
		displayNumber(display.getText());
	}

	@FXML
	public void handleChangeSign() {
		double number = Double.parseDouble(display.getText());
		try {
			if (number != 0) {
				number = number * (-1);
				displayNumber(Double.toString(number));
//
//				actualText = display.getText();
//				System.out.println("Input data: " + actualText);
			}
		} catch (IllegalFormatException e) {
		//	display.setText("Error");
		//	e.printStackTrace();
		}
	}
	
	@FXML
	public void handleEquals() {
		double result = 0;
		temporary[1] = Double.parseDouble(display.getText());
		if (operator[0]){
			operator[0] = false;
			result = temporary[0] + temporary[1];
		} else if (operator[1]){
			operator[1] = false;
			result = temporary[0] - temporary[1];
		} else if (operator[2]){
			operator[2] = false;
			result = temporary[0] * temporary[1];
		} else if (operator[3]) {
			operator[3] = false;
			result = temporary[0] / temporary[1];
		} else if (operator[4]) {
		    operator[4] = false;
		    result = temporary[0] % temporary[1];
        }
		System.out.println("handleEquals: " + result);

		displayNumber(Double.toString(result));

	//	operatorCount = 0;
	}

	//ВЫВОД ЧИСЛА С ТОЧКОЙ ИЛИ БЕЗ
	private void displayNumber(String numberToDisplay) {
		Double number;

		try {
			number = Double.parseDouble(numberToDisplay);
		} catch (IllegalFormatException e) {
			//System.err.println("Wrong format");
			return;
		}

		if (number == Math.floor(number) && Double.isFinite(number)) {
			display.setText(Integer.toString(number.intValue()));
		} else {
			display.setText(Double.toString(number));
		}
	}

	//ВЫВОД ЦИФРЫ ПОСЛЕ НАЖАТИЯ КНОПКИ
    private void displayDigit(String digit) {
        if (newNumber) {
            display.setText(digit.substring(1));
            newNumber = false;
        } else {
            display.appendText(digit.substring(1));
        }
    }
}