package io.primeval.reflect.proxy.composite;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsUpdater;

public final class ArgumentsMock implements Arguments, ArgumentsUpdater {

    private final List<Parameter> parameters;

    private final Map<String, Object> arguments = new HashMap<>();

    public ArgumentsMock(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public List<Parameter> parameters() {
        return parameters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T objectArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (T) object;
    }

    @Override
    public int intArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (int) object;
    }

    @Override
    public short shortArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (short) object;
    }

    @Override
    public long longArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (long) object;
    }

    @Override
    public byte byteArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (byte) object;
    }

    @Override
    public boolean booleanArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (boolean) object;
    }

    @Override
    public float floatArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (float) object;
    }

    @Override
    public double doubleArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (double) object;
    }

    @Override
    public char charArg(String argName) {
        Object object = arguments.get(argName);
        if (object == null) {
            throw new IllegalArgumentException(argName);
        }
        return (char) object;
    }

    @Override
    public ArgumentsUpdater updater() {
        return this;
    }

    @Override
    public Arguments update() {
        return this;
    }

    @Override
    public <T> ArgumentsUpdater setObjectArg(String argName, T newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setIntArg(String argName, int newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setShortArg(String argName, short newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setLongArg(String argName, long newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setByteArg(String argName, byte newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setBooleanArg(String argName, boolean newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setFloatArg(String argName, float newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setDoubleArg(String argName, double newValue) {
        arguments.put(argName, newValue);
        return this;
    }

    @Override
    public ArgumentsUpdater setCharArg(String argName, char newValue) {
        arguments.put(argName, newValue);
        return this;
    }

}
