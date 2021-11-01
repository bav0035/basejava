package ru.javawebinar.basejava;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// HW12
public class MainStream {
    public static void main(String[] args) {
//        int[] values = {1, 2, 3, 3, 2, 3};
        int[] values = {9, 8};
        System.out.println(minValue(values));

    }

    static int minValue(int[] values) {
        List<Integer> list = Arrays.stream(values).distinct()
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i) * Math.pow(10, i);
        }
        return result;
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        List<Integer> odd = new ArrayList<>();
        List<Integer> even = new ArrayList<>();
        for (int i : integers) {
            if (i % 2 == 0) {
                even.add(i);
            } else {
                odd.add(i);
            }
        }
        return odd.size() % 2 == 0 ? odd : even;
    }

}
