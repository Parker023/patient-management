package com.parker.patientservice.dto.wrapper;

import java.util.List;

/**
 * An abstract base class designed to serve as a wrapper for exporting data of generic type {@code T}.
 * This class is intended to be extended to provide specific implementation details for data retrieval.
 *
 * @param <T> the type of the data that this wrapper will handle
 */
public abstract class BaseExportWrapper<T> {
    public abstract List<T> getData();

}
