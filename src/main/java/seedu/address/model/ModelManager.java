package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.client.Client;
import seedu.address.model.client.Phone;
import seedu.address.model.expense.Expense;
import seedu.address.model.manager.AppointmentManager;
import seedu.address.model.manager.ReadOnlyAppointmentManager;
import seedu.address.model.manager.ReadOnlyServiceManager;
import seedu.address.model.manager.ServiceManager;
import seedu.address.model.service.Service;
import seedu.address.model.service.ServiceCode;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook; // This is ClientManager.
    private final ServiceManager serviceManager;
    private final AppointmentManager appointmentManager;
    private final UserPrefs userPrefs;

    private final FilteredList<Client> filteredClients;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Service> filteredServices;
    private final FilteredList<Appointment> filteredAppointments;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs,
                        ReadOnlyServiceManager serviceManager, ReadOnlyAppointmentManager appointmentManager) {
        super();
        requireAllNonNull(addressBook, userPrefs, serviceManager);

        logger.fine("Initializing with SuperSalon: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.serviceManager = new ServiceManager(serviceManager);
        this.appointmentManager = new AppointmentManager(appointmentManager);

        filteredClients = new FilteredList<>(this.addressBook.getClientList());
        filteredExpenses = new FilteredList<>(this.addressBook.getExpenseList());
        filteredServices = new FilteredList<>(this.serviceManager.getServiceList());
        filteredAppointments = new FilteredList<>(this.appointmentManager.getAppointmentList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ServiceManager(), new AppointmentManager());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return addressBook.hasClient(client);
    }

    @Override
    public boolean hasClient(Phone phone) {
        requireAllNonNull(phone);
        return addressBook.hasClient(phone);
    }

    @Override
    public void deleteClient(Client target) {
        addressBook.removeClient(target);
    }

    @Override
    public void addClient(Client client) {
        addressBook.addClient(client);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        requireAllNonNull(target, editedClient);
        addressBook.setClient(target, editedClient);
    }

    @Override
    public Client getClientByPhone(Phone phone) {
        requireAllNonNull(phone);
        return addressBook.getClientByPhone(phone);
    }

    //=========== Filtered Expense List Accessors =============================================================
    @Override
    public void deleteExpense(Expense target) {
        addressBook.removeExpense(target);
    }

    @Override
    public void addExpense(Expense expense) {
        addressBook.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
    }

    @Override
    public void setExpense(Expense target, Expense editedExpense) {
        requireAllNonNull(target, editedExpense);
        addressBook.setExpense(target, editedExpense);
    }

    @Override
    public void setExpenses(List<Expense> expenses) {
        addressBook.setExpenses(expenses);
    }

    //=========== Filtered Client List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredClientList(Predicate<Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of Expenses
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    //=========== ServiceManager ===============
    @Override
    public void addService(Service toAdd) {
        requireNonNull(toAdd);
        serviceManager.addService(toAdd);
        updateFilteredServiceList(PREDICATE_SHOW_ALL_SERVICES);
    }

    @Override
    public boolean hasService(ServiceCode code) {
        requireAllNonNull(code);
        return serviceManager.hasService(code);
    }

    @Override
    public void updateFilteredServiceList(Predicate<Service> predicate) {
        requireNonNull(predicate);
        filteredServices.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Service> getFilteredServiceList() {
        return filteredServices;
    }

    @Override
    public ReadOnlyServiceManager getServiceManager() {
        return this.serviceManager;
    }

    @Override
    public Service getServiceByServiceCode(ServiceCode serviceCode) {
        requireAllNonNull(serviceCode);
        return serviceManager.getServiceByServiceCode(serviceCode);
    }

    //================== AppointmentManager ==================
    @Override
    public void addAppointment(Appointment toAdd) {
        requireNonNull(toAdd);
        appointmentManager.addAppointment(toAdd);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
    }

    @Override
    public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
        requireNonNull(predicate);
        filteredAppointments.setPredicate(predicate);
    }

    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return filteredAppointments;
    }

    @Override
    public ReadOnlyAppointmentManager getAppointmentManager() {
        return appointmentManager;
    }

    @Override
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireAllNonNull(target, editedAppointment);
        appointmentManager.setAppointments(target, editedAppointment);
    }

    @Override
    public void setAppointment(List<Appointment> appointments) {
        appointmentManager.setAppointments(appointments);
    }

    @Override
    public boolean hasAppointment(Appointment appointment) {
        return appointmentManager.hasAppointment(appointment);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredClients.equals(other.filteredClients)
                && filteredServices.equals(other.filteredServices)
                && filteredAppointments.equals(other.filteredAppointments);
    }
}
