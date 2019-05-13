package com.aamacgregor.messageprocessor.processor;

import com.aamacgregor.messageprocessor.model.enums.StateType;
import com.aamacgregor.messageprocessor.processor.detail.RunningState;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is context class for a state pattern.
 * The ProcessManager is responsible for managing the state of the message processing (i.e. given the
 * number of messages received, whether processing is still allowed)
 */
@Component
public class ProcessManager {

    @Value("${message_limit}")
    private int messageLimit = 50;
    private final IReportConsumer reportConsumer;

    private Map<StateType, String> stateReportConsumer = new HashMap<>();
    private IProcessingState state;

    /**
     * @param reportConsumer the consumer to use when reporting state changes (i.e. when it is stopping,
     *                       or cannot process a message because the message limit has been reached).
     */
    public ProcessManager(@Autowired IReportConsumer reportConsumer) {
        this.reportConsumer = reportConsumer;
        this.state = new RunningState(this, messageLimit);
        stateReportConsumer.put(StateType.STOPPING, "Application pausing - stopping processing of future messages!");
        stateReportConsumer.put(StateType.STOPPED, "Message refused - the application has stopped processing messages");
    }

    /**
     * Updates the state, if the state changes the change is reported.
     *
     * @return true if the current message is allowed to be processed, else false
     */
    public boolean process() {
        state.execute();
        Optional.ofNullable(stateReportConsumer.get(state.getStateType()))
                .ifPresent(reportConsumer::consume);
        return state.getStateType() != StateType.STOPPED;
    }

    public void setState(IProcessingState state) {
        this.state = state;
    }
}
