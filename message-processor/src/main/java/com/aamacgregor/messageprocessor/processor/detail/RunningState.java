package com.aamacgregor.messageprocessor.processor.detail;

import com.aamacgregor.messageprocessor.model.enums.StateType;
import com.aamacgregor.messageprocessor.processor.IProcessingState;
import com.aamacgregor.messageprocessor.processor.ProcessManager;

public class RunningState implements IProcessingState, IResolvableState {

    private final ProcessManager context;
    private int messageCountRemaining;

    public RunningState(ProcessManager context, int messageCountRemaining) {
        this.context = context;
        this.messageCountRemaining = messageCountRemaining;
        context.setState(resolveState());
    }

    @Override
    public StateType getStateType() {
        return StateType.RUNNING;
    }

    @Override
    public void execute() {
        context.setState(resolveState());
        --messageCountRemaining;
    }

    @Override
    public IProcessingState resolveState() {
        return messageCountRemaining <= 1
                ? new StoppingState(context, messageCountRemaining).resolveState()
                : this;
    }
}
