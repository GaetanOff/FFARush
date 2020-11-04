package com.lighter.ffarush.manager;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Manager {
    final ManagerHandler handler;
}
