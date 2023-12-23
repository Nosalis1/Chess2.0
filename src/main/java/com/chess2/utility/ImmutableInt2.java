package com.chess2.utility;

import org.jetbrains.annotations.NotNull;

/**
 * A class representing an immutable two-dimensional integer vector.
 * Instances of this class cannot be modified after creation.
 */
public class ImmutableInt2 extends Int2 {

    /**
     * Constructs an immutable vector initialized to (0, 0).
     */
    public ImmutableInt2() {
        super();
    }

    /**
     * Constructs an immutable vector with both coordinates set to the specified scalar value.
     *
     * @param scalar The scalar value to set for both coordinates.
     */
    public ImmutableInt2(final int scalar) {
        super();
        this.x = this.y = scalar;
    }

    /**
     * Constructs an immutable vector with the specified x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public ImmutableInt2(final int x, final int y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs an immutable vector from an array of integer data.
     * The first two elements of the array represent the x and y coordinates, respectively.
     *
     * @param data An array containing the x and y coordinates.
     */
    public ImmutableInt2(final int @NotNull [] data) {
        super();
        if (data.length < 2) return;
        this.x = data[0];
        this.y = data[1];
    }

    /**
     * Constructs an immutable vector from another Int2 instance.
     *
     * @param other The Int2 instance to copy values from.
     */
    public ImmutableInt2(final Int2 other) {
        super();
        if (other == null) return;
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Creates and returns a copy of this {@code ImmutableInt2} object.
     * The new object is an independent copy with the same values for the x and y coordinates.
     *
     * @return A copy of this {@code ImmutableInt2} object.
     */
    @Override
    public @NotNull Int2 copy() {
        return new ImmutableInt2(this.x, this.y);
    }
}