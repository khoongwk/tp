package seedu.homerce.model.appointment;

import static seedu.homerce.testutil.TypicalClients.ALICE;

import seedu.homerce.model.client.Client;
import seedu.homerce.model.service.Service;
import seedu.homerce.model.util.attributes.Date;

/** A utility class that helps to build Appointment Objects */
public class AppointmentBuilder {
    public static final String DEFAULT_DATE = "";
    public static final String DEFAULT_TIME_OF_DAY = "";
    public static final Client DEFAULT_CLIENT = ALICE;
    public static final Service DEFAULT_SERVICE = null; //TODO Fill with typical service from Han's code.

    private Date date;
    private TimeOfDay timeOfDay;
    private Client client;
    private Service service;

    /**
     * Creates a {@code ClientBuilder} with the default details.
     */
    public AppointmentBuilder() {
        date = new Date(DEFAULT_DATE);
        timeOfDay = new TimeOfDay(DEFAULT_TIME_OF_DAY);
        client = DEFAULT_CLIENT;
        service = DEFAULT_SERVICE;
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code appointmentToCopy}.
     */
    public AppointmentBuilder(Appointment appointmentToCopy) {
        date = appointmentToCopy.getAppointmentDate();
        timeOfDay = appointmentToCopy.getAppointmentStartTime();
        client = appointmentToCopy.getClient();
        service = appointmentToCopy.getService();
    }

    /**
     * Sets the {@code Date} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Service} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withService(Service service) {
        this.service = service;
        return this;
    }

    /**
     * Sets the {@code TimeOfDay} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withTime(String time) {
        this.timeOfDay = new TimeOfDay(time);
        return this;
    }

    /**
     * Sets the {@code Client} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withClient(Client client) {
        this.client = client;
        return this;
    }

    public Appointment build() {
        return new Appointment(date, timeOfDay, client, service);
    }
}
