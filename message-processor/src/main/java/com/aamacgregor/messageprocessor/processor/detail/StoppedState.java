package com.aamacgregor.messageprocessor.processor.detail;

import com.aamacgregor.messageprocessor.model.enums.StateType;
import com.aamacgregor.messageprocessor.processor.IProcessingState;

public class StoppedState implements IProcessingState {

    @Override
    public void execute() {
        //Poor Liskov! Alas there is nothing to do in a final state!
    }

    @Override
    public StateType getStateType() {
        return StateType.STOPPED;
    }
}
