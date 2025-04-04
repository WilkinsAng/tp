package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMPLOYEE_ID_PREFIX;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_PREFIX_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_PREFIX_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEmployeeAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Employee employeeToDelete = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(employeeToDelete.getEmployeeId());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_EMPLOYEE_SUCCESS,
                Messages.format(employeeToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteEmployee(employeeToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(EmployeeId.fromString(INVALID_EMPLOYEE_ID_PREFIX));

        assertCommandFailure(deleteCommand, model,
                String.format(Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND,
                        INVALID_EMPLOYEE_ID_PREFIX
                ));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);

        Employee employeeToDelete = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(employeeToDelete.getEmployeeId());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_EMPLOYEE_SUCCESS,
                Messages.format(employeeToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteEmployee(employeeToDelete);
        showNoEmployee(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);
        Index outOfBoundIndex = INDEX_SECOND_EMPLOYEE;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getEmployeeList().size());

        DeleteCommand deleteCommand = new DeleteCommand(EmployeeId.fromString(INVALID_EMPLOYEE_ID_PREFIX));

        assertCommandFailure(deleteCommand, model, String.format(
                Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND,
                INVALID_EMPLOYEE_ID_PREFIX
        ));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_AMY));
        DeleteCommand deleteSecondCommand = new DeleteCommand(EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_BOB));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_AMY));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        String employeeIdPrefix = VALID_EMPLOYEE_ID_PREFIX_AMY;
        DeleteCommand deleteCommand = new DeleteCommand(EmployeeId.fromString(employeeIdPrefix));
        String expected = DeleteCommand.class.getCanonicalName() + "{employeeIdPrefix=" + employeeIdPrefix + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEmployee(Model model) {
        model.updateFilteredEmployeeList(p -> false);

        assertTrue(model.getFilteredEmployeeList().isEmpty());
    }
}
