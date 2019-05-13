package com.aamacgregor.messageprocessor.processor.detail;

import com.aamacgregor.messageprocessor.processor.IProcessingState;

public interface IResolvableState {

    IProcessingState resolveState();
}
