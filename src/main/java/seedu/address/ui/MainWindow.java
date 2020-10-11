package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.logging.Logger;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.clientspanel.ClientListPanel;
import seedu.address.ui.servicespanel.ServiceListPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    // Tabs panels for each service component
    private ServiceListPanel serviceListPanel;
    private ClientListPanel clientListPanel;

    @FXML
    private StackPane serviceListPanelPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane clientListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane sideTabsBarPlaceholder;

    @FXML
    private StackPane tabPanelPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        //setAccelerators();

        helpWindow = new HelpWindow();

        // Set up list panels
        clientListPanel = new ClientListPanel(logic.getFilteredClientList());
        serviceListPanel = new ServiceListPanel(logic.getFilteredServiceList());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

//    private void setAccelerators() {
//        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
//    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
//    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
//        menuItem.setAccelerator(keyCombination);
//
//        /*
//         * TODO: the code below can be removed once the bug reported here
//         * https://bugs.openjdk.java.net/browse/JDK-8131666
//         * is fixed in later version of SDK.
//         *
//         * According to the bug report, TextInputControl (TextField, TextArea) will
//         * consume function-key events. Because CommandBox contains a TextField, and
//         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
//         * not work when the focus is in them because the key event is consumed by
//         * the TextInputControl(s).
//         *
//         * For now, we add following event filter to capture such key events and open
//         * help window purposely so to support accelerators even when focus is
//         * in CommandBox or ResultDisplay.
//         */
//        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
//                menuItem.getOnAction().handle(new ActionEvent());
//                event.consume();
//            }
//        });
//    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
//        serviceListPanel = new ServiceListPanel(logic.getFilteredServiceList());
//        serviceListPanelPlaceholder.getChildren().add(serviceListPanel.getRoot());

//        clientListPanel = new ClientListPanel(logic.getFilteredClientList());
//        clientListPanelPlaceholder.getChildren().add(clientListPanel.getRoot());

//        tabPanel = new TabPanel(
//                logic.getFilteredClientList(), logic.getFilteredServiceList()
//        );
//        tabPanelPlaceholder.getChildren().add(tabPanel.getRoot());

        switchTab(ClientListPanel.TAB_NAME);
        // Independent Ui parts residing in this Ui container
        sideTabsBarPlaceholder.getChildren().add(new SideTabsBar(this::switchTab).getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Switches tab to the specified tab name.
     */
    private void switchTab(String tabName) {
        tabPanelPlaceholder.getChildren().clear();
        statusbarPlaceholder.getChildren().clear();
        //statusbarPlaceholder.getChildren().add(new StatusBarFooter(tabName).getRoot());

        switch (tabName) {
        case ClientListPanel.TAB_NAME:
            tabPanelPlaceholder.getChildren().add(clientListPanel.getRoot());
            break;
        case ServiceListPanel.TAB_NAME:
            tabPanelPlaceholder.getChildren().add(serviceListPanel.getRoot());
            break;
        default:
            throw new AssertionError("No such tab name: " + tabName);
        }
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }


    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
