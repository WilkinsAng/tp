package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;

/**
 * Deletes an employee identified using it's displayed index from the address book.
 */

public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the employee identified by the prefix of their Employee ID.\n"
            + "Parameters: EMPLOYEE_ID_PREFIX (must be a prefix of exactly one employee in the list)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EMPLOYEE_SUCCESS = "Deleted Employee: %1$s";

    private final EmployeeId employeeIdPrefix;

    public DeleteCommand(EmployeeId employeeIdPrefix) {
        this.employeeIdPrefix = employeeIdPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Employee> matchedEmployees = model.getFullFilteredByEmployeeIdPrefixListFromData(employeeIdPrefix);

        if (matchedEmployees.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdPrefix
            ));
        }

        if (matchedEmployees.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND,
                    employeeIdPrefix
            ));
        }

        // Save the state before any potential changes
        model.commitChanges();

        Employee employeeToDelete = matchedEmployees.get(0);
        model.deleteEmployee(employeeToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_EMPLOYEE_SUCCESS, Messages.format(employeeToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return employeeIdPrefix.equals(otherDeleteCommand.employeeIdPrefix);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeIdPrefix", employeeIdPrefix.toString())
                .toString();
    }
}
