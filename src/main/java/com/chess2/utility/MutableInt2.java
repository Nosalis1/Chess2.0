package com.chess2.utility;

import org.jetbrains.annotations.NotNull;

/**
 * A class representing a mutable two-dimensional integer vector.
 * Instances of this class can be modified after creation.
 */
public class MutableInt2 extends Int2 {

    /**
     * Constructs a mutable vector initialized to (0, 0).
     */
    public MutableInt2() {
        super();
    }

    /**
     * Constructs a mutable vector with both coordinates set to the specified scalar value.
     *
     * @param scalar The scalar value to set for both coordinates.
     */
    public MutableInt2(final int scalar) {
        super();
        this.set(scalar, scalar);
    }

    /**
     * Constructs a mutable vector with the specified x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public MutableInt2(final int x, final int y) {
        super();
        this.set(x, y);
    }

    /**
     * Constructs a mutable vector from an array of integer data.
     * The first two elements of the array represent the x and y coordinates, respectively.
     *
     * @param data An array containing the x and y coordinates.
     */
    public MutableInt2(final int @NotNull [] data) {
        super();
        if (data.length < 2) return;
        this.set(data[0], data[1]);
    }

    /**
     * Constructs a mutable vector from another Int2 instance.
     *
     * @param other The Int2 instance to copy values from.
     */
    public MutableInt2(final Int2 other) {
        super();
        if (other == null) return;
        this.set(other.x, other.y);
    }

    /**
     * Sets the x-coordinate of the vector.
     *
     * @param x The new x-coordinate.
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the vector.
     *
     * @param y The new y-coordinate.
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * Sets both x and y coordinates of the vector.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void set(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Clamps the x-coordinate within the specified range.
     *
     * @param min The minimum allowed x-coordinate.
     * @param max The maximum allowed x-coordinate.
     */
    public void clampX(final int min, final int max) {
        if (this.inBoundX(min, max)) return;
        this.x = Math.max(Math.min(this.x, max), min);
    }

    /**
     * Clamps the y-coordinate within the specified range.
     *
     * @param min The minimum allowed y-coordinate.
     * @param max The maximum allowed y-coordinate.
     */
    public void clampY(final int min, final int max) {
        if (this.inBoundY(min, max)) return;
        this.y = Math.max(Math.min(this.y, max), min);
    }

    /**
     * Clamps both x and y coordinates within the specified range.
     *
     * @param min The minimum allowed coordinate value.
     * @param max The maximum allowed coordinate value.
     */
    public void clamp(final int min, final int max) {
        this.clampX(min, max);
        this.clampY(min, max);
    }

    /**
     * Creates and returns a copy of this {@code MutableInt2} object.
     * The new object is an independent copy with the same values for the x and y coordinates.
     *
     * @return A copy of this {@code MutableInt2} object.
     */
    @Override
    public @NotNull Int2 copy() {
        return new MutableInt2(this.x, this.y);
    }
}