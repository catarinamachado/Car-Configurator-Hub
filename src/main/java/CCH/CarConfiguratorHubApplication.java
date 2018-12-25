package CCH;

import CCH.business.CCH;
import CCH.dataaccess.CCHConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

	private static CCH cch;
	public static CCH getCch() {
		return cch;
	}

	private ConfigurableApplicationContext context;
	private Parent rootNode;

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(CarConfiguratorHubApplication.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/welcome.fxml"));
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

		cch = new CCH();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(new Scene(rootNode, 600, 400));
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	@Override
	public void stop() {
		context.close();
	}
}

