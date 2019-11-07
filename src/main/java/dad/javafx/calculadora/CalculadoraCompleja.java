package dad.javafx.calculadora;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class CalculadoraCompleja extends Application {

	//atributos comentados para el caso de los bind
	
	/*
	private DoubleProperty operando1 = new SimpleDoubleProperty();
	private DoubleProperty operando2 = new SimpleDoubleProperty();
	private DoubleProperty operando3 = new SimpleDoubleProperty();
	private DoubleProperty operando4 = new SimpleDoubleProperty();
	private DoubleProperty resultado1 = new SimpleDoubleProperty();
	private DoubleProperty resultado2 = new SimpleDoubleProperty();
	private StringProperty operador = new SimpleStringProperty(); */

	/*
	 * private StringProperty operador2 = new SimpleStringProperty(); private
	 * StringProperty operador3 = new SimpleStringProperty();
	 */
	
	// modelo
	
	private ComplejoFX complejo1 = new ComplejoFX();
	private ComplejoFX complejo2 = new ComplejoFX();
	private ComplejoFX resultado = new ComplejoFX();
	private StringProperty operador = new SimpleStringProperty();
	private StringConverter<Number> converter = new NumberStringConverter();  //esto me permite que pueda bindear una stringProperty con una doubleProperty
	
	// vista

	private TextField operando1Text;
	private TextField operando2Text;
	private TextField operando3Text;
	private TextField operando4Text;
	private TextField resultado1Text;
	private TextField resultado2Text;
	private ComboBox<String> operadorCombo;
	private Label operadorLabel1; 
	private Label operadorLabel2; 
	private Label operadorLabel3; 

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		operando1Text = new TextField();
		operando1Text.setPrefColumnCount(4);

		operando2Text = new TextField();
		operando2Text.setPrefColumnCount(4);

		operando3Text = new TextField();
		operando3Text.setPrefColumnCount(4);

		operando4Text = new TextField();
		operando4Text.setPrefColumnCount(4);

		resultado1Text = new TextField();
		resultado1Text.setPrefColumnCount(4);
		resultado1Text.setDisable(true); // comprobar aquí

		resultado2Text = new TextField();
		resultado2Text.setPrefColumnCount(4);
		resultado2Text.setDisable(true);

		operadorCombo = new ComboBox<String>();
		operadorCombo.getItems().addAll("+", "-", "*", "/");
		// realizamos un get del valor del combo

		operadorLabel1 = new Label("+");
		operadorLabel2 = new Label("+");
		operadorLabel3 = new Label("+");

		// primer VBox que es el combo donde se selecciona el signo de la operación

		VBox vBoxLeft = new VBox();
		vBoxLeft.setSpacing(5);
		vBoxLeft.setAlignment(Pos.CENTER);
		vBoxLeft.setFillWidth(false);
		vBoxLeft.getChildren().addAll(operadorCombo);

		// HBox que contendrán los símbolos de los label y los TextField()

		HBox upBoxes = new HBox(5);
		upBoxes.setAlignment(Pos.BASELINE_CENTER);
		upBoxes.getChildren().addAll(operando1Text, operadorLabel1, operando2Text);

		HBox middleBoxes = new HBox(5);
		middleBoxes.setAlignment(Pos.BASELINE_CENTER);
		middleBoxes.getChildren().addAll(operando3Text, operadorLabel2, operando4Text);

		HBox downBoxes = new HBox(5);
		downBoxes.setAlignment(Pos.BASELINE_CENTER); // esto me permite que el operador salga centrado en el medio.
														// Antes me salía un poco por encima del medio
		downBoxes.getChildren().addAll(resultado1Text, operadorLabel3, resultado2Text);

		// segundo VBox que contendrá al HBox anterior con los TextField()

		VBox vBoxMiddle = new VBox();
		vBoxMiddle.setSpacing(5);
		vBoxMiddle.setAlignment(Pos.CENTER);
		vBoxMiddle.setFillWidth(false);
		vBoxMiddle.getChildren().addAll(upBoxes, middleBoxes, downBoxes); // incluímos cada uno de los HBox() del medio

		// a continuación se crea el HBox que contendrá los VBox y luego servirá para componer la escena

		HBox root = new HBox(5, vBoxLeft, vBoxMiddle);
		root.setAlignment(Pos.CENTER); // con esto logramos que la calculadora quede en el centro y no pegada a la
										// izquierda
		
		//bindeos realizados entre los distintos operandos (sólo con Bind sin Beans). Es igual que lo de abajo, 
		//lo único que cambié es que el StringConverter<> lo declaré arriba
/*
		Bindings.bindBidirectional(operando1Text.textProperty(), operando1, new NumberStringConverter());
		Bindings.bindBidirectional(operando2Text.textProperty(), operando2, new NumberStringConverter());
		Bindings.bindBidirectional(operando3Text.textProperty(), operando3, new NumberStringConverter());
		Bindings.bindBidirectional(operando4Text.textProperty(), operando4, new NumberStringConverter());
		Bindings.bindBidirectional(resultado1Text.textProperty(), resultado1, new NumberStringConverter());
		Bindings.bindBidirectional(resultado2Text.textProperty(), resultado2, new NumberStringConverter());
*/
			
		Bindings.bindBidirectional(operando1Text.textProperty(), complejo1.realProperty(), converter);
		Bindings.bindBidirectional(operando2Text.textProperty(), complejo1.imaginarioProperty(), converter);
		Bindings.bindBidirectional(operando3Text.textProperty(), complejo2.realProperty(), converter);
		Bindings.bindBidirectional(operando4Text.textProperty(), complejo2.imaginarioProperty(), converter);
		Bindings.bindBidirectional(resultado1Text.textProperty(), resultado.realProperty(), converter);
		Bindings.bindBidirectional(resultado2Text.textProperty(), resultado.imaginarioProperty(), converter);
		
		
		operador.bind(operadorCombo.getSelectionModel().selectedItemProperty());

		operador.addListener((o, ov, nv) -> onOperadorChanged(nv));
		operadorCombo.getSelectionModel().selectFirst();  //colocando esto aquí logramos que la suma se realice desde un primer momento, además de que esté seleccionada
														//como primer símbolo. Los bindeos ya se ejecutaron antes por ello es que ya la operación suma se da desde el inicio

		Scene scene = new Scene(root, 320, 200);

		primaryStage.setTitle("Calculadora Compleja");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	private void onOperadorChanged(String nv) {
		// TODO Auto-generated method stub
		
		switch (nv) {
		case "+":		
			resultado.realProperty().bind(complejo1.realProperty().add(complejo2.realProperty()));
			resultado.imaginarioProperty().bind(complejo1.imaginarioProperty().add(complejo2.imaginarioProperty()));
			break;
		case "-":
			resultado.realProperty().bind(complejo1.realProperty().subtract(complejo2.realProperty()));
			resultado.imaginarioProperty().bind(complejo1.imaginarioProperty().subtract(complejo2.imaginarioProperty()));
			break;
		case "*":
			resultado.realProperty().bind(complejo1.realProperty().multiply(complejo2.realProperty()));
			resultado.imaginarioProperty().bind(complejo1.imaginarioProperty().multiply(complejo2.imaginarioProperty()));
			break;
		case "/":
			resultado.realProperty().bind(complejo1.realProperty().divide(complejo2.realProperty()));
			resultado.imaginarioProperty().bind(complejo1.imaginarioProperty().divide(complejo2.imaginarioProperty()));
			break;
		}	
	}
	
		
	//abajo es si fuera con los bind
	
/*
	private void onOperadorChanged(String nv) {
		// TODO Auto-generated method stub

		switch (nv) {
		case "+":
			resultado1.bind(operando1.add(operando3));
			resultado2.bind(operando2.add(operando4));
			break;
		case "-":
			resultado1.bind(operando1.subtract(operando3));
			resultado2.bind(operando2.subtract(operando4));
			break;
		case "*":
			resultado1.bind(operando1.multiply(operando2));
			resultado2.bind(operando2.multiply(operando4));
			break;
		case "/":
			resultado1.bind(operando1.divide(operando2));
			resultado2.bind(operando2.divide(operando4));
			break;
		}

	}
*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);

	}

}
