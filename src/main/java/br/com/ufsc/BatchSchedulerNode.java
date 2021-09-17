package br.com.ufsc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongArray;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BatchSchedulerNode {
  private static int BASE_ID = 0;
  private final int id = BASE_ID++;
  private final List<Command> batch = new ArrayList<>();
  private final BloomFilter<Integer> dependencies = BloomFilter.create(Funnels.integerFunnel(), 500, 0.01);

  private Boolean taken = false;

  public int getId() {
    return id;
  }

  public Boolean getTaken() {
    return taken;
  }

  public void setTaken(Boolean taken) {
    this.taken = taken;
  }

  public List<Command> getBatch() {
    return batch;
  }

  public BloomFilter<Integer> getDependencies() {
    return dependencies;
  }

  public void addCommand(Command command) {
    batch.add(command);
    command.getDependencies().forEach(dependencies::put);
  }

  public Boolean hasConflict(BatchSchedulerNode otherNode) {
    BitSet thisBitSet = getBitSet(dependencies);
    BitSet thatBitSet = getBitSet(otherNode.getDependencies());
    BitSet intersectionBitSet = new BitSet(thisBitSet.size());

    intersectionBitSet.or(thisBitSet);
    intersectionBitSet.and(thatBitSet);

    return intersectionBitSet.isEmpty();
  }

  private static <T> BitSet getBitSet(BloomFilter<T> bloomFilter) {
    try {
      Field bitsField = BloomFilter.class.getDeclaredField("bits");
      bitsField.setAccessible(true);
      Object bitArray = bitsField.get(bloomFilter);
      Field dataField = bitArray.getClass().getDeclaredField("data");
      dataField.setAccessible(true);
      AtomicLongArray atomicLongArray = (AtomicLongArray) dataField.get(bitArray);
      long[] bits = new long[atomicLongArray.length()];

      for (int i = 0; i < atomicLongArray.length(); i++) {
        bits[i] = atomicLongArray.get(i);
      }

      return BitSet.valueOf(bits);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("BatchSchedulerNode [id=");
    builder.append(id);
    builder.append(", taken=");
    builder.append(taken);
    builder.append("]");
    return builder.toString();
  }
}
