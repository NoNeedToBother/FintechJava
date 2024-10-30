package ru.kpfu.itis.paramonov;

import ru.kpfu.itis.paramonov.list.CustomLinkedList;
import ru.kpfu.itis.paramonov.list.impl.CustomLinkedListImpl;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        CustomLinkedList<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        Integer first = list.get(0);
        System.out.printf("First element is %s%n", first);
        Integer removed = list.remove(0);
        System.out.printf("Removed element is %s%n", removed);
        boolean contains = list.contains(1);
        System.out.printf("Result of contains method is %s%n", contains);

        List<Integer> listToAdd = List.of(2, 3);
        list.addAll(listToAdd);
        System.out.printf("First and second elements after adding are: %s and %s%n", list.get(0), list.get(1));

        Random random = new Random();
        CustomLinkedList<Integer> listFromStream = Stream
                .generate(() -> random.nextInt(1000))
                .limit(50)
                .reduce(
                        new CustomLinkedListImpl<>(),
                        (integerCustomLinkedList, integer) -> {
                            integerCustomLinkedList.add(integer);
                            return integerCustomLinkedList;
                        },
                        (integerCustomLinkedList, integerCustomLinkedList2) -> {
                            for (int i = 0; i < integerCustomLinkedList2.size(); i++) {
                                integerCustomLinkedList.add(integerCustomLinkedList2.get(i));
                            }
                            return integerCustomLinkedList;
                        }
                );
        System.out.print("Result list from stream: ");
        for (int i = 0; i < listFromStream.size(); i++) {
            System.out.printf("%s ", listFromStream.get(i));
        }
    }
}
