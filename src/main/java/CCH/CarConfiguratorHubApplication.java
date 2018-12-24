package CCH;

import CCH.dataaccess.CCHConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CarConfiguratorHubApplication extends Application {
	@Autowired
	private Environment env;

	private ConfigurableApplicationContext context;
	private Parent rootNode;

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(CarConfiguratorHubApplication.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/tipoUtilizador/index.fxml"));
		loader.setControllerFactory(context::getBean);
		rootNode = loader.load();
	}

	@PostConstruct
	public void initProperties() {
		CCHConnection.startConnection(
				env.getProperty("spring.datasource.driverClassName"),
				env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"),
				env.getProperty("spring.datasource.password")
		);
	}

	@Override
	public void start(Stage primaryStage) {
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		double width = visualBounds.getWidth();
		double height = visualBounds.getHeight();

		primaryStage.setScene(new Scene(rootNode, width, height));
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	@Override
	public void stop() {
		context.close();
	}
}

