package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(addOrEven(Arrays.asList(1, 2, 3, 4, 5)));
        System.out.println(addOrEven(Arrays.asList(8, 9)));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (a, b) -> 10 * a + b);
    }

    private static List<Integer> addOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> result = integers.stream()
                .collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.toList()));
//        return integers.stream().reduce(0, Integer::sum) % 2 == 0 ? result.get(false) : result.get(true);
        return result.get(result.get(false).size() % 2 != 0);
    }
}
