package seedu.address.logic.commands.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_SERVICE_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME_OF_DAY;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.client.Client;
import seedu.address.model.service.Service;

/**
 * Adds an appointment to SuperSalon.
 */
public class AddAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "addappt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to the address book. "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME_OF_DAY + "TIME "
            + PREFIX_SERVICE_SERVICE_CODE + "SERVICE_CODE "
            + PREFIX_PHONE + "PHONE" + "\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "15-2-2021 "
            + PREFIX_TIME_OF_DAY + "1430 "
            + PREFIX_SERVICE_SERVICE_CODE + "SC001 "
            + PREFIX_PHONE + "94759600";

    public static final String MESSAGE_SUCCESS = "New appointment added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in SuperSalon.";
    public static final String MESSAGE_INVALID_PHONE = "The phone number specified does not refer to an existing client.";
    public static final String MESSAGE_INVALID_SERVICE_CODE = "The service code specified does not exist in SuperSalon.";

    private final Appointment toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Client}
     */
    public AddAppointmentCommand(Appointment appointment) {
        requireNonNull(appointment);
        toAdd = appointment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasAppointment(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } else if (!model.hasClient(toAdd.getPhone())) {
            throw new CommandException(MESSAGE_INVALID_PHONE);
        } else if (!model.hasService(toAdd.getServiceCode())) {
            throw new CommandException(MESSAGE_INVALID_SERVICE_CODE);
        }
        Client clientToAdd = model.getClientByPhone(toAdd.getPhone());
        Service serviceToAdd = model.getServiceByServiceCode(toAdd.getServiceCode());
        toAdd.setClient(clientToAdd);
        toAdd.setService(serviceToAdd);
        model.addAppointment(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAppointmentCommand // instanceof handles nulls
                && toAdd.equals(((AddAppointmentCommand) other).toAdd));
    }
}
