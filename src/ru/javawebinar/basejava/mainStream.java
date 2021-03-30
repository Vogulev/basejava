package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class mainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(addOrEven(Arrays.asList(1, 2, 3, 4, 5)));
    }

    private static int minValue(int[] values) {
        return Integer.parseInt(Arrays.stream(values).distinct().sorted().mapToObj(String::valueOf).collect(Collectors.joining()));
    }

    private static List<Integer> addOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> result = integers.stream().collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.toList()));
        return integers.stream().mapToInt((p) -> p).sum() % 2 == 0 ? result.get(true) : result.get(false);
    }
}
