package com.aamacgregor.messageprocessor.processor.detail;

import com.aamacgregor.messageprocessor.model.enums.StateType;
import com.aamacgregor.messageprocessor.processor.IProcessingState;
import com.aamacgregor.messageprocessor.processor.ProcessManager;

public class StoppingState implements IProcessingState, IResolvableState {

    private final ProcessManager context;
    private int messageCountRemaining;

    public StoppingState(ProcessManager context, int messageCountRemaining) {
        this.context = context;
        this.messageCountRemaining = messageCountRemaining;
    }

    @Override
    public StateType getStateType() {
        return StateType.STOPPING;
    }

    @Override
    public void execute() {
        --messageCountRemaining;
        context.setState(resolveState());
    }

    @Override
    public IProcessingState resolveState() {
        return messageCountRemaining <= 0
                ? new StoppedState()
                : this;
    }
}
