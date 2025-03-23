package dev.definex.finalproject.tool;

import dev.definex.finalproject.enums.State;
import dev.definex.finalproject.exception.InvalidStateException;
import org.springframework.stereotype.Component;

@Component
public class TaskStateValidator {

    public void validateStateTransition(State currentState, State newState, String reason) {
        if (currentState == State.Completed && newState != State.Completed) {
            throw new InvalidStateException("Completed tasks cannot be moved to another state.");
        }

        if (newState == State.Blocked) {
            if (currentState != State.InAnalysis && currentState != State.InDevelopment) {
                throw new InvalidStateException("Tasks can only be blocked from 'In Analysis' or 'In Development'.");
            }
        } else if (currentState == State.Blocked) {
            if (newState != State.InAnalysis && newState != State.InDevelopment) {
                throw new InvalidStateException("Blocked tasks can only return to 'In Analysis' or 'In Development'.");
            }
        }

        if ((newState == State.Cancelled || newState == State.Blocked) &&
                (reason == null || reason.trim().isEmpty())) {
            throw new InvalidStateException("Reason is required for Cancelled or Blocked states.");
        }
    }
}

