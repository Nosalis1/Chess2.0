package com.chess2.utility;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * An abstract class representing a two-dimensional integer vector.
 * Subclasses can extend this class to represent specific types of two-dimensional vectors.
 */
@SuppressWarnings("unused")
public abstract class Int2 implements Serializable {

    /**
     * The x-coordinate of the vector.
     */
    protected int x;

    /**
     * The y-coordinate of the vector.
     */
    protected int y;

    /**
     * Default constructor that initializes the vector to the origin (0, 0).
     */
    public Int2() {
        this.x = this.y = 0;
    }

    /**
     * Gets the x-coordinate of the vector.
     *
     * @return The x-coordinate.
     */
    public final int getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the vector.
     *
     * @return The y-coordinate.
     */
    public final int getY() {
        return this.y;
    }

    /**
     * Gets an array containing the x and y coordinates of the vector.
     *
     * @return An array containing the x and y coordinates.
     */
    public final int @NotNull [] getData() {
        return new int[]{this.x, this.y};
    }

    /**
     * Checks if the x-coordinate is within the specified range.
     *
     * @param min The minimum allowed x-coordinate.
     * @param max The maximum allowed x-coordinate.
     * @return {@code true} if the x-coordinate is within the range, {@code false} otherwise.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */
    public final boolean inBoundX(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return this.x >= min && this.x <= max;
    }

    /**
     * Checks if the y-coordinate is within the specified range.
     *
     * @param min The minimum allowed y-coordinate.
     * @param max The maximum allowed y-coordinate.
     * @return {@code true} if the y-coordinate is within the range, {@code false} otherwise.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */
    public final boolean inBoundY(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return this.y >= min && this.y <= max;
    }

    /**
     * Checks if both x and y coordinates are within the specified range.
     *
     * @param min The minimum allowed coordinate value.
     * @param max The maximum allowed coordinate value.
     * @return {@code true} if both x and y coordinates are within the range, {@code false} otherwise.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */
    public final boolean inBound(final int min, final int max) {
        return this.inBoundX(min, max) && this.inBoundY(min, max);
    }

    /**
     * Creates and returns a copy of this {@code Int2} object.
     * The new object is an independent copy with the same values for the x and y coordinates.
     *
     * <p>The specific behavior of the copy operation is determined by the implementation in the concrete subclass.</p>
     *
     * @return A copy of this {@code Int2} object.
     */
    abstract @NotNull Int2 copy();

    /**
     * Returns a string representation of the vector in the format "Int2(x,y)".
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Int2(" + this.x + "," + this.y + ")";
    }

    /**
     * Checks if the vector is equal to another object.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int2 int2 = (Int2) o;
        return this.x == int2.x && this.y == int2.y;
    }

    /**
     * Generates a hash code for the vector.
     *
     * @return The hash code of the vector.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
