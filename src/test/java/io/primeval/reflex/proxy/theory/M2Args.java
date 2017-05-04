package io.primeval.reflex.proxy.theory;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsUpdater;

public final class M2Args implements Arguments {

    public final List<Parameter> parameters;

    public final double a;

    public final int[] b;

    public M2Args(List<Parameter> parameters, double a, int[] b) {
        super();
        this.parameters = parameters;
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hashCode(a);
        result = prime * result + Objects.hashCode(b);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        M2Args other = (M2Args) obj;

        return this.a == other.a && Objects.equals(this.b, other.b);

    }

    @Override
    public String toString() {
        return M2Args.class.getSimpleName() + " [" + "a" + a + ", " + "b=" + b + "]";
    }

    @Override
    public List<Parameter> parameters() {
        return parameters;
    }

    @Override
    public ArgumentsUpdater updater() {
        return new M2ArgsUpdater(parameters, a, b);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T objectArg(String argName) {
        if (argName.equals("b")) {
            return (T) b;
        } else {
            throw new IllegalArgumentException("No object parameter named " + argName);
        }
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
        throw new IllegalArgumentException("Bad type");
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
        switch (argName) {
        case "a":
            return a;
        default:
            throw new IllegalArgumentException("No double parameter named " + argName);
        }
    }

    @Override
    public char charArg(String argName) {
        throw new IllegalArgumentException("Bad type");
    }

}
