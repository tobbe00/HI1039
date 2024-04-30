package com.example.cpr.Model;

import java.util.Arrays;

public class Batch {
    private int[] batch;
    private int id;

    public Batch(int[] batch, int id) {
        this.batch = batch;
        this.id = id;
    }

    public int[] getBatch() {
        return batch;
    }

    public void setBatch(int[] batch) {
        this.batch = batch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batch=" + Arrays.toString(batch) +
                ", id=" + id +
                '}';
    }
}
