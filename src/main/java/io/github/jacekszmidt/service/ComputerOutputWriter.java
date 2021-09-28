package io.github.jacekszmidt.service;

import io.github.jacekszmidt.model.Laptop;
import io.github.jacekszmidt.model.PersonalComputer;
import io.github.jacekszmidt.model.User;
import org.apache.commons.lang3.tuple.Pair;

public interface ComputerOutputWriter {

    Pair<String, byte[]> writeOutput(User user, Laptop laptop);

    Pair<String, byte[]> writeOutput(User user, PersonalComputer personalComputer);
}


