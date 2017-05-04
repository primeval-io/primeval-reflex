package io.primeval.reflex.proxy.theory;

import java.lang.reflect.Parameter;
import java.util.List;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsUpdater;

public final class M2ArgsUpdater implements ArgumentsUpdater {

    public final List<Parameter> parameters;

    public final double a;

    public final int[] b;

    public M2ArgsUpdater(List<Parameter> parameters, double a, int[] b) {
        super();
        this.parameters = parameters;
        this.a = a;
        this.b = b;
    }

    @Override
    public List<Parameter> parameters() {
        return parameters;
    }

    @Override
    public <T> T objectArg(String argName) {
        throw new IllegalArgumentException("No int parameter named " + argName);

    }

    @Override
    public int intArg(String argName) {
        switch (argName) {
        default:
            throw new IllegalArgumentException("No int parameter named " + argName);
        }
    }

    @Override
    public short shortArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

    @Override
    public long longArg(String argName) {
        throw new IllegalArgumentException("No object parameter named " + argName);
    }

    @Override
    public byte byteArg(String argName) {
        throw new IllegalArgumentException("No int parameter named " + argName);

    }

    @Override
    public boolean booleanArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

    @Override
    public float floatArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

    @Override
    public double doubleArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

    @Override
    public char charArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

    @Override
    public Arguments update() {
        return new M2Args(parameters, a, b);
    }

    @Override
    public <T> ArgumentsUpdater setObjectArg(String argName, T newValue) {
        throw new IllegalArgumentException("No object parameter named " + argName);
    }

    @Override
    public ArgumentsUpdater setIntArg(String argName, int newValue) {
        throw new IllegalArgumentException("No object parameter named " + argName);

    }

    @Override
    public ArgumentsUpdater setShortArg(String argName, short newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setLongArg(String argName, long newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setByteArg(String argName, byte newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setBooleanArg(String argName, boolean newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setFloatArg(String argName, float newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setDoubleArg(String argName, double newValue) {
        return this;
    }

    @Override
    public ArgumentsUpdater setCharArg(String argName, char newValue) {
        return this;
    }

}
