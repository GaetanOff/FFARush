package com.gaetan.ffarush.manager;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Manager {
    /**
     * Reference to the ManagerHandler class.
     */
    final ManagerHandler handler;
}
