package com.chess2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChessPosition {
    public enum CP_INDEX {
        ROW(0), COL(1);
        final int index;

        CP_INDEX(final int index) {
            this.index = index;
        }
    }

    private int row, col;

    public final int row() {
        return this.row;
    }

    public final int fxRow() {
        return this.col;
    }

    public final int col() {
        return this.col;
    }

    public final int fxCol() {
        return this.row;
    }

    @Contract(value = " -> new", pure = true)
    public final int @NotNull [] get() {
        return new int[]{this.row, this.col};
    }

    @Contract(value = " -> new", pure = true)
    public final int @NotNull [] getFx() {
        return new int[]{this.col, this.row};
    }

    public final int get(final CP_INDEX index) {
        return index == CP_INDEX.ROW ? this.row : this.col;
    }

    public final int getFx(final CP_INDEX index) {
        return index == CP_INDEX.ROW ? this.col : this.row;
    }

    public void row(final int row) {
        if (!ChessBoard.inBounds(row)) return;
        this.row = row;
    }

    public void col(final int col) {
        if (!ChessBoard.inBounds(col)) return;
        this.col = col;
    }

    public void set(final int row, final int col) {
        this.row(row);
        this.col(col);
    }

    public ChessPosition() {
        this.row = this.col = -1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ChessPosition that = (ChessPosition) object;
        return this.row == that.row && this.col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}