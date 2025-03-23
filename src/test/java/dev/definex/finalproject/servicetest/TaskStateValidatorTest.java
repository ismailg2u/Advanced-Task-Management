package dev.definex.finalproject.servicetest;



import dev.definex.finalproject.enums.State;
import dev.definex.finalproject.exception.InvalidStateException;
import dev.definex.finalproject.tool.TaskStateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskStateValidatorTest {

    private TaskStateValidator taskStateValidator;

    @BeforeEach
    public void setUp() {
        taskStateValidator = new TaskStateValidator();
    }

    @Test
    public void testValidStateTransition() {
        assertDoesNotThrow(() -> taskStateValidator.validateStateTransition(State.Backlog, State.InAnalysis, "Valid reason"));
    }

    @Test
    public void testCompletedTaskCannotChangeState() {
        InvalidStateException exception = assertThrows(InvalidStateException.class,
                () -> taskStateValidator.validateStateTransition(State.Completed, State.InDevelopment, "Trying to change"));

        assertEquals("InvalidStateException Completed tasks cannot be moved to another state.", exception.getMessage());
    }

    @Test
    public void testBlockedStateCanOnlyBeSetFromValidStates() {
        InvalidStateException exception = assertThrows(InvalidStateException.class,
                () -> taskStateValidator.validateStateTransition(State.Backlog, State.Blocked, "Blocked reason"));

        assertEquals("InvalidStateException Tasks can only be blocked from 'In Analysis' or 'In Development'.", exception.getMessage());
    }

    @Test
    public void testBlockedTaskCanOnlyReturnToAllowedStates() {
        InvalidStateException exception = assertThrows(InvalidStateException.class,
                () -> taskStateValidator.validateStateTransition(State.Blocked, State.Completed, "Blocked reason"));

        assertEquals("InvalidStateException Blocked tasks can only return to 'In Analysis' or 'In Development'.", exception.getMessage());
    }

    @Test
    public void testCancelledStateRequiresReason() {
        InvalidStateException exception = assertThrows(InvalidStateException.class,
                () -> taskStateValidator.validateStateTransition(State.InDevelopment, State.Cancelled, null));

        assertEquals("InvalidStateException Reason is required for Cancelled or Blocked states.", exception.getMessage());
    }

    @Test
    public void testBlockedStateRequiresReason() {
        InvalidStateException exception = assertThrows(InvalidStateException.class,
                () -> taskStateValidator.validateStateTransition(State.InAnalysis, State.Blocked, ""));

        assertEquals("InvalidStateException Reason is required for Cancelled or Blocked states.", exception.getMessage());
    }
}

