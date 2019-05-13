package com.aamacgregor.messageprocessor.processor;

import com.aamacgregor.messageprocessor.model.enums.StateType;

/**
 * The state interface for the StatePattern
 */
public interface IProcessingState {

    /**
     * Executes the current state
     */
    void execute();

    /**
     * Returns the type of the current state
     *
     * @return the current state type
     */
    StateType getStateType();
}
